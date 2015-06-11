package fi.ni.nodenamer;

import java.util.List;
import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;

public class TripleLister {
	
	static public TripleSet listStatements(Node node) {
		TripleSet tripleset = new TripleSet();
		Set<String> triple_strings = tripleset.getTriple_strings();
        
		// LITERAALIT
		List<Connection> cons_lit = node.getEdges_literals();
		for (Connection c : cons_lit) {
			String key = node.getURI() + " " + c.getProperty() + " "
					+ c.getPointedNode().getLexicalValue();			
			triple_strings.add(key);
		}

		// OUT: OSOITETUT LUOKAT tyypin mukaan
		List<Connection> cons_out = node.getEdges_out();
		for (Connection c : cons_out) {
			String key = node.getURI() + " " + c.getProperty() + " "
					+ c.getPointedNode().getURI();
			triple_strings.add(key);
		}

		return tripleset;
	}

}
