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

    private final HashMap<Integer, Vehicle> VEHICLE_MAP = new HashMap<>();

    /**
     * Adds a new vehicle with the corespondent key
     *
     * @param v The vehicle to add
     * @param key The key that is associated
     */
    public void add(Vehicle v, Integer key) {
        VEHICLE_MAP.put(key, v);
    }

    /**
     * Removes an item
     *
     * @param key The item to be removed
     * @return The item removed
     */
    public Vehicle remove(Integer key) {
        return VEHICLE_MAP.remove(key);
    }

    @Override
    public int size() {
        return VEHICLE_MAP.size();
    }

    @Override
    public boolean isEmpty() {
        return VEHICLE_MAP.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Vehicle) {
            return VEHICLE_MAP.containsValue((Vehicle) o);
        } else if (o instanceof Integer) {
            return VEHICLE_MAP.containsKey((Integer) o);
        }
        return false;
    }

    @Override
    public Iterator<Vehicle> iterator() {
        return VEHICLE_MAP.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return VEHICLE_MAP.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return VEHICLE_MAP.values().toArray(a);
    }

    @Override
    public boolean add(Vehicle e) {
        if (VEHICLE_MAP.containsKey(e.getIndex())) {
            return false;
        } else {
            VEHICLE_MAP.put(e.getIndex(), e);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Vehicle) {
            if (VEHICLE_MAP.containsValue((Vehicle) o)) {
                VEHICLE_MAP.remove(((Vehicle) o).getIndex());
                return true;
            }
            return false;
        } else if (o instanceof Integer) {
            if (VEHICLE_MAP.containsKey((Integer) o)) {
                VEHICLE_MAP.remove((Integer) o);
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
        VEHICLE_MAP.clear();
    }

}
