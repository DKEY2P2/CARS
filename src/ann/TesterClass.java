package ann;

import java.util.Arrays;

/**
 * A class to test out the ANN (artifical neural network). To be later plugged
 * into the real simulation
 *
 * @author Kareem
 */
public class TesterClass {

    public static void main(String[] args) {
        NeuralNetwork test = NNfactory.createMultilayerPerceptron(1, new int[]{3}, 1);
        test.printWeights();
        System.out.println(test.size());
        System.out.println(Arrays.toString(test.activate(new double[]{2})));

        
    }
}
