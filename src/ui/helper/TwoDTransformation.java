package ui.helper;

import java.awt.geom.AffineTransform;

import ui.setting.GraphicsSetting;

/**
 * A helper class meant to help with moving and scaling objects
 *
 * @author Kareem Horstink
 */
public class TwoDTransformation {

    public static AffineTransform getAfflineTransform() {
        double zoom = GraphicsSetting.getInstance().getZoom();
        double panX = GraphicsSetting.getInstance().getPanX();
        double panY = GraphicsSetting.getInstance().getPanY();
        double mouseX = GraphicsSetting.getInstance().getMouseX();
        double mouseY = GraphicsSetting.getInstance().getMouseY();

        AffineTransform finalT = new AffineTransform();
        AffineTransform panCenterMouse = new AffineTransform();
        AffineTransform panCenterMouseMin = new AffineTransform();
        AffineTransform translate = new AffineTransform();
        AffineTransform zoomT = new AffineTransform();
        
        
        panCenterMouse.setToTranslation(mouseX, mouseY);
        panCenterMouseMin.setToTranslation(-mouseX, -mouseY);
        translate.setToTranslation(panX, panY);
        zoomT.setToScale(zoom, zoom);
        
        
        finalT.concatenate(panCenterMouse);
        finalT.concatenate(zoomT);
        finalT.concatenate(panCenterMouseMin);
        finalT.concatenate(translate);

        return finalT;
    }
}
