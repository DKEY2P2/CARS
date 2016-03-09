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
     * A string to say it should spawn a sport car
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
     * Creates a car of specific type (eg sport car) and with a specific start,
     * model, path finding agent and goal
     *
     * @param type The type of car wanted
     * @param m The model to be used by the car
     * @param al The path finding agent
     * @param r The road that it starts on
     * @param percentage The percentage of the road that the vehicle has
     * completed already
     * @param goal The goal you want to go to
     * @return
     */
    public Vehicle createVehicle(String type, Model m, Algorithm al, Road r, double percentage, Intersection goal) {
        switch (type) {
            case "Sport":
                return new SportCar(r, percentage, m, al, goal);
        }
        return null;
    }

    /**
     * Creates a car of the specific type (eg sport car) and with a specific
     * start, model and path finding agent with a random goal
     *
     * @param type The type of car
     * @param m The model of the car
     * @param al The algorithm wanted for path finding
     * @param r The road that it starts on
     * @param percentage The percentage of the road that the vehicle has
     * completed already
     * @return The new car
     */
    public Vehicle createVehicle(String type, Model m, Algorithm al, Road r, double percentage) {
        ArrayList<Intersection> a = controller.Controller.getInstance().getMap().getIntersections();
        return createVehicle(type, m, al, r, percentage, (Intersection) StupidHelper.getRandom(a));
    }

    /**
     * Creates a car of the specific type (eg sport car) and with a specific
     * model and path finding agent with a random start and goal
     *
     * @param type The type of car
     * @param m The model of the car
     * @param al The algorithm wanted for path finding
     * @return The new car
     */
    public Vehicle createVehicle(String type, Model m, Algorithm al) {
        ArrayList<Road> b = controller.Controller.getInstance().getMap().getRoads();
        ArrayList<Intersection> a = controller.Controller.getInstance().getMap().getIntersections();
        return createVehicle(type, m, al, (Road) StupidHelper.getRandom(b), r.nextDouble(), (Intersection) StupidHelper.getRandom(a));
    }

    /**
     * Creates a car of the specific type (eg sport car) and with a specific
     * model with a random start and goal while using the default path finding
     * AI as set in SettingSimulation
     *
     * @param type The type of car
     * @param m The model wanted
     * @return The new car
     */
    public Vehicle createVehicle(String type, Model m) {
        return createVehicle(type, m, SimulationSettings.getInstance().getPathFindingAI());
    }

    /**
     * Creates a car of the specific type (eg sport car) and with a specific
     * path finding agent with a random start and goal while using the default
     * model and path finding AI as set in SettingSimulation
     *
     * @param type The type of car
     * @param al The path finding agent
     * @return The new car
     */
    public Vehicle createVehicle(String type, Algorithm al) {
        return createVehicle(type, SimulationSettings.getInstance().getModel(), al);
    }

    /**
     * Creates a car of the specific type (eg sport car) with a random start and
     * goal while using the default model and path finding AI as set in
     * SettingSimulation
     *
     * @param type The type of car
     * @return The new car
     */
    public Vehicle createVehicle(String type) {
        return createVehicle(type, SimulationSettings.getInstance().getModel(), SimulationSettings.getInstance().getPathFindingAI());
    }

    /**
     * Currently just gives you a sport car with default settings
     *
     * @return The new car
     */
    public Vehicle createVehicle() {
        return createVehicle(SPORT_CAR);
    }
}
