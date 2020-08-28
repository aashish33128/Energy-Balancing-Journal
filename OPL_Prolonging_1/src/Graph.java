import java.util.ArrayList;

public class Graph {
	
	int v; //number of vertices
	ArrayList<Integer>[] adjList;
	
	
	public Graph(int v)
	{
		this.v = v;
		initadjList();
		
	}
	
	@SuppressWarnings("unchecked")
	public void initadjList()
	{
		adjList = new ArrayList[v];
		for(int i=0;i<v;i++)
		{
			adjList[i] = new ArrayList<>();
		}
	}
	
	public void addEdge(int u , int v)
	{
		adjList[u].add(v);
	}
}
