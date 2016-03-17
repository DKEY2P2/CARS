package models;

import controller.Controller;
import map.Road;
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
	public GM(double calibrationA,double calibrationM,double calibrationL, double maxFollow) {
		this.calibrationA= calibrationA;
		this.calibrationM= calibrationM;
		this.calibrationL= calibrationL;
		this.maxFollow= maxFollow;
	}
	
	
	private Vehicle getInFront(Vehicle veh, Road r) {
		
		Vehicle inFrontVehicle = null;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == veh) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        //Get the car ahead of you
        return inFrontVehicle;
    }

	
	public void calculate(Vehicle v) {
		
		
		Road r = v.getPosition().getKey();
		double roadLength = r.getLength();
		double t= Controller.getInstance().getTicker().getTickTimeInS();
		
		//at traffic light
		if(v.getPosition().getKey()==null) {
			return;
			
		}
		
		
		Vehicle inFrontVehicle = getInFront(v,r);
		
		if(followDistance(v,inFrontVehicle) < maxFollow) {
			
			v.setAcceleration(calibrationA*(Math.pow(v.getSpeed(),calibrationM)/
					(Math.pow(followDistance(v,inFrontVehicle),calibrationL)))
						*(inFrontVehicle.getSpeed()-v.getSpeed()));
		
		
			//Vehicle will only accelerate if it is not at the speed limit, and will only accelerate to the speed limit.
			if(v.getAcceleration() > 0 && v.getSpeed() != r.getSpeedLimit()){
				accelerate(v,v.getAcceleration());
				
			
				//Vehicle will only decelerate if it is not stopped.
			} else if(v.getAcceleration() < 0 && v.getSpeed() != 0) {
				accelerate(v,v.getAcceleration());
			}
		}
	}

/*
 * basic acceleration method. Definitely could be improved to be more realistic
 */
private void accelerate(Vehicle v, double accelerate) {
	if((v.getSpeed()+ accelerate) < v.getPosition().getKey().getSpeedLimit() ) {
		v.setSpeed(v.getSpeed()+accelerate);
	
		v.setAcceleration(0);
	} else {
		v.setSpeed(v.getPosition().getKey().getSpeedLimit());
	}
}
	
	
	
	public void setCalibration(double a,double m,double l) {
		this.calibrationA=a;
		this.calibrationM=m;
		this.calibrationL=l;
	}
	
	private double followDistance(Vehicle follow, Vehicle lead) {
		
		return lead.getPosition().getValue() - follow.getPosition().getValue();
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
