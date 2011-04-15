package dungeon.configuration;

import java.util.HashMap;

public abstract class Configurations {
	
	public abstract HashMap<Integer, Double> getParameters();

	public abstract void setParameters(HashMap<Integer, Double> params);
	
	public abstract int[] getKeys();
	
	public abstract HashMap<Integer, String> getLabels();
	
	public abstract HashMap<Integer, Double> getDefaultValues();
	
	public abstract HashMap<Integer, Double> getParameterIntervals();
	
	public abstract HashMap<Integer, Double> getParameterMins();
	
	public abstract HashMap<Integer, Double> getParameterMax();
}
