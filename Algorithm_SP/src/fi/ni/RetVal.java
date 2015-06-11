package fi.ni;

public class RetVal
{
	int removed=0;
	int added=0;
	
	public RetVal(int removed, int added) {
		super();
		this.removed = removed;
		this.added = added;
	}
	
	public int getCompValue()
	{
		return removed+added;
		
	}
	
}