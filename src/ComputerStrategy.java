
public class ComputerStrategy implements Strategy{
    
    Player p;

    @Override
    public void setPlayer(Player whom) {
        p = whom;
    }

    //Returns true if the player will attack, given the state of the game as
    //described by the current game board. As this determination is made, the
    //method may construct data structures whose content can be used to assist
    //in the processing of getAttacker() and getDefender().
    @Override
    public boolean willAttack(Map board) {
        // TODO Auto-generated method stub
        return false;
    }

    //Returns a reference to a territory that will be the source of the next
    //attack. The selected territory must have at least two dice and be owned
    //by the player associated with this strategy object. This method should be
    //called only after willAttack() has been called.
    @Override
    public Territory getAttacker() {
        // TODO Auto-generated method stub
        return null;
    }

    
    //Returns a reference to the territory object that will defend the attack
    //of the territory just identified by getAttacker(). The defending territory
    //must be occupied (owned) by another player and must be adjacent to the
    //attacking territory. This method should be called only after getAttacker()
    //has been called.
    @Override
    public Territory getDefender() {
        // TODO Auto-generated method stub
        return null;
    }

}
