package dungeon.ai.ai_code;

import java.util.Random;

import dungeon.App;
import dungeon.ai.ai_code.TeamState.FactionAction;
import dungeon.ai.ai_code.TeamState.FactionState;

public class Q_learner {

	public static float[] Q_table=null;
	public static float alpha=0.2f;//learning rate
	public static float gamma=0.7f;//discount
	public static float greedy=0.5f;
	public static int numberOfActions=0;
	public static int numberOfStates=0;
	public static int size=0;
	
	public static void initTable(int actions,int states)
	{
		numberOfActions=actions;
		numberOfStates=states;
		size=actions*states;
		Q_table = new float[size];
		for (int x = 0; x < size; x++) {
			Q_table[x] = 5 + new Random().nextFloat();
		}
	}
	
	public static void updateTable(FactionState myOldState, FactionState myNewState, FactionAction currentAction, float reward)
	{
		// convert state to number
		int stateAction = myOldState.ordinal() * 2 + currentAction.ordinal();
		// update the table
		Q_table[stateAction] += alpha	* (reward + gamma * getBestActionValue(myNewState) - Q_table[stateAction]);
		// print out the table
		printTable();
	}

	public static void printTable() {
//		System.out.println();System.out.println("Table values: ");
		App.log("Table values: ");
		int i = 0;
		for (int s = 0; s < numberOfStates; s++) {
			App.log("state " + s + " : ");
			for (int a = 0; a < numberOfActions; a++) {
				App.log(" action " + a + " : " + Q_table[i] + ";");
				i++;
			}
			App.log("");
		}
	}

	public static int selectAction( FactionState myState){
		System.out.print("selecting an action...");
		if(new Random().nextFloat()<greedy){
			System.out.println("choosing best action");
			return getBestAction(myState);
		}else
		{
			int action=new Random().nextInt(numberOfActions);
			System.out.println("choosing random other action: "+action);
			return action;
		}
	}
	
	private static float getBestActionValue(FactionState myState) {
		// TODO Auto-generated method stub
		return Q_table[getBestAction(myState)+myState.ordinal()*numberOfActions];
	}

	private static int getBestAction(FactionState myState) {
		// TODO Auto-generated method stub
		float bestSoFarValue = -100;
		int bestSoFarIndex = 0;
		int base=myState.ordinal()*numberOfActions;
		for (int i = 0; i < numberOfActions; i++)
			if (Q_table[i+base] > bestSoFarValue) {
				bestSoFarValue = Q_table[i+base];
				bestSoFarIndex = i;
			}
		return bestSoFarIndex;
	}
}
