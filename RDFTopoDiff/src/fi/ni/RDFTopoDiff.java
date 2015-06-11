package fi.ni;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.topodiff.algo.TDiffHelper;
import org.topodiff.algo.ToposortGraphProcessor;
import org.topodiff.graph.GraphView;
import org.topodiff.graph.Node;
import org.topodiff.io.TripleReceiver;
import org.topodiff.util.Filters;
import org.topodiff.util.hash.MessageDigesters;



public class RDFTopoDiff {
	static long timestamp1;
	static long timestamp2;
	private final static Set<Node> DEFAULT_PROPS_TO_FOLLOW = new HashSet<Node>();
	public static interface GraphSortingLaw {
		public void sort(GraphView model, TripleReceiver tripleWriter);
	}
	
	private final static GraphSortingLaw ALGO = new GraphSortingLaw() {
		public void sort(GraphView model, TripleReceiver tripleWriter) {
			MessageDigest hashAlgo = MessageDigesters.createSimpleHash32();
			
			ToposortGraphProcessor adapter = new ToposortGraphProcessor(tripleWriter, Filters.inList(DEFAULT_PROPS_TO_FOLLOW), hashAlgo);
			adapter.process(model);
		}
		
	};
	
	
	public RDFTopoDiff(String file1,String file2) {
		List<String> model1= new ArrayList<String>();
		List<String> model2= new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       model1.add(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       model2.add(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] source1 = new String[model1.size()];
		source1 = model1.toArray(source1);
		
		String[] source2 = new String[model2.size()];
		source2 = model2.toArray(source2);
		timestamp1=System.nanoTime();
		
		TDiffHelper.testDiffSummary(source1, source2, ALGO);
		timestamp2=System.nanoTime();
		System.out.println("CPU time:"+((timestamp2-timestamp1)/100000000));
		long curheap=(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()).getUsed()/(1024*1014);
		System.out.println("HEAP size:"+curheap);
	}
	

	
	public static void main(String[] args) {
       new RDFTopoDiff("c:/jo/export1_t.n","c:/jo/export3_t.n");		
	}

}
