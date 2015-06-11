package fi.ni;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.rdfcontext.signing.RDFC14Ner;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class CanonizationDiff {
	static long timestamp1;
	static long timestamp2;
	static public void test(String directory, String filename1,String filename2, String type) {

	GenStats gs1 = new GenStats();
	gs1.read(directory+ filename1, type);

	GenStats gs2 = new GenStats();
	gs2.read(directory+ filename2, type);
	timestamp1=System.nanoTime();
	
	
	showModelStats("1",gs1.getModel());
	
	RDFC14Ner r1=new RDFC14Ner(gs1.getModel());
	RDFC14Ner r2=new RDFC14Ner(gs2.getModel());
	
	ArrayList<String> s1=(ArrayList<String>)r1.getCanonicalStringsArray().clone();
	ArrayList<String> s2=(ArrayList<String>)r2.getCanonicalStringsArray().clone();
	
	
	Set<String> statements1=new HashSet<String>();
	Set<String> statements2=new HashSet<String>();
	
	statements1.addAll(s1);
	statements2.addAll(s2);
	
	
	System.out.println("statements 1: "+statements1.size());
	System.out.println("statements 2: "+statements2.size());
	int removed=0;
	for(String s:statements1)
	{
		if(!statements2.contains(s))
			removed++;
	}
	System.out.println("removed: "+removed);

	int added=0;
	for(String s:statements2)
	{
		if(!statements1.contains(s))
			added++;
	}
	System.out.println("added: "+added);
	timestamp2=System.nanoTime();
	System.out.println("CPU time:"+((timestamp2-timestamp1)/100000000));
	long curheap=(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()).getUsed()/(1024*1014);
	System.out.println("HEAP size:"+curheap);
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
     			anons++;
     		if(n.isLiteral())
     			literals++;
     	}
     	System.out.println("nodes:"+nodes.size()	);

     	System.out.println("iris:"+iris);
     	System.out.println("bn:"+anons);
     	System.out.println("literals:"+literals);
    }

    
	static public void testset() {
		test("c:/jo/","export1.n","export3.n", "N3");
		
	}

    
    public static void main(String[] args) {
    	testset();
		System.out.println("Done");
    }

}
