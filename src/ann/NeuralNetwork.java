package ann;

/**
 *
 * @author Imray
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
     *
     * @param input
     * @return
     * @throws IllegalArgumentException
     */
    public double[] activate(double[] input);

}
