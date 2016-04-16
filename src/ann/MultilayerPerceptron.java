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

    /**
     * For creating the random weights
     */
    private static final Random RANDOM = new Random(24031995);

    // [layer][nodeNumber]
    private Node[][] nodes;

    //[layer][nodeNumber][weightNumber]
    private double[][][] weights;

    protected MultilayerPerceptron(int inputLayer, int[] hiddenLayer, int outputLayer) {
        /* The creation of the nodes */
        ArrayList<Node[]> tmp = new ArrayList();

        for (int i = 0; i < hiddenLayer.length + 2; i++) {
            if (i == 0) {//input layer
                tmp.add(new Node[inputLayer]);
            } else if (i == hiddenLayer.length + 1) { //out layer
                tmp.add(new Node[outputLayer]);
                break;
            } else {//every other layer
                tmp.add(new Node[hiddenLayer[i - 1]]);
            }
        }

        nodes = tmp.toArray(new Node[1][1]); //make it into a "array" 

        /* Creates the nodes */
        for (Node[] node : nodes) {
            for (int j = 0; j < node.length; j++) {
                node[j] = new Node();
            }
        }

        /*Creation of the weights*/
        generateRandomWeights();
    }

    /**
     * Generates the structure of the weights and set the weights to some random
     * numbers
     */
    private void generateRandomWeights() {
        /*Don't ask me how to explain this, it works and I already forgotten
         how the is worked and I wrote it like 5 mins ago ~ Kareem*/
        ArrayList<double[]> nodeWeight = new ArrayList<>();
        ArrayList<double[][]> layer = new ArrayList<>();

        //Magic
        for (int i = 0; i < nodes.length - 1; i++) {
            Node[] nodeLayer = nodes[i];
            for (Node nodeLayer1 : nodeLayer) {
                nodeWeight.add(new double[nodes[i + 1].length]);
            }
            layer.add(nodeWeight.toArray(new double[1][1]));
            nodeWeight.clear();
        }
        weights = layer.toArray(new double[1][1][1]);

        /* Creates the random weights */
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
        /* Checks the input */
        if (input.length != nodes[0].length) {
            throw new IllegalArgumentException("The amount of input does not reflect the amount of input nodes");
        }

        /* Set the input nodes value to the new values */
        for (int i = 0; i < input.length; i++) {
            nodes[0][i].setCurrentValue(input[i]);
        }

        /* Calculates the rest of the values */
        for (int layer = 1; layer < nodes.length; layer++) {

            Node[] currentLayer = nodes[layer];//The current layer that the program is updating
            Node[] previousLayer = nodes[layer - 1]; //The previous layer that is used to update the current layer

            for (int nodeNumber = 0; nodeNumber < currentLayer.length; nodeNumber++) {
                Node node1 = currentLayer[nodeNumber]; //Which node we are updating

                double sum = 0;
                //Get the sum of all the input to the current node
                for (int i = 0; i < previousLayer.length; i++) {
                    sum += previousLayer[i].getCurrentValue() * weights[layer - 1][i][nodeNumber];
                }
                //Set the current value of the node to activation based on the input
                node1.setCurrentValue(activationFunction(sum));
            }
        }
        
        //Get the results based on the output layer nodes
        double[] results = new double[nodes[nodes.length - 1].length];
        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            results[i] = nodes[nodes.length - 1][i].getCurrentValue();
        }
        return results;
    }

    @Override
    public void printWeights() {
        //Prints the weights all "pretty" like
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
