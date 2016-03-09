package vehicle;

import algorithms.AStar;
import algorithms.Algorithm;
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
     * Creates a car with the type specified
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
}
