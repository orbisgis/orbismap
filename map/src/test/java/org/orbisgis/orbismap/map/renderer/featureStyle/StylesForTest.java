/**
 * Map is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle;

import java.awt.Color;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.IRule;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.color.HexaColor;
import org.orbisgis.orbismap.style.color.RGBColor;
import org.orbisgis.orbismap.style.fill.DensityFill;
import org.orbisgis.orbismap.style.fill.DotMapFill;
import org.orbisgis.orbismap.style.fill.GraphicFill;
import org.orbisgis.orbismap.style.fill.HatchedFill;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.graphic.AnchorPosition;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox;
import org.orbisgis.orbismap.style.label.Label;
import org.orbisgis.orbismap.style.label.LineLabel;
import org.orbisgis.orbismap.style.label.PointLabel;
import org.orbisgis.orbismap.style.label.RelativeOrientation;
import org.orbisgis.orbismap.style.label.StyleFont;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;
import org.orbisgis.orbismap.style.stroke.GraphicStroke;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_CAP;
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_JOIN;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.LineSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.PointSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.TextSymbolizer;
import org.orbisgis.orbismap.style.transform.Rotate;
import org.orbisgis.orbismap.style.transform.Transform;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class StylesForTest {

    /**
     * Create a style LineSymbolizer with a filter rule
     *
     * @return
     */
    public static Feature2DStyle createLineSymbolizerRulesExpression() {
        Feature2DStyle style = new Feature2DStyle();
        style.setName("Unique values map");
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.initDefault();
        lineSymbolizer.setStroke(createPenStroke(Color.GREEN, 2));
        Feature2DRule rule1 = new Feature2DRule();
        rule1.setFilter(new Expression("type='hedgeTalus'"));
        rule1.addSymbolizer(lineSymbolizer);
        LineSymbolizer lineSymbolizer2 = new LineSymbolizer();
        lineSymbolizer2.initDefault();
        lineSymbolizer2.setStroke(createPenStroke(Color.GRAY,3));
        Feature2DRule rule2 = new Feature2DRule();
        rule2.setFilter(new Expression("type='talus'"));
        rule2.addSymbolizer(lineSymbolizer2);
        style.addRule(rule1);
        style.addRule(rule2);
        return style;
    }

    /**
     * Create a style area with a filter rule
     *
     * @return
     */
    public static Feature2DStyle createAreaSymbolizerRuleExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.initDefault();
        SolidFill solidFill = new SolidFill();
        solidFill.initDefault();
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        ps.initDefault();
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.setFilter(new Expression("st_area(the_geom) < 5000"));
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createStyleWithLineSymbolizerSizeExpression() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setWidth(new Expression("CASE WHEN TYPE = 'cereals' then 5 else 1 END"));
        Expression colorExpression = new Expression(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        SolidFill solidFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        solidFill.setColor(hexaColor);
        solidFill.setOpacity(1);
        ps.setFill(solidFill);
        lineSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }

    /*
         * Create a style with one <code>LineSymbolizer</code>
         *
         * @param color line color
         * @param width of the line
         * @param offset of the line
         * @param uom unit of measure
         * @return a  <code>Style</code>
     */
    public static Feature2DStyle createTwoLineSymbolizers() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setUom(Uom.PX);
        lineSymbolizer.setPerpendicularOffset(0);
        PenStroke ps = new PenStroke();
        ps.setWidth(10);
        ps.setFill(createSolidFill(Color.RED));
        lineSymbolizer.setStroke(ps);
        LineSymbolizer lineSymbolizer2 = new LineSymbolizer();
        lineSymbolizer2.setUom(Uom.PX);
        lineSymbolizer2.setPerpendicularOffset(0);
        PenStroke ps2 = new PenStroke();
        ps2.setWidth(2);
        ps2.setFill(createSolidFill(Color.YELLOW));
        lineSymbolizer2.setStroke(ps2);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        rule.addSymbolizer(lineSymbolizer2);
        style.addRule(rule);
        return style;
    }

    /*
         * Create a style with one <code>LineSymbolizer</code>
         *
         * @param color line color
         * @param width of the line
         * @param offset of the line
         * @param uom unit of measure
         * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizer(Color color, float width, double offset, Uom uom) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setUom(uom);
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
    public static Feature2DStyle createDashedLineSymbolizer(Color color, float width, double offset, String dashArray) {
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
     * @param color
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createDashedAreaymbolizer(Color color, float width, double offset, String dashArray) {
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

    public static Feature2DStyle createAreaSymbolizer(Color fillColor, float opacity, double offset, Color strokeColor, float strokeWidth) {
        Feature2DStyle style = new Feature2DStyle();
        style.setName("Single symbol map");
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setFill(createSolidFill(fillColor, opacity));
        if (offset != 0) {
            areaSymbolizer.setPerpendicularOffset(new Literal(offset));
        }
        areaSymbolizer.setStroke(createPenStroke(strokeColor, strokeWidth));
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
        pointLabel.initDefault();
        pointLabel.setLabelText(new Literal("OrbisGIS"));
        textSymbolizer.setLabel(pointLabel);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        style.addRule(rule);
        return style;
    }

    public static StyleFont createFont() {
        StyleFont font = new StyleFont();
        font.setFontFamily(new Literal("Arial"));
        font.setFontWeight(new Literal("Normal"));
        font.setFontStyle(new Literal("Normal"));
        font.setFontSize(new Literal(12f));
        return font;
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
        pointLabel.setHorizontalAlign(Label.HorizontalAlignment.CENTER);
        pointLabel.setLabelText(new Expression("type"));
        pointLabel.setFont(createFont());
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
    public static Feature2DStyle createHatchedAreaSymbolizer(Color hatchColor, float hatchWidth, float hatchAngle, float hatchDistance,
            Color strokeAreaColor, float strokeWidth) {
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

    public static HatchedFill createHatchedFill(Color hatchColor, float hatchWidth, float hatchAngle, float hatchDistance) {
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
    public static Feature2DStyle createAreaSymbolizerHatchedColorExpression(String colorExpression, float hatchWidth, float hatchAngle, float hatchDistance) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        hatchedFill.setAngle(new Literal(hatchAngle));
        hatchedFill.setDistance(new Literal(hatchDistance));
        PenStroke psHatchedFill = new PenStroke();
        psHatchedFill.setWidth(new Literal(hatchWidth));
        SolidFill sfhatchedFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(new Expression(colorExpression));
        sfhatchedFill.setColor(hexaColor);
        sfhatchedFill.setOpacity(1.0f);
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
        SolidFill solidFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        solidFill.setColor(hexaColor);
        solidFill.setOpacity(opacity);
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

    public static Feature2DStyle createAreaSymbolizerStyleRGBColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        Literal opacity = new Literal(1f);
        SolidFill solidFill = new SolidFill();
        RGBColor rgbColor = new RGBColor();
        rgbColor.setRed(new Expression("CASE WHEN RUNOFF_WIN= 1 THEN 255 WHEN RUNOFF_WIN>0.2 AND  RUNOFF_WIN<1 THEN 100 ELSE 0 END"));
        rgbColor.setGreen(new Expression("CASE WHEN RUNOFF_SUM=1 THEN 255 WHEN RUNOFF_SUM>0.2 AND  RUNOFF_SUM<1 THEN 100 ELSE 0 END"));
        rgbColor.setBlue(new Literal(0));
        solidFill.setColor(rgbColor);
        solidFill.setOpacity(opacity);
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
        mg.setWellKnownName(new Literal("circle"));
        mg.setUom(Uom.PX);
        mg.setGraphicSize(new ViewBox(1f));
        dotMapFill.addGraphic(mg);
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
        pointLabel.initDefault();
        pointLabel.setLabelText("type");
        Expression colorExpression = new Expression(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END ");
        SolidFill solidFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        solidFill.setColor(hexaColor);
        solidFill.setOpacity(1.0f);
        pointLabel.setFill(solidFill);
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
        areaSymbolizer.setStroke(createPenStroke(Color.BLUE, 2));
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
        SolidFill sfhatchedFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        sfhatchedFill.setColor(hexaColor);
        sfhatchedFill.setOpacity(1.0f);
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
        markGraphic.setWellKnownName(new Literal("circle"));
        Expression colorExpression = new Expression(""
                + "CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        Literal opacity = new Literal(1f);
        SolidFill markFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        markFill.setColor(hexaColor);
        markGraphic.setFill(markFill);
        markGraphic.setGraphicSize(new ViewBox(12f));
        densityFill.addGraphic(markGraphic);
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
        SolidFill solilFillG = new SolidFill();
        solilFillG.setColor("#ff6d6d");
        GraphicFill graphicFill = new GraphicFill();
        MarkGraphic mg = new MarkGraphic();
        mg.setWellKnownName("circle");
        mg.setFill(solilFillG);
        mg.setGraphicSize(new ViewBox(12f));
        graphicFill.addGraphic(mg);
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
        ps.setGeometryParameter(new GeometryParameter("the_geom"));
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setWellKnownName(new Literal(wkn));
        markGraphic.setGraphicSize(new ViewBox(size));
        markGraphic.setFill(createSolidFill(fillColor));
        markGraphic.setStroke(createPenStroke(strokeColor, strokeSize));
        ps.addGraphic(markGraphic);
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
        ps.setUom(Uom.PX);
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setWellKnownName(new Literal("square"));
        markGraphic.setGraphicSize(new ViewBox(12f));
        markGraphic.setFill(createSolidFill(Color.ORANGE));
        ps.addGraphic(markGraphic);
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
        markGraphic.setWellKnownName(new Literal("square"));
        ViewBox viewBox = new ViewBox();
        viewBox.setWidth(new Expression("CASE WHEN ST_AREA(THE_GEOM)> 20000 THEN 60.0  ELSE 5.0 END"));
        markGraphic.setGraphicSize(viewBox);
        ps.addGraphic(markGraphic);
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
        pointSymbolizer.addGraphic(createMarkGraphic());
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
        mg.setWellKnownName(new Literal("circle"));
        mg.setGraphicSize(new ViewBox(10f));
        pointSymbolizerCircle.setOnVertex(true);
        pointSymbolizerCircle.addGraphic(mg);
        pointSymbolizerCircle.setLevel(2);
        PointSymbolizer pointSymbolizerCross = new PointSymbolizer();
        MarkGraphic mg_cross = new MarkGraphic();
        mg_cross.setWellKnownName(new Literal("cross"));
        mg_cross.setGraphicSize(new ViewBox(10f));
        mg_cross.setFill(createSolidFill(Color.RED));
        pointSymbolizerCross.setOnVertex(true);
        pointSymbolizerCross.addGraphic(mg_cross);
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
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(color);
        solidFill.setOpacity(opacity);
        return solidFill;
    }

    /**
     * Create a <code>SolidFill</code>
     *
     * @param color
     * @return a  <code>SolidFill</code>
     */
    public static SolidFill createSolidFill(Color color) {
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(color);
        solidFill.setOpacity(1.0f);
        return solidFill;
    }

    /**
     * Create a standard PenStroke.
     *
     * @param color
     * @param width
     * @param uom
     * @return
     */
    public static PenStroke createPenStroke(Color color, float width, Uom uom) {
        PenStroke penStroke = new PenStroke();
        penStroke.setFill(createSolidFill(color));
        penStroke.setWidth(new Literal(width));
        penStroke.setUom(uom);
        penStroke.setDashOffset(new NullParameterValue());
        penStroke.setDashArray(new NullParameterValue());
        penStroke.setLineCap(DEFAULT_CAP);
        penStroke.setLineJoin(DEFAULT_JOIN);
        return penStroke;
    }

    /**
     * Create a standard PenStroke in PX.
     *
     * @param color
     * @param width
     * @return
     */
    public static PenStroke createPenStroke(Color color, float width) {
        return createPenStroke(color, width, Uom.PX);
    }

    /**
     * Create a standard <code>MarkGraphic</code>
     *
     * @return
     */
    public static MarkGraphic createMarkGraphic() {
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setUom(Uom.PX);
        markGraphic.setWellKnownName("square");
        markGraphic.setGraphicSize(new ViewBox(6f));
        markGraphic.setFill(createSolidFill(Color.GRAY));
        markGraphic.setStroke(createPenStroke(Color.magenta, 1));
        return markGraphic;
    }

    /**
     * Create a style with one <code>LineSymbolizer</code>
     *
     * @param color
     * @param width
     * @param offset
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createGraphicStrokeLineSymbolizer(Color color, float width, double offset, float distance) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setLevel(1);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        graphicStroke.setDistance(distance);
        MarkGraphic verticalLineGraphic = new MarkGraphic();
        verticalLineGraphic.setUom(Uom.PX);
        verticalLineGraphic.setWellKnownName(new Literal("square"));
        verticalLineGraphic.setGraphicSize(new ViewBox(5f));
        verticalLineGraphic.setFill(createSolidFill(Color.GRAY));
        verticalLineGraphic.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(verticalLineGraphic);
        MarkGraphic circleGraphic = new MarkGraphic();
        circleGraphic.setWellKnownName(new Literal("circle"));
        circleGraphic.setUom(Uom.PX);
        circleGraphic.setGraphicSize(new ViewBox(5f));
        circleGraphic.setFill(createSolidFill(Color.GRAY));
        circleGraphic.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(circleGraphic);
        lineSymbolizer.setPerpendicularOffset(new Literal(offset));
        lineSymbolizer.setStroke(graphicStroke);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        rule.addSymbolizer(lineSymbolizerBase);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a style with one <code>LineSymbolizer</code>
     *
     * @param color
     * @param width
     * @param offset
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createGraphicStrokeLineSymbolizerNoOverlaps(Color color, float width, double offset, float distance) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setLevel(1);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setOverlap(false);
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        graphicStroke.setDistance(distance);
        MarkGraphic verticalLineGraphic = new MarkGraphic();
        verticalLineGraphic.setUom(Uom.PX);
        verticalLineGraphic.setWellKnownName(new Literal("square"));
        verticalLineGraphic.setGraphicSize(new ViewBox(5f));
        verticalLineGraphic.setFill(createSolidFill(Color.GRAY));
        verticalLineGraphic.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(verticalLineGraphic);
        MarkGraphic circleGraphic = new MarkGraphic();
        circleGraphic.setWellKnownName(new Literal("circle"));
        circleGraphic.setUom(Uom.PX);
        circleGraphic.setGraphicSize(new ViewBox(5f));
        circleGraphic.setFill(createSolidFill(Color.RED));
        circleGraphic.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(circleGraphic);
        lineSymbolizer.setPerpendicularOffset(new Literal(offset));
        lineSymbolizer.setStroke(graphicStroke);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        rule.addSymbolizer(lineSymbolizerBase);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a line label text symbolizer
     *
     * @return
     */
    static Feature2DStyle createLineLabelTextSymbolizer() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.initDefault();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        LineLabel lineLabel = new LineLabel();
        lineLabel.initDefault();
        lineLabel.setLabelText(new Expression("type"));
        textSymbolizer.setLabel(lineLabel);
        textSymbolizer.setLevel(1);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a line label text symbolizer
     *
     * @return
     */
    static Feature2DStyle createPointLabelTextSymbolizer() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.initDefault();
        TextSymbolizer textSymbolizer = new TextSymbolizer();
        PointLabel pointLabel = new PointLabel();
        pointLabel.initDefault();
        pointLabel.setLabelText(new Expression("type"));
        textSymbolizer.setLabel(pointLabel);
        textSymbolizer.setLevel(1);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(textSymbolizer);
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

    static Feature2DStyle createMeteoColdFrontLineSymbolizer(float markSize, float distance, RelativeOrientation orientation) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setUom(Uom.MM);
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setLevel(1);
        lineSymbolizer.setUom(Uom.MM);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.MM);
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        graphicStroke.setDistance(distance);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setAnchorPosition(AnchorPosition.LOWER_LEFT);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(markSize));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 0.26f, Uom.MM));
        coldFrontMark.setUom(Uom.MM);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizer.setStroke(graphicStroke);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        rule.addSymbolizer(lineSymbolizerBase);
        style.addRule(rule);
        return style;
    }

    public static Feature2DStyle createPointSymbolizerTransform() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        ps.setUom(Uom.PX);
        MarkGraphic markGraphic = new MarkGraphic();
        Transform transform = new Transform();
        markGraphic.setTransform(transform);
        Rotate rotate = new Rotate(new Literal(180f));
        markGraphic.addTransform(rotate);
        markGraphic.setWellKnownName(new Literal("HALFCIRCLE"));
        markGraphic.setGraphicSize(new ViewBox(35f));
        markGraphic.setFill(createSolidFill(Color.ORANGE));
        ps.addGraphic(markGraphic);
        MarkGraphic markGraphic2 = new MarkGraphic();
        markGraphic2.setWellKnownName(new Literal("HALFCIRCLE"));
        markGraphic2.setGraphicSize(new ViewBox(35f));
        markGraphic2.setFill(createSolidFill(Color.BLUE));
        ps.addGraphic(markGraphic2);
        rule.addSymbolizer(ps);
        return style;
    }

    static Feature2DStyle createElevationBarLineSymbolizer(float markSize, float distance, RelativeOrientation orientation) {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setUom(Uom.MM);
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setLevel(1);
        lineSymbolizer.setUom(Uom.MM);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.MM);
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        graphicStroke.setDistance(new Expression("CASE WHEN Z >= 70 then 0.2 WHEN Z<70 and Z>=40 then 0.5 else 1 end"));
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setAnchorPosition(AnchorPosition.LOWER_LEFT);
        coldFrontMark.setWellKnownName(new Literal("VERTLINE"));
        coldFrontMark.setGraphicSize(new ViewBox(markSize));
        coldFrontMark.setStroke(createPenStroke(Color.BLACK, 0.05f, Uom.MM));
        coldFrontMark.setUom(Uom.MM);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizer.setStroke(graphicStroke);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        rule.addSymbolizer(lineSymbolizerBase);
        style.addRule(rule);
        return style;
    }

}
