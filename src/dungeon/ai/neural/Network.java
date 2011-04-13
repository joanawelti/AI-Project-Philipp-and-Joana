package dungeon.ai.neural;

import java.io.PrintStream;
import java.io.Serializable;

/**
 * a feedforward neural network.
 *
 * @author john alexander
 */
public class Network implements Cloneable, Serializable {
    
    //need to know our first and last layer
    private Layer _inputLayer;
    private Layer _outputLayer;
    
    //numbers of layers in our  network
    private int _layerCount;
    
    //activation funciton for the neurons
    private ActivationFunction _activationFunction;
    
    //can't alter a finalized network
    private boolean _finalize;
    
    //output stream for logging
    private transient final PrintStream _log = new PrintStream(System.out);
    
    //error value for the network
    private double _error;
    
    //bunch of constants for rprop
    public static final double INCREASE_FACTOR = 1.2;
    public static final double DECREASE_FACTOR = 0.5;
    public static final double DELTA_MAX = 50;
    public static final double DELTA_MIN = 1e-6;
    public static final double INITIAL_VALUE_DEFAULT = 0.1;
    public static final double ZERO_VALUE = 0.00000000000000001;
    
    
    //inital update value for rprop
    private double _initialUpdate;
    
    //input and ideal values for traning
    private double[][] _ideal;
    private double[][] _input;
     
    /*
     * generate a new network.
     *
     * inputNeurons - number of inputs
     * outputNeurons - number of outputs
     * activationFunction - the activation function type
     * initalUpdateValue - inital update value for matrix
     */
    public Network(int inputNeurons, int outputNeurons, int activationFunction, 
            double initialUpdateValue) {
        
        _finalize = false;
        _layerCount = 2;
        _activationFunction = new ActivationFunction(activationFunction);
        _inputLayer = new Layer(_activationFunction, inputNeurons);
        _outputLayer =  new Layer(_activationFunction, outputNeurons);
        _error = Double.MAX_VALUE;
        _initialUpdate = initialUpdateValue;
    }
    
    /*
     * generate a new network, with default initial update value.
     *
     * inputNeurons - number of inputs
     * outputNeurons - number of outputs
     * activationFunction - the activation function type
     */
    public Network(int inputNeurons, int outputNeurons, int activationFunction) {
        
        _finalize = false;
        _layerCount = 2;
        _activationFunction = new ActivationFunction(activationFunction);
        _inputLayer = new Layer(_activationFunction, inputNeurons);
        _outputLayer =  new Layer(_activationFunction, outputNeurons);
        _error =  Double.MAX_VALUE;
        _initialUpdate = INITIAL_VALUE_DEFAULT;
    }
    
    /*
     * add a hidden layer to the network
     */
    public void addHiddenLayer(int neuronCount) {
        
        if (_finalize) {
            _log.println("You cannot alter a finalized network");
            return;
        }
        
        _layerCount++;
        Layer hidden = new Layer(_activationFunction, neuronCount);
        
        Layer temp = getLayer(_layerCount -2);
        temp.setNext(hidden);
        hidden.setPrevious(temp);
    }
    
    /*
     * return a layer in the network
     */
    public Layer getLayer(int layerNumber) {
        
        if (layerNumber > _layerCount) {
            _log.println("Network only contains " + _layerCount + " layers");
            return null;
        }
        
        if (layerNumber == 1)
            return _inputLayer;
        if (layerNumber == _layerCount)
            return _outputLayer;
        
        int i = 1;
        Layer temp = _inputLayer.getNext();
        while (i++ < (layerNumber - 1))
            temp = temp.getNext();
        
        return temp;
    }
    
    /*
     * set the inital update values to all layers
     * for the biases and weights
     */
    public void setInitialUpdateValues() {
        
        Layer temp = _inputLayer;
        while (temp.getNext() != null) {
                temp.setInitalDeltas(_initialUpdate);
                temp = temp.getNext();
        }
    }
    
    /*
     * finalize the network so we can  use it
     * we attach the output layer to the last
     * hidden layer, and set all defualts for
     * the rprop learning
     *
     */
    public void finalizeNetwork() {
        
        Layer temp = getLayer(_layerCount - 1);
        temp.setNext(_outputLayer);
        _outputLayer.setPrevious(temp);
        _finalize = true;
        resetNetwork();
    }

    /*
     * get the output  layer
     */
    public Layer getOutputLayer() {
        return _outputLayer;
    }

    /*
     * set the output layer
     */
    public void setOutputLayer(Layer outputLayer) {
        _outputLayer = outputLayer;
    }

    /*
     * get the input layer
     */
    public Layer getInputLayer() {
        return _inputLayer;
    }

    /*
     * set the input layer
     */
    public void setInputLayer(Layer inputLayer) {
        _inputLayer = inputLayer;
    }
    
    /*
     * returns whether this network is finalized
     */
    public boolean getFinalize() {
        return _finalize;
    }
    
    /*
     * sets whether this netowkr is finalized
     */
    public void setFinalize(boolean value) {
        _finalize = value;
    }

    /*
     * get the layer count
     */
    public int getLayerCount() {
        return _layerCount;
    }

    /*
     * set the layer count
     */
    public void setLayerCount(int layerCount) {
        _layerCount = layerCount;
    }
    
    
    /*
     * set the inital update value
     */
    public void setInitialUpdate(double value) {
        _initialUpdate = value;
    }

    
    /*
     * randomize all weights and thresholds
     */
    public void resetNetwork() {
        
        if (!_finalize) {
            _log.println("You cannot reset a network until it is finalized");
            return;
        }
        
        Layer temp = _inputLayer;        
        for (int i = 1; i < _layerCount; i++) {
            temp.reset();
            temp = temp.getNext();
        }
    }
    
    /*
     * compute outputs as a batch
     */
    public double[][] computeOutputs(double[][] input) {
        
        double[][] outputs = new double[input.length][_outputLayer.getNeurons()];
        
        for (int i = 0; i < input.length; i++)
            outputs[i] = computeOutputs(input[i]);
        
        return outputs;
    }
    
    /*
     * compute output from the network
     * this is the feedforward algorithm
     */
    public double[] computeOutputs(double[] input, Layer layer) {
        
        if (!_finalize) {
            _log.println("You cannot use a network until it is finalized");
            return null;
        }
        
        if (input.length != layer.getNeurons()) {
            _log.println("Input vector size must match input layer size");
            return null;
        }
        
        if (!layer.outputLayer())
            return computeOutputs(layer.computeOutputs(input), layer.getNext());
        else
            return layer.computeOutputs(input);
        
    }
    
    /*
     * compute output from the network
     * this is the feedforward algorithm
     */
    public double[] computeOutputs(double[] input) {
        return computeOutputs(input, _inputLayer);
    }
    
    /*
     * usefull for debuging
     */
    @Override
    public String toString() {
        
        String string = "";   
        Layer temp = _inputLayer;
        for (int i = 0; i < _layerCount; i++) {
            string += "Layer " + i + ": " + temp.toString() + "\n";
            temp = temp.getNext();
        }
                
        return string;

    }
    
    /*
     * calculate the error for the network
     */
    public void calculateError(double[] ideal) {
        
        if (ideal.length != _outputLayer.getNeurons()) {
            _log.println("Output layer only has " + _outputLayer.getNeurons() + " neurons");
            return;
        }
        
        int j = 0;
        Layer temp = _inputLayer;
        
        //propagate forwards reseting each error value
        while (j++ < _layerCount) {
            for (int i = 0; i < temp.getNeurons(); i++)
                temp.setError(i,0);
            if (j < _layerCount)
                temp = temp.getNext();
        }  
        
        //propagate backwards to calculate error
        while (j-- > 1) {
            if (temp.outputLayer())
                temp.calculateError(ideal);
            else
                temp.calculateError();
            if (j > 1)
                temp = temp.getPrevious();
        }
                
    }
    
    /*
     * return the current network as a clone
     * of this object
     */
    @Override
    public Network clone() throws CloneNotSupportedException {
        
        Network temp = new Network(1,1,_activationFunction.getType());
        temp.setError(_error);
        temp.setInitialUpdate(_initialUpdate);
        temp.setInputLayer(_inputLayer.clone());
        temp.setOutputLayer(_outputLayer.clone());
        temp.setLayerCount(_layerCount);

        Layer t = _inputLayer;
        Layer ttt = temp.getInputLayer();
        for (int i = 0; i < temp.getLayerCount() - 1; i++) {
            Layer tt = t.getNext().clone();
            ttt.setNextPointer(tt);
            tt.setPrevious(ttt);
            t = t.getNext();
            ttt = tt;
        }

        temp.setFinalize(_finalize);
        return temp;
    }
    
    /*
     * error function for the network, in this
     * case using a root mean squared error function
     */
    public double ErrorFunction(double[][] ideal, double[][] input) {
        
        int size = 0;
        double error = 0;
        
        for (int i = 0; i < ideal.length; i++) {
            computeOutputs(input[i]);
            for (int j = 0; j < ideal[i].length; j++)
                error += Math.pow(ideal[i][j] - _outputLayer.getOutput()[j], 2);
            size += ideal[i].length;
        }
        
        return Math.min(Math.max(Math.sqrt(error / size), -1e20), 1e20);
        
    }
    
    
    /*
     * set the error value for the network
     */
    public void setError(double error) {
        _error = error;
    }
    
    /*
     * get the error value for the netwok
     */
    public double getError() {
        return _error;
    }
    
    /*
     * preform a training iteration
     */
    public void iteration() {
        
        
        if ((_ideal == null) || (_input == null)) {
            _log.println("No training data supplied");
            return;
        }
        
        for (int i = 0; i < _input.length; i++) {
            computeOutputs(_input[i]);
            calculateError(_ideal[i]);
        }
        
        learn();
        _error = ErrorFunction(_ideal, _input);     
    }
    
    /*
     * Set the input and ideals for training
     */
    public void setTrainingData(double[][] input, double[][] ideal) {
        _ideal = ideal;
        _input = input;
        setInitialUpdateValues();
    }
    
    /*
     * modify weights and thresholds through the network.
     */
    public void learn() {
        
        Layer temp = _inputLayer;
        while(temp.getNext() != null) {
            temp.learn();
            temp = temp.getNext();
        }
        
    }
    
}
