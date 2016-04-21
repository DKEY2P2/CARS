package controller;

import java.util.ArrayList;
import map.Map;
import statistics.Stats;
import ui.ControllerUI;
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

    private ControllerUI UI;

    /**
     * Creates an empty map
     */
    private Map map = new Map(new ArrayList<>(), new ArrayList<>());

    /**
     * Holds a reference to the holder of the vehicles
     */
    private VehicleHolder vehicles = VehicleHolder.getInstance();
    /**
     * The ticker that contains whether or not a tick should happen
     */
    private Ticker ticker;

    /**
     * Made to be private so no one else can use
     */
    private Controller() {
    }

    /**
     * Get the value of UI
     *
     * @return the value of UI
     */
    public ControllerUI getUI() {
        return UI;
    }

    /**
     * Set the value of UI
     *
     * @param UI new value of UI
     */
    public void setUI(ControllerUI UI) {
        this.UI = UI;
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

    /**
     * Get the value of ticker
     *
     * @return the value of ticker
     */
    public Ticker getTicker() {
        return ticker;
    }

    /**
     * Set the value of ticker
     *
     * @param ticker new value of ticker
     */
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    private Stats stats;

    /**
     * Set the value of stats
     *
     * @param stats new value of stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * Gets the stats
     * @return The <b>current</b> stats of the system
     */
    public Stats getStats() {
        return stats.copy();
    }
    
    

}
