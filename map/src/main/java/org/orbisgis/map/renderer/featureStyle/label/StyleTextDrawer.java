/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.label;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.IFill;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.string.StringParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class StyleTextDrawer implements IStyleDrawer<StyledText> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(Halo.class, new HaloDrawer());
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, StyledText styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
            String txt = ValueHelper.getString(sp, styleNode.getText());
            if (txt != null) {
            Label.VerticalAlignment va  = (Label.VerticalAlignment) properties.get("verticalalignment");

            if (va == null) {
                va  = Label.VerticalAlignment.TOP;
            }
            AffineTransform at = (AffineTransform) properties.get("affinetransform");

            ArrayList<Shape> outlines = new ArrayList<Shape>();
            outlines.add(getOutline(sp, g2, txt, properties, mapTransform, at, va, styleNode));
            drawOutlines(sp, g2, outlines, properties, mapTransform, styleNode);
        }
    }

    /**
     * Draw the list of given "outlines", that is the list of characters already
     * transformed to {@code Shape} instances.We'll use for that, of course, the
     * inner {@code Fill}, {@code Halo} and {@code Stroke} instances.If they are
     * not set, a simple default {@code SolidFill} will be used.
     *
     * @param sp
     * @param g2 The graphics we draw with
     *
     * @param outlines The list of needed outlines
     * @param mapTransform
     * @throws ParameterException
     */
    public void drawOutlines(JdbcSpatialTable sp, Graphics2D g2, ArrayList<Shape> outlines, Map<String, Object> properties,
            MapTransform mapTransform, StyledText styleNode) throws ParameterException, SQLException {

        Halo halo = styleNode.getHalo();

        if (halo != null) {
            if (drawerMap.containsKey(halo.getClass())) {
                IStyleDrawer haloDrawer = drawerMap.get(halo.getClass());
                for (Shape outline : outlines) {
                    properties.put("shape", outline);
                    haloDrawer.draw(sp, g2, mapTransform, halo, properties);
                }
            }
        }

        IFill fill = styleNode.getFill();
        Stroke stroke = styleNode.getStroke();
        IStyleDrawer strokeDrawer = null;
        IStyleDrawer fillDrawer = null;
        if (stroke != null ){
            if (drawerMap.containsKey(stroke.getClass())) {
                strokeDrawer = drawerMap.get(stroke.getClass());
            }
        }
        if(fill != null) {

            if (drawerMap.containsKey(fill.getClass())) {
                fillDrawer = drawerMap.get(fill.getClass());
            }
        }

            for (Shape outline : outlines) {
                /**
                 * No fill and no stroke : apply default SolidFill !
                 */
                if (fillDrawer == null && strokeDrawer == null) {
                    SolidFill sf = new SolidFill(Color.BLACK, 1.0);
                    sf.setParent(styleNode);
                    properties.put("shape", outline);
                    new SolidFillDrawer().draw(sp, g2, mapTransform, sf, properties);
                }
                if (fillDrawer != null) {
                    properties.put("shape", outline);
                    fillDrawer.draw(sp, g2, mapTransform, fill, properties);
                }
                if (strokeDrawer != null) {
                    properties.put("shape", outline);
                    properties.put("offset", 0.0);
                    strokeDrawer.draw(sp, g2, mapTransform, stroke, properties);
                }
            }


    }

    /**
     * Get the minimal {@code Rectangle2D} that contains this
     * {@code StyledText}.
     *
     * @param g2 The graphics we draw with
     * @param text The text for which we need the bounds.
     * @param map The map of input values
     * @param mt The current IMapTransform
     * @return The bounds of the text
     * @throws ParameterException
     * @throws IOException
     */
    public Rectangle2D getBounds(JdbcSpatialTable sp, Graphics2D g2, String text, Map<String, Object> map,
            MapTransform mt, StyledText styleNode) throws ParameterException {

        Font font = getFont(sp, map, mt, styleNode);
        FontMetrics metrics = g2.getFontMetrics(font);
        return metrics.getStringBounds(text, null);
    }

    /**
     * Gets the outline of the given {@code String} as a shape. This shapes is
     * made of the boundary(ies) of the text, that will have to be stroked and
     * fill.
     *
     * @param g2 The {@code Graphics2D} instance used to render the map we are
     * drawing.
     * @param text The text we want to compute the outline of.
     * @param map The map of input values
     * @param mt Used to compute the font's size.
     * @param at The AffineTransform that we must apply to the shape before
     * returning it.
     * @param va The {@code Label.VerticalAlignment} we must use to determine
     * where to put the baseline of {@code text}.
     * @return The needed Shape
     * @throws ParameterException If we fail to retrieve a parameter used to
     * configure this {@code
     * StyledText}.
     * @throws IOException If an error occurred while retrieving the
     * {@code Font}.
     */
    public Shape getOutline(JdbcSpatialTable sp, Graphics2D g2, String text, Map<String, Object> map,
            MapTransform mt, AffineTransform at, Label.VerticalAlignment va, StyledText styleNode)
            throws ParameterException {
        Font font = getFont(sp, map, mt, styleNode);
        TextLayout tl = new TextLayout(text, font, g2.getFontRenderContext());
        FontMetrics metrics = g2.getFontMetrics(font);
        double dy = 0;
        switch (va) {
            case BASELINE:
                break;
            case BOTTOM:
                dy = metrics.getAscent();
                break;
            case TOP:
                dy = -metrics.getDescent();
                break;
            case MIDDLE:
            default:
                dy = (metrics.getAscent() - metrics.getDescent()) / 2.0;
        }
        AffineTransform rat;
        if (at != null) {
            rat = new AffineTransform(at);
        } else {
            rat = new AffineTransform();
        }
        //We apply the translation used to manage the height of the text on the
        //line BEFORE to apply at : we use concatenate.
        rat.concatenate(AffineTransform.getTranslateInstance(0, dy));
        return tl.getOutline(rat);
    }

    /**
     *
     * @param sp
     * @param map
     * @param mt
     * @param styleNode
     * @return
     * @throws ParameterException
     * @throws IOException
     */
    private Font getFont(JdbcSpatialTable sp, Map<String, Object> map, MapTransform mt, StyledText styleNode) throws ParameterException {
        String family = "Arial";
        StringParameter fontFamily = styleNode.getFontFamily();
        if (fontFamily != null) {
            family = fontFamily.getValue(map);
        }

        // TODO Family is comma delimeted list of fonts family. Choose the first available
        StringParameter fontWeight = styleNode.getFontWeight();
        String weight = "normal";
        if (fontWeight != null) {
            weight = fontWeight.getValue(map);
        }

        String style = "normal";
        StringParameter fontStyle = styleNode.getFontStyle();
        if (fontStyle != null) {
            style = fontStyle.getValue(map);
        }

        //double size = Uom.toPixel(12, Uom.PT, mt.getDpi(), mt.getScaleDenominator(), null);
        double size = 12.0;
        RealParameter fontSize = styleNode.getFontSize();
        if (fontSize != null) {
            size = UomUtils.toPixel(fontSize.getValue(map), styleNode.getFontUom(), mt.getDpi(), mt.getScaleDenominator(), null);
        }

        int st = Font.PLAIN;

        if (weight.equalsIgnoreCase("bold")) {
            st = Font.BOLD;
        }

        if (style.equalsIgnoreCase("italic")) {
            if (st == Font.PLAIN) {
                st |= Font.ITALIC;
            } else {
                st = Font.ITALIC;
            }
        }

        return new Font(family, st, (int) size);
    }
}
