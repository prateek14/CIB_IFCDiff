package fi.ni;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;

public class ModelHandler {
	Set<Node> nodes_version1 = new HashSet<Node>();
	Set<Node> nodes_version2 = new HashSet<Node>();

	public ModelHandler() {
	}

	public void handle(String directory,String filename, String datatype,String setname) {
		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF(directory, filename, datatype);

		InternalModel im1=new InternalModel();
		im1.handle(model, nodes_version1);

		randomFilterModel(model);  // remove items from the same model!
		InternalModel im2=new InternalModel();
		im2.handle(model, nodes_version2);
		save(filename,setname);
	}

	
	private void randomFilterModel(OntModel model)
	{
		Random randomGenerator = new Random(System.currentTimeMillis());
		StmtIterator iter1 = model.listStatements();

		List<Statement> removals=new ArrayList<Statement>();
		while (iter1.hasNext()) {
			Statement stmt= iter1.nextStatement(); // get next statement
			int r=randomGenerator.nextInt(50);
			if(r==0)
			{
				removals.add(stmt);
				System.out.println("removed: "+stmt);
				continue;  // remove 1/50
			}
		}
		model.remove(removals);

	}
	public Set<Node> getNodes_verson1() {
		return nodes_version1;
	}

	public Set<Node> getNodes_verson2() {
		return nodes_version2;
	}

	
	public void save(String dataset_name,String setname) {
		save(nodes_version1,dataset_name+"_v1_"+setname);
		save(nodes_version2,dataset_name+"_v2_"+setname);
	};

	private void save(Set<Node> nodes,String dataset_name) {
		//XStream x=new XStream();
		//System.out.println(x.toXML(nodes_version1));
		
		
		XMLEncoder e = null;
		try {
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
					"c:\\2014\\datasets\\"+dataset_name+".xml")));
			e.writeObject(nodes);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (e != null)
				e.close();
		}
	}

	public void open(int ver,String dataset_file) {
		
		FileInputStream fis;
		XMLDecoder xmlDecoder=null;
		try {
			fis = new FileInputStream("c:\\2014\\datasets\\"+dataset_file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			xmlDecoder = new XMLDecoder(bis);
			if(ver==1)
			   nodes_version1 = (Set<Node> ) xmlDecoder.readObject();
			else
			   nodes_version2 = (Set<Node> ) xmlDecoder.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(xmlDecoder!=null)
			  xmlDecoder.close();
		}
		Set<Node> dummies=new HashSet<Node>();
		for(Node n:nodes_version1)
		{
			for (Connection e : n.getEdges_out()) {

				if (e.getPointedNode() == null) {
					dummies.add(n);
					continue;
				}

			}
			for (Connection e : n.getEdges_in()) {

				if (e.getPointedNode() == null) {
					dummies.add(n);
					continue;
				}

			}
		}
		for(Node n:dummies)
		{
			nodes_version1.remove(n);
		}
		dummies.clear();
		for(Node n:nodes_version2)
		{
			for (Connection e : n.getEdges_out()) {

				if (e.getPointedNode() == null) {
					dummies.add(n);
					continue;
				}

			}
			for (Connection e : n.getEdges_in()) {

				if (e.getPointedNode() == null) {
					dummies.add(n);
					continue;
				}

			}
		}
		for(Node n:dummies)
		{
			nodes_version2.remove(n);
		}
		
	}

	
	
	public Set<Node> copy_nodes(Set<Node> nodes) {
		ByteArrayOutputStream strout = new ByteArrayOutputStream();
		
		XMLEncoder e = null;
		e = new XMLEncoder(strout);
		e.writeObject(nodes);
		e.close();
		String objectstream=strout.toString();
		
		InputStream is = new ByteArrayInputStream(objectstream.getBytes());
		XMLDecoder xmlDecoder = new XMLDecoder(is);
		
		@SuppressWarnings("unchecked")
		Set<Node> new_nodes = (Set<Node>) xmlDecoder.readObject();
		if(xmlDecoder!=null)
			  xmlDecoder.close();
		return new_nodes;
	}


}
