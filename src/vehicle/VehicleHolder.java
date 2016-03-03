package vehicle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple holder for all the cars
 *
 * @author Kareem
 */
public class VehicleHolder implements Collection<Vehicle> {

    private static VehicleHolder instance;

    /**
     * Get the value of instance
     *
     * @return the value of instance
     */
    public static VehicleHolder getInstance() {
        if (instance == null) {
            instance = new VehicleHolder();
        }
        return instance;
    }

    private VehicleHolder() {
    }

    private HashMap<Integer, Vehicle> vehicleMap = new HashMap<>();

    /**
     * Adds a new vehicle with the corespondent key
     *
     * @param v The vehicle to add
     * @param key The key that is associated
     */
    public void add(Vehicle v, Integer key) {
        vehicleMap.put(key, v);
    }

    /**
     * Removes an item
     *
     * @param key The item to be removed
     * @return The item removed
     */
    public Vehicle remove(Integer key) {
        return vehicleMap.remove(key);
    }

    @Override
    public int size() {
        return vehicleMap.size();
    }

    @Override
    public boolean isEmpty() {
        return vehicleMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Vehicle) {
            return vehicleMap.containsValue((Vehicle) o);
        } else if (o instanceof Integer) {
            return vehicleMap.containsKey((Integer) o);
        }
        return false;
    }

    @Override
    public Iterator<Vehicle> iterator() {
        return vehicleMap.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return vehicleMap.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return vehicleMap.values().toArray(a);
    }

    @Override
    public boolean add(Vehicle e) {
        if (vehicleMap.containsKey(e.getIndex())) {
            return false;
        } else {
            vehicleMap.put(e.getIndex(), e);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Vehicle) {
            if (vehicleMap.containsValue((Vehicle) o)) {
                vehicleMap.remove(((Vehicle) o).getIndex());
                return true;
            }
            return false;
        } else if (o instanceof Integer) {
            if (vehicleMap.containsKey((Integer) o)) {
                vehicleMap.remove((Integer) o);
                return true;
            }
        }
        return false;
    }

    /**
     * Do not use
     *
     * @param c
     * @return
     * @deprecated
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().noneMatch((c1) -> (!contains(c1)));
    }

    @Override
    public boolean addAll(Collection<? extends Vehicle> c) {
        return c.stream().noneMatch((c1) -> (!add(c1)));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return c.stream().noneMatch((c1) -> (!this.remove(c1)));
    }

    /**
     * Do not use
     *
     * @return
     * @deprecated
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        vehicleMap.clear();
    }

}
