package controller;

import java.util.ArrayList;
import map.Map;
import vehicle.VehicleHolder;

/**
 * Controls the rest of the project
 * <p>
 * Currently does no controlling
 *
 * @author Kareem Horstink
 */
public class Controller {

    /**
     * The instance of the Controller
     */
    private static Controller instance;

    /**
     * Get the singleton instance of the controller
     *
     * @return A controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Creates an empty map
     */
    private Map map = new Map(new ArrayList<>(), new ArrayList<>());

    /**
     * Holds a reference to the holder of the vehicles
     */
    private VehicleHolder vehicles = VehicleHolder.getInstance();

    /**
     * Made to be private so no one else can use
     */
    private Controller() {
    }

    /**
     * Get the value of vehicles
     *
     * @return the value of vehicles
     */
    public VehicleHolder getVehicles() {
        return vehicles;
    }

    /**
     * Get the value of map
     *
     * @return the value of map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Set the value of map
     *
     * @param map new value of map
     */
    public void setMap(Map map) {
        this.map = map;
    }

}
