package fi.ni;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestParams {
   final int maxsteps;
   final boolean useHash;



public TestParams(int maxsteps, boolean useHash) {
	super();
	this.maxsteps = maxsteps;
	this.useHash = useHash;
}



public int getMaxsteps() {
	return maxsteps;
}


public boolean isUseHash() {
	return useHash;
}


public String toString() {
	StringBuffer sb=new StringBuffer();
	Method methods[] = this.getClass().getMethods();
	for (Method method:methods) {		    
		if (method.getName().startsWith("get")&&!method.getName().startsWith("getClass")) {
			Object o;
			try {
				o = method.invoke(this);
				sb.append(" "+method.getName().substring(3)+":"+o.toString());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (method.getName().startsWith("is")) {
			Object o;
			try {
				o = method.invoke(this);
				sb.append(" "+method.getName().substring(2)+":"+o.toString());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	return sb.toString();
}

}
