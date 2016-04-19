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
        NeuralNetwork test = NNfactory.createMultilayerPerceptron(2, new int[]{2}, 1);
        test.printWeights();
        test.learn(new double[]{1, 1}, new double[]{0});
        test.learn(new double[]{0, 1}, new double[]{1});
        test.learn(new double[]{1, 0}, new double[]{1});
        test.learn(new double[]{0, 0}, new double[]{0});

        System.out.println("1,1 " + Arrays.toString(test.activate(new double[]{1, 1})));
        System.out.println("1,0 " + Arrays.toString(test.activate(new double[]{1, 0})));
        System.out.println("0,1 " + Arrays.toString(test.activate(new double[]{0, 1})));
        System.out.println("0,0 " + Arrays.toString(test.activate(new double[]{0, 0})));

        test.printWeights();

    }
}
