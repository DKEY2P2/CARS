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
        NeuralNetwork test = NNfactory.createMultilayerPerceptron(2, new int[]{20,20,200,20,20}, 1);
//        test.printWeights();
        System.out.println("size: " + test.size());
        System.out.println("Before");
        System.out.println("1,1 " + Arrays.toString(test.activate(new double[]{1, 1})));
        System.out.println("1,0 " + Arrays.toString(test.activate(new double[]{1, 0})));
        System.out.println("0,1 " + Arrays.toString(test.activate(new double[]{0, 1})));
        System.out.println("0,0 " + Arrays.toString(test.activate(new double[]{0, 0})));
        for (int i = 0; i < 10000; i++) {
            test.learn(new double[]{1, 1}, new double[]{1});
            test.learn(new double[]{0, 1}, new double[]{1});
            test.learn(new double[]{1, 0}, new double[]{1});
            test.learn(new double[]{0, 0}, new double[]{0});
        }
        System.out.println("After");
        System.out.println("1,1 " + Arrays.toString(test.activate(new double[]{1, 1})));
        System.out.println("1,0 " + Arrays.toString(test.activate(new double[]{1, 0})));
        System.out.println("0,1 " + Arrays.toString(test.activate(new double[]{0, 1})));
        System.out.println("0,0 " + Arrays.toString(test.activate(new double[]{0, 0})));

//test.printWeights();
    }
}
