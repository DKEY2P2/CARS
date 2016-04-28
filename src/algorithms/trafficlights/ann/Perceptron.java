package algorithms.trafficlights.ann;

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

    private static double eps = 0.0001;
    private static double learningRate = 0.1;
    private Node[] input;
    private Node[] output;
    //[nodeNumber][weightNumber]
    private double[][] weight;
    private int iterationThreshold;

    /**
     * Creates a perceptron
     *
     * @param inputNode The number of input nodes wanted
     * @param outputNode The number of output nodes wanted
     * @param iterationThreshold How many iteration is allowed for it to handle
     */
    protected Perceptron(int inputNode, int outputNode, int iterationThreshold) {
        input = new Node[inputNode];
        output = new Node[outputNode];
        this.iterationThreshold = iterationThreshold;
        for (int i = 0; i < inputNode; i++) {
            input[i] = new Node();
        }
        for (int i = 0; i < outputNode; i++) {
            output[i] = new Node();
        }
        weight = new double[inputNode][outputNode];
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                weight[i][j] = RANDOM.nextDouble();
            }
        }
    }

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
//        return input > 1 ? 1 : 0;
    }

    @Override
    public void printWeights() {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < weight.length; j++) {
            builder.append(" ------").append(Integer.toString(j));
            builder.append("\n");
            for (int k = 0; k < weight[j].length; k++) {
                builder.append("\t------").append(Double.toString(weight[j][k]));
                builder.append("\n");
            }
        }
        System.out.println(builder);
    }

    @Override
    public int size() {
        return output.length + input.length;
    }

    @Override
    public double[] activate(double[] input) {
        //checks if the input is the right size
        if (input.length != this.input.length) {
            throw new IllegalArgumentException("The amount of input does not reflect the amount of input nodes");
        }
        //set the input layer
        for (int i = 0; i < this.input.length; i++) {
            this.input[i].setCurrentValue(input[i]);
        }
        //Calculates the activation function
        double sum = 0;
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < this.input.length; j++) {
                sum += this.input[j].getCurrentValue() * weight[j][i];
            }
            output[i].setCurrentValue(activationFunction(sum));
            sum = 0;
        }
        //Gives out the output
        double[] results = new double[output.length];
        for (int i = 0; i < output.length; i++) {
            results[i] = output[i].getCurrentValue();
        }
        return results;
    }

    @Override
    public double[] learn(double[] input, double[] expectedOutput) {
        if (expectedOutput.length != output.length) {
            throw new IllegalArgumentException("The amount of expected results does not reflect the amount of output nodes");
        }
        double[] results = null;
        for (int t = 0; t < iterationThreshold; t++) {
            results = activate(input);
            for (int i = 0; i < results.length; i++) {
                if (eps > expectedOutput[i] - results[i]) {
                    break;
                }
            }
            for (int i = 0; i < this.input.length; i++) {
                for (int j = 0; j < output.length; j++) {
                    weight[i][j] = weight[i][j] + learningRate * (expectedOutput[j] - results[j]) * input[i];
                }
            }
        }
        return results;
    }
}
