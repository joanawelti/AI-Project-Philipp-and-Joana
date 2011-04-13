package dungeon.ai.ai_code;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import dungeon.ai.neural.ActivationFunction;
import dungeon.ai.neural.GraphNNforCirc;
import dungeon.ai.neural.GraphNNp2;
import dungeon.ai.neural.Network;

public class CircularNetwork {

	private static CircularNetwork instance;
	public static int runningstate = 0;
	public double INPUT[][];
	public JFrame experienceGraphFrame = new JFrame();
	public GraphNNforCirc experiencegraph = new GraphNNforCirc();
	public JFrame networkOutputGraphFrame = new JFrame();
	public GraphNNp2 networkOutputGraph = new GraphNNp2();
	public static final int epochs = 1000;
	private static Network NN;
	public ArrayList<Experience> experienceList = new ArrayList<Experience>();

	public static CircularNetwork getInstance() {
		if (instance == null) {
			instance = new CircularNetwork();
		}
		return instance;
	}

	public CircularNetwork() {
		initializeNN();
		// set up the graphics to draw the experiences as they are gathered
		experienceGraphFrame
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		experiencegraph.initGraphQ(experienceList);
		experienceGraphFrame.add(experiencegraph);
		experienceGraphFrame.setSize(550, 550);
		experienceGraphFrame.setLocation(00, 500);
		experienceGraphFrame.setVisible(true);
	}

	private void initializeNN() {
		if (NN == null) {
			NN = new Network(2, 1, ActivationFunction.SIGMODIAL);
			NN.addHiddenLayer(10);
			NN.finalizeNetwork();
		}
	}

	public void trainwithGraphicsNN() {
		// draw network output
		networkOutputGraphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		networkOutputGraph.initGraphNN(NN, 0.0);
		networkOutputGraphFrame.add(networkOutputGraph);
		networkOutputGraphFrame.setSize(500, 550);
		networkOutputGraphFrame.setLocation(00, 00);
		networkOutputGraphFrame.setVisible(true);

		// this is the main loop that does all the learning
		NNtrain(experienceList);
		// do all the graphics for NN output
		networkOutputGraphFrame.update(networkOutputGraphFrame.getGraphics());
	}

	public void NNtrain(List<Experience> experiences) {
		INPUT = new double[experiences.size()][2];
		// values are scaled to be between 0 and 1 before training
		double[][] IDEAL = null;
		IDEAL = new double[experiences.size()][1];

		for (int index = 0; index < experiences.size(); index++) {
			Experience t = experiences.get(index);
			INPUT[index][0] = t.getCurrentState().getOgreEnergy(); // energy
			INPUT[index][1] = t.getCurrentState().getEnemyDistance(); // distance
			IDEAL[index][0] = t.getAction(); // action 1 = attack 0 = evade
		}

		NN.setTrainingData(INPUT, IDEAL);

		System.out.println("size: " + experiences.size());

		int epoch = 0;
		while ((epoch < epochs) && (NN.getError() > 0.001)) {
			NN.iteration();
			System.out.println("Epoch: " + epoch + " Error: " + NN.getError());
			epoch++;
		}
		System.out.println("Epoch: " + epoch + " Error: " + NN.getError());
	}

	public double[] queryNetwork(OgreState state, double action) {
		double[] input = new double[2];
		input[0] = state.getOgreEnergy();
		input[1] = state.getEnemyDistance();
		return NN.computeOutputs(input);
	}

	public void addExperienceNoTrain(Experience t) {
		experienceList.add(t);
		experiencegraph.initGraphQ(experienceList);
		experienceGraphFrame.update(experienceGraphFrame.getGraphics());
	}
}