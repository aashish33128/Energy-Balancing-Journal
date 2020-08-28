import java.util.ArrayList;

public class Node {
	
	//Node leftChild;
	//Node rightChild;
	//Edge edge = new Edge();
	int id = 0;
	int value=0;
	boolean isSource = false;
	boolean isDest = false;
	ArrayList<Edges> links = new ArrayList<>();
	int numTimesinTransaction = 0;
	
	
	
	public Node(int id, int value)
	{
			this.id = id;
			this.value= value;
			
	}
	
	public void setSource(boolean value)
	{
		this.isSource = value;
	}
	
	public void setDest(boolean dest)
	{
		this.isDest = dest;
	}
}
