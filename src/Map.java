import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {

    public final int ROWS, COLUMNS, //Size of the board.
    VICTIMS, //Number of unused (‘invisible’) territories.
    NUMTERRITORIES, //Synonym for ROWS times COLUMNS.
    OCCUPIED, //Synonym for NUMTERRITORIES minus VICTIMS.
    MAXDICE; //The largest quantity of dice a territory may have.

    private Random RNG;

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
        RNG = new Random();
        map = new Territory[ROWS][COLUMNS];
        int id_counter = 0;
        for (int x = 0;x<ROWS;x++)
            for (int y = 0; y<COLUMNS; y++)
                map[x][y] = new Territory(this, null, maxDice, id_counter++);
        neighbor_graph = constructGraph(rows, columns, victims);
        partitionTerritories();
        distributeDice();
    }

    public Territory[][] getMap () {return map;}

    public Graph getGraph () {return neighbor_graph;}

    public Territory getTerritory (int row, int column) {return map[row][column];}

    private Territory getTerritory (int index) {
        return getTerritory(index/COLUMNS, index%COLUMNS);
    }

    //: Given the row and column of a territory, compute and return the territory’s
    //ID#. The Territory class supplies getRow() and getCol() methods; see
    //“The Supplied Classes” section, below.
    public int getTerritoryId (int row, int column){
        return (row * COLUMNS) + column;
    }

    //: Determine and return the quantity of territories owned by the given player.
    public int countTerritories (Player player) {
        return getPropertyOf(player).size();        
    }

    //Determine and return the total number of dice currently assigned to this player’s territories.
    public int countDice (Player player) {
        ArrayList<Territory> propety = getPropertyOf(player);
        int counter = 0;
        for (Territory t: propety)
            counter += t.getDice();
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
        ArrayList<Territory> return_list = new ArrayList<Territory>();
        for (Integer i:neighbor_graph.getAdjacent(cell.getIdNum()))
            return_list.add(getTerritory(i));
        return return_list;
    }

    //Similar to getNeighbors(), above, but the returned list contains only
    //references to neighboring territories controlled by another player.
    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        ArrayList<Territory> return_list = new ArrayList<Territory>();
        for (Integer i:neighbor_graph.getAdjacent(cell.getIdNum())) {
            Territory t = getTerritory(i);
            if (t.getOwner()!=cell.getOwner()) return_list.add(t);
        }
        return return_list;
    }

    //This method is called by the constructor after the array of un-owned
    //territories has been built. This method assigns to each player the same
    //(or nearly the same) quantity of territories. Each player’s territories
    //are to be selected randomly. That is, do not assign players to territories
    //based on a pattern.
    private void partitionTerritories() {
        int number_of_players = players.size();
        for (int step = 0; step < OCCUPIED;step++) {
            Territory t;
            int index;
            do {
                index = RNG.nextInt(NUMTERRITORIES);
                t = getTerritory(index);
            } while (t.getOwner() != null || !neighbor_graph.isInGraph(index));
            t.setOwner(players.get(step%number_of_players));
            t.setDice(1);
        }
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
        int number_to_add = (OCCUPIED / players.size()) * 2;
        for (Player p:players) {
            ArrayList<Territory> properties = getPropertyOf(p);
            for (int step = 0; step < number_to_add; step++) {
                Territory t = properties.get(RNG.nextInt(properties.size()));
                t.setDice(t.getDice()+1);
            }
        }
    }

    private boolean[] marked;
    private int count;
    private ArrayList<Territory> properties;

    //Returns a count of the number of territories in the largest connected cluster
    //of territories owned by the given player.
    public int countConnected (Player player) {
        properties = getPropertyOf(player);
        int highest = 0;
        marked = new boolean[properties.size()];
        for (Territory t:properties) {
            count = 0;
            countConnected(t);
            highest = Math.max(highest, count);
        }
        return highest;
    }

    private void countConnected(Territory t) {
        int property_index = properties.indexOf(t);
        if (!marked[property_index]) {
            marked[property_index] = true;
            count++;
            List<Integer> connected_list = neighbor_graph.getAdjacent(t.getIdNum());
            for (Integer i:connected_list) {
                Territory connected_territory = getTerritory(i);
                if (connected_territory.getOwner()==t.getOwner())
                    countConnected(connected_territory);
            }
        }
    }

    //Builds and returns a reference to a graph representing all of the active
    //territories in the game. An acceptable graph has the appropriate number
    //of active territories (Map.OCCUPIED), ‘scatters’ the inactive territories
    //among the active territories in an unpredictable (pseudo-random) fashion,
    //and ensures that all of the active vertices are connected.
    public Graph constructGraph(int rows, int cols, int victims) {
        int total = rows * cols;
        Graph g = new Graph(total);
        marked = new boolean[total];

        for (int step = 0; step < total; step++)
            activateTerritory(step, g, rows, cols);

        for (int step = 0; step < victims; step++) {
            int test_move;
            do {
                test_move = RNG.nextInt(total);
                g.removeVertex(test_move);
                marked[test_move] = false;
                if (!g.connected()) activateTerritory(test_move, g, rows, cols);
            } while (marked[test_move]);
        }

        return g;
    }

    private void activateTerritory(int index, Graph g, int rows, int cols) {
        marked[index] = true;
        ArrayList<Integer> connected = validMoves(index, cols, rows);
        for (Integer move:connected)
            if (marked[move])
                g.addEdge(index, move);
    }

    private ArrayList<Integer> validMoves(int index, int cols, int rows) {
        ArrayList<Integer> moves = new ArrayList<>();

        //up
        int cursor = index - cols;
        if (cursor > -1)
            moves.add(cursor);

        //down
        cursor = index + cols;
        if (cursor < rows * cols)
            moves.add(index);

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
