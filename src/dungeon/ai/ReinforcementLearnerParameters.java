package dungeon.ai;

public class ReinforcementLearnerParameters {
	
	private int nrOgres;
	private double initValue;
	private double alphaValue;
	private double discountFactorValue;
	private double greedinessValue;
	
	private final int ogreMax = 10;
	private final int ogreMin = 0;
	private final double fractionMax = 1.0;
	private final double fractionMin = 0.0;
	
	private final int ogreDefaultValue = 1;
	private final double initDefaultValue = 0.01;
	private final double alphaDefaultValue = 0.5;
	private final double discountDefaultValue = 0.9;
	private final double greedinessDefaultValue = 0.9;
	
	/*public ReinforcementLearnerParameters(int nrOgres, double initValue, double alphaValue, double discountFactorValue, double greedinessValue) {
		this.nrOgres = nrOgres;
		this.initValue = initValue;
		this.alphaValue = alphaValue;
		this.discountFactorValue = discountFactorValue;
		this.greedinessValue = greedinessValue;
	} */
	
	public int getNrOgres() {
		return nrOgres;
	}
	
	public double getInitValue() {
		return initValue;
	}
	
	
	public double getAlphaValue() {
		return alphaValue;
	}
	
	
	public double getDiscountFactorValue() {
		return discountFactorValue;
	}
	
	
	public double getGreedinessValue() {
		return greedinessValue;
	}
	
	
	
	public void setOgreNumber(int number) {
		if (number < ogreMax && number > ogreMin) {
			this.nrOgres = number;
		} else {
			this.nrOgres = this.ogreDefaultValue;
		}
	}
	
	
	public void setInitValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			initValue = value;
		} else {
			initValue = initDefaultValue;
		}
	}
	
	
	public void setAlphaValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			alphaValue = value;
		} else {
			alphaValue = alphaDefaultValue;
		}
	}

	
	public void setDiscountValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			discountFactorValue = value;
		} else {
			discountFactorValue = discountDefaultValue;
		}
	}
	
	
	public void setGreedinessValue(double value) {
		if (value < fractionMax && value > fractionMin) {
			greedinessValue = value;
		} else {
			greedinessValue = greedinessDefaultValue;
		}
	}

}
