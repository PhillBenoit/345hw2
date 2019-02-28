import java.awt.Color;
import java.util.ArrayList;

/**
 * Test code for student developed classes
 * 
 * @author Phillip Benoit
 *
 */
public class Test {

    /**
     * Entry point
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        
        //create players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Moe", new Color(0xFF0000)));
        players.add(new Player("Larry", new Color(0x00FF00)));
        players.add(new Player("Curly", new Color(0x0000FF)));

        //command to compile strategy
        ComputerStrategy s = new ComputerStrategy();
        s.setPlayer(players.get(0));
        
        //create and display map using blank spaces and number of vertices
        //for an ascii representation
        int rows = 5, cols = 10;
        Map m = new Map(players, rows, cols, 20, 8);
        for (int x = 0; x < rows; x++) {
        	for (int y = 0; y < cols; y++)
        		System.out.print(m.getNeighbors(m.getTerritory(x, y)).isEmpty()? " " : m.getGraph().degree((x*cols) + y));
        	System.out.println();
        }
    }

}
