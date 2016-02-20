package ui;

import helper.Logger;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Holds all the images used by the UI
 * <p>
 * Note for moving images, such as gif and the sort, it better to use ints as
 * keys rather than Strings
 *
 * @author Kareem
 */
public class ImageMap {

    private static final ImageMap SINGLETON = new ImageMap();

    public static ImageMap getInstance() {
        return SINGLETON;
    }

    private final HashMap<String, BufferedImage> STRING_MAP = new HashMap<>();
    private final HashMap<Integer, String> INT_MAP = new HashMap<>(); //link an index to the string so we can link it to the buffered image

    private ImageMap() {
        pitaBread();
    }

    /**
     * Size of the map
     *
     * @return How many images is used
     */
    public int getSize() {
        return STRING_MAP.size();
    }

    /**
     * Adds a new image to the map with the specific key
     *
     * @param i The buffered image
     * @param key The key associated to the image
     * @return The index associated to the image
     */
    public int addImage(BufferedImage i, String key) {
        if (i == null || key == null) {
            throw new IllegalArgumentException();
        }
        if (STRING_MAP.containsKey(key)) {
            replace(i, key);
            return 0;
        } else {
            STRING_MAP.put(key, i);
            int index = INT_MAP.size();
            INT_MAP.put(index, key);
            return index;
        }
    }

    /**
     * Adds a new images to the map with the specific key
     *
     * @param i An array of bufferedImages to be inputted
     * @param key The keys associated to the image
     * @return The indexes associated to the image
     */
    public int[] addImages(BufferedImage[] i, String[] key) {
        int[] index = new int[key.length];
        for (int j = 0; j < key.length; j++) {
            index[j] = addImage(i[j], key[j]);
        }
        return index;
    }

    /**
     * Gets the image based on the key and the rotation amount and the rescaling
     * amount
     *
     * @param key The string that represent the image
     * @param rotation How much you want to rotate the image. In radians
     * @param scaleFactor How much you want to scale the image
     * @return The affected Buffered Image
     */
    public BufferedImage getImage(String key, double rotation, double scaleFactor) {
        if (rotation == 0 && scaleFactor == 1) {
            return STRING_MAP.get(key);
        }
        BufferedImage orginal = STRING_MAP.get(key);
        BufferedImage copy = BufferedImageHelper.deepCopy(orginal);
        if (rotation != 0) {
            copy = BufferedImageHelper.rotate(copy, rotation);
        }
        if (scaleFactor != 1) {
            copy = BufferedImageHelper.resize(copy, (int) (copy.getWidth() * scaleFactor),
                    (int) (copy.getHeight() * scaleFactor));
        }
        return copy;
    }

    /**
     * Gets the image based on the key and the rotation amount and the rescaling
     * amount
     *
     * @param key The int that represent the image
     * @param rotation How much you want to rotate the image. In radians
     * @param scaleFactor How much you want to scale the image
     * @return The affected Buffered Image
     */
    public BufferedImage getImage(Integer key, double rotation, double scaleFactor) {
        String keyString = INT_MAP.get(key);
        if (keyString == null || keyString.isEmpty()) {
            throw new NullPointerException();
        }
        return ImageMap.this.getImage(keyString, rotation, scaleFactor);
    }

    /**
     * Returns the bufferedimage from the map
     *
     * @param key The key to return the image
     * @return The bufferedimage that has been requested
     */
    public BufferedImage getImage(int key) {
        return getImage(key, 0, 1);
    }

    /**
     * Returns the bufferedimage from the map
     *
     * @param key The key to return the image
     * @return The bufferedimage that has been requested
     */
    public BufferedImage getImage(String key) {
        return ImageMap.this.getImage(key, 0, 1);
    }

    /**
     * Replace an image with a new image
     *
     * @param i The new image to be replaced
     * @param key The key of what to replace
     */
    public void replace(BufferedImage i, String key) {
        STRING_MAP.replace(key, i);
    }

    /**
     * Replace an image with a new image
     *
     * @param i The new image to be replaced
     * @param key The key of what to replace
     */
    public void replace(BufferedImage i, Integer key) {
        String keyString = INT_MAP.get(key);
        STRING_MAP.replace(keyString, i);
    }

    /**
     * Encapsulation for a single integer. Internal use only. Should be replaced
     */
    @Deprecated
    private class IntEncapse {

        int a;

        public IntEncapse() {
        }

        protected void setA(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

    }

    /**
     * Need to rename.
     * <p>
     * It loads the current resources into the map.
     */
    public void pitaBread() {
        URL parentURL = getClass().getClassLoader().getResource("cars");
        File parentFile = new File(parentURL.getPath());
        for (File listFile : parentFile.listFiles()) {
            if (listFile.isFile()) {
                if (listFile.getName().endsWith(".png")) {
                    try {
                        addImage(ImageIO.read(listFile), listFile.getName().split("\\.")[0]);
                    } catch (IOException ex) {
                        Logger.LogError(ex);
                    }
                }
            }
        }

        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.red);
        g.fill(new Ellipse2D.Double(0, 0, 10, 10));
        g.dispose();
        addImage(bi, "CircleRed");

    }

    public static void main(String[] args) {
        new ImageMap().pitaBread();
    }

    /**
     * Clears the current data in the imagemap
     */
    public void clear() {
        INT_MAP.clear();
        STRING_MAP.clear();
    }
}
