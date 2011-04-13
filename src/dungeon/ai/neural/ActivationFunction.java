package dungeon.ai.neural;

import java.io.Serializable;

/**
 * contains sigmoidal (positive) and hyperbolic tangent (positive and negitive)
 * activation  functions.
 *
 *
 * @author john alexander
 */
public class ActivationFunction implements Serializable {

    public static final int SIGMODIAL = 1;
    public static final int HYPERBOLIC_TANGENT = 2;
    public static final int POLYNOMIAL = 3;

    //maximal curvate of the signmoid
    public static final double MAX_CURVATURE_SIGMOID = 2.38;


    //bounds for any number
    private static final double BOUND_MAX = 1e20;
    private static final double BOUND_MIN = -1e20;


    // are we using sigmodial or hyperbolic
    private int _type;

    /*
     * sets the type of function to use
     */
    public ActivationFunction(final int type) {
        _type = type;
    }

    /*
     * threshold function
     */
    public double activationFunction(double d) {
        if (_type == SIGMODIAL) {
            return sigmodial(d);
        }
        if (_type == HYPERBOLIC_TANGENT) {
            return hyperbolic(d);
        }
        if (_type == POLYNOMIAL) {
            return polynomial(d);
        }
        //should not reach here
        return 0.0;
    }

    /*
     * 2nd degree polynomial activation function
     * (should work even though it is polynomial)
     */
    public double polynomial(double d) {
  //     System.out.println(sign(d) * Math.sqrt(d * d)+1);
        return sign(d) * Math.sqrt(d * d + 1);

    }

    /*
     * sigmodial threshold function
     */
    public double sigmodial(double d) {
        return 1 / (1 + bound(Math.exp(-d)));
    }

    /*
     * hyperbolic tangent threshold function
     */
    public double hyperbolic(double d) {
        double temp = bound(Math.exp(-2 * d));
        return (1 - temp) / (1 + temp);
    }

    /*
     * derivative of the threshold function
     */
    public double derivativeFunction(double d) {
        if (_type == SIGMODIAL) {
            return sigmodialDerivative(d);
        }
        if (_type == HYPERBOLIC_TANGENT) {
            return hyperbolicDerivative(d);
        }
        if (_type == POLYNOMIAL) {
            return polynomialDerivative(d);
        }
        //should not reach here
        return 0.0;
    }

    /*
     * derivative of the polynomial threshold function
     */
    public double polynomialDerivative(double d) {
        return sign(d) * d / (Math.sqrt(d*d + 1));
    }

    /*
     * derivative of the sigmodial threshold function
     */
    public double sigmodialDerivative(double d) {
        return d * (1 - d);
    }

    /*
     * derivative of the hyperbolic tangent threshold function
     */
    public double hyperbolicDerivative(double d) {
        return 1 - d * d;
    }
    
    /*
     * return the type of this function
     */
    public int getType() {
        return _type;
    }


    /*
     * bound a number
     */
    private double bound(double d) {
        if (d < BOUND_MIN)
            return BOUND_MIN;
        else if (d > BOUND_MAX)
            return BOUND_MAX;
        return d;
    }

    /*
     * determine the sign of the value
     */
    private int sign(double value) {

        //we use an aproximation of 0
        if (Math.abs(value) < Network.ZERO_VALUE) {
            return 0;
        } else if (value > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
