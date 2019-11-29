/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.se;

import java.awt.Color;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.renderer.se.fill.SolidFill;
import org.orbisgis.coremap.renderer.se.stroke.PenStroke;

/**
 *
 * @author ebocher
 */
public class StyleFactory {
    
    /**
     * Create a style with one <code>PointSymbolizer</code>
     * @param layer
     * @return a  <code>Style</code>
     */
    public static Style createPointSymbolizerStyle(ILayer layer) {
        Style style = new Style(layer,true);
        FeatureRule rule = style.getRules().get(0);
        rule.getCompositeSymbolizer().addSymbolizer(new PointSymbolizer());
        return style;
    }
    
    /**
     * Create a style with one <code>PointSymbolizer</code>
     * @param layer
     * @return a  <code>Style</code>
     */
    public static Style createLineSymbolizerStyle(ILayer layer) {
        Style style = new Style(layer,true);
        FeatureRule rule = style.getRules().get(0);
        rule.getCompositeSymbolizer().getSymbolizerList().remove(0);
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setFill(new SolidFill(Color.BLUE));
        lineSymbolizer.setStroke(ps);
        rule.getCompositeSymbolizer().addSymbolizer(lineSymbolizer);
        return style;
    }
    
    
}
