package fi.ni;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaDiff {
	static long timestamp1;
	static long timestamp2;
	
	static long ms_timestamp1;
	static long ms_timestamp2;
	
    static public void testrun() {

	GenStats gs1 = new GenStats();
	gs1.read("c:/jo/E1.n", "N3");

	GenStats gs2 = new GenStats();
	gs2.read("c:/jo/E3.n", "N3");
	
	System.out.println("is isomorphic 1: "+gs1.getModel().isIsomorphicWith(gs2.getModel()));
	System.out.println("is isomorphic 2: "+gs2.getModel().isIsomorphicWith(gs1.getModel()));
	

	timestamp1=System.nanoTime();
	ms_timestamp1=System.currentTimeMillis();
	
    Model d1=gs1.getModel().difference(gs2.getModel());
    Model d2=gs2.getModel().difference(gs1.getModel());
    
	System.out.println("Graphs ready GC");
	System.gc();
	System.out.println("Graphs ready");
	
	timestamp2=System.nanoTime();
	ms_timestamp2=System.currentTimeMillis();
	System.out.println("CPU time ms:"+(ms_timestamp2-ms_timestamp1));
	System.out.println("CPU time:"+((timestamp2-timestamp1)/1000000));
 
    }

  
    static public void showModelStats(String name,Model model)
    {
    	StmtIterator iter1 = model.listStatements();
        int i=0;
     	while (iter1.hasNext()) {	
     		iter1.nextStatement();
     		i++;
     	}
     	System.out.println(name+" statemet count: "+i);
    }

    
    public static void main(String[] args) {
	testrun();
    }

}
