/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.LineLabel;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class LineLabelDrawer implements ILabelDrawer<LineLabel> {

    private Shape shape;

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, LineLabel styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        if (shape != null) {
            StyledText styleText = styleNode.getLabel();

            if (styleText != null) {
                StyleTextDrawer styleTextDrawer = new StyleTextDrawer();
                String text = (String) styleText.getText().getValue();
                 if(text!=null && !text.isEmpty()){
        Rectangle2D bounds = styleTextDrawer.getBounds(g2, text, properties, mapTransform, styleText);
        double totalWidth = bounds.getWidth();

        // TODO, is shp a polygon ? Yes so create a line like:

        /**
         *         ___________
         *   _____/           \
         *   \                 \
         *   /   - - - - - -    \
         *  /                    \
         * |_____________________/
         *
         * And plot label as:
         *         ___________
         *   _____/           \
         *   \                 \
         *   /   A  L  P  E  S  \
         *  /                    \
         * |_____________________/
         *
         * Rather than:
         *         ___________
         *   _____/           \
         *   \                 \
         *   /       ALPES      \
         *  /                    \
         * |_____________________/
         *
         */

        Label.VerticalAlignment vA = styleNode.getVerticalAlign();
        Label.HorizontalAlignment hA = styleNode.getHorizontalAlign();
        RelativeOrientation ra = styleNode.getOrientation();

        if (vA == null) {
            vA = Label.VerticalAlignment.TOP;
            //The four important lines, here, according to the SE norm, are the
            //middle line, the baseline, the ascent line and the descent line.
        }
        if (hA == null) {
            hA = Label.HorizontalAlignment.CENTER;
        }
        if (ra == null) {
            ra = RelativeOrientation.NORMAL_UP;
        }
        double lineLength = ShapeHelper.getLineLength(shape);
        double startAt;
        double stopAt;
        switch (hA) {
            case RIGHT:
                startAt = lineLength - totalWidth;
                stopAt = lineLength;
                break;
            case LEFT:
                startAt = 0.0;
                stopAt = totalWidth;
                break;
            default:
            case CENTER:
                startAt = (lineLength - totalWidth) / 2.0;
                stopAt = (lineLength + totalWidth) / 2.0;
                break;

        }
        if (startAt < 0.0) {
            startAt = 0.0;
        }
        if (stopAt > lineLength) {
            stopAt = lineLength;
        }
        Point2D.Double ptStart = ShapeHelper.getPointAt(shape, startAt);
        Point2D.Double ptStop = ShapeHelper.getPointAt(shape, stopAt);
        int way = 1;
        // Do not laid out the label upside-down !
        if (ptStart.x > ptStop.x) {
            // invert line way
            way = -1;
            startAt = stopAt;
        }

        double currentPos = startAt;
        double glyphWidth;

        String[] glyphs = text.split("");

        ArrayList<Shape> outlines = new ArrayList<Shape>();

        for (String glyph : glyphs) {
            if (glyph != null && !glyph.isEmpty()) {
                Rectangle2D gBounds = styleTextDrawer.getBounds( g2, glyph, properties, mapTransform, styleText);
                
                glyphWidth = gBounds.getWidth() * way;
                Point2D.Double pAt = ShapeHelper.getPointAt(shape, currentPos);
                Point2D.Double pAfter = ShapeHelper.getPointAt(shape, currentPos + glyphWidth);
                //We compute the angle we must use to rotate our glyph.
                double theta = Math.atan2(pAfter.y - pAt.y, pAfter.x - pAt.x);
                //We compute the place where we will draw the chatacter, and
                //the orientation it must have.
                AffineTransform at = AffineTransform.getTranslateInstance(pAt.x, pAt.y);
                at.concatenate(AffineTransform.getRotateInstance(theta));
                currentPos += glyphWidth;
                outlines.add(styleTextDrawer.getOutline(g2, glyph, properties, mapTransform, at, vA, styleText));
            } else {
                //System.out.println ("Space...");
                //currentPos += emWidth*way;
            }
        }
        styleTextDrawer.drawOutlines( g2, outlines, properties, mapTransform,styleText);
    }
            }
    }
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
