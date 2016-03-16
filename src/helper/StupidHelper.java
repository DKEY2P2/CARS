package helper;

import java.util.Collection;
import java.util.Random;

/**
 * A simple helper class that has method I need more than once
 *
 * @author Kareem Horstink
 */
public class StupidHelper {

    /**
     * Randomly chooses a item of a collection
     *
     * @param c The things to choose from
     * @return A random object from that collection
     */
    public static Object getRandom(Collection c) {
        if(c.isEmpty()){
            return null;
        }
        Random r = new Random();
        return c.toArray()[r.nextInt(c.size())];
    }
}
