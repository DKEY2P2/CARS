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
	/*
	 * Driving habit will not change if the follow distance is farther that maxFollow in meters. 
	 */
// Source: https://www.civil.iitb.ac.in/tvm/1111_nptel/533_CarFol/plain/plain.html#x1-100003.1
	
	public GM() {
		
		a=1;
		m=1;
		l=1;
		
	}

	private double acceleration(Vehicle lead, Vehicle follow) {
		return (lead.getSpeed()-follow.getSpeed())
					*(Math.pow(a*follow.getSpeed(),m))
						/Math.pow((lead.getPosition().getValue())-follow.getPosition().getValue(),l);
	}
	public void calculate(Vehicle v) {
		
		Vehicle inFrontVehicle = v.getPredecessor();
		double react = v.getReactionTime();
		Road r =v.getPosition().getKey();
		TrafficLight trl = r.getEnd().
		velocity = v.getSpeed();
		position = v.getPosition().getValue();
		acceleration = v.getAcceleration();
		double minDist= v.getLength();
		
		//a*(speed of following car)*(speedOfLeadCar-speedOfFollowCar)/(positionOfLead-positionOfFollow)
		if(inFrontVehicle != null && (inFrontVehicle.getPosition().getValue()-v.getPosition().getValue()) < minDist ) {
			acceleration= acceleration(inFrontVehicle,v);
			//velocity=velocity+acceleration*Controller.getInstance().getTicker().getTickTimeInS();
			
		}else{

			
			if(!trl.isGreen()) {
				if(position-r.getLength() < minDist) {
					acceleration = -1*v.getMaxDecceleration();
					
				} else {
					acceleration = Math.min(r.getSpeedLimit(),v.getDesiredSpeed());
				}
			} else {
				if(velocity == 0) {
					acceleration = v.getMaxAcceleration();
				}
					acceleration = Math.min(r.getSpeedLimit(),v.getDesiredSpeed());
			}
			//velocity=Math.max(0, Math.min(velocity+acceleration*Controller.getInstance().getTicker().getTickTimeInS() , r.getSpeedLimit()));
		}
		v.updateAll(velocity+acceleration*Controller.getInstance().getTicker().getTickTimeInS(), acceleration, r);
		
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