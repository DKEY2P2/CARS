package algorithms;

import helper.Logger;
import helper.StupidHelper;
import java.util.ArrayList;
import map.Intersection;
import map.Road;

/**
 * A test path finding AI that just picks a random road
 *
 * @author Kareem Horstink
 * @since 07-03-16
 */
public class TestAl implements Algorithm {

    @Override
    public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {
        map.Map m = controller.Controller.getInstance().getMap();
        ArrayList<Intersection> tmp = new ArrayList<>();

        ArrayList<Road> r = start.getRoads();
        for (Road r1 : r) {
            if(r1)
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
