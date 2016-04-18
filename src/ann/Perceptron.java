package ann;

import java.util.Random;

/**
 * The implementation of a perceptron see
 * https://en.wikipedia.org/wiki/Perceptron or
 * http://computing.dcu.ie/~humphrys/Notes/Neural/single.neural.html
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
public class Perceptron implements NeuralNetwork {
    
     /**
     * For creating the random weights
     */
    private static final Random RANDOM = new Random(24031995);
    
    /**
     * The activation function.
     *
     * @param input The value to be evaluated
     * @return A value between -1 and 1 based on the input
     */
    private double activationFunction(double input) {
        return Math.tanh(input);
    }

    @Override
    public void printWeights() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] activate(double[] input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] learn(double[] input, double[] expectedOutput) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
