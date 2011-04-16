package dungeon.configuration;

import java.util.HashMap;

public abstract class Configurations {
	
	/**
	 * Parameters of this configurations
	 * @return current values for every parameter
	 */
	public abstract HashMap<Integer, Double> getParameters();

	/**
	 * Set parameters to params
	 * @param params 
	 */
	public abstract void setParameters(HashMap<Integer, Double> params);
	
	/**
	 * Each parameter needs to have an integer key, with with all the
	 * other data can be accessed
	 * @return array with an entry for each key
	 */
	public abstract int[] getKeys();
	
	/**
	 * Returns a label for each parameter, accessible with parameter's key
	 * @return labels
	 */
	public abstract HashMap<Integer, String> getLabels();
	
	/**
	 * Returns a default value for each parameter, accessible with parameter's key
	 * @return default values
	 */
	public abstract HashMap<Integer, Double> getDefaultValues();
	
	/**
	 * Returns the interval for each parameter, accessible with parameter's key
	 * Parameter can only have discrete values. Intervals specify the steps.
	 * @return intervals, 
	 */
	public abstract HashMap<Integer, Double> getParameterIntervals();
	
	/**
	 * Returns the minimal value for each parameter, accessible with parameter's key
	 * @return minimums
	 */
	public abstract HashMap<Integer, Double> getParameterMins();
	
	/**
	 * Returns the maximal value for each parameter, accessible with parameter's key
	 * @return maximums
	 */
	public abstract HashMap<Integer, Double> getParameterMax();
}
