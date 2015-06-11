package fi.ni;

import java.util.HashSet;
import java.util.Set;

import fi.ni.nodenamer.TripleLister;
import fi.ni.nodenamer.datastructure.Node;
public class SP_test {
	long timestamp1;
	long timestamp2;
	NodeNamer gs1 = new NodeNamer();
	NodeNamer gs2 = new NodeNamer();
	SimplifiedGeom tr1 = new SimplifiedGeom();
	SimplifiedGeom tr2 = new SimplifiedGeom();
	
	public SP_test(String codebase, String application_name, boolean report,String directory, String filename1, String filename2, String type) {
		
		if(type.equals("SAVED"))
		{
             ModelHandler mh=new ModelHandler();
             mh.open(1,filename1);
             mh.open(2,filename2);
             
             gs1.setInternalGraph(mh.getNodes_verson1());
             gs2.setInternalGraph(mh.getNodes_verson2());
		}
		else{
		    gs1.createInternalGraph(directory,filename1, type);
			gs2.createInternalGraph(directory,filename2, type);

		}
		timestamp1=System.nanoTime();
		tr1.setInternalGraph(gs1.getNodes());		 
		tr1.calculatePlacementsCksum();

		tr2.setInternalGraph(gs2.getNodes());		 
		tr2.calculatePlacementsCksum();
}
	
	
	public RetVal test(TestParams p) {
		
		gs1.make(p);
		//System.gc();

		gs2.make(p);
		System.out.println("Graphs ready GC");
		System.gc();
		System.out.println("Graphs ready");

		Set<String> statements1=new HashSet<String>();
		Set<String> statements2=new HashSet<String>();
		
		for (Node n1 : gs1.getNodes()) 
		{
			   if(n1.getNodeType() != Node.LITERAL)
				statements1.addAll(TripleLister.listStatements(n1).getTriple_strings());							
		}
		
		for (Node n1 : gs2.getNodes()) {
			  if(n1.getNodeType() != Node.LITERAL)
				statements2.addAll(TripleLister.listStatements(n1).getTriple_strings());				
		}
		
		
		int removed=0;
		for(String s1:statements1)
		{
			if(!statements2.contains(s1))
			{
				removed++;
			}
		}

		int added=0;
		for(String s1:statements2)
		{
			if(!statements1.contains(s1))
			{
				added++;
			}
		}
		
		timestamp2=System.nanoTime();
		
		System.out.println("CPU time:"+((timestamp2-timestamp1)/100000000));
        return new RetVal(removed,added);		
	}


	static public void test(boolean report, int maxsteps, boolean useHash) {

		TestParams p = new TestParams(maxsteps, useHash);
		System.out.println(p);
		System.out.println("Huge Extended Plus");
		SP_test hs=new SP_test("common","common",report,"C:/2015_test/","0.IFC", "1.IFC", "IFC");
		RetVal ret=hs.test(p);
		System.out.println("Result: "+ret.removed+" "+ret.added);
		
	}

	public static void main(String[] args) {
		test(true,15, true);
	}


}


