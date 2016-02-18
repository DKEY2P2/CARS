package ui;

/**
 * Contains some settings for the drawing of the map
 *
 * @author Kareem
 */
public class GraphicsSetting {

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
