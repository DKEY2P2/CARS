package algorithms.trafficlights.ann;

/**
 * The factory that creates the neural network
 *
 * @author Kareem Horstink
 */
public class NNfactory {

    /**
     * Creates Multilayer Perceptron
     *
     * @param inputLayer The amount of input nodes wanted
     * @param hiddenLayer The amount of nodes wanted for each hidden layer
     * @param outputLayer The amount of output nodes
     * @return The the NN
     */
    public static NeuralNetwork createMultilayerPerceptron(int inputLayer, int[] hiddenLayer, int outputLayer) {
        return new MultilayerPerceptron(inputLayer, hiddenLayer, outputLayer);
    }
    
    public static NeuralNetwork createPerceptron(int inputNode,int outputNode, int iterationThreshold){
        return new Perceptron(inputNode, outputNode,iterationThreshold );
    }
}
