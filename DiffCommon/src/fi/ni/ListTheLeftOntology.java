package fi.ni;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fi.ni.nodenamer.RDFHandler;


public class ListTheLeftOntology{
	

	public ListTheLeftOntology() {
		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF();

		StmtIterator iter1 = model.listStatements();

		while (iter1.hasNext()) {			
			Statement stmt = iter1.nextStatement();
			System.out.println(stmt);
		}
		//InternalModel im1=new InternalModel();
		//im1.handle(model, nodes_version1);
	
		
	}

	
	static public void test() {
		new ListTheLeftOntology();
	}


	static public void testset() {
		test();
	}

	public static void main(String[] args) {

		testset();
		System.out.println("Done");
	}
}
