package ui.setting;

/**
 * Contains some settings for the drawing of the map
 *
 * @author Kareem
 */
public class GraphicsSetting {

    private boolean showTraffficLight = true;

    /**
     * Get the value of showTraffficLight
     *
     * @return the value of showTraffficLight
     */
    public boolean isShowTraffficLight() {
        return showTraffficLight;
    }

    /**
     * Set the value of showTraffficLight
     *
     * @param showTraffficLight new value of showTraffficLight
     */
    public void setShowTraffficLight(boolean showTraffficLight) {
        this.showTraffficLight = showTraffficLight;
    }

    private boolean showIntersection = true;

    /**
     * Get the value of showIntersection
     *
     * @return the value of showIntersection
     */
    public boolean isShowIntersection() {
        return showIntersection;
    }

    /**
     * Set the value of showIntersection
     *
     * @param showIntersection new value of showIntersection
     */
    public void setShowIntersection(boolean showIntersection) {
        this.showIntersection = showIntersection;
    }

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

    private double zoom = 1d;

    /**
     * Get the value of zoom
     *
     * @return the value of zoom
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Set the value of zoom
     *
     * @param zoom new value of zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    private int panX = 0;

    /**
     * Get the value of panX
     *
     * @return the value of panX
     */
    public int getPanX() {
        return panX;
    }

    /**
     * Set the value of panX
     *
     * @param panX new value of panX
     */
    public void setPanX(int panX) {
        this.panX = panX;
    }

    private int panY = 0;

    /**
     * Get the value of panY
     *
     * @return the value of panY
     */
    public int getPanY() {
        return panY;
    }

    /**
     * Set the value of panY
     *
     * @param panY new value of panY
     */
    public void setPanY(int panY) {
        this.panY = panY;
    }

}
