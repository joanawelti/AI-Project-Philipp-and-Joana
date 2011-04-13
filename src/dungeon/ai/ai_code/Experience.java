package dungeon.ai.ai_code;

import java.io.Serializable;

public class Experience  implements Serializable{
	private OgreState currentState;
	private int action; //0 = attack 1 = evade
	
	public OgreState getCurrentState() {
		return currentState;
	}

	public int getAction() {
		return action;
	}

	public Experience(OgreState currentState, int action) {
		super();
		this.currentState = currentState;
		this.action = action;
	}
	

}
