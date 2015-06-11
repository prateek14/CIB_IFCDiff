package fi.ni.nodenamer.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import com.hp.hpl.jena.rdf.model.RDFNode;

import fi.ni.util.StringChecksum;

public class Node {
	final static public int LITERAL = 0;
	final static public int IRINODE = 1;
	final static public int BLANKNODE = 2;
	private int nodeType;

	private boolean sameAs = false;
	private String local_uri = "";
	private String lexicalValue = "";
	private boolean collided = false;

	private String literal_chksum = "  ";

	private AANodeData aa = new AANodeData(); // Used in the AA Paths algorithm

	
	private String class_name = "";
	boolean list = false;

	List<Connection> edges_in = new ArrayList<Connection>();
	List<Connection> edges_out = new ArrayList<Connection>();
	List<Connection> edges_literals = new ArrayList<Connection>();

	public String local_placement_chk=null; 
	
	
	final List<String> crossings = new ArrayList<String>();  // PathName.inx
	
	public Node() {

	}

	public Node(RDFNode node, String class_name) {
		super();
		this.class_name = class_name;
		if (node.isAnon()) {
			nodeType = BLANKNODE;
		} else {
			if (node.isLiteral()) {
				nodeType = LITERAL;
				local_uri = node.asLiteral().getString();
				lexicalValue = node.asLiteral().getLexicalForm();
			} else {
				nodeType = IRINODE;
				local_uri = node.asResource().getURI().toString();
			}
		}
	}

	public Node(RDFNode node, String class_name, boolean sameAs) {
		super();
		this.sameAs = sameAs;

		this.class_name = class_name;
		if (node.isAnon()) {
			nodeType = BLANKNODE;
		} else {
			if (node.isLiteral()) {
				nodeType = LITERAL;
				local_uri = node.asLiteral().getString();
				lexicalValue = node.asLiteral().getLexicalForm();
			} else {
				nodeType = IRINODE;
				local_uri = node.asResource().getURI().toString();
			}
		}
	}
	
	public String getRDFClass_name() {
		if (class_name == null)
			class_name = "unknown";
		if (class_name.equals("list"))
			return "rdf:list";
		return class_name;
	}

	public void setRDFClass_name(String class_name) {
		this.class_name = class_name;
	}

	public void addINConnection(Connection c) {
		edges_in.add(c);
	}

	public void addOUTConnection(Connection c) {
		edges_out.add(c);
	}

	public void addLiteralConnection(Connection c) {
		edges_literals.add(c);
	}

	public List<Connection> getEdges_in() {
		return edges_in;
	}
	
	public List<Connection> getEdges_in(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_in) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	

	public List<Connection> getEdges_out() {
		return edges_out;
	}
	
	public List<Connection> getEdges_out(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_out) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	

	private void listNumbers(Node s, int inx, List<Double> dlist) {
    	List<Connection> cons_lit = s.getEdges_literals();
    	for (Connection c : cons_lit) {
    		try
    		{
    		Double d=Double.parseDouble(c.getPointedNode().getLexicalValue());
    		dlist.add(d);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}

		
		for (Connection e : s.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				listNumbers(e.getPointedNode(), inx + 1, dlist);
			}
			else if(e.getPointedNode().getRDFClass_name().equals("http://drum.cs.hut.fi/ontology/ifc2x3tc1#IfcCartesianPoint"))
			{
				listNumbers(e.getPointedNode().getEdges_out("coordinates").get(0).getPointedNode(), inx + 1, dlist);
			}
			else
				System.out.println("Something else: "+e.getProperty()+" "+e.getPointedNode().getRDFClass_name()+" node s:"+s.getRDFClass_name());
		}
	}

	public double[] giveEdges_outAsNumberVector(String property) {
		List<Double> retlist = new ArrayList<Double>();
    	for (Connection c : edges_out) {    		
    	    if(c.getProperty().equals(property))
    	    {
    	    	Node n=c.getPointedNode();
    	    	if (n.getRDFClass_name().equals("rdf:list")) {
    	    		listNumbers(n, 0, retlist);
				}
    	    	else
    	    		System.out.println("-- c:"+n.getRDFClass_name()+" property"+property);
    	    }
    	    else
    	    	if(c.getProperty().endsWith("first"))
        	    {
        	    	Node n=this;
        	    	if (n.getRDFClass_name().equals("rdf:list")) {
        	    		listNumbers(n, 0, retlist);
    				}
        	    	else
        	    		System.out.println("first -- c:"+n.getRDFClass_name());
        	    }
    	    	else
    	    		if(c.getProperty().endsWith("rest"))
    	    	    {
    	    	    	Node n=this;
    	    	    	if (n.getRDFClass_name().equals("rdf:list")) {
    	    	    		listNumbers(n, 0, retlist);
    					}
    	    	    	else
    	    	    		System.out.println("rest -- c:"+n.getRDFClass_name());
    	    	    }
    	    		else
	    	             System.out.println("--- "+property+" !="+c.getProperty()+" at:"+this.getRDFClass_name());

    	}
    	if(retlist.size()==2)
    	{
    		retlist.add(0d);
    		System.out.println("list size 2 added 0d");
    	}
    	double[] ret=new double[retlist.size()];
    	int i=0;
    	for(Double d:retlist)
    		ret[i++]=d.doubleValue(); 
    	return ret;
	}

	public void putNumberVector(String property,RealMatrix vector) {
    	for (Connection c : edges_out) {
    	    if(c.getProperty().equals(property))
    	    {
    	    	Node n=c.getPointedNode();
    	    	if (n.getRDFClass_name().equals("rdf:list")) {
    	    		putNumbers(n, 0, vector);
				}
    	    }
    	}
	}

	
	private void putNumbers(Node n, int i, RealMatrix vector) {
    	List<Connection> cons_lit = n.getEdges_literals();
    	for (Connection c : cons_lit) {
    		c.getPointedNode().setLexicalValue(""+vector.getEntry(i, 0));
    	}

		
		for (Connection e : n.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				
				putNumbers(e.getPointedNode(), i + 1, vector);
			}
		}
	}

	public List<Connection> getEdges_literals() {
		return edges_literals;
	}

	public List<Connection> getEdges_literals(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_literals) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	
	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public void setURI(String local_uri) {
		this.local_uri = local_uri;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

	public String getURI() {
		if (this.sameAs)
			return local_uri + "_sameAs";
		else
			return local_uri;
	}

	public AANodeData getAa() {
		return aa;
	}

	public void setAa(AANodeData aadata) {
		this.aa = aadata;
	}

	public String getLexicalValue() {
		return lexicalValue;
	}

	public boolean isSameAs() {
		return sameAs;
	}

	// ------------------------------------------->
	// For streaming

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodetype) {
		this.nodeType = nodetype;
	}

	public String getLiteral_chksum() {
		return literal_chksum;
	}

	public void setLiteral_chksum(String literal_chksum) {
		this.literal_chksum = literal_chksum;
	}

	public void setSameAs(boolean sameAs) {
		this.sameAs = sameAs;
	}

	public void setLocal_uri(String local_uri) {
		this.local_uri = local_uri;
	}

	public void setLexicalValue(String lexicalValue) {
		System.out.println("lex: "+this.getRDFClass_name()+" "+lexicalValue);
		this.lexicalValue = lexicalValue;
	}

	public void setEdges_in(List<Connection> edges_in) {
		this.edges_in = edges_in;
	}

	public void setEdges_out(List<Connection> edges_out) {
		this.edges_out = edges_out;
	}

	public void setEdges_literals(List<Connection> edges_literals) {
		this.edges_literals = edges_literals;
	}

    // Straigh path
	int maxPathLength=Integer.MAX_VALUE;
	List<STPath> paths =new ArrayList<STPath>();

	public boolean addPath(STPath p)
    {
    	if(getNodeType()!=Node.BLANKNODE)
    		return false;
    	if(p.getSteps_taken()>0)
    	{
    		if(p.getSteps_taken()<maxPathLength)
    		{
    			maxPathLength=p.getSteps_taken();
    			paths.clear();
    			paths.add(p);
    		}
    		else
        		if(p.getSteps_taken()==maxPathLength)
        		{
        			paths.add(p);
        		}
        		else
        			return false;
    	}
    	return true;
    }
    

    public void resetPaths()
    {
    	paths.clear();
    	maxPathLength=Integer.MAX_VALUE;
    }     

	
	public String guid_context=null;
    public void calculateNearestGUIDContext()
    {
    	StringChecksum sc=new StringChecksum(true);
    	if(paths.size()==0)
    		return;
    	List<String> pc=new ArrayList<String>();
    	for(STPath p:paths)
    	{
    		pc.add(p.getFirst_node().getLiteral_chksum());
    	}
    	Collections.sort(pc);
    	StringBuffer sb=new StringBuffer();
    	for(String s:pc)
    	{
    		sb.append(s+",");
    	}
    	if(guid_context==null)
    	{
    		sc.update(sb.toString());
    		guid_context=sc.getChecksumValue();  // Shorter string
    	}
    }     

	public List<String> getCrossings() {
		return crossings;
	}

	    
}
