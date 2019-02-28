/**
 * Territory object for a Bones Battle board
 * 
 * @author Phillip Benoit
 *
 */
public class Territory {
    
    /**
     * associated map
     */
    private Map map;
    
    /**
     * number of dice on the space and ID
     */
    private int number_of_dice, id;
    
    /**
     * associated owning player
     */
    private Player owner;
    
    /**
     * default constructor
     * 
     * @param map associated map
     */
    public Territory(Map map) {
        this.map = map;
        owner = null;
        number_of_dice = 0;
        id = 0;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param map associated map
     * @param owner associated owner
     * @param dice number of dice
     * @param idNum ID
     */
    public Territory (Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.owner = owner;
        number_of_dice = dice;
        id = idNum;
    }
    
    /**
     * get number of dice in territory
     * 
     * @return number of dice in territory
     */
    int getDice () {return number_of_dice;}
    
    /**
     * get ID number
     * 
     * @return ID number
     */
    int getIdNum () {return id;}
    
    /**
     * get associated map
     * 
     * @return associated map
     */
    Map getMap () {return map;}
    
    /**
     * get associated owning player
     * 
     * @return associated owning player
     */
    Player getOwner () {return owner;}
    
    /**
     * set number of dice in territory
     * 
     * @param d number of dice in territory
     */
    void setDice(int d) {number_of_dice = d;}
    
    /**
     * set ID number
     * 
     * @param n ID number
     */
    void setIdNum (int n) {id = n;}
    
    /**
     * set owning player
     * 
     * @param o owning player
     */
    void setOwner (Player o) {owner = o;}
    
    /**
     * get row (calculated from ID and map data)
     * 
     * @return row (calculated from ID and map data)
     */
    int getRow () {return id/map.COLUMNS;}
    
    /**
     * get column (calculated from ID and map data)
     * 
     * @return column (calculated from ID and map data)
     */
    int getCol () {return id%map.COLUMNS;}

}
