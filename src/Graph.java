import java.util.ArrayList;
import java.util.List;

public class Graph {
    
	private ArrayList<ArrayList<Integer>> graph;
	
	private final int NUMBER_OF_VERTICIES;
	
    public Graph (int numVertices) {
        NUMBER_OF_VERTICIES = numVertices;
    	graph = new ArrayList<ArrayList<Integer>>();
        for (int step = 0; step < NUMBER_OF_VERTICIES; step++)
        	graph.add(new ArrayList<Integer>());
    }
    
    //Returns a list of the ID#s of the inactive vertices of the graph. If there
    //are no inactive vertices, a reference to an empty list object is returned.
    public List<Integer> getUnusedVertices () {
        ArrayList<Integer> inactiveIntegers = new ArrayList<Integer>();
        for (int step = 0;step < NUMBER_OF_VERTICIES; step++)
        	if (graph.get(step).isEmpty()) inactiveIntegers.add(step);
        return inactiveIntegers;
    }
    
    //Returns true if the graph possesses an edge directly connecting the given
    //source and destination vertices. That is, ‘true’ is returned if these
    //vertices are adjacent.
    public boolean isEdge (int source, int destination) {
        return graph.get(source).contains(destination);
    }
    
    //Ensures that the graph contains an edge connecting the given source and
    //destination vertices.
    public void addEdge (int source, int destination) {
        graph.get(source).add(destination);
        graph.get(destination).add(source);
    }
    
    //Ensures that the graph does not contain an edge connecting the given
    //source and destination vertices.
    public void removeEdge (int source, int destination) {
        graph.get(source).remove((Integer)destination);
        graph.get(destination).remove((Integer)source);
    }
    
    //Returns true if the given vertex is active within the graph.
    public boolean isInGraph (int vertex) {
        return !graph.get(vertex).isEmpty();
    }
    
    //Called to mark a vertex as inactive. Inactive vertices have no neighbors,
    //and thus have a degree of 0.
    public void removeVertex (int vertex) {
        ArrayList<Integer> list = graph.get(vertex);
        for (int step = 0; step < list.size(); step++)
            graph.get(step).remove((Integer)vertex);
        list.clear();
    }
    
    //Returns a list of vertex ID#s. A vertex is in the list if it is active
    //and adjacent to the given vertex.
    public List<Integer> getAdjacent (int vertex) {
        return graph.get(vertex);
    }
    
    //Returns the degree of the given vertex.
    public int degree (int vertex) {
    	return graph.get(vertex).size();
    }
    
    private boolean[] marked;
    
    //Returns true if the graph is connected; that is, if every active vertex
    //is reachable from every other active vertex.
    public boolean connected () {
        marked = new boolean[NUMBER_OF_VERTICIES];
        int index = 0;
        while (index < NUMBER_OF_VERTICIES && graph.get(index).isEmpty())
            index++;
        traverseGraph(index);
        for (; index < NUMBER_OF_VERTICIES; index++)
        	if (!marked[index] && !graph.get(index).isEmpty())
        		return false;
        return true;
    }
    
    private void traverseGraph(int index) {
    	marked[index] = true;
    	for (Integer i:graph.get(index))
    		if (!marked[i]) traverseGraph(i);
    }
    
}
