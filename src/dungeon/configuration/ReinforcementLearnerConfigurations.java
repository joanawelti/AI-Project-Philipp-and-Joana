package dungeon.configuration;

import java.util.HashMap;


public class ReinforcementLearnerConfigurations extends Configurations {
	
	/**
	 * currently used values
	 */
	private double initValue;
	private double alphaValue;
	private double discountFactorValue;
	private double greedinessValue;
	
	private final double fractionMax = 1.0;
	private final double fractionMin = 0.0;
	private final double interval = 0.01;
	
	/**
	 * default values
	 */
	private final double initDefaultValue = 0.01;
	private final double alphaDefaultValue = 0.5;
	private final double discountDefaultValue = 0.9;
	private final double greedinessDefaultValue = 0.9;
	
	/**
	 * Labels for the parameters
	 */
	private final String initLabel = "Initial Q-Values";
	private final String alphaLabel = "Alpha value";
	private final String discountfactorLabel = "Discount factor";
	private final String greedinessLabel = "Greediness";
	
	/**
	 * keys for each parameter
	 */
	private final int init = 0;
	private final int alpha = 1;
	private final int discountfactor = 2;
	private final int greediness = 3;
	
	
	
	private HashMap<Integer, Double> parameters;
	
	
	/**
	 * Sets init to value if it is a legal value, otherwise it is set to default value
	 * @param value for init value
	 */
	private void setInitValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			initValue = value;
		} else {
			initValue = initDefaultValue;
		}
	}
	
	/**
	 * Sets alpha to value if it is a legal value, otherwise it is set to default value
	 * @param value for alpha 
	 */
	private void setAlphaValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			alphaValue = value;
		} else {
			alphaValue = alphaDefaultValue;
		}
	}

	/**
	 * Sets discount factor to value if it is a legal value, otherwise it is set to default value
	 * @param value for discount factor
	 */
	private void setDiscountValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			discountFactorValue = value;
		} else {
			discountFactorValue = discountDefaultValue;
		}
	}
	
	/**
	 * Sets greediness to value if it is a legal value, otherwise it is set to default value
	 * @param value for greediness
	 */
	private void setGreedinessValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			greedinessValue = value;
		} else {
			greedinessValue = greedinessDefaultValue;
		}
	}
	
	@Override
	public HashMap<Integer, Double> getParameters() {
		return this.parameters;
	}

	@Override
	public void setParameters(HashMap<Integer, Double> params) {
		setInitValue(params.get(init));
		setAlphaValue(params.get(alpha));
		setDiscountValue(params.get(discountfactor));
		setGreedinessValue(params.get(greediness));
		
		this.parameters = new HashMap<Integer, Double>();
		parameters.put(init, initValue);
		parameters.put(alpha, alphaValue);
		parameters.put(discountfactor, discountFactorValue);
		parameters.put(greediness, greedinessValue);				
	}

	@Override
	public HashMap<Integer, Double> getDefaultValues() {
		HashMap<Integer, Double> defaultValues = new HashMap<Integer, Double>();
		defaultValues.put(init, initDefaultValue);
		defaultValues.put(alpha, alphaDefaultValue);
		defaultValues.put(discountfactor, discountDefaultValue);
		defaultValues.put(greediness, greedinessDefaultValue);
		return defaultValues;
	}



	@Override
	public HashMap<Integer, Double> getParameterIntervals() {
		HashMap<Integer, Double> intervals = new HashMap<Integer, Double>();
		intervals.put(init, interval);
		intervals.put(alpha, interval);
		intervals.put(discountfactor, interval);
		intervals.put(greediness, interval);
		return intervals;
	}


	@Override
	public HashMap<Integer, Double> getParameterMins() {
		HashMap<Integer, Double> mins = new HashMap<Integer, Double>();
		mins.put(init, fractionMin);
		mins.put(alpha, fractionMin);
		mins.put(discountfactor, fractionMin);
		mins.put(greediness, fractionMin);
		return mins;
	}


	@Override
	public HashMap<Integer, Double> getParameterMax() {
		HashMap<Integer, Double> maxs = new HashMap<Integer, Double>();
		maxs.put(init, fractionMax);
		maxs.put(alpha, fractionMax);
		maxs.put(discountfactor, fractionMax);
		maxs.put(greediness, fractionMax);
		return maxs;
	}


	@Override
	public int[] getKeys() {
		int[] keys = {init, alpha, discountfactor, greediness};
		return keys;
	}


	@Override
	public HashMap<Integer, String> getLabels() {
		HashMap<Integer, String> labels = new HashMap<Integer, String>();
		labels.put(init, initLabel);
		labels.put(alpha, alphaLabel);
		labels.put(discountfactor, discountfactorLabel);
		labels.put(greediness, greedinessLabel);
		return labels;
	}

}
