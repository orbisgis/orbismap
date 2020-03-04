/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.renderer.style;

import java.awt.Color;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.symbolizer.AreaSymbolizer;

/**
 *
 * @author ebocher
 */
public class StyleFactoryTest {
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatchedStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        SolidFill psFill = new SolidFill(Color.BLUE);
        ps.setFill(psFill);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }
    
     /**
     * Create a style with one <code>AreaSymbolizer</code> and a SolidFill color
     * expression
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerStyleColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 50000 THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        ExpressionParameter opacity = new ExpressionParameter("1");
        SolidFill solidFill = new SolidFill(colorExpression, opacity);
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
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerDotFillStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        DotMapFill dotMapFill = new DotMapFill();
        dotMapFill.setGraphic(new MarkGraphic());
        dotMapFill.setQuantityPerMark(new RealLiteral(3));
        dotMapFill.setTotalQuantity(new RealLiteral(20));
        areaSymbolizer.setFill(dotMapFill);
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
