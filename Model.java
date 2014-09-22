import java.util.ArrayList;
// The stochastic environement class composed of a state space and the states individual transition model.
public class Model {
    private ArrayList<State> stateSpace;
    private double gamma; // discount rate
    private double epsilon; // convergence threshold
    private int currentEpoch;// current backwards induction epoch that must reach k for finite horizon
    Model(){
        stateSpace = new ArrayList<State>();
        gamma = 1;
        epsilon = 0;
        currentEpoch = 0;
    }
    

    //Does discounted infinite value iteration with discount pDisoucnt and convergence threshold pEpsilon
    //Loop halts when the max norm [the largest scalar difference between corresponding elements of the utility vectors U and U'] is less than (e(1-g)/g)
    void infiniteValueIteration(double pDiscount, double pEpsilon){
        gamma = pDiscount;
        epsilon = pEpsilon;
        double maxNorm;
        maxNorm = pEpsilon;
        double possibleMax;
        while(epsilon*(1-gamma)/gamma <= maxNorm){
            possibleMax = 0;
            changeWholeEpoch();
            for (int j = 0; j < stateSpace.size(); j++) {
                stateSpace.get(j).updateValueAndPolicy(gamma);
                if(stateSpace.get(j).getDiff()> possibleMax){
                    possibleMax = stateSpace.get(j).getDiff();
                }
            }
            maxNorm = possibleMax;
        }
    }
    
    
    
    
    
    void addState(State pState){
        stateSpace.add(pState);
    }
    void print(){
        for (int j = 0; j < stateSpace.size(); j++) {
                stateSpace.get(j).print();
            }
    }
    void printPolicy(){
        for (int j = 0; j < stateSpace.size(); j++) {
                stateSpace.get(j).printPolicy();
            }
    }
    
    void changeWholeEpoch(){
        timeProceedsSinEpochPP();// I separated it like this to clarify the difference between the two is minimal
        currentEpoch++; //  epoch change happens here
    }
    void timeProceedsSinEpochPP(){
        for (int j = 0; j < stateSpace.size(); j++) {
                stateSpace.get(j).changeEpoch();
        }
    }
    void printEpoch(){
        System.out.println("The Current Epoch is " + currentEpoch);
    }
    
    //

    
    double getMaxReward(){ // what is the maximum possible reward in any state in the model.
        double outVal = stateSpace.get(0).getReward();
        double candidateVal;
        for(int i = 1; i< stateSpace.size(); i++){
            candidateVal = stateSpace.get(i).getReward();
            if(candidateVal > outVal) outVal = candidateVal;
        }
        return outVal;
    }
    
    
    
    // a single episode of q-learning given a model and start state
    void Q_LearnEpisode(State pState, double learnRate, double discount){ 
        State currentState = pState;
        State futureState;
        
        double r;
        if(currentState.isTerminal()){
            currentState.qSNone = currentState.getReward();
        }else{
            Action a = pState.getMaxQValdAct();
            double calcVal;
            double diff;
            double RPLUS = getMaxReward();
            //double RPLUS = 1000.0;
            
            double toChangeTo;
            while(currentState != null && !currentState.isTerminal()){

            a = funExplorationPolicy(currentState, currentState.getMaxQValdAct().getFreq(), RPLUS);
            //a = funExplorationPolicy(currentState, 1-currentState.getMaxQValdAct().getFreq(), RPLUS);
            //a = funExplorationPolicy(currentState, currentState.getMaxQValdAct().getFreq(), 1000);
            //a = infrequentFunExplorationPolicy(currentState, 5, 2);
            //a = funExplorationPolicy(currentState, .00001, RPLUS);
            //a = completelyRandomAction(currentState);
            
            currentState.incrementTimesVisted();
            a.calcFrequency(currentState.timesVisited);
            futureState = a.doAction();
            r = currentState.getReward();
            //
            
            if(futureState== null ||futureState.isTerminal()){
                diff = discount*futureState.getReward() - a.getQVal();
            }else{
                diff = discount*futureState.getMaxQValdAct().getQVal() - a.getQVal();
            }
            //
            //calcVal = learnRate*  (r +  diff); // this one converges
            //calcVal = learnRate* a.getFreq() * (r +  diff); //this one doesn't necessarily (maybe due to roundoff error?... seems unlikely)
            calcVal = learnRate* ((a.getFreq()+ 1)/2) * (r +  diff); //this definately converes and has good average time complexity but has HORRIBLE upper bounds
            //
            toChangeTo= calcVal + a.getQVal();
            a.setQVal(toChangeTo);
            // s,a,r,<-- s', argmax a' f(Q[s',a'], frequency[s',a']),r'
            currentState = futureState;
            }
        }
    }
    

    //
    //
    //
    Action completelyRandomAction(State pState){
        int numberOfActions = pState.getActions().size();
        double randVal = Math.random();
        return pState.getActions().get((int)(((double)numberOfActions)* randVal));
    }
   static Action funExplorationPolicy(State pState, double explrThresh, double RPlus){
        Action retAct = pState.getActions().get(0);// initialize it to the first action
        Action candidate;// candidate action
        double candidateVal;// the value of the candidate
        double currentMax = retAct.getQVal();// initialize the currentMax to the value of the initial action
        for(int i = 0; i< pState.getActions().size(); i++){
            candidate = pState.getActions().get(i);
            if(candidate.getFreq() < explrThresh){// give it the highest if it hasnt occured as often as that thing
                candidateVal = RPlus;
                //System.out.println("EXPLORATION OCCURED");
            }else{
                candidateVal = candidate.getQVal();
                //System.out.println("FUN OCCURED");
            }
            if (candidateVal > currentMax) {
                retAct = candidate;
                currentMax = candidateVal;
            }
        }
        return retAct;
    }
   
   static Action infrequentFunExplorationPolicy(State pState, int explrThresh, double RPlus){
        Action retAct = pState.getActions().get(0);// initialize it to the first action
        Action candidate;// candidate action
        double candidateVal;// the value of the candidate
        double currentMax = retAct.getQVal();// initialize the currentMax to the value of the initial action
        for(int i = 0; i< pState.getActions().size(); i++){
            candidate = pState.getActions().get(i);
            if(candidate.timesDone < explrThresh){// give it the highest if it hasnt occured as often as that thing
                candidateVal = RPlus;
            }else{
                candidateVal = candidate.getQVal();
            }
            if (candidateVal > currentMax) {
                retAct = candidate;
                currentMax = candidateVal;
            }
        }
        return retAct;
    }
    void printQPolicy(){
        for(int zzz = 0; zzz < stateSpace.size(); zzz++){
            System.out.print("At state " +'"'+ stateSpace.get(zzz).name + '"'+  " the policy is: ");
            if(!stateSpace.get(zzz).isTerminal()){
                System.out.println(stateSpace.get(zzz).getMaxQValdAct().actName);
            }else{
                System.out.println("NOTHING BECAUSE ITS TERMINAL");
            }
            
        }
    }
    
    void printQPolicyAndVal(){
        for(int zzz = 0; zzz < stateSpace.size(); zzz++){
            System.out.print("At state " +'"'+ stateSpace.get(zzz).name + '"'+  " the policy is: ");
            if(!stateSpace.get(zzz).isTerminal()){
                System.out.println(stateSpace.get(zzz).getMaxQValdAct().actName + " with value: " + stateSpace.get(zzz).getMaxQValdAct().getQVal());
            }else{
                System.out.println("NOTHING BECAUSE ITS TERMINAL");
            }
            
        }
    }
    
    double compare_QValPol_and_TruePol(){
        double diff;
        double maxDiff = 0;
        //
        Action aP;
        Action aQ;
        State s = stateSpace.get(0);
        State maxDiffState = s;
        for (int uuu = 0; uuu < stateSpace.size(); uuu++){
            s = stateSpace.get(uuu);
            if(!s.isTerminal()){
                aP = s.getBestAction();
                aQ = s.getMaxQValdAct();
                diff = Math.abs(aP.calculateExpectedValue() - aQ.calculateExpectedValue());
                if(aP == aQ) diff = 0;
            }else{
                diff = 0;
            }
            
            if(diff>maxDiff){
                maxDiff = diff;
                maxDiffState = s;
            }
        }
        //
        //System.out.println("The difference is: " + maxDiff + " and occured in state " + maxDiffState.name);
        return maxDiff;
    }
    int getSizeOfStateSpace(){
        return stateSpace.size();
    }

    
    ArrayList[] Q_Learn_To_Beyond_Convergence_And_Record_Objective_Valuations(){
        int stateSpaceSize = getSizeOfStateSpace();
        ArrayList[] valCols = new ArrayList[stateSpaceSize];
        for(int hhh = 0; hhh < valCols.length; hhh++){
            valCols[hhh] = new ArrayList<Double>();
        }
        long countOfWhile = 0;
        while(compare_QValPol_and_TruePol() >0|| countOfWhile < 30){// considered to have converged when the valuation difference is ==
            countOfWhile++;
            Q_LearnEpisode(stateSpace.get(7), .99, .99);
            //RECORD RESULTS
            for(int stateCount = 0; stateCount< stateSpaceSize; stateCount++){
                if(!stateSpace.get(stateCount).isTerminal()){
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getMaxQValdAct().calculateExpectedValue()));
                }else{
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getReward()));
                }
            }

        }
        
        System.out.println("It took " + countOfWhile + " episodes to get it to converge.");
        for(int count =0; count < (1*countOfWhile); count++){
            Q_LearnEpisode(stateSpace.get(7), .99, .99);
            for(int stateCount = 0; stateCount< stateSpaceSize; stateCount++){
                if(!stateSpace.get(stateCount).isTerminal()){
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getMaxQValdAct().calculateExpectedValue()));
                }else{
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getReward()));
                }
            }

        }
        // RETURN RESULTS and display in main
        return valCols;
    }
    
       ArrayList[] Q_Learn_To_Beyond_Convergence_And_Record_Q_Val_Results(){
        int stateSpaceSize = getSizeOfStateSpace();
        ArrayList[] valCols = new ArrayList[stateSpaceSize];
        for(int hhh = 0; hhh < valCols.length; hhh++){
            valCols[hhh] = new ArrayList<Double>();
        }
        long countOfWhile = 0;
        while(compare_QValPol_and_TruePol() >0/*|| countOfWhile < 50*/){// considered to have converged when the valuation difference is ==
            countOfWhile++;
            Q_LearnEpisode(stateSpace.get(7), .5, .99);
            //RECORD RESULTS
            for(int stateCount = 0; stateCount< stateSpaceSize; stateCount++){
                if(!stateSpace.get(stateCount).isTerminal()){
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getMaxQValdAct().getQVal()));
                }else{
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getReward()));
                }
            }

        }
        printQPolicy();
        System.out.println("It took " + countOfWhile + " episodes to get it to reach optimal policy (though more to technically converge).");
        for(int count =0; count < (4*countOfWhile); count++){
            //alt_Q_LearnEpisode(stateSpace.get(7), .99, .99);
            Q_LearnEpisode(stateSpace.get(7), .5, .99);
            for(int stateCount = 0; stateCount< stateSpaceSize; stateCount++){
                if(!stateSpace.get(stateCount).isTerminal()){
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getMaxQValdAct().getQVal()));
                }else{
                    valCols[stateCount].add(new Double(stateSpace.get(stateCount).getReward()));
                }
            }

        }
        // RETURN RESULTS and display in main
        return valCols;
    }
    
    String[] exportNames(){
        String[] outArr = new String[stateSpace.size()];
        for(int c = 0; c < stateSpace.size(); c++){
            outArr[c] = stateSpace.get(c).name;
        }
        return outArr;
    }
    //
    //
    //
    
    // The exploration ==> exploitation policy I mention in my report. Explores to the first occurence of optimal policy then switches to exploitation.
    void alt_Q_LearnEpisode(State pState, double learnRate, double discount){ // q learn given a model and start state
        State currentState = pState;
        State futureState;
        
        double r;
        if(currentState.isTerminal()){
            currentState.qSNone = currentState.getReward();
        }else{
            Action a = pState.getMaxQValdAct();
            double calcVal;
            double diff;
            double RPLUS = getMaxReward();
            
            double toChangeTo;
            while(currentState != null && !currentState.isTerminal()){

            
            a = funExplorationPolicy(currentState, 1-currentState.getMaxQValdAct().getFreq(), RPLUS);
            
            currentState.incrementTimesVisted();
            a.calcFrequency(currentState.timesVisited);
            futureState = a.doAction();
            r = currentState.getReward();// pretty sure this is supposed to be current not future as its indicated as r not r'
            //
            
            if(futureState== null ||futureState.isTerminal()){
                diff = discount*futureState.getReward() - a.getQVal();
            }else{
                diff = discount*futureState.getMaxQValdAct().getQVal() - a.getQVal();
            }
            //
            calcVal = learnRate* ((a.getFreq()+ 1)/2) * (r +  diff); //this definately converes and has good average time complexity but has HORRIBLE upper bounds
            //
            toChangeTo= calcVal + a.getQVal();
            a.setQVal(toChangeTo);
            // s,a,r,<-- s', argmax a' f(Q[s',a'], frequency[s',a']),r'
            currentState = futureState;
            }
        }
    }
}
