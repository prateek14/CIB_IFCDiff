package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.AlgSignSummer;
import fi.ni.nodenamer.BottomUpNamer;
import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.SimpleNamer;
import fi.ni.nodenamer.PathsTraverser;
import fi.ni.nodenamer.datastructure.Node;

public class NodeNamer {
	Set<Node> nodes = new HashSet<Node>();

	final public AlgSignSummer nodeliteralsummer;

	public NodeNamer() {
		nodeliteralsummer = new AlgSignSummer();
	}

	public void setInternalGraph(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public void createInternalGraph(String directory, String filename,
			String datatype) {
		RDFHandler ifch = new RDFHandler();
		OntModel model = ifch.handleRDF(directory, filename, datatype);
		InternalModel im = new InternalModel();

		im.handle(model, nodes);

	}

	private void name(List<Node> node_list, TestParams p) {
		nodeliteralsummer.setliteralChecksums(nodes, p);
		//BottomUpNamer buNamer = new BottomUpNamer();
		//buNamer.setBottomUpChecksums(nodes, p);

		PathsTraverser rn = new PathsTraverser();
		// For IRI contexts
		System.out.println("Contexts 1.............. for GUID");
		for (Node n : nodes) {
			if (n.getNodeType() == Node.IRINODE) {
				rn.setTheReachedNodesDirect(n, 700);
			}

		}
		System.out.println("Name Contexts 1..............");
		for (Node bn : nodes) {
			if (bn.getNodeType() == Node.BLANKNODE) {
				bn.calculateNearestGUIDContext();
			}
			bn.resetPaths();
		}
		rn.resetNodes();

		pathNaming(1,node_list);
		for(int n=2;n<p.maxsteps;n++)
		{
		  checkUniqueness(node_list);
		  pathNamingForCollisions(n,node_list);
		  System.out.println("size:"+node_list.size());
		}
		
	}

	private void pathNaming(int maxsteps,List<Node> node_list) {
		System.out.println("Initial Naming.."+maxsteps);
		SimpleNamer sn = new SimpleNamer();
		for (Node bn : node_list) {
			if (bn.getNodeType() == Node.BLANKNODE) {
				 bn.setURI(sn.getPSum(bn, maxsteps));
			}
		}
	}

	private void pathNamingForCollisions(int maxsteps,List<Node> node_list) {
		System.out.println("Naming.."+maxsteps);
		SimpleNamer sn = new SimpleNamer();
		long i=0;
		for (Node bn : node_list) {
			if (bn.getNodeType() == Node.BLANKNODE) {
				if (bn.isCollided())
				{
				 bn.setURI(sn.getPSum(bn, maxsteps));
				 i++;
				}
			}
		}
		System.out.println("list size:"+i);  // if zero..  end ...  datarakenteen kokoa tulisi  pienent‰‰.
	}


	private boolean isSame(Node bn, Node ex) {
		if (bn.getAa().hasBottomUp_cksum()) {
			if (ex.getAa().hasBottomUp_cksum()) {
				if (bn.getAa().getBottomUp_chksum()
						.equals(ex.getAa().getBottomUp_chksum()))
					return true;
			}
		}
		return false;
	}

	public void make(TestParams p) {
		
		for (Node bn : nodes) {
			if (bn.getNodeType()==Node.BLANKNODE)
				if(bn.getEdges_literals().size()==0 && bn.getEdges_out().size()==0)
					System.out.println("found");
		}

		for (Node bn : nodes) {
			if (bn.getRDFClass_name().contains("IfcPropertySet"))
				bn.setNodeType(Node.BLANKNODE);
		}
		
		
		
		List<Node> node_list = new ArrayList<Node>();
		for (Node n : nodes)
			node_list.add(n);
		java.util.Collections.shuffle(node_list);

		name(node_list, p);

		makeUnique(node_list);
	}


	private void makeUnique(List<Node> node_list) {
		

		checkUniqueness(node_list);
		//System.gc();
		Map<String, Integer> class_inx = new HashMap<String, Integer>();
		for (Node bn : node_list) {
			if (bn.isCollided()) {
				{
					Integer count = class_inx.get(bn.getURI());
					if (count == null)
						count = 0;
					class_inx.put(bn.getURI(), count + 1);
					if (count != 0) // to be comparable with 0 count, when 1
									// removed from list of 2 items
						bn.setURI(bn.getURI() + ".#" + count);
				}
			}
		}
	}

	private void checkUniqueness(List<Node> node_list) {
		for (Node bn : node_list) {
			bn.setCollided(false);
		}
		Map<String, Node> lchecksums = new HashMap<String, Node>();
		for (Node bn : node_list) {
			Node ex = lchecksums.put(bn.getURI(), bn);
			if (ex != null) {
				if (!isSame(bn, ex)) {

					if (ex.guid_context != null && bn.guid_context != null
							&& !ex.guid_context.equals(bn.guid_context)) {
						
						ex.setURI(ex.guid_context + ex.getURI());
						bn.setURI(bn.guid_context + bn.getURI());
					} else {
						ex.setCollided(true);
						bn.setCollided(true);
					}
				}
			}
		}
	}

	public Set<Node> getNodes() {
		return nodes;
	}

}
