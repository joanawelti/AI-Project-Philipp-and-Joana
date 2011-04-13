package dungeon.ai.neural;

import java.io.Serializable;

/**
 * represents a layer in the ann
 *
 * @author john alexander
 */
public class Layer implements Cloneable, Serializable {

    //weights and thresholds
    private Matrix _matrix;
    //activation function
    private ActivationFunction _activationFunction;
    //number of neurons
    private int _neurons;
    //next and previous layers
    private Layer _next;
    private Layer _previous;
    //last result of output calculation
    private double[] _output;
    //last error calculation
    private double[] _error;
    //neuron error deltas
    private double[] _delta;
    //accumluated deltas for weight and bias values
    private Matrix _accMatrixDelta;
    //previous deltas for checking sign change
    private Matrix _lastMatrixDelta;
    //matrix deltas
    private Matrix _matrixDelta;


    /*
     * constuct a layer
     */
    public Layer(ActivationFunction activationFunction, int neuronCount) {
        _neurons = neuronCount;
        _activationFunction = activationFunction;
        _output = new double[_neurons];
        _error = new double[_neurons];
        _delta = new double[_neurons];
    }

    /*
     * empty layer - used to clone
     */
    public Layer() {
        super();
    }

    /*
     * randomize the weight and threshold value matrix
     */
    public void reset() {

        //bias values should be close to 0
        for (int i = 0; i < _next._neurons; i++) {
            _matrix.set(_neurons, i, (Math.random() * 0.2) - 0.1);
        }

        //weights should not be too close to +1,-1.
        for (int i = 0; i < _neurons; i++) {
            for (int j = 0; j < _next._neurons; j++) {
                _matrix.set(i, j, (Math.random() * 1.6) - 0.8);
            }
        }
    }

    /*
     * set the weight and threshold matrix
     */
    public void setMatrix(Matrix matrix) {
        _matrix = matrix;
    }

    /*
     * set the activation function for this layer
     */
    public void setActivationFunction(ActivationFunction activationFunction) {
        _activationFunction = activationFunction;
    }

    /*
     * set the number of neurons in the layer
     */
    public void setNeurons(int neurons) {
        _neurons = neurons;
    }

    /*
     * set the accumulated matrix deltas
     */
    public void setAccMatrixDeltas(Matrix matrix) {
        _accMatrixDelta = matrix;
    }

    /*
     * set teh last matrix deltas
     */
    public void setLastMatrixDelta(Matrix matrix) {
        _lastMatrixDelta = matrix;
    }
    /*
     * set the matrix deltas
     */

    public void setMatrixDelta(Matrix matrix) {
        _matrixDelta = matrix;
    }

    /*
     * set the neuron deltas
     */
    public void setDeltas(double[] delta) {
        System.arraycopy(delta, 0, _output, 0, delta.length);
    }

    /*
     * set the neuron erros
     */
    public void setErrors(double[] error) {
        System.arraycopy(error, 0, _output, 0, error.length);
    }

    /*
     * set the neuron outputs
     */
    public void setOutputs(double[] output) {
        System.arraycopy(output, 0, _output, 0, output.length);
    }

    /*
     * get the weight and threshold matrix
     */
    public Matrix getMatrix() {
        return _matrix;
    }

    /*
     * get the neuron count for this layer
     */
    public int getNeurons() {
        return _neurons;
    }

    /*
     * determins if we are an input layer
     */
    public boolean inputLayer() {
        return (_previous == null);
    }

    /*
     * determins if we are an output layer
     */
    public boolean outputLayer() {
        return (_next == null);
    }

    /*
     * determeins if we are a hidden layer
     */
    public boolean hiddenLayer() {
        return !(inputLayer() || outputLayer());
    }

    /*
     * set the next layer and create a new weight and threshold matrix
     */
    public void setNext(Layer next) {
        _next = next;
        _matrix = new Matrix(_neurons + 1, _next.getNeurons());
        _accMatrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
        _lastMatrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
        _matrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
    }

    /*
     * add a new node to this layer
     * must be given a weight range
     */
    public void addNode(double low, double high) {

        _neurons += 1;
        Matrix matrix = new Matrix(_neurons + 1, _next.getNeurons());
        Matrix accMatrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
        Matrix lastMatrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
        Matrix matrixDelta = new Matrix(_neurons + 1, _next.getNeurons());
        double[] output = new double[_neurons];
        double[] error  = new double[_neurons];
        double[] delta  = new double[_neurons];

        
        //old stuff for the matricies
        for (int i = 0; i < _neurons - 2; i++) {
            for (int j = 0; j < _next.getNeurons(); j++) {
                matrix.add(i, j, _matrix.get(i, j));
                accMatrixDelta.add(i, j, _accMatrixDelta.get(i, j));
                lastMatrixDelta.add(i, j, _lastMatrixDelta.get(i, j));
                matrixDelta.add(i, j, _matrixDelta.get(i, j));
            }
        }

        //bias row
        for (int j = 0; j < _matrix.getCols(); j++) {
            matrix.add(matrix.getRows() -1, j, _matrix.get(_matrix.getRows() -1, j));
            accMatrixDelta.add(accMatrixDelta.getRows() - 1, j, _accMatrixDelta.get(_accMatrixDelta.getRows() -1, j));
            lastMatrixDelta.add(lastMatrixDelta.getRows() -1, j, _lastMatrixDelta.get(_lastMatrixDelta.getRows() -1, j));
            matrixDelta.add(matrixDelta.getRows() - 1, j, _matrixDelta.get(_matrixDelta.getRows() -1, j));
        }

        //old stuff for they arrays
        System.arraycopy(_output, 0, output, 0, _output.length);
        System.arraycopy(_error, 0, error, 0, _error.length);
        System.arraycopy(_delta, 0, delta, 0, _delta.length);


        //new neuron
        for (int j = 0; j < _matrix.getCols(); j++) {
            matrix.add(matrix.getRows() -2, j, Math.random() * (high - low) + low);
            accMatrixDelta.add(accMatrixDelta.getRows() - 2, j, 0);
            lastMatrixDelta.add(lastMatrixDelta.getRows() -2, j, 0);
            matrixDelta.add(matrixDelta.getRows() -2, j, 0);
        }
        
        //copy in the new array/matrices
        _matrix = matrix;
        _accMatrixDelta = accMatrixDelta;
        _lastMatrixDelta = lastMatrixDelta;
        _matrixDelta = matrixDelta;
        _output = output;
        _error = error;
        _delta = delta;

    }

    /*
     * set the next layer, pointer only!
     */
    public void setNextPointer(Layer next) {
        _next = next;
    }

    /*
     * return the next layer
     */
    public Layer getNext() {
        return _next;
    }

    /*
     * return the previous layer
     */
    public Layer getPrevious() {
        return _previous;
    }

    /*
     * return the output from this layer
     */
    public double[] getOutput() {
        return _output;
    }

    /*
     * set the previous layer
     */
    public void setPrevious(Layer previous) {
        _previous = previous;
    }

    /*
     * set the bias value for a neuron
     */
    public void setBias(int i, double value) {
        _matrix.set(i - 1, _next._neurons, value);
    }

    /*
     * get te bias value for a neuron
     */
    public double getBias(int i) {
        return _matrix.get(i - 1, _next._neurons);
    }

    /*
     * set the inital delta values
     */
    public void setInitalDeltas(double value) {
        _matrixDelta.set(value);
    }

    /*
     * set the error value for a neuron
     */
    public void setError(int i, double value) {
        _error[i] = 0;
    }

    /*
     * create an input matrix from the given pattern
     */
    public Matrix createInputMatrix(double pattern[]) {

        double[][] temp = new double[1][pattern.length + 1];
        System.arraycopy(pattern, 0, temp[0], 0, pattern.length);
        temp[0][pattern.length] = 1;

        return new Matrix(temp);
    }

    /*
     * compute the output from this layer from the given input pattern
     */
    public double[] computeOutputs(double[] pattern) {

        System.arraycopy(pattern, 0, _output, 0, _neurons);
        //if this is the output layer, just return input
        if (outputLayer()) {
            return _output;
        }

        Matrix input = createInputMatrix(pattern);
        double[] output = new double[_next._neurons];

        //for each neuron in the next layer set its output
        for (int i = 0; i < _next._neurons; i++) {
            Matrix temp = _matrix.getCol(i);
            double result = MatrixMath.dotProduct(temp, input);
            output[i] = _activationFunction.activationFunction(result);
        }

        return output;
    }

    /*
     * usefull for debuging
     */
    @Override
    public String toString() {

        String string = "";

        if (!outputLayer()) {
            if (inputLayer()) {
                string += "Input layer\n";
            } else {
                string += "Hidden layer\n";
            }

            for (int i = 0; i < _neurons; i++) {
                Matrix temp = _matrix.getRow(i);
                string += "\tNeuron " + i + ": " + temp.toString();
            }
        } else {
            string += "Output layer\n";
        }

        return string;
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

    /*
     * calulate the error value for this output layer
     * given the ideal values
     */
    public void calculateError(double[] ideal) {

        for (int i = 0; i < _neurons; i++) {
            _delta[i] = Math.min(Math.max(_activationFunction.derivativeFunction(_output[i])
                    * (ideal[i] - _output[i]), -1e20), 1e20);
        }

    }

    /*
     * calculate the error value for this layer
     */
    public void calculateError() {

        for (int i = 0; i < _next.getNeurons(); i++) {
            for (int j = 0; j < _neurons; j++) {

                //weight deltas
                _accMatrixDelta.add(j, i, _next._delta[i] * _output[j]);
                _error[j] += _matrix.get(j, i) * _next._delta[i];
            }

            //bias deltas - on the 'virtual' row
            _accMatrixDelta.add(_neurons, i, _next._delta[i]);

        }

        //if this is a hidden layer, need to set the error deltas
        if (hiddenLayer()) {
            for (int i = 0; i < _neurons; i++) {
                _delta[i] = Math.min(Math.max(_activationFunction.derivativeFunction(_output[i])
                        * _error[i], -1e20), 1e20);
            }
        }

    }

    /*
     * learn this layer in the network
     * this is the interesting bit of rprop
     */
    public void learn() {


        // for every value in our weight and bias matrix
        for (int i = 0; i < _matrix.getRows(); i++) {
            for (int j = 0; j < _matrix.getCols(); j++) {

                // check if the gradient has changed direction
                final int change = sign(_accMatrixDelta.get(i, j) * _lastMatrixDelta.get(i, j));
                double weightChange = 0;

                // no change, so we should increase the delta so we converge faster
                if (change > 0) {

                    // increase the delta
                    double delta = _matrixDelta.get(i, j) * Network.INCREASE_FACTOR;

                    // check we havn't gone too large
                    delta = Math.min(delta, Network.DELTA_MAX);

                    // set the new weight in the direction of the gradient
                    weightChange = sign(_accMatrixDelta.get(i, j)) * delta;

                    // set the altered delta for the neuron
                    _matrixDelta.set(i, j, delta);

                    // set the previous gradient value for this neuron
                    _lastMatrixDelta.set(i, j, _accMatrixDelta.get(i, j));

                    // change<0, so the last delta was too big
                } else if (change < 0) {

                    // decrease the delta
                    double delta = _matrixDelta.get(i, j) * Network.DECREASE_FACTOR;

                    // check haven't gone to small
                    delta = Math.max(delta, Network.DELTA_MIN);

                    // set the altered delta for the neuron.
                    _matrixDelta.set(i, j, delta);

                    // set previous gradient=0 so no change in sign next time.
                    _lastMatrixDelta.set(i, j, 0);


                    // so no change to the delta
                } else if (change == 0) {

                    // keep moving in the direction of the gradient
                    weightChange = sign(_accMatrixDelta.get(i, j)) * _matrixDelta.get(i, j);

                    // set the previous gradient value for this neuron
                    _lastMatrixDelta.set(i, j, _accMatrixDelta.get(i, j));
                }

                // change the weights and biases
                _matrix.add(i, j, weightChange);

            }
        }

        // clear out the gradient accumulator for the next iteration
        _accMatrixDelta.clear();
    }


    /*
     * clone this layer
     */
    @Override
    protected Layer clone() throws CloneNotSupportedException {

        Layer layer = new Layer();
        layer.setMatrix(_matrix.clone());
        layer.setActivationFunction(new ActivationFunction(_activationFunction.getType()));
        layer.setNeurons(_neurons);
        layer.setAccMatrixDeltas(_accMatrixDelta.clone());
        layer.setLastMatrixDelta(_lastMatrixDelta.clone());
        layer.setMatrixDelta(_matrixDelta.clone());
        layer.setDeltas(_delta);
        layer.setErrors(_error);
        layer.setOutputs(_output);
        return layer;
    }
}
