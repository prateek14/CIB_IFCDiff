package fi.ni.nodenamer.datastructure;

import java.util.ArrayList;
import java.util.List;

import fi.ni.util.StringChecksum;

public class Path {
	private final StringChecksum checksum;
	int steps_taken;
	public Node last_node;
	final public List<Node> nodes = new ArrayList<Node>();

	public Path(Node blank_node) {
		checksum = new StringChecksum(true);
		nodes.add(blank_node);
		last_node = blank_node;
		update(blank_node.getLiteral_chksum());
		update(blank_node.getRDFClass_name());
		steps_taken = 0;
	}

	public Path(Path last_step, Connection edge) {
		for(Node node:last_step.nodes)
			nodes.add(node);
		checksum = last_step.checksum.copy();
		last_node = edge.getPointedNode();
		if(last_node==null)
			return;

		update(edge.getProperty());
		update(last_node.getLiteral_chksum());
		update(last_node.getRDFClass_name());
		steps_taken = last_step.steps_taken + 1;
	}

	public boolean addEdge(Connection e) {
		Node u=e.getPointedNode();
		if (u.getNodeType()!=Node.LITERAL)
		{
			if(!nodes.contains(u))
			{
			    nodes.add(u);
			    return true;
			}
			else
			   return false;
		}
		return false;
	}


	public void update(String txt) {
		checksum.update(txt);
	}

	public Node getLast_node() {
		return last_node;
	}

	public String getChecksum() {
		return checksum.getChecksumValue();
	}


	public int getSteps_taken() {
	    return steps_taken;
	}


	public List<Node> getNodes() {
	    return nodes;
	}


}
