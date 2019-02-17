import java.awt.Color;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Moe", new Color(0xFF0000)));
        players.add(new Player("Larry", new Color(0x00FF00)));
        players.add(new Player("Curly", new Color(0x0000FF)));
        
        Map m = new Map(players, 5, 10, 5, 8);
        System.out.println(m.NUMTERRITORIES);
    }

}
