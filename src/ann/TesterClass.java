package ann;

/**
 * A class to test out the ANN (artifical neural network). To be later plugged
 * into the real simulation
 *
 * @author Kareem
 */
public class TesterClass {

    public static void main(String[] args) {
        NeuralNetwork test = NNfactory.createMultilayerPerceptron(2, new int[]{6, 3}, 1);
        test.printWeights();
        System.out.println(test.size());
    }
}
