import java.util.ArrayList;
// The class for actions (directly associated with states). Has a set of probabilistic moves called transitions.
public class Action {
    private ArrayList<Transition> possibleTrans;
    String actName;
    //
    double qVal;
    int timesDone;
    double freqDone;
    
    
    Action(String pName){
        timesDone = 0;
        possibleTrans = new ArrayList<Transition>();
        actName = pName;
        qVal = 0;
        freqDone = 0;
    }

    void addTransition(State pState){
        Transition current = new Transition(pState);
        possibleTrans.add(current);
    }
    void addTransition(double pProb, State pState){
        Transition current = new Transition(pProb, pState);
        possibleTrans.add(current);
    }
    
    ArrayList<Transition> getPossibilities(){
        return possibleTrans;
    }
    double calculateExpectedValue(){
        double expVal;
        expVal = 0;
        for (int i = 0; i< possibleTrans.size(); i++){
            expVal+= possibleTrans.get(i).getProbWeightedVal();// get average summed expected reward
        }
        return expVal;
    }
    void print(){
        System.out.println("Action Name: " + actName);
        for (int i = 0; i< possibleTrans.size(); i++){
            possibleTrans.get(i).print();
        }
    }
    void printLess(){
        System.out.println("\tAction Name: " + actName);
    }
    //
    //
    //
    State doAction(){
        incrementActionCount();
        State landing = null;
        double randVal;
        randVal = (double) Math.random();
        double accumVal; // sums of the passed over probabilities
        accumVal =0;
        for(int i = 0; i < possibleTrans.size(); i++ ){
            accumVal += possibleTrans.get(i).getProb();
            if(accumVal > randVal || accumVal > 1){
                return possibleTrans.get(i).getState();
            }
        }
        return landing;
    }
    
    void incrementActionCount(){
        timesDone++;
    }
    void setQVal( double pVal){
        qVal = pVal;
    }
    double getQVal(){
        return qVal;
    }
    int getTimesDone(){
        return timesDone;
    }
    
    void calcFrequency(int denominator){
        freqDone = ((double)timesDone)/ ((double)denominator);
    }
    double getFreq(){
        return freqDone;
    }
}
