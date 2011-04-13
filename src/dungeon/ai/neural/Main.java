package dungeon.ai.neural;

/**
 * class currently holds the XOR problem
 * and is used for testing
 *
 * credit to heaton research for nn tutorial
 *
 * @author john alexander
 */
public class Main {

    public static void main(String[] args) {
        
        double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };

        double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

        Network nn = new Network(2,1,ActivationFunction.SIGMODIAL);
        nn.addHiddenLayer(3);
        nn.finalizeNetwork();
        
        int epoch = 0;
        
        nn.setTrainingData(XOR_INPUT, XOR_IDEAL);
        
//        System.out.println("Epoch: " + epoch + " Error: " + nn.getError());
        
        

        long startTime = System.currentTimeMillis();
        while ((epoch < 5000) && (nn.getError() > 0.001)) {
            
            nn.iteration();
            
//            if (epoch % 10 == 0)
//                System.out.println("Epoch: " + epoch + " Error: " + nn.getError());
            
            epoch++;
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("Epoch: " + epoch + " Error: " + nn.getError());
        
        int i = 0;
        
        for (double[] input : XOR_INPUT) {
            double[] output = nn.computeOutputs(input);
            System.out.println("\nInput: " + input[0] + ", " + input[1] + "\nActual: " + output[0] + "\nIdeal: " + XOR_IDEAL[i++][0] + "\n");
        }

        System.out.println("Time taken: " + (endTime - startTime) + " millis");
        
    }
    
}
