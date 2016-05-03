package map.road;

import map.Intersection;
import map.Road;
import ui.setting.GraphicsSetting;

/**
 *
 * @author Kareem
 */
public class NormalRoad extends Road {

    public NormalRoad(Intersection start, Intersection end) {
        super(start, end, Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2)) * GraphicsSetting.getInstance().getScale());
        setSpeedLimit(10);//100kmh
        setWidth(3.75);
    }

    public NormalRoad(Intersection start, Intersection end, double speedLimit) {
        super(start, end, Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2)) * GraphicsSetting.getInstance().getScale());
        setSpeedLimit(speedLimit);
        setWidth(3.75);
    }

    @Override
    public String toString() {
        return "Normal Road " + "Start " + getStart() + " end " + getEnd();
    }
}
