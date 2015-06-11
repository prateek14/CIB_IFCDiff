package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.util.StringChecksum;

public class SimplifiedGeom {
	Set<Node> nodes = new HashSet<Node>();
	public SimplifiedGeom() {
	}

	public void setInternalGraph(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public void calculatePlacementsCksum() {
		for (Node n : nodes) {
			if (n.getRDFClass_name().endsWith("IfcLocalPlacement")) {

				List<Connection> place_relTo = n.getEdges_out("placementRelTo");
				if (place_relTo.size() == 0) {
					// ROOT placement
					List<Double>  t = transformData(n);
					StringChecksum checksum=new StringChecksum(true);
					if (t != null) {
			            for(Double d:t)
			            	checksum.update(d+"");
						traversePlacements(n, checksum);
					}
				}
			}
		}
		checkUnique(); 
	}
	
	private void checkUnique() {
		Map<String, Integer> class_inx = new HashMap<String, Integer>();
		Map<String, Node> location = new HashMap<String, Node>();
		for (Node bn : nodes) {
			bn.setCollided(false);
		}
		for (Node bn : nodes) {
			Node ex = location.put(bn.local_placement_chk, bn);
			if (ex != null) {
				ex.setCollided(true);
				bn.setCollided(true);
			}
		}

		// Set the unique locations to IRIs
		for (Node bn : nodes) {
			if(bn.getNodeType()==Node.BLANKNODE)
			if (!bn.isCollided() && bn.local_placement_chk!=null) {				
				bn.setURI(bn.getURI()+"."+bn.local_placement_chk);
				bn.setNodeType(Node.IRINODE);
			}
		}
	}

	
	private void traversePlacements(Node n, StringChecksum in_checksum) {
		StringChecksum checksum=null;
		List<Double>  t = transformData(n);
		
		if (t != null) {
			checksum = in_checksum.copy();					
            for(Double d:t)
            	checksum.update(d+"");
			n.local_placement_chk = checksum.getChecksumValue();
		}
		List<Connection> in_place_relTo = n.getEdges_in("placementRelTo");
		if (!in_place_relTo.isEmpty())
			for (Connection c : in_place_relTo) {
				traversePlacements(c.getPointedNode(), checksum);  // kaikki
			}
	}

	
	private List<Double> transformData(Node n) {
		List<Double> ret=new ArrayList<Double>();
		List<Connection> rp = n.getEdges_out("relativePlacement");
		if (!rp.isEmpty()) {
			double[] axis = null;
			double[] ref_direction = null;
			double[] location = null;

			List<Connection> ax = rp.get(0).getPointedNode().getEdges_out("axis");
			if (!ax.isEmpty()) {
				axis = ax.get(0).getPointedNode().giveEdges_outAsNumberVector("directionRatios");
			}
			List<Connection> ref = rp.get(0).getPointedNode().getEdges_out("refDirection");
			if (!ref.isEmpty()) {
				ref_direction = ref.get(0).getPointedNode().giveEdges_outAsNumberVector("directionRatios");
			}
			List<Connection> lo = rp.get(0).getPointedNode().getEdges_out("location");
			if (!lo.isEmpty()) {
				location = lo.get(0).getPointedNode().giveEdges_outAsNumberVector("coordinates");
			}

			if(axis!=null)
				for(double d:axis)
					ret.add(d);
			if(ref_direction!=null)
				for(double d:ref_direction)
					ret.add(d);
			if(location!=null)
				for(double d:location)
					ret.add(d);
			return ret;
		}
		return null;
	}
}
