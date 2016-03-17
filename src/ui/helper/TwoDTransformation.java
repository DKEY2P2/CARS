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
        double zoom = GraphicsSetting.getInstance().getZoom();
        int panX = GraphicsSetting.getInstance().getPanX();
        int average = GraphicsSetting.getInstance().getMouseX() + panX;

        if (zoom != 1) {
            x = translate(x, -average);
            x = scale(x, zoom);
            x = translate(x, average);
        }
        x = translate(x, panX);
        return x;
    }

    public static int transformY(int y) {
        double zoom = GraphicsSetting.getInstance().getZoom();
        int panY = GraphicsSetting.getInstance().getPanY();
        int average = GraphicsSetting.getInstance().getMouseY() + panY;

        if (zoom != 1) {
            y = translate(y, -average);
            y = scale(y, zoom);
            y = translate(y, average);
        }
        y = translate(y, panY);
        return y;
    }

    public static SimpleImmutableEntry<Integer, Integer> transformX(int x1, int x2) {
        double zoom = GraphicsSetting.getInstance().getZoom();
        int panX = GraphicsSetting.getInstance().getPanX();
        int average = GraphicsSetting.getInstance().getMouseX() + panX;
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
        double zoom = GraphicsSetting.getInstance().getZoom();
        int panY = GraphicsSetting.getInstance().getPanY();
        int average = GraphicsSetting.getInstance().getMouseY() + panY;

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
