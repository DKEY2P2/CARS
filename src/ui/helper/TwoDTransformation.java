package ui.helper;

import java.util.AbstractMap.SimpleImmutableEntry;

import ui.setting.GraphicsSetting;

/**
 * A helper class meant to help with moving and scaling objects
 *
 * @author Kareem Horstink
 */
public class TwoDTransformation {

    public static int transformX(int x) {
        double zoom = getZoom();
        int panX = getPanX();
        int average = getAverageX(panX);

        if (zoom != 1) {
            x = translate(x, -average);
            x = scale(x, zoom);
            x = translate(x, average);
        }
        x = translate(x, panX);
        return x;
    }

    private static int getPanX() {
        return (int) (GraphicsSetting.getInstance().getPanX());
    }

    private static int getAverageX(int panX) {
        return panX + GraphicsSetting.getInstance().getMouseX();
    }

    private static int getAverageY(int panY) {
        return panY + GraphicsSetting.getInstance().getMouseY();
    }

    private static int getPanY() {
        return (int) (GraphicsSetting.getInstance().getPanY());
    }

    public static int transformY(int y) {
        double zoom = getZoom();
        int panY = getPanY();
        int average = getAverageY(panY);

        if (zoom != 1) {
            y = translate(y, -average);
            y = scale(y, zoom);
            y = translate(y, average);
        }
        y = translate(y, panY);
        return y;
    }

    private static double getZoom() {
        return GraphicsSetting.getInstance().getZoom();
    }

    public static SimpleImmutableEntry<Integer, Integer> transformX(int x1, int x2) {
        double zoom = getZoom();
        int panX = getPanX();
        int average = getAverageX(panX);
        if (zoom != 1) {
            int tmp1 = translate(x1, -average);
            int tmp2 = translate(x2, -average);
            tmp1 = scale(tmp1, zoom);
            tmp2 = scale(tmp2, zoom);
            x1 = translate(tmp1, average);
            x2 = translate(tmp2, average);
        }
        x1 = translate(x1, panX);
        x2 = translate(x2, panX);
        return new SimpleImmutableEntry<>(x1, x2);
    }

    public static SimpleImmutableEntry<Integer, Integer> transformY(int y1, int y2) {
        double zoom = getZoom();
        int panY = getPanY();
        int average = getAverageY(panY);

        if (zoom != 1) {
            int tmp1 = translate(y1, -average);
            int tmp2 = translate(y2, -average);

            tmp1 = scale(tmp1, zoom);
            tmp2 = scale(tmp2, zoom);

            y1 = translate(tmp1, average);
            y2 = translate(tmp2, average);
        }

        y1 = translate(y1, panY);
        y2 = translate(y2, panY);
        return new SimpleImmutableEntry<>(y1, y2);
    }

    public static int translate(int i, int j) {
        return i + j;
    }

    public static int scale(int i, double scale) {
        return (int) (i * scale);
    }
}
