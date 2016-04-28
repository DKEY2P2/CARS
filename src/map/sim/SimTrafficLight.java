/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.sim;

import algorithms.trafficlights.SmartTrafficLight;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.intersection.detectors.Detector;

/**
 *
 * @author Kareem Horstink
 */
public class SimTrafficLight extends SmartTrafficLight{

    public SimTrafficLight(Detector detector) {
        super(null,null,null, detector);
    }

    @Override
    public void addOut(Road r) {
    }

    @Override
    public Road getIn() {
        return null;
    }
    
    
}
