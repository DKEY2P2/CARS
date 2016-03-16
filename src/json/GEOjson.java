package json;

import controller.Ticker;
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

public class GEOjson {
    /**
     * Converts a GEOjson object into our map
     * 
     * @param filepath The location of the JSON file
     * @throws FileNotFoundException Exception thrown if file not found
     */
    public static Map GEOJsonConverter(String filepath) throws FileNotFoundException {
	//Map we're going to return
	Map map = new Map(new ArrayList<Road>(), new ArrayList<Intersection>());
	//chuk chuk
	String JSON = file2String(filepath);
	
	//COnverts the whole file into a JSON object	
	JSONObject obj = new JSONObject(JSON);
	//Extract an array of features, holding JSON Objects
	JSONArray features = obj.getJSONArray("features");
	
	//For each Feature object...
	for (int i = 0; i < features.length(); i++) {
//	    System.out.println("Feature " + i);
	    JSONObject geom = features.getJSONObject(i).getJSONObject("geometry");
	    
	    if (geom.getString("type").equals("LineString")) {
//		System.out.println("LineString object");
		ArrayList<Intersection> ints = new ArrayList<Intersection>();
		
		JSONArray coordinates = geom.getJSONArray("coordinates");
		
		//Extracting intersections from points and adding them to the list
		for (int j = 0; j < coordinates.length(); j++) {
		    
		    JSONArray point = coordinates.getJSONArray(j);
		    
		    int xcoord = (int) (point.getDouble(0)*100000);
		    int ycoord = (int) (point.getDouble(1)*100000);
		    
		    ints.add(includeIntersectionAtPoint(xcoord, ycoord, map));
		    
		}
		//now that we have intersections, let's string shit up
		for (int j = 0; j < ints.size() - 1; j++) {
		    map.addRoad(new NormalRoad(ints.get(j), ints.get(j + 1)));
		    map.addRoad(new NormalRoad(ints.get(j + 1), ints.get(j)));
		}
		
	    }
	    
	}
	return map;
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
	return i;
	
    }
    
}
