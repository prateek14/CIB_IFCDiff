package fi.ni.nodenamer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.nodenamer.datastructure.STPath;

public class PathsTraverser {
	public PathsTraverser() {
		
	}

	public void setTheReachedNodesDirect(Node node, int maxpath) {
		Queue<STPath> q = new LinkedList<STPath>();
		STPath p0 = new STPath(node);

		q.add(p0);
		while (!q.isEmpty()) {
			STPath p1 = q.poll();
            
			if (p1.getSteps_taken() > maxpath) {
				continue;
			}

			handleCandidateLinks(q, p1, p1.getLast_node().getEdges_out());
		}
	}

	public void resetNodes()
	{
		nodes.clear();
	}
	
	public void setTheReachedNodesAll(Node node, int maxpath) {
		
		Queue<STPath> q = new LinkedList<STPath>();
		STPath p0 = new STPath(node);

		q.add(p0);
		while (!q.isEmpty()) {
			STPath p1 = q.poll();
            
			if (p1.getSteps_taken() > maxpath) {
				continue;
			}

			handleCandidateLinks(q, p1, p1.getLast_node().getEdges_in());
			handleCandidateLinks(q, p1, p1.getLast_node().getEdges_out());
		}
	}

	
	Map<Node, Integer> nodes = new HashMap<Node, Integer>();

	private int handleCandidateLinks(Queue<STPath> q, STPath p1, List<Connection> edges) {
		
		for (Connection e : edges) {

			Node u = e.getPointedNode();
			STPath p2 = new STPath(p1, e);
			Integer i = nodes.get(u);
			if (i == null) {
				i = new Integer(p2.getSteps_taken());
				nodes.put(u, i);
			}
			
			if (p2.getSteps_taken() == i) {
				p2.addEdge(e);
				if(u.addPath(p2))
				   q.add(p2);

			} 

		}
		return 1;
	}



}
