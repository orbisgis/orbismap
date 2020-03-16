/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.awt.Color;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IRule;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.*;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author ebocher
 */
public class StyleFactoryTest {

    /**
     * Create a style with one <code>LineSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizerSizeExpression() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter("CASE WHEN TYPE = 'cereals' then 5 else 1 END", true));
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ", true);
        ExpressionParameter opacity = new ExpressionParameter(1);
        ps.setFill(new SolidFill(colorExpression, opacity));
        lineSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>LineSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizer() {
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
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createTextSymbolizer() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        pointLabel.setLabel(new StyledText("OrbisGIS"));
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>AreaSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createTextSymbolizerWithField() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        StyledText styledText = new StyledText();
        styledText.setText(new ExpressionParameter("type", true));
        pointLabel.setLabel(styledText);
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>AreaSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatched() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new ExpressionParameter(2));
        hatchedFill.setStroke(psHatchedFill);
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter(2));
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
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatchedColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new ExpressionParameter(2));
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ", true);
        ExpressionParameter opacity = new ExpressionParameter(1);
        SolidFill sfhatchedFill = new SolidFill(colorExpression, opacity);
        psHatchedFill.setFill(sfhatchedFill);
        hatchedFill.setStroke(psHatchedFill);
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter(2));
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
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END  ", true);
        ExpressionParameter opacity = new ExpressionParameter(1);
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
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerDotFillStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        DotMapFill dotMapFill = new DotMapFill();
        MarkGraphic mg = new MarkGraphic();
        mg.setWkn(new ExpressionParameter("circle"));
        mg.setUom(Uom.PX);
        mg.setViewBox(new ViewBox(new ExpressionParameter(1)));
        dotMapFill.setGraphic(mg);
        dotMapFill.setQuantityPerMark(new ExpressionParameter(3));
        dotMapFill.setTotalQuantity(new ExpressionParameter(2000));
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
        StyledText styledText = new StyledText("type");
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END ", true);
        ExpressionParameter opacity = new ExpressionParameter(1);
        SolidFill solidFill = new SolidFill(colorExpression, opacity);
        styledText.setFill(solidFill);
        pointLabel.setLabel(styledText);
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    static Feature2DStyle createAreaSymbolizerGeometryExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setGeometryParameter(new GeometryParameter("ST_BUFFER(THE_GEOM, 50)"));
        SolidFill solidFill = new SolidFill(Color.ORANGE);
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

    public static Feature2DStyle createAreaSymbolizerRuleExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        SolidFill solidFill = new SolidFill(Color.YELLOW);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        SolidFill psFill = new SolidFill(Color.BLUE);
        ps.setFill(psFill);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.setExpression(new ExpressionParameter("st_area(the_geom) < 5000"));
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>AreaSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatchDensityFillColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        DensityFill densityFill = new DensityFill();
        densityFill.setPercentageCovered(new ExpressionParameter(0.2));
        densityFill.setHatchesOrientation(new ExpressionParameter(120));
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new ExpressionParameter("1"));
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ", true);
        ExpressionParameter opacity = new ExpressionParameter(1);
        SolidFill sfhatchedFill = new SolidFill(colorExpression, opacity);
        psHatchedFill.setFill(sfhatchedFill);
        densityFill.setHatches(psHatchedFill);
        areaSymbolizer.setFill(densityFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter(2));
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
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerMarkDensityFillColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        DensityFill densityFill = new DensityFill();
        densityFill.setPercentageCovered(new ExpressionParameter(0.2));
        densityFill.setHatchesOrientation(new ExpressionParameter(120));
        MarkGraphic markGraphic = new MarkGraphic();
        ExpressionParameter colorExpression = new ExpressionParameter("#ff6d6d");
        ExpressionParameter opacity = new ExpressionParameter(1);
        SolidFill markFill = new SolidFill(colorExpression, opacity);
        markGraphic.setFill(markFill);
        densityFill.setGraphic(markGraphic);
        areaSymbolizer.setFill(densityFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new ExpressionParameter(3));
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
    public static Feature2DStyle createAreaSymbolizerGraphicFillColor() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        ExpressionParameter colorExpression = new ExpressionParameter("#ff6d6d");
        ExpressionParameter opacity = new ExpressionParameter(1);
        SolidFill solilFillG = new SolidFill(colorExpression, opacity);
        GraphicFill graphicFill = new GraphicFill();
        MarkGraphic mg = new MarkGraphic();
        mg.setFill(solilFillG);
        mg.setViewBox(new ViewBox(new ExpressionParameter(12), new ExpressionParameter(12)));
        graphicFill.setGraphic(mg);
        areaSymbolizer.setFill(graphicFill);
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
     * Create a style with one <code>PointSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createPointSymbolizer() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        rule.addSymbolizer(new PointSymbolizer());
        return style;
    }

    /**
     * Create a style with one <code>PointSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createPointSymbolizerVertex() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        ps.setOnVertex(true);
        rule.addSymbolizer(ps);
        return style;
    }

    /**
     * Create a style with one <code>PointSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createPointSymbolizerMarkGraphicSizeExpression() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        MarkGraphic markGraphic = new MarkGraphic();
        PenStroke penStroke = new PenStroke();
        penStroke.setWidth(new ExpressionParameter(1.0));
        markGraphic.setStroke(penStroke);
        markGraphic.setViewBox(new ViewBox(new ExpressionParameter("CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN 10.0 WHEN ST_AREA(THE_GEOM)< 5000  and ST_AREA(THE_GEOM)< 10000 THEN 30.0 ELSE 1.0 END", true)));
        ps.setGraphic(markGraphic);
        rule.addSymbolizer(ps);
        return style;
    }

    static Feature2DStyle AreaSymbolizerAndPointSymbolizerVertex() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        SolidFill solidFill = new SolidFill(Color.GREEN);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        SolidFill psFill = new SolidFill(Color.BLUE);
        ps.setFill(psFill);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        PointSymbolizer pointSymbolizer = new PointSymbolizer();
        pointSymbolizer.setOnVertex(true);
        rule.addSymbolizer(pointSymbolizer);        
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

}
