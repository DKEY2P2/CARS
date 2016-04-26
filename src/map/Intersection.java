package map;

import controller.Observer;
import controller.SimulationSettings;
import controller.Ticker;
import helper.UniqueNumberGenerator;
import ui.Drawable;
import vehicle.Vehicle;

import java.awt.*;
import java.util.ArrayList;

/**
 * The abstract class for all types of intersections present in the simulation
 * <p>
 * An intersection is similar to a Node/Vertex of a graph
 *
 * @author Lucas Vanparijs, Kareem Horstink
 * @since 27-02-16
 */
public abstract class Intersection extends geom.Rectangle implements Drawable, Observer {

    TrafficLight trafficLight;

    private Ticker ticker;
    /**
     * All the roads that are connected to this intersection
     */
    private ArrayList<Road> roads = new ArrayList<Road>();

    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

    public int ID;

    /**
     * The constructor of the abstract class
     *
     * @param x the x coordinate of the intersection
     * @param y the y coordinate of the intersection
     */
    public Intersection(int x, int y, Ticker t) {
        super(x,y,SimulationSettings.intersectionSize,SimulationSettings.intersectionSize);//TODO the whole underlying representation should be in meters
        t.addObserver(this);
        this.ticker = t;
        this.ID = UniqueNumberGenerator.getID("i");
    }

    /**
     * Adds a road to the intersection
     *
     * @param r the road to be added
     * @return the added road
     */
    public Road addRoad(Road r) {

        int side = -1;

        if(this.getRoads().size() < 8){
            if(r.getStart() == this){
                side = this.getSide(r.getStart().p);
                if(this.isOccupied(side)){
                    System.err.println("Sorry this side of the intersection is already occupied");
                    r = null;
                }else{
                    r.setStart(this.getMidPoint(side));
                    roads.add(r);
                }
            }else{
                side = this.getSide(r.getEnd().p);
                if(this.isOccupied(side)){
                    System.err.println("Sorry this side of the intersection is already occupied");
                    r = null;
                }else{
                    r.setEnd(this.getMidPoint(side));
                    roads.add(r);
                }
            }
        }else{
            System.err.println("No Road added");
        }
        return r;
    }

    public TrafficLight addTrafficLight(TrafficLight tl) {
        //trafficLight = new TrafficLight(this); //TODO trafficLight should only take in one intersection
        return trafficLight;
    }
    public void update(){
        //UPDATE THE LIGHTS
        //UPDATE THE CARS ON THE INTERSECTION
    }

    /**
     * Returns all the roads connected to the intersection
     *
     * @return all the connected roads
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Removes a road from the intersection
     *
     * @param r the road to be removed
     * @return the removed road
     */
    public Road removeRoad(Road r) {
        int i = roads.indexOf(r);
        roads.remove(r);
        return r;
    }

    /**
     * Returns whether the light of a certain road connected to the intersection
     * is green(true) or not(false)
     *
     * @param r The road of which to check the light from
     * @return True if the light is green, false if it is red
     */
    public boolean isGreen(Road r) {
        int i = roads.indexOf(r);
        return trafficLight.isGreen(); //TODO this needs to go in the TrafficLights
    }

    public ArrayList<Road> getIns() {
        ArrayList<Road> ins = new ArrayList<Road>();
        for (Road r : getRoads()) {
            if (r.getEnd() == this) {
                ins.add(r);
            }
        }
        return ins;
    }

    public ArrayList<Road> getOuts() {
        ArrayList<Road> outs = new ArrayList<Road>();
        for (Road r : getRoads()) {
            if (r.getStart() == this) {
                outs.add(r);
            }
        }
        return outs;
    }

    /**
     * Returns all the neighbouring intersections
     * <p>
     * Works by my understanding, moved one thing
     *
     * @author jvacek, Kareem
     * @return ArrayList&#60;Intersection&#62; with all the neighbouring
     * intersections
     */
    public ArrayList<Intersection> getNeighbours() {
        ArrayList<Intersection> ret = new ArrayList<>();
        for (Road r : getRoads()) {
            Intersection tmp = null;
            if (r.getEnd() == this) {	//if the end intersection is the same one as "this"...
                tmp = r.getStart();	//... add the OTHER one to the neighbours...
            } else {
                tmp = r.getEnd();	//... or the end one, if it wasn't...
            }
            if ((tmp != null) && !ret.contains(tmp)) {
                ret.add(tmp);		//... but only if it's not on the list already
            }

        }
        return ret;
    }

    public Road hasRoad(Intersection i) {
        for (Road r1 : getRoads()) {
            for (Road r2 : i.getRoads()) {
                if (r1 == r2) {
                    return r1;
                }
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics g) {

        //TODO drawIntersection(g,this)

        /*g.drawRect(p.x,p.y,width(null),height(null)); //TODO we need some sort of scaling thing for the width and going from coordinates to pixels


        g.setColor(Color.WHITE);
        double zoom = GraphicsSetting.getInstance().getZoom();
        int tmpX = (int) (((int) (x)) - (DIAMETER * zoom) / 2);
        int tmpY = (int) (((int) (y)) - (DIAMETER * zoom) / 2);

        g.fillOval(tmpX,
                tmpY,
                (int) (DIAMETER * zoom),
                (int) (DIAMETER * zoom));
        int i = 0;
        if (GraphicsSetting.getInstance().isShowTraffficLight()) {
            for (TrafficLight tLight : tLights) {
                tLight.draw(g, i++);
            }
        }*/
    }

    @Override
    public void update(String args) {
        update();
    }

    @Override
    public String toString() {
        return ("Intersection: x= " + p.x + ", y= " + p.y + " ID: " + ID);
    }

}
