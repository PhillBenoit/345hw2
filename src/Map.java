import java.util.ArrayList;

public class Map {
    
    public final int ROWS, COLUMNS, //Size of the board.
    VICTIMS, //Number of unused (‘invisible’) territories.
    NUMTERRITORIES, //Synonym for ROWS times COLUMNS.
    OCCUPIED, //Synonym for NUMTERRITORIES minus VICTIMS.
    MAXDICE; //The largest quantity of dice a territory may have.
    
    private ArrayList<Player> players;
    
    private Territory[][] map;
    
    private Graph neighbor_graph;
    
    public Map (ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        this.players = players;
        ROWS = rows;
        COLUMNS = columns;
        VICTIMS = victims;
        MAXDICE = maxDice;
        NUMTERRITORIES = ROWS * COLUMNS;
        OCCUPIED = NUMTERRITORIES - VICTIMS;
        map = new Territory[ROWS][COLUMNS];
        int id_counter = 0;
        for (Territory[] row: map)
            for (Territory t: row)
                t = new Territory(this, null, maxDice, id_counter++);
        
    }
    
    public Territory[][] getMap () {return map;}

    public Graph getGraph () {return neighbor_graph;}
    
    public Territory getTerritory (int row, int column) {return map[row][column];}
    
    //: Given the row and column of a territory, compute and return the territory’s
    //ID#. The Territory class supplies getRow() and getCol() methods; see
    //“The Supplied Classes” section, below.
    public int getTerritoryId (int row, int column){
        return (row * COLUMNS) + column;
    }
    
    //: Determine and return the quantity of territories owned by the given player.
    public int countTerritories (Player player) {
        int counter = 0;
        for (Territory[] row: map)
            for (Territory t: row)
                if (t.getOwner() == player) counter++;
        return counter;        
    }
    
    //Determine and return the total number of dice currently assigned to this player’s territories.
    public int countDice (Player player) {
        int counter = 0;
        for (Territory[] row: map)
            for (Territory t: row)
                if (t.getOwner() == player) counter += t.getDice();
        return counter;        
    }
    
    //Construct and return a reference to an ArrayList of Territory object
    //references. The territories referenced by the list are those
    //currently owned by the given player.
    public ArrayList<Territory> getPropertyOf(Player player) {
        ArrayList<Territory> list = new ArrayList<>();
        for (Territory[] row: map)
            for (Territory t: row)
                if (t.getOwner() == player) list.add(t);
        return list;        
    }

    //Each territory has at least one adjacent (edge-sharing) neighboring
    //territory, but no more than four. This method returns a reference to an
    //ArrayList of references to the given territory’s neighbors. The Graph object
    //offers a helpful method: isInGraph(). isInGraph() takes a territory ID# and
    //returns true if the territory is participating in the game (remember, some
    //territories are ‘invisible’ and thus can’t be neighbors).
    public ArrayList<Territory> getNeighbors(Territory cell) {
        
    }
    
    //Similar to getNeighbors(), above, but the returned list contains only
    //references to neighboring territories controlled by another player.
    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        
    }
    
    //This method is called by the constructor after the array of un-owned
    //territories has been built. This method assigns to each player the same
    //(or nearly the same) quantity of territories. Each player’s territories
    //are to be selected randomly. That is, do not assign players to territories
    //based on a pattern.
    private void partitionTerritories() {
        
    }
    
    //This method is called by the constructor after partitionTerritories() has
    //been called. Collectively, the assigned territories of each player have
    //the same quantity of dice as the territories of every other player: three
    //times the player’s number of territories. For example, if there were three
    //players and 15 territories per player, each player would start with 45 dice.
    //This method randomly distributes each player’s dice allotment across his or
    //her territories. Each territory must have at least one die, but no territory
    //can have more than MAXDICE dice.
    private void distributeDice() {
        
    }
    
    //Returns a count of the number of territories in the largest connected cluster
    //of territories owned by the given player.
    public int countConnected (Player player) {
        
    }
    
    //Builds and returns a reference to a graph representing all of the active
    //territories in the game. An acceptable graph has the appropriate number
    //of active territories (Map.OCCUPIED), ‘scatters’ the inactive territories
    //among the active territories in an unpredictable (pseudo-random) fashion,
    //and ensures that all of the active vertices are connected.
    public Graph constructGraph(int rows, int cols, int victims) {
        
    }

}
