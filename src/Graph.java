import java.util.ArrayList;
import java.util.List;

/**
 * Graph class used to keep track of territories in the Bones Battle map
 * 
 * @author Phillip Benoit
 *
 */
public class Graph {
    
    /**
     * base data structure
     */
	private ArrayList<ArrayList<Integer>> graph;
	
	/**
	 * number of vertices to create
	 */
	private final int NUMBER_OF_VERTICIES;
	
	/**
	 * constructor
	 * 
	 * @param numVertices number of vertices
	 */
    public Graph (int numVertices) {
        NUMBER_OF_VERTICIES = numVertices;
    	graph = new ArrayList<ArrayList<Integer>>();
        for (int step = 0; step < NUMBER_OF_VERTICIES; step++)
        	graph.add(new ArrayList<Integer>());
    }
    
    /**
     * Returns a list of the ID#s of the inactive vertices of the graph. If there
     * are no inactive vertices, a reference to an empty list object is returned.
     * 
     * @return list of unused vertices
     */
    public List<Integer> getUnusedVertices () {
        ArrayList<Integer> inactiveIntegers = new ArrayList<Integer>();
        for (int step = 0;step < NUMBER_OF_VERTICIES; step++)
        	if (graph.get(step).isEmpty()) inactiveIntegers.add(step);
        return inactiveIntegers;
    }
    
    /**
     * Returns true if the graph possesses an edge directly connecting the given
     * source and destination vertices. That is, ‘true’ is returned if these
     * vertices are adjacent.
     * 
     * @param source first vertex
     * @param destination second vertex
     * @return true if they're connected
     */
    public boolean isEdge (int source, int destination) {
        return graph.get(source).contains(destination);
    }
    
    /**
     * Ensures that the graph contains an edge connecting the given source and
     * destination vertices.
     * 
     * @param source first vertex
     * @param destination second vertex
     */
    public void addEdge (int source, int destination) {
        graph.get(source).add(destination);
        graph.get(destination).add(source);
    }
    
    /**
     * Ensures that the graph does not contain an edge connecting the given
     * source and destination vertices.
     * 
     * @param source first vertex
     * @param destination second vertex
     */
    public void removeEdge (int source, int destination) {
        graph.get(source).remove((Integer)destination);
        graph.get(destination).remove((Integer)source);
    }
    
    /**
     * Returns true if the given vertex is active within the graph.
     * 
     * @param vertex test vertex
     * @return true if it has no edges
     */
    public boolean isInGraph (int vertex) {
        return !graph.get(vertex).isEmpty();
    }
    
    /**
     * Called to mark a vertex as inactive. Inactive vertices have no neighbors,
     * and thus have a degree of 0.
     * 
     * @param vertex vertex to remove
     */
    public void removeVertex (int vertex) {
        Object[] list = graph.get(vertex).toArray();
        for (Object step:list)
            removeEdge(vertex, (int)step);
    }
    
    /**
     * Returns a list of vertex ID#s. A vertex is in the list if it is active
     * and adjacent to the given vertex.
     * 
     * @param vertex test vertex
     * @return list with indices of the vertex's neighbors
     */
    public List<Integer> getAdjacent (int vertex) {
        return graph.get(vertex);
    }
    
    /**
     * Returns the degree of the given vertex.
     * 
     * @param vertex test vertex
     * @return number of edges
     */
    public int degree (int vertex) {
    	return graph.get(vertex).size();
    }
    
    /**
     * used to keep track of used spaces in recursion
     */
    private boolean[] marked;
    
    /**
     * Returns true if the graph is connected; that is, if every active vertex
     * is reachable from every other active vertex.
     * 
     * @return true if all spaces are connected
     */
    public boolean connected () {
        
        //marking to stop recursion
        marked = new boolean[NUMBER_OF_VERTICIES];
        int index = 0;
        
        //find first used vertex
        while (index < NUMBER_OF_VERTICIES && graph.get(index).isEmpty())
            index++;
        
        //use found index for recursion entry point
        traverseGraph(index);
        
        //test to see if any remaining vertices are active
        for (; index < NUMBER_OF_VERTICIES; index++)
        	if (!marked[index] && !graph.get(index).isEmpty())
        		return false;
        
        //default behavior
        return true;
    }
    
    /**
     * recursive test for connected vertices
     * 
     * @param index starting point
     */
    private void traverseGraph(int index) {
    	
        //marks each starting point
        marked[index] = true;
    	
    	//traverses all previously untraversed edges
    	for (Integer i:graph.get(index))
    		if (!marked[i]) traverseGraph(i);
    }
    
}
