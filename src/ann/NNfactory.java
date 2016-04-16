package ann;

/**
 * The factory that creates the neural network
 *
 * @author Kareem Horstink
 */
public class NNfactory {
    
    public static NeuralNetwork createMultilayerPerceptron(int inputLayer, int[] hiddenLayer, int outputLayer) {
        return new MultilayerPerceptron(inputLayer, hiddenLayer, outputLayer);
    }
}
