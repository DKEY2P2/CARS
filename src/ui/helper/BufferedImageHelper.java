package ui.helper;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * A helper class to help with dealing with Buffered Images
 *
 * @author Kareem Horstink
 */
public class BufferedImageHelper {

    //To be fair, I copied like all this code
    /**
     * Resizes the image to a certain size
     *
     * @param image The image to be manipulated
     * @param newW The new requested width
     * @param newH The new requested height
     * @return The replaced image
     */
    public static BufferedImage resize(BufferedImage image, int newW, int newH) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    /**
     * Does a deep copy of a bufferedImage
     *
     * @param image The image to be copied
     * @return A clone of a bufferedImage
     */
    public static BufferedImage deepCopy(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Rotates the image. Uses Bilinear filtering
     *
     * @param image The image to be rotated
     * @param rotation How much to rotate it by, in radians
     * @return The rotated image
     */
    public static BufferedImage rotate(BufferedImage image, double rotation) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotation, image.getWidth() / 2, image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

}
