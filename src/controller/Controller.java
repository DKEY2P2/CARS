package controller;

import java.util.ArrayList;
import map.Map;
import map.Road;
import vehicle.VehicleHolder;

/**
 * Controls the rest of the project
 *
 * @author Kareem
 */
public class Controller {
    
    private static Controller instance;

    /**
     * Get the singlton instance of the controller
     *
     * @return A controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }
    
    private Map map = new Map(new ArrayList<>(), new ArrayList<>());
    
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
