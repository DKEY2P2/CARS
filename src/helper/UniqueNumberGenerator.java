package helper;

/**
 * Created by lvanp on 21/04/2016.
 */
public class UniqueNumberGenerator {

    public static int intersectionCounter = 0;

    public static int roadCounter = 0;

    public static int vehicleCounter = 0;

    public static int getID(String s){
        if(s.contains("i")){
            intersectionCounter++;
            return intersectionCounter;
        }else if(s.contains("r")){
            roadCounter++;
            return roadCounter;
        }else if(s.contains("v")){
            vehicleCounter++;
            return vehicleCounter;
        }else{
            System.err.println("No known input");
        }
        return -1;
    }


}
