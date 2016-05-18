package json;

import controller.Ticker;
import helper.Logger;
import map.Intersection;
import map.Map;
import map.Road;
import map.intersection.DefaultIntersection;
import map.road.NormalRoad;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONException;
import java.lang.Math;

public class GEOjson {
    
    public static int intersectioncnt = 0;
    public static int roadcnt	      = 0;
    public static int refX	      = 69696;
    public static int refY	      = 69696;
				      
    /**
     * Converts a GEOjson object into our map
     *
     * @param filepath The location of the JSON file
     * @return
     * @throws FileNotFoundException Exception thrown if file not found
     */
    public static Map GEOJsonConverter(String filepath) throws FileNotFoundException {
	try {
	    helper.Logger.printOther(filepath);
	    //Clear ints.
	    intersectioncnt = 0;
	    roadcnt = 0;
	    //Map we're going to return
	    Map map = new Map(new ArrayList<Road>(), new ArrayList<Intersection>());
	    //chuk chuk
	    String JSON = file2String(filepath);
	    
	    //COnverts the whole file into a JSON object	
	    JSONObject obj = new JSONObject(JSON);
	    //Extract an array of features, holding JSON Objects
	    JSONArray features = obj.getJSONArray("features");
	    
	    //For each Feature object...
	    System.out.println("Importing " + features.length() + "GeoJSON features");
	    
	    for (int i = 0; i < features.length(); i++) {
		//	    System.out.println("Feature " + i);
		JSONObject geom = features.getJSONObject(i).getJSONObject("geometry");
		
		if (geom.getString("type").equals("LineString")) {
		    //                    helper.Logger.printOther("LineString object");
		    
		    JSONArray coordinates = geom.getJSONArray("coordinates");
		    LineStringChugger(coordinates, map);
		    
		} else if (geom.getString("type").equals("MultiLineString")) {
		    //                    helper.Logger.printOther("MutliLineString object");
		    JSONArray coordinates = geom.getJSONArray("coordinates");
		    
		    //Extracting intersections from points and adding them to the list
		    for (int j = 0; j < coordinates.length(); j++) {
			LineStringChugger(coordinates.getJSONArray(j), map);
		    }
		    
		} else if (geom.getString("type").equals("Polygon")) {
		    //                    helper.Logger.printOther("Polygon object");
		    JSONArray coordinates = geom.getJSONArray("coordinates");
		    
		    //Extracting intersections from points and adding them to the list
		    for (int j = 0; j < coordinates.length(); j++) {
			LineStringChugger(coordinates.getJSONArray(j), map);
		    }
		}
		
	    }
	    //	    System.out
	    //	            .println("Processed " + intersectioncnt + " intersection & " + roadcnt + " roads from " + filepath);
	    return map;
	} catch (FileNotFoundException | JSONException e) {
	    System.err.println("An error has occured when importing a map");
	    System.err.println(e);
	    Logger.LogError(e);
	    return new Map(new ArrayList<>(), new ArrayList<>());
	} catch (Exception ex) {
	    System.err.println("An error has occured when importing a map");
	    System.err.println(ex);
	    Logger.LogError(ex);
	    return new Map(new ArrayList<>(), new ArrayList<>());
	}
    }
    
    /**
     * Converts a file into a String
     *
     * @param filepath
     * @return
     * @throws FileNotFoundException
     */
    public static String file2String(String filepath) throws FileNotFoundException {
	File file = new File(filepath);
	Scanner scanner = new Scanner(file);
	String text = scanner.useDelimiter("\\A").next();
	scanner.close(); // Put this call in a finally block
	return text;
    }
    
    /**
     * Attempts to add an intersection to the map, but if it has already been
     * added before, it returns it instead
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param map the map the intersection is being added to
     * @return the intersection created
     */
    public static Intersection includeIntersectionAtPoint(int x, int y, Map map) {
	for (Intersection i : map.getIntersections()) {
	    if (i.getX() == x & i.getY() == y) {
		return i;
	    }
	}
	Ticker t = controller.Controller.getInstance().getTicker();
	//	System.out.println(t);
	DefaultIntersection i = new DefaultIntersection(x, y, t);
	map.addIntersection(i);
	intersectioncnt++;
	return i;
	
    }
    
    public static void LineStringChugger(JSONArray coordinates, Map map) {
	//Extracting intersections from points and adding them to the list
	
	//If these are not set yet, let's do.
	if (refX == 69696 || refY == 69696) {
	    refX = (int)
		    //(coordinates.getJSONArray(0).getDouble(0) * 1000000);
		    Mercator.mercX(coordinates.getJSONArray(0).getDouble(0));
	    refY = (int)
		    //(coordinates.getJSONArray(0).getDouble(1) * 1000000);
		    Mercator.mercY(coordinates.getJSONArray(0).getDouble(1));
	
	    
	}
	
	ArrayList<Intersection> ints = new ArrayList<Intersection>();
	for (int j = 0; j < coordinates.length(); j++) {
	    
	    JSONArray point = coordinates.getJSONArray(j);
	    
	    int xcoord = (int)
		    //((point.getDouble(0) * 1000000) - refX);
		    Mercator.mercX(point.getDouble(0)) - refX;
	    int ycoord = (int)
		    //((-1) * ((point.getDouble(1) * 1000000) - refY));
		    Mercator.mercY(point.getDouble(1)) - refY;
	    ints.add(includeIntersectionAtPoint(xcoord, ycoord, map));
	    
	}
	//now that we have intersections, let's string shit up
	for (int j = 0; j < ints.size() - 1; j++) {
	    map.addRoad(new NormalRoad(ints.get(j), ints.get(j + 1)));
	    map.addRoad(new NormalRoad(ints.get(j + 1), ints.get(j)));
	    roadcnt++;
	}
    }
    
    public static Map simplifier(Map map) {
	for (Intersection i : map.getIntersections()) {
	    //If the intersection has a road coming in and a road coming out 
	    if (i.getRoads().size() == 2) {
		Road r1 = i.getRoads().get(0);
		Road r2 = i.getRoads().get(1);
		
		if (r1.getStart() == i){}
//			r1.set
		
	    }
	}
	return null;
    }
    /**
    
    public static class SphericalMercator {
	public static final double RADIUS = 6378137.0;
	
	public static double y2lat(double aY) {
	    return Math.toDegrees(2 * Math.atan(Math.exp(Math.toRadians(aY / RADIUS))) - Math.PI / 2);
	}
	
	public static double lat2y(double aLat) {
	    return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS;
	}
	
	public static double x2lon(double aX) {
	    return Math.toDegrees(aX / RADIUS);
	}
	
	public static double lon2x(Angle aLong) {
	    return aLong.radians * RADIUS;
	}
    }
    
    **/
    
    public static class Mercator {
	final private static double R_MAJOR = 6378137.0;
	final private static double R_MINOR = 6356752.3142;
					    
	public static double[] merc(double x, double y) {
	    return new double[]{mercX(x), mercY(y)};
	}
	
	private static double mercX(double lon) {
	    return R_MAJOR * Math.toRadians(lon);
	}
	
	private static double mercY(double lat) {
	    if (lat > 89.5) {
		lat = 89.5;
	    }
	    if (lat < -89.5) {
		lat = -89.5;
	    }
	    double temp = R_MINOR / R_MAJOR;
	    double es = 1.0 - (temp * temp);
	    double eccent = Math.sqrt(es);
	    double phi = Math.toRadians(lat);
	    double sinphi = Math.sin(phi);
	    double con = eccent * sinphi;
	    double com = 0.5 * eccent;
	    con = Math.pow(((1.0 - con) / (1.0 + con)), com);
	    double ts = Math.tan(0.5 * ((Math.PI * 0.5) - phi)) / con;
	    double y = 0 - R_MAJOR * Math.log(ts);
	    return y;
	}
    }
    
}
