package fi.ni.nodenamer.datastructure;

public class PathPosition {
  private final int inx;
  private final String pchsum;
  private final int path_length;
  private final String lastNode;
  
public PathPosition(int inx, String pchsum, int path_length,String lastNode) {
	super();
	this.inx = inx;
	this.pchsum = pchsum;
	this.path_length = path_length;
	this.lastNode=lastNode;
}

public int getInx() {
	return inx;
}
public String getPchsum() {
	return pchsum;
}
public int getPath_length() {
	return path_length;
}

public String getLastNode() {
	return lastNode;
}
  

}
