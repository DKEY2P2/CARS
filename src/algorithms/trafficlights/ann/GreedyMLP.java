package algorithms.trafficlights.ann;

import algorithms.trafficlights.SmartTrafficLightExtended;
import algorithms.trafficlights.greedy.GreedyVersion2;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import map.TrafficLight;
import map.intersection.detectors.Detector;
import org.neuroph.core.data.DataSet;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;

/**
 * Takes a greedy and makes ANN based on that
 *
 * @author Kareem Horstink
 */
public class GreedyMLP {

    public static void main(String[] args) {
        GreedyMLP gmlp = new GreedyMLP(3);
    }

    public GreedyMLP(int numberOfLights) {
        org.neuroph.core.NeuralNetwork neuralNetwork = NeuralNetworkFactory.createMLPerceptron("6 6 6 6 6 6 3", TransferFunctionType.SIGMOID);
        numberOfLights = 3;
        DataSet data;
        data = DataSet.load("C:\\Users\\Kareem\\Documents\\NetBeansProjects\\data1.csv");
//        data = new DataSet(6, 3);
        if (false) {
            ArrayList<SimulatedPerfectDector> simD = new ArrayList<>();
            for (int i = 0; i < numberOfLights; i++) {
                simD.add(new SimulatedPerfectDector());
            }
            GreedyVersion2 gv = new GreedyVersion2(100, 100, null);
            for (int i = 0; i < numberOfLights; i++) {
                gv.addRoad(null, simD.get(i));
            }

            int pizza = 0;
            boolean flag = false;
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {
                    for (int k = 0; k < 50; k++) {
//                    System.out.println("i: " + i);
//                    System.out.println("j: " + j);
//                    System.out.println("k: " + k);
                        gv.getTrafficLights().forEach(tr -> {
                            tr.setLight(false);
                            ((SmartTrafficLightExtended) tr).setCurrentGreenTime(0);
                        });

                        simD.get(0).setWhatToOutput(i);
                        simD.get(1).setWhatToOutput(j);
                        simD.get(2).setWhatToOutput(k);
                        while (simD.stream().filter(sim -> sim.getWhatToOutput() == 0).count() != 3) {
//                        System.out.println("counter " + pizza++);
                            double[] input = new double[6];
                            int index = 0;
                            for (Iterator<TrafficLight> it = gv.getTrafficLights().iterator(); it.hasNext();) {
                                SmartTrafficLightExtended trafficLight = (SmartTrafficLightExtended) it.next();
                                input[index] = trafficLight.getCurrentGreenTime();
                                input[index + 3] = trafficLight.getDetector().getNumberOfCarsDetected();
                                index++;
                            }
                            gv.update();
                            double[] output = new double[3];
                            index = 0;
                            for (TrafficLight trafficLight : gv.getTrafficLights()) {
                                output[index] = trafficLight.isGreen() ? 1 : 0;
                            }
                            data.addRow(input, output);
                            if (pizza++ % 1000 == 0) {
                                System.out.println("Saving: " + pizza / 1000);
                                flag= !flag;
                                if (flag) {
                                    data.save("C:\\Users\\Kareem\\Documents\\NetBeansProjects\\data1.csv");
                                }else{
                                    data.save("C:\\Users\\Kareem\\Documents\\NetBeansProjects\\data2.csv");
                                }
                                System.out.println("Done");
                            }
                        }
                    }
                }
            }
        }
        data.shuffle();
        System.out.println(data.size());
        System.out.println("Going to start learning");
        neuralNetwork.learn(data);
        neuralNetwork.save("C:\\Users\\Kareem\\Documents\\NetBeansProjects\\ANN3.csv");
    }

    public static class SimulatedPerfectDector implements Detector {

        private int whatToOutput;

        /**
         * Get the value of whatToOutput
         *
         * @return the value of whatToOutput
         */
        public int getWhatToOutput() {
            return whatToOutput;
        }

        /**
         * Set the value of whatToOutput
         *
         * @param whatToOutput new value of whatToOutput
         */
        public void setWhatToOutput(int whatToOutput) {
            this.whatToOutput = whatToOutput < 0 ? 1 : whatToOutput;
        }

        @Override
        public int getNumberOfCarsDetected() {
            return whatToOutput;
        }

        @Override
        public double getProbablityOfBeingCorrect() {
            return 1;
        }

        @Override
        public int getMaximumAmountOfCarThatCanBeDetected() {
            return 1000000;
        }

        @Override
        public Detector clone() {
            return this;
        }

        @Override
        public Detector clone(TrafficLight t) {
            return this;
        }

    }
}
