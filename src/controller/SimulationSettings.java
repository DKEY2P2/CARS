package controller;

import algorithms.Algorithm;
import models.Model;

/**
 * A class that keeps all the settings for the simulation
 *
 * @author Kareem Horstink
 */
public class SimulationSettings {

    private static SimulationSettings instance;

    private SimulationSettings() {

    }

    private Model model;

    /**
     * Get the value of model
     *
     * @return the value of model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Set the value of model
     *
     * @param model new value of model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    private Algorithm pathFindingAI;

    /**
     * Get the value of pathFindingAI
     *
     * @return the value of pathFindingAI
     */
    public Algorithm getPathFindingAI() {
        return pathFindingAI;
    }

    /**
     * Set the value of pathFindingAI
     *
     * @param pathFindingAI new value of pathFindingAI
     */
    public void setPathFindingAI(Algorithm pathFindingAI) {
        this.pathFindingAI = pathFindingAI;
    }

    public static SimulationSettings getInstance() {
        if (instance == null) {
            instance = new SimulationSettings();
        }
        return instance;
    }

}
