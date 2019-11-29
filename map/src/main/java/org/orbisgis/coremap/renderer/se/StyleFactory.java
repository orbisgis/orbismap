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
import org.orbisgis.style.IRule;

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
    public static FeatureStyle createPointSymbolizerStyle(ILayer layer) {
        FeatureStyle style = new FeatureStyle();
        style.addRule(new FeatureRule());
        IRule rule = style.getRules().get(0);
        rule.addSymbolizer(new PointSymbolizer());
        return style;
    }
    
    /**
     * Create a style with one <code>PointSymbolizer</code>
     * @param layer
     * @return a  <code>Style</code>
     */
    public static FeatureStyle createLineSymbolizerStyle(ILayer layer) {
        FeatureStyle style = new FeatureStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setFill(new SolidFill(Color.BLUE));
        lineSymbolizer.setStroke(ps);
        FeatureRule rule = new FeatureRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }
    
    
}
