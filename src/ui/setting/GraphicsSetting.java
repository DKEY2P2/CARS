package ui.setting;

/**
 * Contains some settings for the drawing of the map
 *
 * @author Kareem
 */
public class GraphicsSetting {
    
    private boolean decorated = true;

    /**
     * Get the value of decorated
     *
     * @return the value of decorated
     */
    public boolean isDecorated() {
        return decorated;
    }

    /**
     * Set the value of decorated
     *
     * @param decorated new value of decorated
     */
    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }


    /**
     * How much you want to scale the UI by
     */
    private double scale = 0.5;

    /**
     * Get the value of scale
     *
     * @return the value of scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the value of scale
     *
     * @param scale new value of scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    private static GraphicsSetting instance;

    /**
     * Ensure that a public construction is not avaliable
     */
    private GraphicsSetting() {
    }

    /**
     * Get the instance of this class
     *
     * @return The instance of this class
     */
    public static GraphicsSetting getInstance() {
        if (instance == null) {
            instance = new GraphicsSetting();
        }
        return instance;
    }
}
