package fi.ni.nodenamer.datastructure;

import java.util.ArrayList;
import java.util.List;

import fi.ni.util.StringChecksum;

public class STPath {
	final Node first_node;
	int steps_taken;
	Node last_node;
	final private List<Connection> path_links = new ArrayList<Connection>();

	public STPath(Node node) {
		this.first_node=node;
		last_node = node;
		steps_taken = 0;
	}

	public STPath(STPath old, Connection edge) {
		for(Connection c:old.path_links)
			path_links.add(c);
		this.first_node=old.first_node;
		last_node = edge.getPointedNode();
		steps_taken = old.steps_taken + 1;
	}

	public void addEdge(Connection e) {
		
		path_links.add(e);

	}

	public Node getLast_node() {
		return last_node;
	}


	public int getSteps_taken() {
	    return steps_taken;
	}

	
	
	public String getChecksum(boolean usehash)
	{
		StringChecksum sc=new StringChecksum(usehash);
		sc.update(first_node.getLiteral_chksum());
		for(Connection c: path_links)
		{
			sc.update(c.getProperty());
		}
		
		return sc.getChecksumValue();
	}

	public Node getFirst_node() {
		return first_node;
	}

	public List<Connection> getPath_links() {
		return path_links;
	}
	
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("--"+first_node.getRDFClass_name());
	    for(Connection c:path_links)
	    	sb.append("."+c.getProperty());
	    sb.append(":"+last_node.getRDFClass_name());
	    return sb.toString();
	}
}
