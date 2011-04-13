package dungeon.ai.ai_code;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import dungeon.ai.neural.GraphFQ;
import dungeon.ai.neural.GraphFQrec;
import dungeon.ai.neural.GraphQ;



public class FQ_learner {
	
	private static FQ_learner instance;

	public static double[][] Q_table=null;
	
	public static final float alpha=01.0f;//learning rate
	public static final float gamma=0.95f;//discount
	public static  float greedy=0.9f;
	public static final int numberOfActions=2;
	public static final int numberOfStates=4;
	public static final int iterations = 30;

	public static float dataIterXpos = 22.5f;
	public static float dataIterYpos = 22.5f;
	public static float dataIterEnergy = 100;
	public static boolean dataIterAttack = true;
	public static int runningstate=0;

	
	private boolean firstTimeQ=true;
	public double INPUT[][];
	
	public static ArrayList<Transition> transitionList = new ArrayList<Transition>();
	
	public static FQ_learner getInstance(){
		if(instance == null){
			instance = new FQ_learner();
		}
		return instance;
	}
	
	public FQ_learner(){
	}
	


	
	
	public void FQwithGraphics(){
		//blank Q values
		Q_table = new double[transitionList.size()][1];
		for (int x = 0; x < Q_table.length; x++) {
			Q_table[x][0] = 0;
		}
		
		//draw Q values
	    JFrame qGraph = new JFrame();
	    qGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphQ qgraph=new GraphQ();
	    qgraph.initGraphQ(Q_table,transitionList);
	    qGraph.add(qgraph);
	    qGraph.setSize(550,550);
	    qGraph.setLocation(00,500);
	    qGraph.setVisible(true);		
		
		//draw network output
	    JFrame attackGraph = new JFrame();
	    attackGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphFQ agraph=new GraphFQ();
	    agraph.initGraphFQ(Q_table,0.0);
	    attackGraph.add(agraph);
	    attackGraph.setSize(500,550);
	    attackGraph.setLocation(00,00);
	    attackGraph.setVisible(true);		
		
	    JFrame evadeGraph = new JFrame();
	    evadeGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphFQ egraph=new GraphFQ();
	    egraph.initGraphFQ(Q_table,1.0);
	    evadeGraph.add(egraph);
	    evadeGraph.setSize(500,550);
	    evadeGraph.setLocation(500,00);
	    evadeGraph.setVisible(true);		
		
	    JFrame recGraph = new JFrame();
	    recGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphFQrec rgraph=new GraphFQrec();
	    rgraph.initGraphFQ(Q_table);
	    recGraph.add(rgraph);
	    recGraph.setSize(500,550);
	    recGraph.setLocation(1000,00);
	    recGraph.setVisible(true);		
		
		printTable();
		System.out.println("got "+transitionList.size()+" transitions, ready to train");
		
		//this is the main loop that does all the learning
		for(int i = 0; i < iterations; i++){
			Qsweep(transitionList);
			//after the Q sweep, display the graphics showing Q values
			qgraph.initGraphQ(Q_table, transitionList);
			qGraph.update(qGraph.getGraphics());

			printTable();
			System.out.println("just finished FQ iteration (outer loop)= " + i);

			// do all the graphics for NN output
			attackGraph.update(attackGraph.getGraphics());
			evadeGraph.update(evadeGraph.getGraphics());
			recGraph.update(recGraph.getGraphics());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // do nothing for 1000 miliseconds (1 second)
		}
	}

	private void printTable() {
		// printout Q table 
		for (int index = 0; index < transitionList.size(); index++) {
			printTrans(transitionList.get(index));
			System.out.println(" i: "+index+" Q: "+Q_table[index][0]+" ");
		}
	}

	/*
	 * input transition exp loop through transition experiences
	 * 
	 * for each transition t use Q update rule oldState and action comes from t
	 * new state comes from t
	 * 
	 * for Q update fill up Q_table (output)
	 */
	public void Qsweep(List<Transition> transitions) {
		if(firstTimeQ)initTable(transitions);
		for (int index = 0; index < transitions.size(); index++) {
			Transition t = transitions.get(index);

			// get reward (if any)
			float reward = -0.08f;// small penalty for each timestep used
			if (t.getNextState().getEnemyHealth() < 0.001) {
				reward = 5;
			} else if (t.getNextState().getOgreHealth() < 0.001) {
				reward = -10;
			} else if (// t.getNextState().getEnemyHealth() <
						// t.getCurrentState()
			// .getEnemyHealth()&&
			t.getNextState().getOgreEnergy() < t.getCurrentState()
					.getOgreEnergy()) {
				// if we just got him with the bow
				reward = 5;
				// System.out.println("got reward");
			}
			Q_table[index][0] += alpha
					* (reward + gamma * queryBestState(t) 
							- Q_table[index][0]);

			// original version
			if (Q_table[index][0] > 10)
				Q_table[index][0] = 10;
			if (Q_table[index][0] < -10)
				Q_table[index][0] = -10;
			// System.out.println("Q: "+index+": "+Q_tableNN[index][0]);
		}
		firstTimeQ = false;
	}
	

	


	
	public double queryBestState(Transition t){
		double energy=t.getNextState().getOgreEnergy();
		double distance=t.getNextState().getEnemyDistance();
		double vAttack=FQ_learner.nearestValue(energy,distance,0);
		double vEvade=FQ_learner.nearestValue(energy,distance,1);
		if(vAttack>vEvade)
		return vAttack; else return vEvade;
	}
	
	public int selectAction(OgreState myState) {
//		System.out.print("selecting an action...");
		if (new Random().nextFloat() < greedy) {
//			System.out.println("choosing best action");
			return getBestAction(myState);
		} else {
			int action = new Random().nextInt(numberOfActions);
//			System.out.println("choosing random other action: " + action);
			return action;
		}
	}
	
	public int getBestAction(OgreState state){
		double energy=state.getOgreEnergy();
		double distance=state.getEnemyDistance();

		double action0 =FQ_learner.nearestValue(energy,distance,0);
		double action1 =FQ_learner.nearestValue(energy,distance,1);
//		state.print();
//		System.out.println("act0:"+action0);
//		System.out.println("act1:"+action1);

		if(action0 > action1){
			return 0;
		}else{
			return 1;
		}
		
	}
	


	
	private void initTable(List<Transition> transitions) {
		Q_table = new double[transitions.size()][1];
		for (int x = 0; x < Q_table.length; x++) {
			Q_table[x][0]=0;
		}
		
	}
	

	public void addTransitionNoTrain(Transition t){
		transitionList.add(t);
		printTrans(t);System.out.println();
		//safe it to the file
         String dir = "DunOut.txt";

        try{
            FileWriter out = new FileWriter(dir, true);
            BufferedWriter writer = new BufferedWriter(out);
            writer.write(t.toString());
            writer.newLine();
            writer.close();
            out.close();
            }catch(Exception e){
            System.err.println(e.toString());
            }   

	}

	private void printTrans(Transition t) {
		System.out.print("trans:"+
				t.getCurrentState().getOgreHealth()+" "+
				t.getCurrentState().getOgreEnergy()+" "+
				t.getCurrentState().getEnemyHealth()+" "+
				t.getCurrentState().getEnemyDistance()+" "+
				t.getAction()+" "+
				t.getNextState().getOgreHealth()+" "+
				t.getNextState().getOgreEnergy()+" "+
				t.getNextState().getEnemyHealth()+" "+
				t.getNextState().getEnemyDistance());
	}

	public static double nearestValue(double energy, double distance,
			double action) {
//		if(distance>0.43&&energy>0.5)
//			System.out.print("test");
		
		//scale energy to nearest 0.1
		energy*=10;
		energy=Math.round(energy);
		energy/=10;
		
		double closestDist = 100;
		Transition trans;
		double bestValue = 1;
		int bestIndex = 1;
		for (int index = 0; index < transitionList.size(); index += 2) {
			trans = transitionList.get(index);
			if (
					 Math.abs(trans.getCurrentState().getOgreEnergy()
							- energy) < 0.001
					&& 
					Math.abs(trans.getCurrentState().getEnemyDistance()-distance)
					< closestDist) 
			{
				closestDist = Math.abs(trans.getCurrentState().getEnemyDistance()-distance);
				bestIndex = index;
			}
		}
		// value of evade
		bestValue =  Q_table[bestIndex][0];
		// if attack then use that value
		if(bestIndex+1<transitionList.size())
		if (action<0.5)
			bestValue =  Q_table[bestIndex+1][0];
		return bestValue;

	}
}