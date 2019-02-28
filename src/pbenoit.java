import java.util.ArrayList;

/**
 * AI for Bones Battle
 * 
 * @author Phillip Benoit
 *
 */
public class pbenoit implements Strategy{
    
    /**
     * Owner of the strategy
     */
    private Player p;
    
    /**
     * Attacking and defending territory (assigned when found)
     */
    private Territory attacker, defender;

    
    @Override
    /**
     * Set owner
     */
    public void setPlayer(Player whom) {
        p = whom;
    }

    @Override
    /**
     *Returns true if the player will attack, given the state of the game as
     *described by the current game board. As this determination is made, the
     *method may construct data structures whose content can be used to assist
     *in the processing of getAttacker() and getDefender().
     */
    public boolean willAttack(Map board) {
        
        //get all of the players properties
        ArrayList<Territory> properties = board.getPropertyOf(p);
        
        //test each property
        for (Territory property:properties) {

            //get list of valid targets
            ArrayList<Territory> attack_list =
                    getAttackList(board, property, property.getDice());
            
            //ensures all properties with max dice attack
            if (property.getDice() == board.MAXDICE)
                for (Territory defender:attack_list) {
                    attacker = property;
                    this.defender = defender;
                    return true;
                }

            //test to make sure stronger tiles are not connected to the next space
            for (Territory defender:attack_list) {
                if (!getAttackList(board, defender,
                        property.getDice()-1).isEmpty()) {
                    attacker = property;
                    this.defender = defender;
                    return true;
                }
            }
        }
        
        //default behavior
        return false;
    }
    
    /**
     * Get a list of valid targets for a specific territory
     * 
     * @param board board to search
     * @param index territory to use to attack
     * @param pieces number of pieces to test with
     * @return list of targets (empty for defense mode)
     */
    private ArrayList<Territory> getAttackList(Map board, Territory index, int pieces) {
        
        //lists for return values
        ArrayList<Territory> return_list = new ArrayList<>(),
                sorted_list = new ArrayList<>();
        
        //list of neighboring properties
        ArrayList<Territory> neighbors = board.getNeighbors(index);
        
        //search all neighbors
        for (Territory neighbor:neighbors) 
            
            //test for ownership
            if (p != neighbor.getOwner()) {
                
                //defense mode if a stronger territory is found
                if (neighbor.getDice() > pieces) {
                    return_list.clear();
                    return return_list;
                }
                
                //add target
                return_list.add(neighbor);
            }
        
        //sort target list with strongest first
        while (!return_list.isEmpty()) {
            Territory max = return_list.get(0);
            for (Territory defender:return_list)
                if (defender.getDice() > max.getDice()) max = defender;
            sorted_list.add(max);
            return_list.remove(max);
        }
        
        //return target list
        return sorted_list;
    }

    @Override
    /**
     *Returns a reference to a territory that will be the source of the next
     *attack. The selected territory must have at least two dice and be owned
     *by the player associated with this strategy object. This method should be
     *called only after willAttack() has been called.
     */
    public Territory getAttacker() {return attacker;}

    @Override
    /**
     *Returns a reference to the territory object that will defend the attack
     *of the territory just identified by getAttacker(). The defending territory
     *must be occupied (owned) by another player and must be adjacent to the
     *attacking territory. This method should be called only after getAttacker()
     *has been called.
     */
    public Territory getDefender() {return defender;}

}
