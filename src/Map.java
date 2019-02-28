import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data structure for a bones battle map
 * 
 * @author Phillip Benoit
 *
 */
public class Map {

    /**
     * constants
     */
	public final int ROWS, COLUMNS, //Size of the board.
	VICTIMS, //Number of unused (‘invisible’) territories.
	NUMTERRITORIES, //Synonym for ROWS times COLUMNS.
	OCCUPIED, //Synonym for NUMTERRITORIES minus VICTIMS.
	MAXDICE; //The largest quantity of dice a territory may have.

	/**
	 * random number generator
	 */
	private Random RNG;

	/**
	 * list of players
	 */
	private ArrayList<Player> players;

	/**
	 * map data structure
	 */
	private Territory[][] map;

	/**
	 * Graph representing map connections
	 */
	private Graph neighbor_graph;

	/**
	 * constructor
	 * 
	 * @param players list of players
	 * @param rows number of rows
	 * @param columns number of columns
	 * @param victims number of unused spaces
	 * @param maxDice max number of dice per territory
	 */
	public Map (ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
		
	    //assign passed and default values
	    this.players = players;
		ROWS = rows;
		COLUMNS = columns;
		VICTIMS = victims;
		MAXDICE = maxDice;
		NUMTERRITORIES = ROWS * COLUMNS;
		OCCUPIED = NUMTERRITORIES - VICTIMS;
		RNG = new Random();
		map = new Territory[ROWS][COLUMNS];
		int id_counter = 0;
		for (int x = 0;x<ROWS;x++)
			for (int y = 0; y<COLUMNS; y++)
				map[x][y] = new Territory(this, null, maxDice, id_counter++);
		
		//setup board
		neighbor_graph = constructGraph(rows, columns, victims);
		partitionTerritories();
		distributeDice();
	}

	/**
	 * get underlying data structure
	 * 
	 * @return underlying data structure
	 */
	public Territory[][] getMap () {return map;}

	/**
	 * get data structure representing map connections
	 * 
	 * @return data structure representing map connections
	 */
	public Graph getGraph () {return neighbor_graph;}

	/**
	 * get a single territory from the map
	 * 
	 * @param row x coordinate of territory
	 * @param column y coordinate of territory
	 * @return specified territory
	 */
	public Territory getTerritory (int row, int column) {return map[row][column];}

	/**
	 * calculates x/y coordinate from passed index value
	 * 
	 * @param index one dimensional value
	 * @return specified territory
	 */
	private Territory getTerritory (int index) {
		return getTerritory(index/COLUMNS, index%COLUMNS);
	}

	/**
     * Given the row and column of a territory, compute and return the territory’s
     * ID#. The Territory class supplies getRow() and getCol() methods; see
     * “The Supplied Classes” section, below.
	 * 
	 * @param row x coordinate of territory
	 * @param column y coordinate of territory
	 * @return calculated one dimensional value
	 */
	public int getTerritoryId (int row, int column){
		return (row * COLUMNS) + column;
	}

	/**
     * Determine and return the quantity of territories owned by the given player.
	 * 
	 * @param player test player
	 * @return number of territories the test player controls
	 */
	public int countTerritories (Player player) {
		return getPropertyOf(player).size();        
	}

	/**
     * Determine and return the total number of dice currently assigned to this
     * player’s territories.
	 * 
	 * @param player test player
	 * @return total of the player's dice
	 */
	public int countDice (Player player) {
		
	    //get list of territories
	    ArrayList<Territory> propety = getPropertyOf(player);
		
	    //add dice from every territory
	    int counter = 0;
		for (Territory t: propety)
			counter += t.getDice();

		//return total
		return counter;        
	}

	/**
     * Construct and return a reference to an ArrayList of Territory object
     * references. The territories referenced by the list are those
     * currently owned by the given player.
	 * 
	 * @param player test player
	 * @return list of all properties owned by the player
	 */
	public ArrayList<Territory> getPropertyOf(Player player) {
		ArrayList<Territory> list = new ArrayList<>();
		for (Territory[] row: map)
			for (Territory t: row)
				if (t.getOwner() == player) list.add(t);
		return list;        
	}

	/**
     * Each territory has at least one adjacent (edge-sharing) neighboring
     * territory, but no more than four. This method returns a reference to an
     * ArrayList of references to the given territory’s neighbors. The Graph object
     * offers a helpful method: isInGraph(). isInGraph() takes a territory ID# and
     * returns true if the territory is participating in the game (remember, some
     * territories are ‘invisible’ and thus can’t be neighbors).
	 * 
	 * @param cell test cell
	 * @return list with indices of all adjacent territories
	 */
	public ArrayList<Territory> getNeighbors(Territory cell) {
		ArrayList<Territory> return_list = new ArrayList<Territory>();
		
		//convert list to array list
		for (Integer i:neighbor_graph.getAdjacent(cell.getIdNum()))
			return_list.add(getTerritory(i));

		//return final array list
		return return_list;
	}

	/**
     * Similar to getNeighbors(), above, but the returned list contains only
     * references to neighboring territories controlled by another player.
	 * 
	 * @param cell test cell
	 * @return list of indices for adjacent territories controlled by other players
	 */
	public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
		ArrayList<Territory> return_list = new ArrayList<Territory>();
		
		//test ownership of territories before adding to the list
		for (Integer i:neighbor_graph.getAdjacent(cell.getIdNum())) {
			Territory t = getTerritory(i);
			if (t.getOwner()!=cell.getOwner()) return_list.add(t);
		}
		
		//final list
		return return_list;
	}

	/**
     * This method is called by the constructor after the array of un-owned
     * territories has been built. This method assigns to each player the same
     * (or nearly the same) quantity of territories. Each player’s territories
     * are to be selected randomly. That is, do not assign players to territories
     * based on a pattern.
	 */
	private void partitionTerritories() {
		
	    //get number fo players for use with modulo
	    int number_of_players = players.size();
		
	    //make sure all occupied territories are assigned
		for (int step = 0; step < OCCUPIED;step++) {
			
		    //get random territory
		    int index = RNG.nextInt(NUMTERRITORIES);
            Territory t = getTerritory(index);
            
            //get next available territory if random number is either unused
            //or occupied 
            while (!neighbor_graph.isInGraph(index) || t.getOwner() != null) {
				index++;
				if (index == NUMTERRITORIES) index = 0;
				t = getTerritory(index);
			}
            
            //set owner and one die
			t.setOwner(players.get(step%number_of_players));
			t.setDice(1);
		}
	}

	/**
     * This method is called by the constructor after partitionTerritories() has
     * been called. Collectively, the assigned territories of each player have
     * the same quantity of dice as the territories of every other player: three
     * times the player’s number of territories. For example, if there were three
     * players and 15 territories per player, each player would start with 45 dice.
     * This method randomly distributes each player’s dice allotment across his or
     * her territories. Each territory must have at least one die, but no territory
     * can have more than MAXDICE dice.
	 */
	private void distributeDice() {
		
	    //assign dice for each player
	    for (Player p:players) {
			
	        //get list of properties and calculate the number of dice to add
	        ArrayList<Territory> properties = getPropertyOf(p);
			int number_to_add = properties.size() * 2;
			
			//loop for adding dice
			for (int step = 0; step < number_to_add; step++) {
				Territory t;
				
				//prevent adding dice to full spaces
				do t = properties.get(RNG.nextInt(properties.size()));
				while (t.getDice() >= MAXDICE);
				
				//add to random territory
				t.setDice(t.getDice()+1);
			}
		}
	}

	/**
	 * used to keep track of visited cells in recursion
	 */
	private boolean[] marked;
	
	/**
	 * used to count the number of properties in recursion
	 */
	private int count;
	
	/**
	 * list of player's territories 
	 */
	private ArrayList<Territory> properties;

	/**
     * Returns a count of the number of territories in the largest connected cluster
     * of territories owned by the given player.
	 * 
	 * @param player test player
	 * @return size of largest block of connected territories
	 */
	public int countConnected (Player player) {
		
	    //get player's properties
	    properties = getPropertyOf(player);
		
	    //keeps track of current longest chain
	    int highest = 0;
		
	    //keeps track of visited territories
	    marked = new boolean[properties.size()];
		
	    //go through each property
	    for (Territory t:properties) {
			count = 0;
			
			//recursive step
			countConnected(t);
			
			//count found with recursion tested against current highest
			highest = Math.max(highest, count);
		}
	    
	    //return the count of the biggest chain
		return highest;
	}

	/**
	 * recursive step for traversing properties
	 * 
	 * @param t starting territory
	 */
	private void countConnected(Territory t) {
		
	    //get index for boolean array
	    int property_index = properties.indexOf(t);
		
	    //prevent adding previously visited territories to traversal
	    if (!marked[property_index]) {
			
	        //mark newly found territories
	        marked[property_index] = true;
			count++;
			List<Integer> connected_list = neighbor_graph.getAdjacent(t.getIdNum());
			
			//test all connected edges
			for (Integer i:connected_list) {
				Territory connected_territory = getTerritory(i);
				
				//attempt to add all edges owned by the same player
				if (connected_territory.getOwner()==t.getOwner())
					countConnected(connected_territory);
			}
		}
	}

	/**
     * Builds and returns a reference to a graph representing all of the active
     * territories in the game. An acceptable graph has the appropriate number
     * of active territories (Map.OCCUPIED), ‘scatters’ the inactive territories
     * among the active territories in an unpredictable (pseudo-random) fashion,
     * and ensures that all of the active vertices are connected.
	 * 
	 * @param rows number of rows
	 * @param cols number of columns
	 * @param victims number of unused territories
	 * @return
	 */
	public Graph constructGraph(int rows, int cols, int victims) {
		
	    //total number of territories
	    int total = rows * cols;
		
	    //new graph for return
	    Graph g = new Graph(total);
		
	    //test for active territories
	    marked = new boolean[total];

	    //activate all cells by default
		for (int step = 0; step < total; step++)
			activateTerritory(step, g, rows, cols);
		
		//remove cells
		for (int step = 0; step < victims; step++) {
			int test_move;
			do {
				//random vertex to remove tested against tracking boolean array
			    do test_move = RNG.nextInt(total);
				while (!marked[test_move]);
				
			    //removes the vertex
			    g.removeVertex(test_move);
				marked[test_move] = false;
				
				//reactivates if removal disrupts board connectivity
				if (!g.connected())
					activateTerritory(test_move, g, rows, cols);
			//keep removing cells until count is fulfilled
			} while (marked[test_move]);
		}
		
		//reset tracking array
		for (int step = 0; step < total; step++)
		    marked[step] = g.isInGraph(step);

		//add back a number of territories to compensate for the ones removed
		//tangentially from random removal (all neighbors eliminated)
		while (VICTIMS < g.getUnusedVertices().size()) {
		    int index = 0;
		    
		    //step through graph until an index is found that is active and has
		    //less than the number of possible edges
		    while (!g.isInGraph(index) ||
		            g.degree(index) == validMoves(index, cols, rows).size())
		        index++;
		    
		    //get list of valid edges
		    ArrayList<Integer> possible_adds = validMoves(index, cols, rows);
		    index = 0;
		    
		    //step through list until unused vertex is found
		    while (g.isInGraph(possible_adds.get(index))) index++;
		    
		    //reactivate it
		    activateTerritory(possible_adds.get(index), g, rows, cols);
		}

		//return completed graph
        return g;
	}

	/**
	 * Activates a cell by connecting it to all currently active edges
	 * 
	 * @param index vertex to activate
	 * @param g graph to modify
	 * @param rows total rows
	 * @param cols total columns
	 */
	private void activateTerritory(int index, Graph g, int rows, int cols) {
		
	    //mark as active
	    marked[index] = true;
		
	    //get valid moves
	    ArrayList<Integer> connected = validMoves(index, cols, rows);
		
	    //add edges for all valid and active vertices
		for (Integer move:connected)
			if (marked[move])
				g.addEdge(index, move);
	}

	/**
	 * Generate a list of territories directly north, south, east or west.
	 * (constrained by number of rows and columns to create a square)
	 * 
	 * @param index test vertex
	 * @param cols number of columns
	 * @param rows number of rows
	 * @return list of all valid moves 
	 */
	private ArrayList<Integer> validMoves(int index, int cols, int rows) {
		ArrayList<Integer> moves = new ArrayList<>();

		//up
		int cursor = index - cols;
		if (cursor > -1)
			moves.add(cursor);

		//down
		cursor = index + cols;
		if (cursor < rows * cols)
			moves.add(cursor);

		//left
		cursor = index + 1;
		if (cursor%cols > 0)
			moves.add(cursor);

		//right
		if (index%cols > 0)
			moves.add(index-1);

		return moves;
	}

}
