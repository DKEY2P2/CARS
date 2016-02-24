package models;

import controller.Controller;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import map.Road;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class Forbe implements Model {
    
    @Override
    public void calculate(Vehicle veh) {
        Road r = veh.getPosition().getKey();
        Vehicle inFrontVehicle = null;
        int i = 0;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == veh) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        double[] percentage;
        if (inFrontVehicle != null) {
            percentage = new double[]{veh.getPosition().getValue(), inFrontVehicle.getPosition().getValue()};
        } else {
            percentage = new double[]{veh.getPosition().getValue(), 0};
        }
        if (inFrontVehicle != null) {
            double roadLength = r.getLength();
            double[] x = new double[]{roadLength * percentage[0], roadLength * percentage[1]};
            double[] l = new double[]{veh.getLength(), inFrontVehicle.getLength()};
            double[] v = new double[]{veh.getSpeed(), inFrontVehicle.getSpeed()};
            double[] reactionTime = new double[]{veh.getReactionTime(), inFrontVehicle.getReactionTime()};
            double distance = x[1] - x[0];
            double s = distance;
            double sMin = reactionTime[0] * v[0] + l[0];
            if (s > sMin) {
                v[0] = Math.max(0, v[0] - veh.getMaxDecceleration());
            } else {
                v[0] = Math.min(v[0], Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()) + veh.getAcceleration());
            }
            double newPercentage = roadLength / (x[0] + v[0] * Controller.getInstance().getTicker().getTickTimeInS());
            veh.setSpeed(v[0]);
            veh.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
        }
    }
    
}
