package fi.ni.nodenamer.datastructure;

public class Connection {
    private String property;
    private Node pointedNode;

    public Connection()
    {
    	
    }
    
    public Connection(String property, Node points_to) {
	super();
	this.property = property;
	this.pointedNode=points_to;
    }

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}


	public Node getPointedNode() {
		return pointedNode;
	}

	public void setPointedNode(Node pointedNode) {
		this.pointedNode = pointedNode;
	}

    
	}
