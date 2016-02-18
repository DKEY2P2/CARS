package controller;

public interface Task {
    /**
     * Takes care of advancing the vehicle and updating its many fields
     * @return a confirmation of success or failure
     */
    public boolean update();
}
