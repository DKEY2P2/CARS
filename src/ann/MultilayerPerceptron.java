package ann;

import java.util.ArrayList;
import java.util.Random;

/**
 * The implementation of a multilayerPerceptron see
 * https://en.wikipedia.org/wiki/Multilayer_perceptron
 * <p>
 * Using a hyperbolic tangent function as the activation function
 * <p>
 * Can be only created in the the NNfactory
 *
 * @see NNfactory
 * @author Kareem Horstink
 */
public class MultilayerPerceptron implements NeuralNetwork {

    private static final Random RANDOM = new Random(24031995);

    // [layer][nodeNumber]
    private Node[][] nodes;

    //[layer][nodeNumber][weightNumber]
    private double[][][] weights;

    protected MultilayerPerceptron(int inputLayer, int[] hiddenLayer, int outputLayer) {
        ArrayList<Node[]> tmp = new ArrayList();

        for (int i = 0; i < hiddenLayer.length + 2; i++) {
            if (i == 0) {
                tmp.add(new Node[inputLayer]);

            } else if (i == hiddenLayer.length + 1) {
                tmp.add(new Node[outputLayer]);
                break;
            } else {
                tmp.add(new Node[hiddenLayer[i - 1]]);
            }
        }

        nodes = tmp.toArray(new Node[1][1]);

        for (Node[] node : nodes) {
            for (int j = 0; j < node.length; j++) {
                node[j] = new Node();
            }
        }
        generateRandomWeights();
    }

    private void generateRandomWeights() {
        ArrayList<double[]> weightLayer = new ArrayList<>();
        ArrayList<double[][]> tmp = new ArrayList<>();

        for (int i = 0; i < nodes.length - 1; i++) {
            Node[] nodeLayer = nodes[i];
            for (int j = 0; j < nodeLayer.length; j++) {
                Node superCurrent = nodeLayer[j];
                weightLayer.add(new double[nodes[i + 1].length]);
            }
            tmp.add(weightLayer.toArray(new double[1][1]));
            weightLayer.clear();
        }
        weights = tmp.toArray(new double[1][1][1]);

        for (double[][] weight : weights) {
            for (double[] weight1 : weight) {
                for (int i = 0; i < weight1.length; i++) {
                    weight1[i] = RANDOM.nextDouble();

                }
            }

        }
    }

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
    public double[] activate(double[] input) {
        if (input.length != nodes[0].length) {
            throw new IllegalArgumentException("The amount of input does not reflect the amount of input nodes");
        }

        for (int i = 0; i < input.length; i++) {
            nodes[0][i].setCurrentValue(input[i]);
        }

        for (int layer = 1; layer < nodes.length; layer++) {
            Node[] currentLayer = nodes[layer];
            Node[] previousLayer = nodes[layer - 1];
            for (int nodeNumber = 0; nodeNumber < currentLayer.length; nodeNumber++) {
                Node node1 = currentLayer[nodeNumber];
                double sum = 0;
                for (int i = 0; i < previousLayer.length; i++) {
                    sum += previousLayer[i].getCurrentValue() * weights[layer - 1][i][nodeNumber];
                }
                node1.setCurrentValue(activationFunction(sum));
            }
        }
        double[] results = new double[nodes[nodes.length - 1].length];
        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            results[i] = nodes[nodes.length - 1][i].getCurrentValue();
        }
        return results;
    }

    @Override
    public void printWeights() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < weights.length; i++) {
            builder.append(Integer.toString(i));
            builder.append("\n");
            for (int j = 0; j < weights[i].length; j++) {
                builder.append(" ------").append(Integer.toString(j));
                builder.append("\n");
                for (int k = 0; k < weights[i][j].length; k++) {
                    builder.append("\t------").append(Double.toString(weights[i][j][k]));
                    builder.append("\n");
                }
            }
        }
        System.out.println(builder);
    }

    @Override
    public int size() {
        int i = 0;
        for (Node[] node : nodes) {
            for (Node node1 : node) {
                i++;
            }
        }
        return i;
    }

}
