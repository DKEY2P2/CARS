package ann;

import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;

/**
 * A class to test out the ANN (artifical neural network). To be later plugged
 * into the real simulation
 *
 * @author Kareem
 */
public class TesterClass {

    public static void main(String[] args) {
        org.neuroph.core.NeuralNetwork a = NeuralNetworkFactory.createMLPerceptron("2 3 3 3 3 3 1", TransferFunctionType.SIGMOID);
        System.out.println("fuck");
        a.randomizeWeights();
        System.out.println("fuck");
        a.setLearningRule(new BackPropagation());
        System.out.println("fuck");
        DataSet pop = new DataSet(2);
        System.out.println("fuck");
//        for (int i = 0; i < 1000; i++) {
        
        pop.addRow(new double[]{1, 1}, new double[]{0});
        pop.addRow(new double[]{0, 1}, new double[]{1});
        pop.addRow(new double[]{1, 0}, new double[]{1});
        pop.addRow(new double[]{0, 0}, new double[]{0});
        System.out.println("fuck");
//        }
        a.learn(pop);
        a.stopLearning();
        a.setInput(1, 1);
        System.out.println("FUCKING LEARNING BITCHES");
//        a.setInput(1, 1);
        a.calculate();
        System.out.println(Arrays.toString(a.getOutput()));
    }
}
