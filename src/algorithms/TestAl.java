/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import helper.Logger;
import helper.StupidHelper;
import java.util.ArrayList;
import map.Intersection;
import map.Road;

/**
 *
 * @author Imray
 */
public class TestAl implements Algorithm {

    @Override
    public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {
        map.Map m = controller.Controller.getInstance().getMap();
        ArrayList<Intersection> tmp = new ArrayList<>();
        loop:
        for (Road road : m.getRoads()) {
            if (road.getStart() == start) {
                ArrayList<Road> a = road.getEnd().getRoads();
                for (Road a1 : a) {
                    if (a1 == road || a1.getStart() == start) {
                        continue;
                    }
                    tmp.add(a1.getStart());
                    tmp.add(a1.getStart());
                    tmp.add(a1.getStart());
                    tmp.add(a1.getStart());
                    tmp.add(a1.getStart());
                }
            }
        }
        if (tmp.isEmpty()) {
            Logger.LogError("Next is empty", this);
        }
        
        Intersection a = (Intersection) StupidHelper.getRandom(tmp);
        tmp = new ArrayList<>();
        tmp.add(a);
        return tmp;

    }

}
