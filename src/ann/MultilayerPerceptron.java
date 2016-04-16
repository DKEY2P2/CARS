package ann;

import helper.Logger;
import java.util.ArrayList;

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
        
        for (int i = 0; i < nodes.length-1; i++) {
            Node[] nodeLayer = nodes[i];
            for (int j = 0; j < nodeLayer.length; j++) {
                Node superCurrent = nodeLayer[j];
                weightLayer.add(new double[nodes[i+1].length]);
            }
            tmp.add(weightLayer.toArray(new double[1][1]));
            weightLayer.clear();
        }
        weights = tmp.toArray(new double[1][1][1]);
    }

    /**
     * The activation function. See {@link #MultilayerPerceptron()}
     *
     * @param input The value to be evaluated
     * @return A value between -1 and 1 based on the input
     */
    private double activationFunction(double input) {
        return Math.tanh(input);
    }

    @Override
    public void printWeights() {
        Logger.LogError("Not supported yet.", this);
    }

    @Override
    public int size() {
        Logger.LogError("Not supported yet.", this);
        return 0;
    }

}
