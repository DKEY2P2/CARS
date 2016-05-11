package vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import algorithms.Algorithm;
import controller.SimulationSettings;
import helper.StupidHelper;
import map.Intersection;
import map.Road;
import models.AutonomousCar;
import models.Model;
import models.OVM;

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
    public static final String SEDAN = "Sedan";
    public static final String MINIBUS = "Minibus";
    public static final String GOOGLECAR = "GoogleCar";

    /**
     * The private instance of the factory
     */
    private static VehicleFactory instance;

    /**
     * Made to be private so no one could just randomly make it
     */
    private VehicleFactory() {
        cars.put(SEDAN, 0);
        cars.put(SPORT_CAR, 1);
        cars.put(MINIBUS, 2);
        cars.put(GOOGLECAR, 3);
        reverse.add(SEDAN);
        reverse.add(SPORT_CAR);
        reverse.add(MINIBUS);
        reverse.add(GOOGLECAR);

        probablity.add(0.25);
        probablity.add(0.25);
        probablity.add(0.25);
        probablity.add(0.25);

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
            case "Sedan":
                return new Sedan(r, percentage, m, al, goal);
            case "Minibus":
                return new Minibus(r, percentage, m, al, goal);
            case "GoogleCar":
                return new GoogleCar(r, percentage, m, al, goal);
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
        Intersection i = (Intersection) StupidHelper.getRandom(a);
        while(r.getStart() == i){
            i = (Intersection) StupidHelper.getRandom(a);
        }
        return createVehicle(type, m, al, r, percentage, i);
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
        Road r = (Road) StupidHelper.getRandom(b);
        Intersection i = (Intersection) StupidHelper.getRandom(a);
        while(r.getStart() == i){
            i = (Intersection) StupidHelper.getRandom(a);
        }
        return createVehicle(type, m, al, (Road) StupidHelper.getRandom(b), 0d, i);
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
        return createVehicle(getType());
    }

    private String getType() {
        Random r = new Random();
        int next = r.nextInt(4);
        return reverse.get(next);
    }

    /**
     * Set the probability that car will spawn
     *
     * @param p The probability
     * @param car The which to make more probable
     */
    public void setProbablity(double p, String car) {
        if (cars.containsKey(car)) {
            probablity.set(cars.get(car), p);
        }
    }

    private HashMap<String, Integer> cars = new HashMap<>();
    private ArrayList<String> reverse = new ArrayList<>();
    private ArrayList<Double> probablity = new ArrayList<>();
}
