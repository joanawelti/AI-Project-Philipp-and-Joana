package dungeon.ai.ai_code;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import dungeon.ai.neural.ActivationFunction;
import dungeon.ai.neural.GraphNN;
import dungeon.ai.neural.GraphNNrec;
import dungeon.ai.neural.GraphQ;
import dungeon.ai.neural.Network;

public class NFQ_learner {
	
	private static NFQ_learner instance;

	public double[][] Q_tableNN=null;
	
	public static final float alpha=01.0f;//learning rate
	public static final float gamma=0.95f;//discount
	public static  float greedy=0.9f;
	public static final int numberOfActions=2;
	public static final int numberOfStates=4;
	public static final int iterations = 30;
	public static final int epochs = 100;
	public static final int transitionsPerTraining = 400;

	public static float dataIterXpos = 22.5f;
	public static float dataIterYpos = 22.5f;
	public static float dataIterEnergy = 100;
	public static boolean dataIterAttack = true;
	public static int runningstate=0;

	
	private static Network NN;
	private boolean firstTimeQ=true;
	public double INPUT[][];
	
	public ArrayList<Transition> transitionList = new ArrayList<Transition>();
	
	public static NFQ_learner getInstance(){
		if(instance == null){
			instance = new NFQ_learner();
		}
		return instance;
	}
	
	public NFQ_learner(){
		initializeNN();
		showNetwork();

	}
	
	private void initializeNN(){
		if(NN == null){
			NN = new Network(5, 1, ActivationFunction.SIGMODIAL);
			NN.addHiddenLayer(7);
			NN.addHiddenLayer(6);
			NN.finalizeNetwork();
		}
	}
	

	public void NFQ(){
		//draw network output
		showNetwork();
				
		System.out.println("got "+transitionList.size()+" transitions, ready to train");
			greedy+=0.1;
		
		for(int i = 0; i < iterations; i++){
			Qsweep(transitionList);
			NNtrain(transitionList);
			//printout Q table and NN side by side
			for(int index=0;index<transitionList.size();index++){
				printTrans(transitionList.get(index));
				System.out.print("Q:"+Q_tableNN[index][0]+" ");
				System.out.print("NN0: "+index+": "+20.0*(-0.5+NN.computeOutputs(INPUT[index])[0]));
				INPUT[index][4]=1;
				System.out.println("NN1: "+index+": "+20.0*(-0.5+NN.computeOutputs(INPUT[index])[0]));
			}
			System.out.println("just finished NFQ iteration (outer loop)= "+i);
		}
		showNetwork();
	}
	
	
	public void NFQwithGraphics(){
		//blank Q values
		Q_tableNN = new double[transitionList.size()][1];
		for (int x = 0; x < Q_tableNN.length; x++) {
			Q_tableNN[x][0] = 0;
		}
		
		//draw Q values
	    JFrame qGraph = new JFrame();
	    qGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphQ qgraph=new GraphQ();
	    qgraph.initGraphQ(Q_tableNN,transitionList);
	    qGraph.add(qgraph);
	    qGraph.setSize(550,550);
	    qGraph.setLocation(00,500);
	    qGraph.setVisible(true);		
		
		//draw network output
	    JFrame attackGraph = new JFrame();
	    attackGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphNN agraph=new GraphNN();
	    agraph.initGraphNN(NN,0.0);
	    attackGraph.add(agraph);
	    attackGraph.setSize(500,550);
	    attackGraph.setLocation(00,00);
	    attackGraph.setVisible(true);		
		
	    JFrame evadeGraph = new JFrame();
	    evadeGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphNN egraph=new GraphNN();
	    egraph.initGraphNN(NN,1.0);
	    evadeGraph.add(egraph);
	    evadeGraph.setSize(500,550);
	    evadeGraph.setLocation(500,00);
	    evadeGraph.setVisible(true);		
		
	    JFrame recGraph = new JFrame();
	    recGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GraphNNrec rgraph=new GraphNNrec();
	    rgraph.initGraphNN(NN);
	    recGraph.add(rgraph);
	    recGraph.setSize(500,550);
	    recGraph.setLocation(1000,00);
	    recGraph.setVisible(true);		
		
		System.out.println("got "+transitionList.size()+" transitions, ready to train");
		
		//this is the main loop that does all the learning
		for(int i = 0; i < iterations; i++){
			Qsweep(transitionList);
			//after the Q sweep, display the graphics showing Q values
			qgraph.initGraphQ(Q_tableNN,transitionList);
			qGraph.update(qGraph.getGraphics());
			
			NNtrain(transitionList);

			//printout Q table and NN side by side
			for(int index=0;index<transitionList.size();index++){
				printTrans(transitionList.get(index));
				System.out.print(" i: "+index+" Q: "+Q_tableNN[index][0]+" ");
				System.out.println("NN: "+20.0*(-0.5+NN.computeOutputs(INPUT[index])[0]));
			}
			System.out.println("just finished NFQ iteration (outer loop)= "+i);

			//do all the graphics for NN output
			attackGraph.update(attackGraph.getGraphics());
			evadeGraph.update(evadeGraph.getGraphics());
			recGraph.update(recGraph.getGraphics());
		}
	}
	

	
	
	/*
	input transition exp
	loop through transition experiences
	
	for each transition t
	use Q update rule oldState and action comes from t
	new state comes from t
	but we use NN to find best action value
	query network using afterstate, and runaway value1
	query NN using afterstate and attack value2
	use best of value1 or 2
	
	for Q update fill up Q_tableNN
	(output)
	*/
	public void Qsweep(List<Transition> transitions) {
		initTable(transitions);
		for (int index = 0; index < transitions.size(); index++) {
			Transition t = transitions.get(index);


			// get reward (if any)
			float reward =  -0.8f;//small penalty for each timestep used
			if (t.getNextState().getEnemyHealth() < 0.001) {
				reward = 10;
			} else if (t.getNextState().getOgreHealth() < 0.001) {
				reward = -10;
			} else if (//t.getNextState().getEnemyHealth() < t.getCurrentState()
					//.getEnemyHealth()&&
					t.getNextState().getOgreEnergy() < t.getCurrentState()
							.getOgreEnergy()) {
				// if we just got him with the bow
				reward = 10;
				// System.out.println("got reward");
			} 

			// update Q value
//			if (index == 79)
//				System.out.println("here");
			
			//this is the Q update part, according to equation
			if (firstTimeQ)
				Q_tableNN[index][0] += alpha
						* (reward - Q_tableNN[index][0]);
			else{
				double best=queryBestState(t);
				//only do updates from high values (to avoid picking up on errors from the NN)
				if(best>5)
				Q_tableNN[index][0]+= alpha
						* (reward + gamma * best - Q_tableNN[index][0]);
			}
			// clamp the values high or low
			// this is the bit I had to put in because the NN wasn't behaving as I wanted
//			if ((index % 2) == 1) {
//				if (Q_tableNN[index][0] > Q_tableNN[index - 1][0]
////						&& Q_tableNN[index][0] > 7.5
//						) {
//					Q_tableNN[index][0] = 10;
////					if(Q_tableNN[index - 1][0]>5)
//					Q_tableNN[index - 1][0] = -10;
//				} else if (Q_tableNN[index][0] < Q_tableNN[index - 1][0]
////						&& Q_tableNN[index - 1][0] > 7.5
//						) {
//					Q_tableNN[index - 1][0] = 10;
////					if(Q_tableNN[index][0]>5)
//						Q_tableNN[index][0] = -10;
//				}
//			}
//			if (Q_tableNN[index][0] > 7.5)
//				Q_tableNN[index][0] = 10;
//			if (Q_tableNN[index][0] < 7.6&&Q_tableNN[index][0] > 2)
//				Q_tableNN[index][0] = 7.5;
//			if (Q_tableNN[index][0] < 2&&Q_tableNN[index][0] > -5)
//				Q_tableNN[index][0] = -5;
//			if (Q_tableNN[index][0] < -5)
//				Q_tableNN[index][0] = -10;
			//original version
			if (Q_tableNN[index][0] > 10)
				Q_tableNN[index][0] = 10;
			if (Q_tableNN[index][0] < -10)
				Q_tableNN[index][0] = -10;
			// System.out.println("Q: "+index+": "+Q_tableNN[index][0]);
		}
		firstTimeQ=false;
	}
	
	/**
	Q_tableNN is ideal
	five inputs come from beforestate of transition experiences
	train network
	*/
	public void NNtrain(List<Transition> experiences){
		INPUT=new double[experiences.size()][5];
		
		for(int index=0;index<experiences.size();index++){
		Transition t= experiences.get(index);
			INPUT[index][0] = t.getCurrentState().getOgreHealth(); //health of himself
			INPUT[index][1] = t.getCurrentState().getOgreEnergy();	//energy self
			INPUT[index][2] = t.getCurrentState().getEnemyHealth();// health enemy
			INPUT[index][3] = t.getCurrentState().getEnemyDistance(); //distance
			INPUT[index][4] = t.getAction(); //action 1 = attack 0 = evade
		}
		
//		values are scaled to be between 0 and 1 before training
			 double[][] scaledQ_tableNN=null;
		scaledQ_tableNN = new double[experiences.size()][1];
		for (int x = 0; x < scaledQ_tableNN.length; x++) {
			scaledQ_tableNN[x][0] = (10.0+Q_tableNN[x][0])/20.0;
			System.out.println("NNip: "+x+": "+Q_tableNN[x][0]);
		}
	
	
		NN.setTrainingData(INPUT, scaledQ_tableNN);

		int epoch = 0;
		while ((epoch < epochs) && (NN.getError() > 0.001)) {
			NN.iteration();
			epoch++;
		}      
		
		System.out.println("Epoch: " + epoch + " Error: " + NN.getError());
	}
	

	public double[] queryNetwork(OgreState state, double action){
		double[] input = new double[5];
		for(int i = 0; i < state.toArray().length; i++){
			input[i] = state.toArray()[i];
		}
		input[4] = action;
		return NN.computeOutputs(input);
	}
	
	public double queryBestState(Transition t){
		double nnQuery0 = 20.0*(-0.5+queryNetwork(t.getNextState(), 0.0)[0]);
		double nnQuery1 = 20.0*(-0.5+queryNetwork(t.getNextState(), 1.0)[0]);
		if(nnQuery1>nnQuery0){
		return nnQuery1;
		}
		return nnQuery0;
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
		double action0 =NFQ_learner.getInstance().queryNetwork(state, 0)[0];
		double action1 =NFQ_learner.getInstance().queryNetwork(state, 1)[0];
		state.print();
		System.out.println("act0:"+action0);
		System.out.println("act1:"+action1);

		if(action0 > action1){
			return 0;
		}else{
			return 1;
		}
		
	}
	

	private void showNetwork() {
		double[] input = new double[5];
		input[0]=0.5;
		input[2]=1.0;
    	double increment=0.05;
//    	System.out.println(gapSize);
		for (double y = 0; y < 1; y += increment) {
			for (double x = 0; x < 1; x += increment) {

				input[1] = x;//x axis is my energy
				input[3] = y;//y axis is the distance
				input[4] = 1.0;
				double attackVal = NN.computeOutputs(input)[0];
				input[4] = 0.0;
				double evadeVal = NN.computeOutputs(input)[0];
				if (attackVal > evadeVal)
					System.out.print("1");
				else
					System.out.print("0");
			}
			System.out.println();
		}
	}
	
	private void initTable(List<Transition> transitions) {
		//I changed this so it doesn't pick up Q valuesfrom the network
		//(i.e. assuming no new transitions on each iteration)
		if(firstTimeQ)Q_tableNN = new double[transitions.size()][1];
		for (int x = 0; x < Q_tableNN.length; x++) {
			if(firstTimeQ)Q_tableNN[x][0]=0;
//			else
				// get old Q value from NN
//			Q_tableNN[x][0] = 20.0*(-0.5+queryNetwork(transitions.get(x).getCurrentState(), transitions.get(x).getAction())[0]);
		}
		
	}
	
	public void addTransition(Transition t){
		transitionList.add(t);
//		printTrans(t);System.out.println();
		if(transitionList.size()%transitionsPerTraining == 0){
			NFQ();
		}
		//safe it to the file
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
	

}