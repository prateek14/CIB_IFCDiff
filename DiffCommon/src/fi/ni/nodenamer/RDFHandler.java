package fi.ni.nodenamer;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

import fi.ni.ExpressReader;
import fi.ni.IFC_ClassModel;

public class RDFHandler {

	
	public OntModel handleRDF() {
		OntModel temp_model=null;
		try {
				temp_model = readIFC();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("statements:"
				+ temp_model.listStatements().toList().size());
		OntModel  model=filterModel(temp_model);
		System.out.println("filtered statements:"
				+ model.listStatements().toList().size());
		return model;
	}

	
	public OntModel handleRDF(String directory, String filename, String datatype) {
		OntModel temp_model=null;
		try {
			if (datatype.equals("IFC"))
				temp_model = readIFC(directory, filename);
			else
				temp_model = readRDF(directory, filename, datatype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("statements:"
				+ temp_model.listStatements().toList().size());
		OntModel  model=filterModel(temp_model);
		System.out.println("filtered statements:"
				+ model.listStatements().toList().size());
		return model;
	}


	
	private OntModel filterModel(OntModel temp_model)
	{
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
		StmtIterator iter1 = temp_model.listStatements();

		while (iter1.hasNext()) {
			Statement stmt = iter1.nextStatement(); // get next statement
			if (isFiltered(stmt.getPredicate().getLocalName()))
				continue;
			if (stmt.getObject().toString().contains("#Property"))
				continue;
			if (stmt.getObject().toString().contains("#Class"))
				continue;

			if (stmt.getPredicate().getURI().contains("subClassOf"))
				continue;
			if (stmt.getPredicate().getURI().contains("domain"))
				continue;
			//if (stmt.getPredicate().getURI().contains("ownerHistory"))
			//	continue;

			model.add(stmt);
		}
		return model;

	}
	
	private boolean isFiltered(String txt) {
		if (txt.equals("line_number"))
			return true;
		if (txt.equals("globalId"))
			return true;
		return false;
	}	
	

	public OntModel readIFC(String directory, String filename) throws IOException {

		ByteArrayOutputStream strout = new ByteArrayOutputStream();
		BufferedWriter log = new BufferedWriter(new OutputStreamWriter(strout));
		ExpressReader er = new ExpressReader("c:\\2014\\IFC2X3_Final.exp");
		er.outputRDFS(log);
		IFC_ClassModel m1 = new IFC_ClassModel(directory+filename, er.getEntities(),
				er.getTypes(), "r1");
		m1.listRDF(log); // does close stream!!
		InputStream is = new ByteArrayInputStream(strout.toString().getBytes());

		OntModel model = ModelFactory
				.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);

		model.read(is, null, "N3");
		return model;

	}
	
	public OntModel readIFC() throws IOException {

		ByteArrayOutputStream strout = new ByteArrayOutputStream();
		BufferedWriter log = new BufferedWriter(new OutputStreamWriter(strout));
		ExpressReader er = new ExpressReader("c:\\2014\\IFC2X3_Final.exp");
		er.outputRDFS(log);
		log.flush();
		InputStream is = new ByteArrayInputStream(strout.toString().getBytes());

		OntModel model = ModelFactory
				.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF);

		model.read(is, null, "N3");
		return model;

	}

	public OntModel readRDF(String directory, String filename, String type) {
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
		InputStream in = FileManager.get().open(directory+filename);
		if (in == null) {
			throw new IllegalArgumentException("File: " + filename
					+ " not found");
		}

		model.read(in, null, type);
		return model;
	}


}
