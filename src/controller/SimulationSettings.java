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

    private int timeUntilSpawn = 150;

    /**
     * Get the value of timeUntilSpawn
     *
     * @return the value of timeUntilSpawn
     */
    public int getTimeUntilSpawn() {
        return timeUntilSpawn;
    }

    /**
     * Set the number of ticks wanted until you want to spawn new vehicles
     *
     * @param timeUntilSpawn new value of timeUntilSpawn in ticks (int)
     */
    public void setTimeUntilSpawn(int timeUntilSpawn) {
        this.timeUntilSpawn = timeUntilSpawn;
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