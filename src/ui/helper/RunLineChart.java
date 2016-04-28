package ui.helper;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import ui.frames.LineCharts;

/**
 * A helper to run the line chart frame
 *
 * @author Kareem Horstink
 */
public class RunLineChart {

    private static boolean flag = false;

    /**
     * Adds data to the line chart
     *
     * @param data The data eg {{X1,Y1},{X2,Y2},{X3,Y3},...,{XN,YN}}
     * @param name The name of the series
     */
    public static void addData(Double[][] data, String name) {
        flag = true;
        LineCharts.addData(data, name);
    }

    /**
     * Adds the data to the line chart
     *
     * @param data The data wanted. All the doubles will be placed in order
     * @param name The name of the series
     */
    public static void addData(ArrayList<Double> data, String name) {
        ArrayList<Double[]> tmp = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Double get = data.get(i);
            tmp.add(new Double[]{(double) i, get});
        }
        addData(tmp.toArray(new Double[1][1]), name);
    }

    /**
     * Adds the data to the line chart
     *
     * @param data The data wanted. All the doubles will be placed in order
     * @param name The name of the series
     */
    public static void addDataI(ArrayList<Integer> data, String name) {
        ArrayList<Double[]> tmp = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Integer get = data.get(i);
            tmp.add(new Double[]{(double) i, (double) get});
        }
        addData(tmp.toArray(new Double[1][1]), name);
    }

    /**
     * Runs the the lineChart
     */
    public static void run() {
        if (!flag) {
            System.err.println("Warning: No data added");
        }
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//            }
//        });
        new JFXPanel(); // initializes JavaFX environment

        LineCharts.launch(LineCharts.class, "");

    }
}
