package ui.setting;

/**
 * Contains some settings for the drawing of the map
 *
 * @author Kareem
 */
public class GraphicsSetting {

    private static GraphicsSetting instance;

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

    private boolean showTraffficLight = true;

    private boolean showIntersection = true;

    private boolean decorated = true;

    /**
     * How much you want to scale the UI by
     */
    private double scale = 0.5;

    private double zoom = 1d;

    private int panX = 0;

    private int panY = 0;

    private int mouseY = 0;

    private int mouseX = 0;

    /**
     * Ensure that a public construction is not avaliable
     */
    private GraphicsSetting() {
    }

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

    /**
     * Get the value of mouseY
     *
     * @return the value of mouseY
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * Set the value of mouseY
     *
     * @param mouseY new value of mouseY
     */
    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    /**
     * Get the value of mouseX
     *
     * @return the value of mouseX
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Set the value of mouseX
     *
     * @param mouseX new value of mouseX
     */
    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

}
