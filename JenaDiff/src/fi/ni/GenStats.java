package fi.ni;

import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class GenStats {
	Model model;

    public GenStats() {
    }


    public void read(String filename,String type) {
    	model = ModelFactory.createDefaultModel();
    	InputStream in = FileManager.get().open( filename );
    	if (in == null) {
    	    throw new IllegalArgumentException("File: " + filename + " not found");
    	}
    	
    	model.read(in, null,type);
    }


	public Model getModel() {
		return model;
	}

    
}
