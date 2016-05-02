/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.threads;

import controller.SimulationSettings;
import controller.Task;
import vehicle.VehicleFactory;

/**
 *
 * @author Kareem
 */
public class SpawnTask implements Task{

    @Override
    public boolean update() {
        for (int i = 0; i < SimulationSettings.getInstance().getNumberOfCarsToSpawn(); i++) {
            VehicleFactory.getFactory().createVehicle();
        }
        return true;
    }

    
}
