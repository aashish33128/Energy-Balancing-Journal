import java.util.ArrayList;
import java.util.List;

public class SearchPath {
	
	int v = 0;
	Graph G;
	int[][] edges;
	//ArrayList<List<Integer>> paths = new ArrayList<List<Integer>>();
	int threshold = 0;
	

	public SearchPath(int v, Graph G, int[][] edges, int threshold)
	{
		this.v = v;
		this.G = G;
		this.edges = edges;
		this.threshold = threshold;
	}
	

	public List<List<Integer>>  getAllPaths(int s, int d)  
    { 
        boolean[] isVisited = new boolean[v]; 
        ArrayList<Integer> pathList = new ArrayList<>(); 
         List<List<Integer>> pathList_new = new ArrayList<>(); 
        //add source to path[] 
        pathList.add(s); 
      
          
        //Call recursive utility 
        getAllPathsUtil(s, d, isVisited, pathList, pathList_new); 
        
        
      //  System.out.println("New list for  " + s + " -> " + d + " is " + pathList_new);
    	List<List<Integer>> selected = new ArrayList<List<Integer>>();
    	
    	
        //Check all paths within time threshold
        for(int i=0; i<pathList_new.size();i++)
        {
        
        	int cost = 0;
        	for(int j=0;j<pathList_new.get(i).size()-1 ; j++)
        	{
        		
        		cost = cost + edges[pathList_new.get(i).get(j)][pathList_new.get(i).get(j+1)];
        		
        	}
        	
      
        	
        	if(cost<= threshold)
        	{
        		//System.out.println("Selected Paths " + pathList_new.get(i) + " , " + cost);
        		selected.add(pathList_new.get(i));
        	}
        	
        }
        
        return selected;
    } 
  
    // A recursive function to print 
    // all paths from 'u' to 'd'. 
    // isVisited[] keeps track of 
    // vertices in current path. 
    // localPathList<> stores actual 
    // vertices in the current path 
	
	public boolean exceedThreshold(List<Integer> localPathList)
	{
		int sum = 0;
		for(int i=0;i<localPathList.size()-1;i++)
		{
			sum = sum + edges[localPathList.get(i)][localPathList.get(i+1)];
			if(sum > threshold)
			{
				return true;
			}
		}
		
		return false;
	}
    private void getAllPathsUtil(Integer u, Integer d, 
                                    boolean[] isVisited, 
                            List<Integer> localPathList,   List<List<Integer>> newList ) { 
          
        // Mark the current node 
    	
    	if(localPathList.size()>1)
    	{
    		if(exceedThreshold(localPathList))
    		{
    			return;
    		}
    	}
    	
		/*
		 * if(isVisited[u] == true) { return; }
		 */
        isVisited[u] = true; 
        //List<Integer> copyLocalPathList = new ArrayList<Integer>();
      //  copyLocalPathList.add(u);
          
        if (u.equals(d))  
        { 
           // System.out.println(localPathList );
            List<Integer> path1 = new ArrayList<Integer>();
            path1.addAll(localPathList);
            newList.add(path1);
           
            // if match found then no need to traverse more till depth 
            isVisited[u]= false; 
            return; 
        } 
      
          
        // Recur for all the vertices 
        // adjacent to current vertex 
        for (Integer i : G.adjList[u])  
        { 
    
            if (!isVisited[i]) 
            { 
                // store current node  
                // in path[] 
                localPathList.add(i); 
               // copyLocalPathList.add(i);
          
                getAllPathsUtil(i, d, isVisited, localPathList, newList); 
                  
                // remove current node 
                // in path[] 
                localPathList.remove(i); 
            } 
        } 
          
        // Mark the current node 
        isVisited[u] = false; 
    } 
   

       
   
}
