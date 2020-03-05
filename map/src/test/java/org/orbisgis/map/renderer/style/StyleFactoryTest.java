/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.awt.Color;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author ebocher
 */
public class StyleFactoryTest {

    /**
     * Create a style with one <code>LineSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter("2"));
        ps.setFill(new SolidFill(Color.BLUE));
        lineSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }

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
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createTextSymbolizer() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        pointLabel.setLabel(new StyledText("'OrbisGIS'"));
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createTextSymbolizerWithField() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        pointLabel.setLabel(new StyledText("type"));
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatched() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new ExpressionParameter("2"));
        hatchedFill.setStroke(psHatchedFill);
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter("2"));
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
    public static Feature2DStyle createAreaSymbolizerHatchedColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new ExpressionParameter("2"));
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        ExpressionParameter opacity = new ExpressionParameter("1");
        SolidFill sfhatchedFill = new SolidFill(colorExpression, opacity);
        psHatchedFill.setFill(sfhatchedFill);
        hatchedFill.setStroke(psHatchedFill);
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter("2"));
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
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END  ");
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

    static Feature2DStyle createTextSymbolizerColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        pointLabel.setLabel(new StyledText("type"));
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    static Feature2DStyle createAreaSymbolizerGeometryExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setGeometryParameter(new GeometryParameter("ST_BUFFER(THE_GEOM, 12)"));
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
