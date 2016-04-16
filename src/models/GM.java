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
	private double velocity;
	private double timeStep;
	/*
	 * Driving habit will not change if the follow distance is farther that maxFollow in meters. 
	 */
// Source: https://www.civil.iitb.ac.in/tvm/1111_nptel/533_CarFol/plain/plain.html#x1-100003.1
	
	public GM() {
		
		a=13;
		m=0;
		l=1;
		
	}

	private double velocity(Vehicle car){
		return car.getSpeed()+car.getAcceleration()*timeStep;
		
	}
	private double position(Vehicle car) {
		return car.getPosition().getValue()*car.getPosition().getKey().getLength()
				+(car.getSpeed()*timeStep)
					+(.5*car.getAcceleration())*Math.pow(timeStep, 2); 
	}
	private double acceleration(Vehicle lead, Vehicle follow) {
		return (velocity(lead)-velocity(follow))
					*(Math.pow(a*follow.getSpeed(),m))
						/Math.pow((position(lead)-position(follow)),l);
	}
	public void calculate(Vehicle v) {
		
		double reactionTime= v.getReactionTime();
		double time = Controller.getInstance().getTicker().getTickTimeInS();
		Road r =v.getPosition().getKey();
		TrafficLight trl = r.getTrafficlight();
		Vehicle inFrontVehicle = v.getPredecessor();
		velocity = v.getSpeed();
		position = v.getPosition().getValue()*r.getLength();
		acceleration = v.getAcceleration();
		
		
		//a*(speed of following car)*(speedOfLeadCar-speedOfFollowCar)/(positionOfLead-positionOfFollow)
		if(inFrontVehicle != null && (position(inFrontVehicle)- position(v))<= 50) {
			timeStep = Controller.getInstance().getTicker().getTickTimeInS()-time;
			acceleration= acceleration(inFrontVehicle,v);
			
			double newPosition = position(v);
			v.setAcceleration(acceleration);
			v.setPosition(new SimpleImmutableEntry<>(r,newPosition));
			
		}else{
			timeStep = Controller.getInstance().getTicker().getTickTimeInS()-time;
			double speed= v.getSpeed();
			double position = v.getPosition().getValue()*r.getLength();
			
			if(!trl.isGreen() && position-r.getLength() == v.getBreakingDistance() ) {
				acceleration = v.getMaxDecceleration();
				double newPosition = position(v);
				speed = velocity(v);
				v.setSpeed(speed);
				v.setPosition(new SimpleImmutableEntry<>(r,newPosition));
			
			} else {
					speed = speed+v.getMaxAcceleration()*Controller.getInstance().getTicker().getTickTimeInS();
					speed= Math.min(speed, r.getSpeedLimit());
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
