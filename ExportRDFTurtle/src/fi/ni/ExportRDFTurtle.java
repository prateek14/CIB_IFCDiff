package fi.ni;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
public class ExportRDFTurtle {
	
	
	public ExportRDFTurtle(String directory, String filename) {
		
		try {
			Model model = readIFC(directory, filename);
			FileOutputStream fout = new FileOutputStream(new File("c:/jo/export3_t.n"));
			model.write(fout,"N-TRIPLES");
			fout.close();
			fout.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
    }
	

	public Model readIFC(String directory, String filename) throws IOException {

		ByteArrayOutputStream strout = new ByteArrayOutputStream();
		BufferedWriter log = new BufferedWriter(new OutputStreamWriter(strout));
		ExpressReader er = new ExpressReader("c:\\2014\\IFC2X3_Final.exp");
		//er.outputRDFS(log);
		IFC_ClassModel m1 = new IFC_ClassModel(directory+filename, er.getEntities(),
				er.getTypes(), "r1");
		m1.listRDF(log); // does close stream!!
		InputStream is = new ByteArrayInputStream(strout.toString().getBytes());

		Model model = ModelFactory.createDefaultModel();
		

		model.read(is, null, "N3");
		return model;

	}

	static public void export(String directory, String filename1) {
		new ExportRDFTurtle(directory,filename1);
		
	}



	public static void main(String[] args) {
		export("C:/2015_testdata/","export3.ifc");
	}


}


