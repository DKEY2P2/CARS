package ui.helper;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.AbstractMap.SimpleImmutableEntry;

import ui.setting.GraphicsSetting;

/**
 * A helper class meant to help with moving and scaling objects
 *
 * @author Kareem Horstink
 */
public class TwoDTransformation {
    
    public static AffineTransform getAfflineTransform(){
        double zoom = GraphicsSetting.getInstance().getZoom();
        double panX = GraphicsSetting.getInstance().getPanX();
        double panY = GraphicsSetting.getInstance().getPanY();
        
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToScale(zoom, zoom);
        affineTransform.setToTranslation(panX, panY);
        return affineTransform;
    }
}
