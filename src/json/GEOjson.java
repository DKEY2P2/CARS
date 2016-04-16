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

public class GEOjson {

    public static int intersectioncnt = 0;
    public static int roadcnt = 0;
    public static int refX = 69696;
    public static int refY = 69696;

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
	System.out.println("Processed " + intersectioncnt + " intersection & " + roadcnt + " roads from "+filepath);
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
	if (refX==69696 || refY == 69696){
	    refX = (int) (coordinates.getJSONArray(0).getDouble(0)* 1000000);
	    refY = (int) (coordinates.getJSONArray(0).getDouble(1)* 1000000);
        }

        ArrayList<Intersection> ints = new ArrayList<Intersection>();
        for (int j = 0; j < coordinates.length(); j++) {

            JSONArray point = coordinates.getJSONArray(j);

	    int xcoord = (int) ((point.getDouble(0) * 1000000)-refX);
	    int ycoord = (int) ((-1)*((point.getDouble(1) * 1000000)-refY));

            ints.add(includeIntersectionAtPoint(xcoord, ycoord, map));

        }
        //now that we have intersections, let's string shit up
        for (int j = 0; j < ints.size() - 1; j++) {
            map.addRoad(new NormalRoad(ints.get(j), ints.get(j + 1)));
            map.addRoad(new NormalRoad(ints.get(j + 1), ints.get(j)));
            roadcnt++;
        }
    }

}
