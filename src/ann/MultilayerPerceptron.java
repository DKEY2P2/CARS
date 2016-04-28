package ann;

import java.util.Random;

/**
 * The implementation of a multilayerPerceptron see
 * https://en.wikipedia.org/wiki/Multilayer_perceptron or
 * http://rfhs8012.fh-regensburg.de/~saj39122/jfroehl/diplom/e-1.html
 * <p>
 * Using a hyperbolic tangent function as the activation function.
 * <p>
 * Uses backpropagation (https://en.wikipedia.org/wiki/Backpropagation) to learn
 * the weights on the edge matrix
 * <p>
 * Can be only created in the the NNfactory
 *
 * @see NNfactory
 * @author Kareem Horstink
 */
public class MultilayerPerceptron implements NeuralNetwork {
    
    double learningRate = 0.1;
    
    protected MultilayerPerceptron(int inputLayer, int[] hiddenLayer, int outputLayer) {
        
        
    }

    /**
     * Generates the structure of the weights and set the weights to some random
     * numbers
     */
    private void generateRandomWeights() {
    }

    @Override
    public double[] activate(double[] input) {
        return null;
    }
    
    @Override
    public void printWeights() {
    }
    
    public void printError() {
    }
    
    public void printValue() {
    }
    
    @Override
    public int size() {
        return 1;
    }

    @Override//http://neuralnetworksanddeeplearning.com/chap2.html
    public double[] learn(double[] input, double[] expectedOutput) {
        return null;
    }
    
}
