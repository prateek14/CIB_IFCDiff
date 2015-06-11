package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.text.MaskFormatter;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.nodenamer.datastructure.Path;
import fi.ni.util.StringChecksum;

public class SimpleNamer {

	public SimpleNamer() {
	}

	public String getPSum(Node bn, int steps) {
		try {
			List<Path> cpaths = getCandidatepathsShort(bn, steps);
			Set<Node> nodes = new HashSet<Node>();
			for (Path p : cpaths) {
				nodes.addAll(p.getNodes());
			}
			List<String> csums = new ArrayList<String>();
			for (Path p : cpaths) {
				csums.add(p.getChecksum());
			}
			
			List<String> crosssings = null;
		    crosssings = findCrossings(cpaths, nodes);
		    csums.addAll(crosssings);
		    
			Collections.sort(csums);

			StringChecksum checksum = new StringChecksum(true);
			for (String s : csums) {
				checksum.update(s);
			}
			return checksum.getChecksumValue();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	   private List<String> findCrossings(List<Path> cpaths, Set<Node> nodes) {
			List<String> ret = new ArrayList<String>();
			for (Path path : cpaths) {
			    for (int i = 0; i < path.getNodes().size(); i++) {
				Node node = path.getNodes().get(i);
				node.getCrossings().add(path.getChecksum() + "." + i);

			    }
			}
			for (Node node : nodes) {
			    if (node.getCrossings().size() > 1) {
				Collections.sort(node.getCrossings());
				StringChecksum checksum = new StringChecksum(true);

				for (String val : node.getCrossings()) {
				    checksum.update(val);
				}
				ret.add(node.getLiteral_chksum() + "=" + checksum.getChecksumValue());
			    }
			    node.getCrossings().clear();
			}
			return ret;
		    }
	   


	private List<Path> getCandidatepathsShort(Node bn, int maxpath) {
	
		List<Path> candidate_paths = new ArrayList<Path>();
		Queue<Path> q = new LinkedList<Path>();
		Path p0 = new Path(bn);

		q.add(p0);
		while (!q.isEmpty()) {
			Path p1 = q.poll();

			if (p1.getSteps_taken() >= maxpath) {
				candidate_paths.add(p1);
				continue;
			}

			handleCandidateLinks("OUT", candidate_paths, q, p1, p1.getLast_node().getEdges_out());
			handleCandidateLinks("IN", candidate_paths, q, p1, p1.getLast_node().getEdges_in());
		}

		return candidate_paths;
	}

	private int handleCandidateLinks(String label, List<Path> candidate_paths, Queue<Path> q, Path p1, List<Connection> edges) {
		if ((edges == null) || (edges.size() == 0)) {
			p1.update(label + " termine");
			candidate_paths.add(p1);
			return 0;
		}
		int max_number = 15;
		if (edges.size() > max_number) {
			p1.update(label + " MAX");
			candidate_paths.add(p1);
			return 0;
		}

		for (Connection e : edges) {
			Node u = e.getPointedNode();
			Path p2 = new Path(p1, e);
			if(p2.last_node==null)
				continue;

			if (p2.addEdge(e)) {
				q.add(p2);
			} else {
				p2.update(u.getLiteral_chksum());
				p2.update("cycle");
				candidate_paths.add(p2);
			}
		}
		return 1;
	}

}
