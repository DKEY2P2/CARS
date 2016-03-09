package vehicle;

import algorithms.Algorithm;
import controller.SimulationSettings;
import helper.StupidHelper;
import java.util.ArrayList;
import java.util.Random;
import map.Intersection;
import map.Road;
import models.Model;

/**
 * A factory for making cars
 *
 * @author Kareem Horstink
 */
public class VehicleFactory {

    /**
     *
     */
    public static final String SPORT_CAR = "Sport";

    /**
     * The private instance of the factory
     */
    private static VehicleFactory instance;

    /**
     * Made to be private so no one could just randomly make it
     */
    private VehicleFactory() {

    }
    /**
     * For the random factor factory
     */
    private static Random r = new Random();

    /**
     * Returns the factory
     *
     * @return The factory
     */
    public static VehicleFactory getFactory() {
        if (instance == null) {
            instance = new VehicleFactory();
        }
        return instance;
    }

    /**
     * Creates a car with the type specified, model and algorithm
     *
     * @param type The type of car
     * @param m The model of the car
     * @param al The algorithm wanted for path finding
     * @return The car
     */
    public Vehicle createVehicle(String type, Model m, Algorithm al) {
        ArrayList<Road> b = controller.Controller.getInstance().getMap().getRoads();
        ArrayList<Intersection> a = controller.Controller.getInstance().getMap().getIntersections();
        switch (type) {
            case "Sport":
                return new SportCar((Road) StupidHelper.getRandom(b),
                        r.nextDouble(), m, al,
                        (Intersection) StupidHelper.getRandom(a));
        }
        return null;
    }

    public Vehicle createVehicle(String type, Model m) {
        return createVehicle(type, m, SimulationSettings.getInstance().getPathFindingAI());
    }

    public Vehicle createVehicle(String type, Algorithm al) {
        return createVehicle(type, SimulationSettings.getInstance().getModel(), al);
    }

    public Vehicle createVehicle(String type) {
        return createVehicle(type, SimulationSettings.getInstance().getModel(), SimulationSettings.getInstance().getPathFindingAI());
    }
}
