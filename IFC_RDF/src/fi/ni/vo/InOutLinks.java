package fi.ni.vo;


public class InOutLinks implements Comparable<InOutLinks>
{
	int in;
	int out;
	public InOutLinks(int in, int out) {
	    super();
	    this.in = in;
	    this.out = out;
	}
		
	private String f(int n)
	{
	    return String.format("%06d", n);
	}
	public String toString() {
	    return f(out)+","+f(in);
	}
	
    public boolean equals(Object obj) {
        InOutLinks i=(InOutLinks)obj;
        if((i.in==this.in)&&(i.out==this.out))
    	    return true;
        else
    	     return false;
    }
    
	public int compareTo(InOutLinks o) {
        return -o.toString().compareTo(this.toString());
	}
	
	public int distance(InOutLinks o)
	{
	    return Math.abs(o.in-this.in) + Math.abs(o.out - this.out);
	}
}
