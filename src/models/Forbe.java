package models;

import map.Road;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class Forbe implements Model {

    @Override
    public void calculate(Vehicle v) {
        Road r = v.getPosition().getKey();
        Vehicle inFrontVehicle = r.getVehicles().getFirst();
        int i = 0;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == v) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        
        
        
        
        double[] percentage = new double[]{v.getPosition().getValue(),inFrontVehicle.getPosition().getValue()};
        
        
    }

}
