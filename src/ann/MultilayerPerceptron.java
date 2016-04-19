package ann;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * For creating the random weights
     */
    private static final Random RANDOM = new Random(240395);

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

//                                                    / *~*~*~*~*
//                                                  .7
//                                       \       , //
//                                       |\.--._/|//
//                                      /\ ) ) ).'/
//                                     /(  \  // /
//                                    /(   J`((_/ \
//                                   / ) | _\     /
//                                  /|)  \  eJ    L
//                                 |  \ L \   L   L
//                                /  \  J  `. J   L
//                                |  )   L   \/   \
//                               /  \    J   (\   /
//             _....___         |  \      \   \```
//      ,.._.-'        '''--...-||\     -. \   \
//    .'.=.'                    `         `.\ [ Y
//   /   /                                  \]  J
//  Y / Y                                    Y   L
//  | | |          \                         |   L
//  | | |           Y                        A  J
//  |   I           |                       /I\ /
//  |    \          I             \        ( |]/|
//  J     \         /._           /        -tI/ |
//   L     )       /   /'-------'J           `'-:.
//   J   .'      ,'  ,' ,     \   `'-.__          \
//    \ T      ,'  ,'   )\    /|        ';'---7   /
//     \|    ,'L  Y...-' / _.' /         \   /   /
//      J   Y  |  J    .'-'   /         ,--.(   /
//       L  |  J   L -'     .'         /  |    /\
//       |  J.  L  J     .-;.-/       |    \ .' /
//       J   L`-J   L____,.-'`        |  _.-'   |
//        L  J   L  J                  ``  J    |
//        J   L  |   L                     J    |
//         L  J  L    \                    L    \
//         |   L  ) _.'\                    ) _.'\
//         L    \('`    \                  ('`    \
//          ) _.'\`-....'                   `-....'
//         ('`    \
//          `-.___/ 

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
        return 1/(1+Math.pow(Math.E, -input));
//        return input > 1 ? 0 : 1;
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
        builder.append("Weights\n");
        for (int layer = 0; layer < weights.length; layer++) {
            builder.append(Integer.toString(layer));
            builder.append("\n");
            for (int node = 0; node < weights[layer].length; node++) {
                builder.append(" ------").append(nodes[layer][node]);
                builder.append("\n");
                for (int weight = 0; weight < weights[layer][node].length; weight++) {
                    builder.append("\t------").append(Double.toString(weights[layer][node][weight]));
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
    //My notes    
    // [layer][nodeNumber] ~ noode
    //[layer][nodeNumber][weightNumber] ~ weight

    @Override//http://neuralnetworksanddeeplearning.com/chap2.html
    public double[] learn(double[] input, double[] expectedOutput) {
        if (expectedOutput.length != nodes[nodes.length - 1].length) {
            throw new IllegalArgumentException("The amount of expected results does not reflect the amount of output nodes");
        }
        double[] results = activate(input);
        System.out.println("-----_----------_----------_----------_-----");
        System.out.println("Input: " + Arrays.toString(input));
        System.out.println("OutPut: " + Arrays.toString(results));
        System.out.println("Expected: " + Arrays.toString(expectedOutput));

        double[] errFromOutput = new double[results.length];
        double cost;
        double sum = 0;

        System.out.println("\nError on ouput");
        //Calculates the intial error
        for (int i = 0; i < errFromOutput.length; i++) {
            double DyDivDz = nodes[nodes.length - 1][i].getCurrentValue() * (1 - nodes[nodes.length - 1][i].getCurrentValue());
            double DeDivDy = -(expectedOutput[i] - nodes[nodes.length - 1][i].getCurrentValue());
            errFromOutput[i] = -expectedOutput[i] - results[i];
            nodes[nodes.length - 1][i].setDelta(errFromOutput[i]);
            System.out.println("Node " + Integer.toString(i) + " - " + nodes[nodes.length - 1][i].getDelta());
            sum += errFromOutput[i];
        }

        cost = Math.pow(sum, 2);
        cost *= 0.5d;
        System.out.println("\nHidden layer error");
        //Calculate the error of the hidden layers
        for (int layer = nodes.length - 2; layer > 0; layer--) {
            for (int nodeNumberCurrent = 0; nodeNumberCurrent < nodes[layer].length; nodeNumberCurrent++) {
                sum = 0;
                for (int nodeNumberOneAhead = 0; nodeNumberOneAhead < nodes[layer + 1].length; nodeNumberOneAhead++) {
                    sum += nodes[layer + 1][nodeNumberOneAhead].getDelta() * weights[layer][nodeNumberCurrent][nodeNumberOneAhead];
                }
                
                double DyDivDz = nodes[layer][nodeNumberCurrent].getCurrentValue() * 
                        (1 - nodes[layer][nodeNumberCurrent].getCurrentValue());
                nodes[layer][nodeNumberCurrent].setDelta(sum * DyDivDz);
                System.out.println("sum: " + sum);
                System.out.println("DyDivDz: " + DyDivDz);
                System.out.println("Layer: " + layer);
                System.out.println("Node: " + nodeNumberCurrent);
                System.out.println("Node error: " + nodes[layer][nodeNumberCurrent].getDelta());
            }
        }
        //update weights of the outputlayer

        for (int layer = nodes.length - 1; layer > 0; layer--) {
            for (int nodeNumber = 0; nodeNumber < nodes[layer].length; nodeNumber++) {
                for (int weightNumber = 0; weightNumber < weights[layer - 1][nodeNumber].length; weightNumber++) {
                    System.out.println("-------------------------------------------");
                    System.out.println("Layer: " + layer);
                    System.out.println("Node: " + nodeNumber);
                    System.out.println("Weight: " + weightNumber);
                    System.out.println("Node err: " + nodes[layer - 1][nodeNumber].getDelta());
                    double errOnWeight = nodes[layer - 1][nodeNumber].getDelta() * nodes[layer][nodeNumber].getCurrentValue();
                    System.out.println("err: " + errOnWeight);
                    weights[layer - 1][nodeNumber][weightNumber] -= learningRate * errOnWeight;

                }
            }
        }

        return null;
    }

}
