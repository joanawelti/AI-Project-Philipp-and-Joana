package dungeon.ai.ai_code;

import java.io.Serializable;

public class Transition  implements Serializable{
	private OgreState currentState;
	private OgreState nextState;
	private int action; //0 = attack 1 = evade
	
	public OgreState getCurrentState() {
		return currentState;
	}

	public OgreState getNextState() {
		return nextState;
	}

	public int getAction() {
		return action;
	}

	public Transition(OgreState currentState, int action, OgreState nextState) {
		super();
		this.currentState = currentState;
		this.nextState = nextState;
		this.action = action;
	}
	
    @Override
    public String toString(){
        String alldata = "";
        
        for(double d:currentState.toArray()){
        alldata = alldata + d + " ";
        }
      
        alldata = alldata+action+" ";
        
        for(double d:nextState.toArray()){
        alldata = alldata + d + " ";
        }
        
        return alldata;
        }

//	public double[] getCurrentStateAndAction(){
//		double[] array = new double[5];
//		int i = 0;
//		for(double d : currentState.toArray()){
//			array[i] = d;
//			i++;
//		}
//		array[4] = action;
//		return array;
//	}
}
