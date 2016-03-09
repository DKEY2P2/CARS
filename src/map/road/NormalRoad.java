package map.road;

import map.Intersection;
import map.Road;

/**
 *
 * @author Kareem
 */
public class NormalRoad extends Road {

    public NormalRoad(Intersection start, Intersection end) {
        super(start, end, Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2))*100);//Distance is eculidan distance
        setSpeedLimit(27.777777778);//100kmh

    }

    @Override
    public String toString() {
        return "Normal Road " + "Start " + getStart() + " end " + getEnd();
    }
}
