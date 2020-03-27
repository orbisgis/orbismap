/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.factory;

import java.awt.Color;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IRule;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.*;
import org.orbisgis.style.graphic.MarkGraphic;
import static org.orbisgis.style.graphic.MarkGraphic.DEFAULT_SIZE;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
import org.orbisgis.style.stroke.PenStroke;
import static org.orbisgis.style.stroke.PenStroke.DEFAULT_CAP;
import static org.orbisgis.style.stroke.PenStroke.DEFAULT_JOIN;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 *
 * @author ebocher
 */
public class StyleFactory {

    /**
     * Create a style with one <code>LineSymbolizer</code>
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizerSizeExpression() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setWidth(new Expression("CASE WHEN TYPE = 'cereals' then 5 else 1 END"));
        Expression colorExpression = new Expression(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        Literal opacity = new Literal(1f);
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
    public static Feature2DStyle createLineSymbolizer(Color color, float width,double offset ) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setPerpendicularOffset(new Literal(offset));
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(width));
        ps.setFill(createSolidFill(color));
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
    public static Feature2DStyle createDashedLineSymbolizer(Color color, float width,double offset, String dashArray ) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setPerpendicularOffset(new Literal(offset));
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(width));
        ps.setFill(createSolidFill(color));
        ps.setDashArray(new Literal(dashArray));
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
    public static Feature2DStyle createDashedAreaymbolizer(Color color, float width,double offset, String dashArray ) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setPerpendicularOffset(new Literal(offset));
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(width));
        ps.setFill(createSolidFill(color));
        ps.setDashArray(new Literal(dashArray));
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createAreaSymbolizer(Color fillColor, float opacity, double offset) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setFill(createSolidFill(fillColor, opacity));
        areaSymbolizer.setPerpendicularOffset(new Literal(offset));
        areaSymbolizer.setStroke(createPenStroke(Color.BLACK, 1));
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
        styledText.setText(new Expression("type"));
        pointLabel.setLabel(styledText);
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a <code>HatchedFill</code> <code>AreaSymbolizer</code>
     *
     * @param hatchColor
     * @param hatchAngle
     * @param hatchDistance
     * @param hatchWidth
     * @param strokeAreaColor
     * @param strokeWidth
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createHatchedAreaSymbolizer(Color hatchColor,  float hatchWidth, float hatchAngle,float hatchDistance,
            Color strokeAreaColor, float strokeWidth ) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();        
        areaSymbolizer.setFill(createHatchedFill(hatchColor, hatchWidth, hatchAngle, hatchDistance));
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(strokeWidth));
        ps.setFill(createSolidFill(strokeAreaColor));
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }
    
    public static HatchedFill createHatchedFill(Color hatchColor,  float hatchWidth, float hatchAngle,float hatchDistance){
        HatchedFill hatchedFill = new HatchedFill();
        hatchedFill.setAngle(new Literal(hatchAngle));
        hatchedFill.setDistance(new Literal(hatchDistance));
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setFill(createSolidFill(hatchColor));
        psHatchedFill.setWidth(new Literal(hatchWidth));
        hatchedFill.setStroke(psHatchedFill);
        return hatchedFill;
    }

    /**
     * Create a style with one <code>AreaSymbolizer</code>
     *
     * @param colorExpression
     * @param hatchWidth
     * @param hatchAngle
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatchedColorExpression(String colorExpression, float hatchWidth, float hatchAngle,float hatchDistance) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        hatchedFill.setAngle(new Literal(hatchAngle));
        hatchedFill.setDistance(new Literal(hatchDistance));
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new Literal(hatchWidth));
        Literal opacity = new Literal(1f);
        SolidFill sfhatchedFill = new SolidFill(new Expression(colorExpression), opacity);
        psHatchedFill.setFill(sfhatchedFill);
        hatchedFill.setStroke(psHatchedFill);
        areaSymbolizer.setFill(hatchedFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(2f));
        ps.setFill(createSolidFill(Color.blue));
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
        Expression colorExpression = new Expression(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END");
        Literal opacity = new Literal(1f);
        SolidFill solidFill = new SolidFill(colorExpression, opacity);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(1.0f));
        ps.setFill(createSolidFill(Color.BLUE));
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
        mg.setFill(createSolidFill(Color.yellow));
        mg.setStroke(createPenStroke(Color.RED, 2));
        mg.setWkn(new Literal("circle"));
        mg.setUom(Uom.PX);
        mg.setViewBox(new ViewBox(new Literal(1f)));
        dotMapFill.setGraphic(mg);
        dotMapFill.setQuantityPerMark(new Literal(3));
        dotMapFill.setTotalQuantity(new Literal(2000));
        areaSymbolizer.setFill(dotMapFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(1.0f));
        ps.setFill(createSolidFill(Color.GREEN));
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createTextSymbolizerColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        StyledText styledText = new StyledText("type");
        Expression colorExpression = new Expression(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END ");
        Literal opacity = new Literal(1f);
        SolidFill solidFill = new SolidFill(colorExpression, opacity);
        styledText.setFill(solidFill);
        pointLabel.setLabel(styledText);
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createAreaSymbolizerGeometryExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setGeometryParameter(new GeometryParameter("ST_BUFFER(THE_GEOM, 50)"));
        areaSymbolizer.setFill(createSolidFill(Color.GREEN));
        areaSymbolizer.setStroke(createPenStroke(Color.BLUE,2));
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createAreaSymbolizerRuleExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setFill(createSolidFill(Color.GREEN));
        PenStroke ps = new PenStroke();
        ps.setFill(createSolidFill(Color.black));
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.setExpression(new Expression("st_area(the_geom) < 5000"));
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
        densityFill.setPercentageCovered(new Literal(0.2f));
        densityFill.setHatchesOrientation(new Literal(120f));
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new Literal(1f));
        Expression colorExpression = new Expression(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        Literal opacity = new Literal(1f);
        SolidFill sfhatchedFill = new SolidFill(colorExpression, opacity);
        psHatchedFill.setFill(sfhatchedFill);
        densityFill.setHatches(psHatchedFill);
        areaSymbolizer.setFill(densityFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(2f));
        ps.setFill(createSolidFill(Color.GREEN));
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
        densityFill.setPercentageCovered(new Literal(0.2f));
        densityFill.setHatchesOrientation(new Literal(120f));
        MarkGraphic markGraphic = new MarkGraphic();        
        markGraphic.setWkn(new Literal("circle"));
        Expression colorExpression = new Expression(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        Literal opacity = new Literal(1f);
        SolidFill markFill = new SolidFill(colorExpression, opacity);
        markGraphic.setFill(markFill);        
        markGraphic.setViewBox(new ViewBox(new Literal(12f), new Literal(12f)));
        densityFill.setGraphic(markGraphic);
        areaSymbolizer.setFill(densityFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(new Literal(3f));
        ps.setFill(createSolidFill(Color.GREEN));
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
        Literal colorExpression = new Literal("#ff6d6d");
        Literal opacity = new Literal(1f);
        SolidFill solilFillG = new SolidFill(colorExpression, opacity);
        GraphicFill graphicFill = new GraphicFill();
        MarkGraphic mg = new MarkGraphic();
        mg.setWkn(new Literal("circle"));
        mg.setFill(solilFillG);
        mg.setViewBox(new ViewBox(new Literal(12f), new Literal(12f)));
        graphicFill.setGraphic(mg);
        areaSymbolizer.setFill(graphicFill);
        PenStroke ps = new PenStroke();
        ps.setFill(createSolidFill(Color.GREEN));
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
    public static Feature2DStyle createPointSymbolizer(String wkn, Color fillColor, float size, Color strokeColor, float strokeSize) {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        MarkGraphic  markGraphic = new MarkGraphic();
        markGraphic.setWkn(new Literal(wkn));
        markGraphic.setViewBox(new ViewBox(new Literal(size)));
        markGraphic.setFill(createSolidFill(fillColor)); 
        markGraphic.setStroke(createPenStroke(strokeColor, strokeSize));
        ps.setGraphic(markGraphic);
        rule.addSymbolizer(ps);
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
        MarkGraphic  markGraphic = new MarkGraphic();
        markGraphic.setWkn(new Literal("square"));
        markGraphic.setViewBox(new ViewBox(new Literal(12f), new Literal(12f)));
        markGraphic.setFill(createSolidFill(Color.ORANGE));                
        ps.setGraphic(markGraphic);
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
        markGraphic.setFill(createSolidFill(Color.blue)); 
        markGraphic.setStroke(createPenStroke(Color.black, 1));        
        markGraphic.setWkn(new Literal("square"));
        markGraphic.setViewBox(new ViewBox(new Expression("CASE WHEN ST_AREA(THE_GEOM)> 20000 THEN 60.0  ELSE 5.0 END")));
        ps.setGraphic(markGraphic);
        rule.addSymbolizer(ps);
        return style;
    }

    public static Feature2DStyle createAreaSymbolizerAndPointSymbolizerVertex() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setFill(createSolidFill(Color.GREEN));
        PenStroke ps = new PenStroke();
        ps.setFill(createSolidFill(Color.ORANGE));
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        PointSymbolizer pointSymbolizer = new PointSymbolizer();
        pointSymbolizer.setGraphic(createMarkGraphic());
        pointSymbolizer.setOnVertex(true);  
        rule.addSymbolizer(areaSymbolizer);
        rule.addSymbolizer(pointSymbolizer); 
        style.addRule(rule);
        return style;
    }
    
    
    public static Feature2DStyle createSymbolsWithLevel() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setFill(createSolidFill(Color.BLUE));
        PenStroke ps = new PenStroke();
        ps.setFill(createSolidFill(Color.PINK));
        areaSymbolizer.setStroke(ps);
        areaSymbolizer.setLevel(1);
        Feature2DRule rule = new Feature2DRule();        
        PointSymbolizer pointSymbolizerCircle = new PointSymbolizer();
        MarkGraphic mg = new MarkGraphic();
        mg.setFill(createSolidFill(Color.yellow));
        mg.setWkn(new Literal("circle"));
        mg.setViewBox(new ViewBox(new Literal(10f)));
        pointSymbolizerCircle.setOnVertex(true);
        pointSymbolizerCircle.setGraphic(mg);
        pointSymbolizerCircle.setLevel(2);
        PointSymbolizer pointSymbolizerCross = new PointSymbolizer();
        MarkGraphic mg_cross = new MarkGraphic();
        mg_cross.setWkn(new Literal("cross"));
        mg_cross.setViewBox(new ViewBox(new Literal(10f)));        
        mg_cross.setFill(createSolidFill(Color.RED));
        pointSymbolizerCross.setOnVertex(true);
        pointSymbolizerCross.setGraphic(mg_cross);
        pointSymbolizerCross.setLevel(3);
        
        rule.addSymbolizer(areaSymbolizer);
        rule.addSymbolizer(pointSymbolizerCircle);
        rule.addSymbolizer(pointSymbolizerCross);
        style.addRule(rule);
        return style;
    }
    
     /**
     * Create a <code>SolidFill</code>
     *
     * @param color
     * @param opacity
     * @return a  <code>SolidFill</code>
     */
    public static SolidFill createSolidFill(Color color, float opacity) {        
        return new SolidFill(ParameterValueHelper.toExpression(color), new Literal(opacity));
    }
    
     /**
     * Create a <code>SolidFill</code>
     *
     * @param color
     * @return a  <code>SolidFill</code>
     */
    public static SolidFill createSolidFill(Color color) {        
        return new SolidFill(ParameterValueHelper.toExpression(color), ParameterValueHelper.createFloatLiteral(1.0f));
    }
    
    /**
     * Create a standard 0.1mm-wide opaque black stroke without dash.
     * @param color
     * @param width
     * @return 
     */
    public static PenStroke createPenStroke(Color color, float width) {
        PenStroke penStroke = new PenStroke();
        penStroke.setFill(StyleFactory.createSolidFill(color));
        penStroke.setWidth(new Literal(width));
        penStroke.setUom(Uom.PX);
        penStroke.setDashOffset(new NullParameterValue());
        penStroke.setDashArray(new NullParameterValue());
        penStroke.setLineCap(DEFAULT_CAP);
        penStroke.setLineJoin(DEFAULT_JOIN);
        return penStroke;
    }    
   
    
    /**
     * Create a standard <code>MarkGraphic</code>
     * @return 
     */
    public static MarkGraphic createMarkGraphic() {
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setUom(Uom.PX);
        markGraphic.setWkn(new Literal("square"));
        markGraphic.setViewBox(new ViewBox(new Literal(6f), new Literal(6f)));
        markGraphic.setFill(createSolidFill(Color.GRAY));
        markGraphic.setStroke(createPenStroke(Color.magenta, 1));
        return markGraphic;
    }
    
     /**
     * Create a standard <code>MarkGraphic</code>
     * @return 
     */
    public static MarkGraphic create3mmMarkGraphic() {
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setUom(Uom.MM);
        markGraphic.setWkn(new Literal("circle"));
        markGraphic.setViewBox(new ViewBox(new Literal(DEFAULT_SIZE), new Literal(DEFAULT_SIZE)));
        markGraphic.setFill(createSolidFill(Color.GRAY));
        markGraphic.setStroke(createPenStroke(Color.ORANGE, 1));
        return markGraphic;
    }
}
