package models;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

public class GM implements Model {
	/**
	 * 
	 * Author Gianni
	 */
	
	//calibration parameters
	private double calibrationA,calibrationM,calibrationL;
	/*
	 * Driving habit will not change if the follow distance is farther that maxFollow in meters. 
	 */
	private double maxFollow;
	
	public GM() {
		calibrationA=1;
		calibrationM=1;
		calibrationL=1;
		
		maxFollow = 20;
		
	}

	
    private Vehicle getInFront(Vehicle v, Road r) {
        Vehicle inFrontVehicle = null;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == v) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        return inFrontVehicle;
    }

	
	public void calculate(Vehicle v) {
		
		TrafficLight tL = v.getNextLight();
		Road r = v.getPosition().getKey();
		double roadLength = r.getLength();
		double t= Controller.getInstance().getTicker().getTickTimeInS();
		double speed = v.getSpeed();
		
		//at traffic light
		if(v.getPosition().getKey()==null) {
			return;
		}
		
		if(v.getPosition().getKey().getEnd() ==v.getDestination()&& v.getPosition().getValue()>1) {
			r.getVehicles().remove(v);
			v=null;
		}
		
		if(v.getPosition().getValue()>1) {
			return;
		}
		
		Vehicle inFrontVehicle = getInFront(v,r);
		//double s = Math.abs(followDistance(v,inFrontVehicle));
		
		if(inFrontVehicle != null) {
				
			if(distance(v.getPosition().getValue(),inFrontVehicle.getPosition().getValue()) < v.getBreakingDistance()*2) {
						
				v.setAcceleration(calibrationA*(Math.pow(speed,calibrationM)/
					(Math.pow(distance(v.getPosition().getValue(),inFrontVehicle.getPosition().getValue()),calibrationL)))
							*(inFrontVehicle.getSpeed()-speed));
					
					
					//Vehicle will only accelerate if it is not at the speed limit, and will only accelerate to the speed limit.
					speed= Math.min(r.getSpeedLimit(), speed + v.getAcceleration()*t);
				
			
			} 
			double newPercentage = (v.getPosition().getValue()*roadLength+ speed*t/roadLength);
			v.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
		} else {
			
			double s =v.getPosition().getValue()*roadLength;
			if(!tL.isGreen() && distance(v.getPosition().getValue(),roadLength) == v.getBreakingDistance()) {
				speed = Math.max(0,speed-v.getMaxDecceleration()*t);
			} else {
				 speed = Math.min(
	                        Math.min(v.getDesiredSpeed(), r.getSpeedLimit()),
	                        speed + v.getMaxAcceleration() * t);
			}
            v.setDistance(v.getDistance() + speed * t);
            double newPercentage = (s + speed * t);
            v.setSpeed(speed);
            v.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
			
		}
	}

	
	
	
	public void setCalibration(double a,double m,double l) {
		this.calibrationA=a;
		this.calibrationM=m;
		this.calibrationL=l;
	}
	
	private double distance(double follow, double lead) {
		
		return lead - follow;
	}

	public double getCalibrationA() {
		return calibrationA;
	}
	
	public double getCalibrationM() {
		return calibrationM;
	}
	
	public double getCalibrationL() {
		return calibrationL;
	}
	
	
}
