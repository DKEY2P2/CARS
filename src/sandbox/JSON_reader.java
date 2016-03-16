package sandbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.Controller;
import controller.Ticker;
import json.GEOjson;
import map.Map;

public class JSON_reader {
    
    public static void main(String[] args) throws FileNotFoundException {
	Controller ctrl = Controller.getInstance();
	ctrl.setTicker(new Ticker(1, 1));
	
	
	// TODO Auto-generated method stub
	String filepath = "src/resources/GEOjson/test.json";
	
	boolean debug = true;
	if (debug) {
	    File file = new File(filepath);
	    Scanner scanner = new Scanner(file);
	    String text = scanner.useDelimiter("\\A").next();
	    scanner.close(); // Put this call in a finally block
//	    System.out.println("File " + text + "\n");
	}
	
	Map map = GEOjson.GEOJsonConverter(filepath);
	System.out.println(map);
	
    }
 
    
    
    
    
    
}
