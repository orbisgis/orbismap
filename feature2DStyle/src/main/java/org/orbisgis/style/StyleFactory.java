/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style;

import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import java.awt.Color;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.stroke.PenStroke;

/**
 *
 * @author ebocher
 */
public class StyleFactory {
    
    /**
     * Create a style with one <code>PointSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createPointSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        rule.addSymbolizer(new PointSymbolizer());
        return style;
    }
    
    /**
     * Create a style with one <code>LineSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setFill(new SolidFill(Color.BLUE));
        lineSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        SolidFill solidFill = new SolidFill(Color.GREEN);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        SolidFill psFill = new SolidFill(Color.BLUE);
        ps.setFill(psFill);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }
    
}
