package fi.ni;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaStats {

	static long timestamp1;
	static long timestamp2;
    static public void testrun() {

	GenStats gs1 = new GenStats();
	gs1.read("c:/jo/T2.n", "N3");

	showModelStats("SMS_NOGEOM_1", gs1.getModel());
    }

   
    static public void showModelStats(String name,Model model)
    {
    	Set<RDFNode> nodes = new HashSet<RDFNode>();
    	
    	StmtIterator iter = model.listStatements();
        int i=0;
     	while (iter.hasNext()) {	
     	   Statement stmt      = iter.nextStatement();  // get next statement
     	   RDFNode  subject   = stmt.getSubject();     // get the subject
     	   RDFNode   object    = stmt.getObject();      // get the object     		
     	   i++;
     	   nodes.add(subject);
     	   nodes.add(object);
     	}
     	System.out.println(name+" statemet count: "+i);
     	
     	long iris=0;
     	long anons=0;
     	long literals=0;
     	for(RDFNode n:nodes)
     	{
     		if(n.isURIResource())
     			iris++;
     		if(n.isAnon())
     		{
     			anons++;
     			Resource rn=(Resource)n;
     			
     		}
     		if(n.isLiteral())
     			literals++;
     	}
     	System.out.println("nodes:"+nodes.size()	);

     	System.out.println("iris:"+iris);
     	System.out.println("bn:"+anons);
     	System.out.println("literals:"+literals);
    }

    
    public static void main(String[] args) {
	testrun();
    }

}
