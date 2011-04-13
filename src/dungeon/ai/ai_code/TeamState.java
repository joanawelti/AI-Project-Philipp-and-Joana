package dungeon.ai.ai_code;

public abstract class TeamState {
	public static boolean closeOrc;
	
	// team state
	public static enum FactionState {
		farAway, close
	};
	

	// team action
	public static enum FactionAction {
		attack, muster
	};


	
	public static FactionState GlobalState=FactionState.farAway;
	public static FactionAction GlobalAction=null;


}
