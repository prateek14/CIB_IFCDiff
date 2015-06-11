package fi.ni;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.bag.TreeBag;

import fi.ni.vo.InOutLinks;


public class InOutBag {

    private class AccComparator implements Comparator<InOutLinks>
    {
	public int compare(InOutLinks o1, InOutLinks o2) {	    
	    return o1.toString().compareTo(o2.toString());
	}	
    }    
    AccComparator acc_comparator=new AccComparator();
    

    // cordinate,bag
    SortedMap<InOutLinks,TreeBag>  cordinate_class_bags_acc=new TreeMap<InOutLinks,TreeBag>(acc_comparator);  
    Map<String,Set<InOutLinks>>  class_cordinate_bags=new HashMap<String,Set<InOutLinks>>();  
    
    public void add(int incount,int outcount,String class_name)
    {
	InOutLinks inout=new InOutLinks(incount, outcount);
        TreeBag tb_cocl=cordinate_class_bags_acc.get(inout);
        if(tb_cocl==null)          
        {
            tb_cocl=new TreeBag();
            cordinate_class_bags_acc.put(inout, tb_cocl);
        }
        tb_cocl.add(class_name);
        
        Set<InOutLinks> tb_clco=class_cordinate_bags.get(class_name);
        if(tb_clco==null)          
        {
            tb_clco=new HashSet<InOutLinks>();
            class_cordinate_bags.put(class_name, tb_clco);
        }
        tb_clco.add(inout);
    }
    
    private boolean isnear(String class_name,InOutLinks mypoint)
    {
	        Set tb=class_cordinate_bags.get(class_name);
	        Iterator<InOutLinks> it=tb.iterator();
	        boolean ret=false;
	        while(it.hasNext())
	        {
	               InOutLinks point=it.next();
	               int distance=point.distance(mypoint);
	               if(distance>0)
	               {
                           if(distance<10)
                           {
                               ret=true;
                           }
	               }
	        }
	return ret;
    }
    
    public Map<String,List<InOutLinks>> getOnes()
    {
	Map<String,List<InOutLinks>> ret=new HashMap<String,List<InOutLinks>>();
	       for(Map.Entry<InOutLinks,TreeBag> entry : cordinate_class_bags_acc.entrySet()) {
		   InOutLinks key = entry.getKey();
	           TreeBag value = entry.getValue();
	           //System.out.println(key+":");
	           Iterator<String> it=value.uniqueSet().iterator();
	           while(it.hasNext())
	           {
	               String class_name=it.next();
	               if(value.getCount(class_name)==1)
	               {
	                 List<InOutLinks> outlist=ret.get(class_name);
	                 if(outlist==null)
	                 {
	        	   outlist=new ArrayList<InOutLinks>();
	        	   ret.put(class_name, outlist);
	                 }
	                  //System.out.println(class_name+","+value.getCount(class_name));
	                  if(!isnear(class_name,key))
	                     outlist.add(key);
	               }
	           }
	       }
        return ret;
    }
    
    public static void main(String[] args) {
	InOutBag ob=new InOutBag();
	ob.add(1, 2, "a");
	ob.add(2, 2, "t");
	ob.add(2, 20, "a");
	ob.add(2, 2, "t");

	ob.add(3, 30, "a");
	ob.add(3, 3, "a");
	ob.add(3, 3, "c");

	Map<String,List<InOutLinks>> ones=ob.getOnes();
	for(Map.Entry<String,List<InOutLinks>> entry : ones.entrySet()) {
	           String class_name = entry.getKey();
	           List<InOutLinks> value = entry.getValue();
	           System.out.println(class_name+", "+value);
	}
    }

}
