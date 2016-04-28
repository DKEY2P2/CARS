package algorithms.trafficlights.ann;

/**
 * The interface that every type of neural network should follow
 *
 * @author Kareem Horstink
 */
public interface NeuralNetwork {

    /**
     * Prints all the weights of the edges
     */
    public void printWeights();

    //Not sure if it is necassary ~ can't spell that word
//    public void printNode();
    /**
     * Get the size of the neural network
     *
     * @return Gets the number of nodes
     */
    public int size();

    /**
     * See what the results is after putting the input based on the current
     * configuration of the neural network
     *
     * @param input The input that the neural network needs
     * @return The results of activation (every output node)
     * @throws IllegalArgumentException If there isn't enough input based on the
     * number of input nodes
     */
    public double[] activate(double[] input);

    /**
     * Start the learning process
     *
     * @param input The input that the neural network needs
     * @param expectedOutput What we expect the results to be
     * @return The error on every output node
     * @throws IllegalArgumentException If there isn't enough input based on the
     * number of input nodes
     */
    public double[] learn(double[] input, double[] expectedOutput);

}
