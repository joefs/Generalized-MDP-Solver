import java.util.ArrayList;
// A class for single states in the state space. Each state has its own possible actions.
public class State {
    private double reward;
    private double curVal;//currentValue
    private double preVal;// previous value
    private ArrayList<Action> possibleActions; // possible actions to take here
    private Action policyParticle;//what action should be taken at this state
    public String name;
    //
    public double qSNone;// the value of its lacking an action
    int timesVisited;
    State(){
        curVal = 0;
        possibleActions = new ArrayList<Action>();
        policyParticle = null;
        qSNone = 0;
        timesVisited = 0;
    }
    State(double pReward, String pName){
        reward = pReward;
        curVal = reward;
        possibleActions = new ArrayList<Action>();
        name = pName;
        policyParticle = null;
        qSNone = 0;
        timesVisited = 0;
    }
    State(double pReward, double pHeurVal, String pName){// if you want to give i an initial heuristic value
        reward = pReward;
        curVal = pHeurVal;//kkk
        possibleActions = new ArrayList<Action>();
        name = pName;
        policyParticle = null;
        timesVisited = 0;
    }
    ArrayList<Action> getActions(){
        return possibleActions;
    }
    double getReward(){
        return reward;
    }
    
    double getCurVal(){
        return curVal;
    }
    double getPreVal(){
        return preVal;
    }
    void changeEpoch(){
        preVal = curVal;
    }
    Action getBestAction(){
        if(possibleActions == null || possibleActions.isEmpty()) return null;
        Action BestAction = policyParticle;
        for(int i = 0; i< possibleActions.size();  i++){
            if(possibleActions.get(i).calculateExpectedValue()> BestAction.calculateExpectedValue()){
                BestAction = possibleActions.get(i);
            }
        }
        return BestAction;
    }
    boolean updatePolicy(){
        Action possible = getBestAction();
        if(possible!= policyParticle){
            policyParticle = getBestAction();
            return true;// IF POLICY DOES CHANGE RETURN TRUE
        }
        return false;
    }
    
    boolean updateValueAndPolicy(double discRate){
        boolean retVal;
        retVal = updatePolicy();
        if(policyParticle== null){
            curVal = reward;
        }else{   
            curVal = reward +( discRate * policyParticle.calculateExpectedValue());
        }
        return retVal;
    }
    
    void updateValueOnly(double discRate){
        if(policyParticle== null){
            curVal = reward;
        }else{   
            curVal = reward +( discRate * policyParticle.calculateExpectedValue());
        }
    }
    
    double getDiff(){
        return preVal - curVal;
    }
    void addAction(Action pAct){
        possibleActions.add(pAct);
        if(policyParticle == null){// If its the first action added make it the policy to avoid nullpointers
            policyParticle = pAct;
        }
    }
    void print(){
        System.out.println("State Name: "+ name + " Value: " + curVal);
        for(int i = 0; i< possibleActions.size();  i++){
            possibleActions.get(i).print();
        }
    }
    void printPolicy(){
        System.out.println("Policy at state " + name + " is ");
        if (policyParticle != null) {
            
            policyParticle.printLess();
        }else{
            System.out.println("\tGoal/Sink State");
        }
        System.out.println("\tThe value of the state is " + curVal);
    }
    //
    //
    //
    boolean isTerminal(){
        if(possibleActions == null || possibleActions.isEmpty()){
            return true;
        }
        return false;
    }
    
    Action getMaxQValdAct(){
        Action maxQValdAct = possibleActions.get(0);
        double valToBeat = maxQValdAct.qVal;
        double contender;
        for(int i = 1; i< possibleActions.size(); i++){
            contender = possibleActions.get(i).qVal;
            if( contender > valToBeat){
                valToBeat = contender;
                maxQValdAct = possibleActions.get(i);
            }
        }
        return maxQValdAct;
    }
    
    
    void incrementTimesVisted(){
        timesVisited++;
    }
}
