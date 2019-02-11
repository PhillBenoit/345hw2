
public class Territory {
    
    private Map map;
    private int number_of_dice, id;
    private Player owner;
    
    public Territory(Map map) {
        this.map = map;
        owner = null;
        number_of_dice = 0;
        id = 0;
    }
    
    public Territory (Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.owner = owner;
        number_of_dice = dice;
        id = idNum;
    }
    
    int getDice () {return number_of_dice;}
    
    int getIdNum () {return id;}
    
    Map getMap () {return map;}
    
    Player getOwner () {return owner;}
    
    void setDice(int d) {number_of_dice = d;}
    
    void setIdNum (int n) {id = n;}
    
    void setOwner (Player o) {owner = o;}
    
    int getRow () {return id/map.COLUMNS;}
    
    int getCol () {return id%map.COLUMNS;}

}
