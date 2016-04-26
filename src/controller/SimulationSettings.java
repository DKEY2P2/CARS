package controller;

import algorithms.AStar;
import algorithms.Algorithm;
import models.IntelligentDriver;
import models.Model;

/**
 * A class that keeps all the settings for the simulation
 *
 * @author Kareem Horstink
 */
public class SimulationSettings {

    public static SimulationSettings instance;

    public static int timeUntilSpawn = 150;

    public static Model model = new IntelligentDriver();

    public static Algorithm pathFindingAI = new AStar();

    public static int numberOfCarsToSpawn = 10;

    public static double speedLimit = 27.666666666;

    public static double intersectionSize = 12.4; //in meters

}
