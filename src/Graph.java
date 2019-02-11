
public class Graph {
    
    public Graph (int numVertices) {
        
    }
    
    //Returns a list of the ID#s of the inactive vertices of the graph. If there
    //are no inactive vertices, a reference to an empty list object is returned.
    public List<Integer> getUnusedVertices () {
        
    }
    
    //Returns true if the graph possesses an edge directly connecting the given
    //source and destination vertices. That is, ‘true’ is returned if these
    //vertices are adjacent.
    public boolean isEdge (int source, int destination) {
        
    }
    
    //Ensures that the graph contains an edge connecting the given source and
    //destination vertices.
    public void addEdge (int source, int destination) {
        
    }
    
    //Ensures that the graph does not contain an edge connecting the given
    //source and destination vertices.
    public void removeEdge (int source, int destination) {
        
    }
    
    //Returns true if the given vertex is active within the graph.
    public boolean isInGraph (int vertex) {
        
    }
    
    //Called to mark a vertex as inactive. Inactive vertices have no neighbors,
    //and thus have a degree of 0.
    public void removeVertex (int vertex) {
        
    }
    
    //Returns a list of vertex ID#s. A vertex is in the list if it is active
    //and adjacent to the given vertex.
    public List<Integer> getAdjacent (int vertex) {
        
    }
    
    //Returns the degree of the given vertex.
    public int degree (int vertex) {
        
    }
    
    //Returns true if the graph is connected; that is, if every active vertex
    //is reachable from every other active vertex.
    public boolean connected () {
        
    }
    
}
