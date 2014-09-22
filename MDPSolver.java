import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
public class MDPSolver {
        static Model baseModel;
        static State[] stateArr;
        static Action ptrAct;
    public static void main(String[] args) {
        baseModel = new Model();
        MDPSolver.assembleFigure17_1();
        baseModel.infiniteValueIteration(.99f, .1f);
        /* This infinite value iteration is only done to evaluate convergence of qLearning to optimal policy
         * The values derived from it are in no other ways used to calculate q
         * values or generate exploration policies/ policy factors.
        */
        runQLearning_and_graphQVals();// Runs q learning and sends the information to a visualizer.

    }
    
    // Runs Q-learning and graphs the values derived.
    static void runQLearning_and_graphQVals(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingProcess gPross = new GraphingProcess();
        gPross.takeInNames(baseModel.exportNames());
        gPross.takeInArray(baseModel.Q_Learn_To_Beyond_Convergence_And_Record_Q_Val_Results());
        f.add(gPross);
        f.setSize(1000,700);
        f.setLocation(0,0);
        f.setVisible(true);
    }
    
    // Runs Q-learning and graphs the objective valuations (rather than standard q valuations)
    static void runQLearning_and_graphObjectiveValuations(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingProcess gPross = new GraphingProcess();
        gPross.takeInNames(baseModel.exportNames());
        gPross.takeInArray(baseModel.Q_Learn_To_Beyond_Convergence_And_Record_Objective_Valuations());
        f.add(gPross);
        f.setSize(1000,700);
        f.setLocation(0,0);
        f.setVisible(true);
    }

    // Assembles one of the possible stochastic environments.
    // While the other similar methods have been removed it is generalizable to k-dimensional environements.
    static void assembleFigure17_1() {
        double r = -0.04f;
        stateArr = new State[11];
        stateArr[0] = new State(r, "State 1,3");
        stateArr[1] = new State(r, "State 2,3");
        stateArr[2] = new State(r, "State 3,3");
        stateArr[3] = new State(1, "State 4,3");
        stateArr[4] = new State(r, "State 1,2");
        stateArr[5] = new State(r, "State 3,2");
        stateArr[6] = new State(-1, "State 4,2");
        stateArr[7] = new State(r, "State 1,1");
        stateArr[8] = new State(r, "State 2,1");
        stateArr[9] = new State(r, "State 3,1");
        stateArr[10] = new State(r, "State 4,1");
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[0]);
        stateArr[0].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.9f, stateArr[0]);
        stateArr[0].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[4]);
        stateArr[0].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.9f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[4]);
        stateArr[0].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[2]);
        stateArr[1].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[2]);
        stateArr[1].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[1]);
        stateArr[1].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[1]);
        stateArr[1].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[3]);
        stateArr[2].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[5]);
        ptrAct.addTransition(.1f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[3]);
        stateArr[2].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[3]);
        ptrAct.addTransition(.1f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[5]);
        stateArr[2].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[1]);
        ptrAct.addTransition(.1f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[5]);
        stateArr[2].addAction(ptrAct);
        //
        //skip [3] because its an absorbing state
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[4]);
        stateArr[4].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[4]);
        stateArr[4].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[7]);
        stateArr[4].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[0]);
        ptrAct.addTransition(.1f, stateArr[7]);
        stateArr[4].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[9]);
        ptrAct.addTransition(.1f, stateArr[6]);
        ptrAct.addTransition(.1f, stateArr[5]);
        stateArr[5].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[6]);
        ptrAct.addTransition(.1f, stateArr[5]);
        stateArr[5].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[6]);
        ptrAct.addTransition(.1f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[5].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[5]);
        ptrAct.addTransition(.1f, stateArr[2]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[5].addAction(ptrAct);
        //
        // skip 6 because its an absorbing state
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[8]);
        stateArr[7].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[8]);
        stateArr[7].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[7]);
        stateArr[7].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[4]);
        ptrAct.addTransition(.1f, stateArr[7]);
        stateArr[7].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[8].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[8].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[9]);
        ptrAct.addTransition(.1f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[8]);
        stateArr[8].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[7]);
        ptrAct.addTransition(.1f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[8]);
        stateArr[8].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[5]);
        ptrAct.addTransition(.1f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[10]);
        stateArr[9].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[9]);
        ptrAct.addTransition(.1f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[10]);
        stateArr[9].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[5]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[9].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[8]);
        ptrAct.addTransition(.1f, stateArr[9]);
        ptrAct.addTransition(.1f, stateArr[5]);
        stateArr[9].addAction(ptrAct);
        //
        ptrAct = new Action("Up");
        ptrAct.addTransition(.8f, stateArr[6]);
        ptrAct.addTransition(.1f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[10].addAction(ptrAct);
        //
        ptrAct = new Action("Down");
        ptrAct.addTransition(.8f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[9]);
        stateArr[10].addAction(ptrAct);
        //
        ptrAct = new Action("Right");
        ptrAct.addTransition(.8f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[10]);
        ptrAct.addTransition(.1f, stateArr[6]);
        stateArr[10].addAction(ptrAct);
        //
        ptrAct = new Action("Left");
        ptrAct.addTransition(.8f, stateArr[9]);
        ptrAct.addTransition(.1f, stateArr[6]);
        ptrAct.addTransition(.1f, stateArr[10]);
        stateArr[10].addAction(ptrAct);
        //
        for (int k = 0; k < stateArr.length; k++) {
            baseModel.addState(stateArr[k]);
        }
        
    }
    
}
