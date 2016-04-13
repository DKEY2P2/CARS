package models;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

public class GM implements Model {
	/**
	 * 
	 * Author Gianni
	 */
	
	//calibration parameters
	private double a,m,l;
	private double acceleration;
	private double position;
	/*
	 * Driving habit will not change if the follow distance is farther that maxFollow in meters. 
	 */
// || ((inFrontVehicle.getPosition().getValue()-position)<=10)
	
	public GM() {
		a=1;
		m=1;
		l=3;	
	}

	private Vehicle getInFront(Vehicle follower, Road r) {// TODO: Find a better way to do this

		Vehicle inFrontVehicle = follower.getPredecessor();
		if (inFrontVehicle == null
				|| follower.getPosition().getKey().getTrafficlight().getWaiting().contains(inFrontVehicle)
				|| !VehicleHolder.getInstance().contains(inFrontVehicle)) {
			return null;
		} else {
			return inFrontVehicle;
		}

	}
	
	public void calculate(Vehicle v) {
		
		
		Road r =v.getPosition().getKey();
		Vehicle inFrontVehicle = getInFront(v.getPredecessor(),r);
		
		//a*(speed of following car)*(speedOfLeadCar-speedOfFollowCar)/(positionOfLead-positionOfFollow)
		if(inFrontVehicle != null) {
			double speed= v.getSpeed();
			position = v.getPosition().getValue()*r.getLength();
			double acceleration = a*Math.pow(speed,m)*
					(inFrontVehicle.getSpeed()-speed)/
					Math.pow((inFrontVehicle.getPosition().getValue()*r.getLength()-position),l);
			speed=speed+acceleration*Controller.getInstance().getTicker().getTickTimeInS();
			speed= Math.min(speed,r.getSpeedLimit());
			double newPosition = (position + speed * Controller.getInstance().getTicker().getTickTimeInS())
					/ r.getLength();
			v.setAcceleration(acceleration);
			v.setSpeed(speed);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			
		}else{
			double speed= v.getSpeed();
			double position = v.getPosition().getValue()*r.getLength();
			
			if((position-r.getLength())!=5+v.getBreakingDistance()) {
				speed = speed+v.getMaxAcceleration()*Controller.getInstance().getTicker().getTickTimeInS();
				speed= Math.min(speed, r.getSpeedLimit());
				double newPosition = (position + speed * Controller.getInstance().getTicker().getTickTimeInS())
						/ r.getLength();
				v.setAcceleration(acceleration);
				v.setSpeed(speed);
				v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			} else{
				double acceleration = a*Math.pow(speed, a)*((r.getSpeedLimit()/2)-speed)/Math.pow((r.getLength()-position),l);
				 speed= speed+acceleration*Controller.getInstance().getTicker().getTickTimeInS();
				 speed = Math.min(speed,r.getSpeedLimit());
				 double newPosition = (position + speed * Controller.getInstance().getTicker().getTickTimeInS())
					 / r.getLength();
				v.setAcceleration(acceleration);
				v.setSpeed(speed);
				v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			}
		}
		
	}
	
	
	
	public void setCalibration(double a,double m,double l) {
		this.a=a;
		this.m=m;
		this.l=l;
	}
	

	public double getA() {
		return a;
	}
	
	public double getM() {
		return m;
	}
	
	public double getL() {
		return l;
	}
	
	
}
