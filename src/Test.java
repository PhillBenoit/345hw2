import java.awt.Color;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Moe", new Color(0xFF0000)));
        players.add(new Player("Larry", new Color(0x00FF00)));
        players.add(new Player("Curly", new Color(0x0000FF)));
        
        int rows = 5, cols = 10;
        Map m = new Map(players, rows, cols, 20, 8);
        for (int x = 0; x < rows; x++) {
        	for (int y = 0; y < cols; y++)
        		System.out.print(m.getNeighbors(m.getTerritory(x, y)).isEmpty()? " " : "#");
        	System.out.println();
        }
    }

}
