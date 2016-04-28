package ui.frames;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * A frame show a simple line chart. Dont ask me how this works, did this like 8
 * months ago and I barely understood it then. Stupid JFX
 */
public class LineCharts extends Application {

    private static ArrayList<SimpleEntry<Double[][], String>> data = new ArrayList<>();

    public static void addData(Double[][] data, String name) {
        LineCharts.data.add(new SimpleEntry<>(data, name));
    }

    public static void removeData(String name) {
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            SimpleEntry<Double[][], String> get = data.get(i);
            index = get.getValue().equals(name) ? i : index;
        }
        if (index != -1) {
            data.remove(index);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        //creating the chart
        final ScatterChart<Number, Number> lineChart = new ScatterChart<>(xAxis, yAxis);

        lineChart.setTitle("Input Data");

        for (SimpleEntry<Double[][], String> data1 : data) {
            XYChart.Series series = new XYChart.Series();
            series.setName(data1.getValue());
            for (int i = 0; i < data1.getKey().length; i++) {
                double x = data1.getKey()[i][0];
                double y = data1.getKey()[i][1];
                series.getData().add(new XYChart.Data(x, y));
            }
            lineChart.getData().add(series);
        }

        //defining a series
        Scene scene = new Scene(lineChart, 800, 600);
//        lineChart.setCreateSymbols(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void start() {
        new LineCharts();
    }
}
