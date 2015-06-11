package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fi.ni.TestParams;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.util.StringChecksum;

public class AlgSignSummer {

	
	public void setliteralChecksums(Set<Node> nodes, TestParams params) {
		for (Node node : nodes) {
			if (node.getNodeType() == Node.LITERAL)
				continue;


			if (node.getNodeType() == Node.IRINODE) {
				node.setLiteral_chksum(node.getURI());
			} else {
				setLChecksum4AnonNode(node,params);
			}
		}
	}
	
	public String algsign(Node node)
	{
	    	List<String> l_in = new ArrayList<String>();
	    	List<String> l_class = new ArrayList<String>();
	    	List<String> l_out = new ArrayList<String>();
	    	
	    	l_class.add("type"+node.getRDFClass_name());
	    	
	    	
	    	// IN: OSOITTAVAT LUOKAT tyypin mukaan
	    	List<Connection> cons_in = node.getEdges_in();
	    	for (Connection c : cons_in) {
	    	    if(c.getPointedNode().getNodeType() == Node.IRINODE)
	    	      l_in.add(c.getPointedNode().getURI());
	    	    else	
	    	      l_in.add("&"+c.getProperty());
	    	}

	    	// LITERAALIT
	    	List<Connection> cons_lit = node.getEdges_literals();
	    	for (Connection c : cons_lit) {
	    	    l_out.add(c.getProperty() + c.getPointedNode().getLexicalValue());
	    	}

	    	// OUT: OSOITETUT LUOKAT tyypin mukaan
	    	List<Connection> cons_out = node.getEdges_out();
	    	for (Connection c : cons_out) {
	    	    if(c.getPointedNode().getNodeType() == Node.IRINODE)
	    		      l_out.add(c.getProperty() + c.getPointedNode().getURI());
	    		    else	
	    		      l_out.add(c.getProperty() + "&");
	    	}
	    	
	    	
	    	Collections.sort(l_in);
	    	Collections.sort(l_class);
	    	Collections.sort(l_out);

	    	StringBuffer sb=new StringBuffer();
	    	boolean first=true;
	    	for(String s:l_in)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	for(String s:l_class)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	for(String s:l_out)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	return sb.toString();
	}


	private void setLChecksum4AnonNode(Node node,TestParams params) {
		String signature = algsign(node);
		StringChecksum lchecksum = new StringChecksum(params.isUseHash());
		lchecksum.update(signature);

		node.setLiteral_chksum(lchecksum.getChecksumValue());		
	}


}
