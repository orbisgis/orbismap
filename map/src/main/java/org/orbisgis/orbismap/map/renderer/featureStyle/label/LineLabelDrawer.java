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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.label;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.WobbleStrokeDrawer;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.fill.Halo;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.label.RelativeOrientation;
import org.orbisgis.orbismap.style.stroke.WobbleStroke;
import org.orbisgis.orbismap.style.utils.ShapeUtils;
import org.orbisgis.orbismap.style.label.Label;
import org.orbisgis.orbismap.style.label.LineLabel;
import org.orbisgis.orbismap.style.label.StyleFont;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;

/**
 * Drawer for the element <code>LineLabel</code>
 * 
 * TODO :  improve this part
 * @author Erwan Bocher, CNRS (2020)
 */
public class LineLabelDrawer extends AbstractDrawerFinder<IStyleDrawer, IStyleNode> implements ILabelDrawer<LineLabel> {

    private Shape shape;

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, LineLabel styleNode) throws ParameterException {
        if (shape != null) {
            String styleText = (String) styleNode.getLabelText().getValue();

            if (styleText != null && !styleText.isEmpty()) {
        Rectangle2D bounds = StyleTextUtil.getBounds(g2, styleText, mapTransform, (StyleFont) styleNode.getFont());
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
        double lineLength = ShapeUtils.getLineLength(shape);
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
        Point2D.Double ptStart = ShapeUtils.getPointAt(shape, startAt);
        Point2D.Double ptStop = ShapeUtils.getPointAt(shape, stopAt);
        int way = 1;
        // Do not laid out the label upside-down !
        if (ptStart.x > ptStop.x) {
            // invert line way
            way = -1;
            startAt = stopAt;
        }

        double currentPos = startAt;
        double glyphWidth;

        String[] glyphs = styleText.split("");

        ArrayList<Shape> outlines = new ArrayList<Shape>();

        for (String glyph : glyphs) {
            if (glyph != null && !glyph.isEmpty()) {
                Rectangle2D gBounds = StyleTextUtil.getBounds(g2, glyph, mapTransform, (StyleFont) styleNode.getFont());
                
                glyphWidth = gBounds.getWidth() * way;
                Point2D.Double pAt = ShapeUtils.getPointAt(shape, currentPos);
                Point2D.Double pAfter = ShapeUtils.getPointAt(shape, currentPos + glyphWidth);
                //We compute the angle we must use to rotate our glyph.
                double theta = Math.atan2(pAfter.y - pAt.y, pAfter.x - pAt.x);
                //We compute the place where we will draw the chatacter, and
                //the orientation it must have.
                AffineTransform at = AffineTransform.getTranslateInstance(pAt.x, pAt.y);
                at.concatenate(AffineTransform.getRotateInstance(theta));
                currentPos += glyphWidth;
                outlines.add(StyleTextUtil.getOutline(g2, glyph, mapTransform, at, vA, styleNode));
            } else {
                //System.out.println ("Space...");
                //currentPos += emWidth*way;
            }
        }
                drawOutlines( g2, outlines, mapTransform,styleNode);
    
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
    
    
     /**
     * Draw the list of given "outlines", that is the list of characters already
     * transformed to {@code Shape} instances.We'll use for that, of course, the
     * inner {@code Fill}, {@code Halo} and {@code Stroke} instances.If they are
     * not set, a simple default {@code SolidFill} will be used.
     *
     * @param g2 The graphics we draw with
     *
     * @param outlines The list of needed outlines
     * @param mapTransform
     * @param styleNode
     * @throws ParameterException
     */
    public void drawOutlines(Graphics2D g2, ArrayList<Shape> outlines,
            MapTransform mapTransform, LineLabel styleNode) throws ParameterException {
        Halo halo = styleNode.getHalo();
        IStyleDrawer haloDrawer = getDrawer(halo);      
        if (haloDrawer != null) {
            for (Shape outline : outlines) {
                haloDrawer.setShape(outline);
                haloDrawer.draw(g2, mapTransform, halo);
            }

        }

        IFill fill = styleNode.getFill();
        Stroke stroke = styleNode.getStroke();
        IStyleDrawer strokeDrawer = getDrawer(stroke);
        IStyleDrawer fillDrawer = getDrawer(fill);

        for (Shape outline : outlines) {
            /**
             * No fill and no stroke : apply default SolidFill !
             */
            if (fillDrawer == null && strokeDrawer == null) {
                SolidFill sf = new SolidFill();
                sf.setColor(Color.BLACK);
                sf.setOpacity(1f);
                sf.setParent(styleNode);
                SolidFillDrawer drawer = new SolidFillDrawer();
                drawer.setShape(outline);
                drawer.draw(g2, mapTransform, sf);
            }
            if (fillDrawer != null) {
                fillDrawer.setShape(outline);
                fillDrawer.draw(g2, mapTransform, fill);
            }
            if (strokeDrawer != null) {
                strokeDrawer.setShape(outline);
                strokeDrawer.draw(g2, mapTransform, stroke);
            }
        }

    }
      @Override
    public IStyleDrawer getDrawer(IStyleNode styleNode) {
        if (styleNode != null) {
            IStyleDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof Halo) {
                    drawer = new HaloDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof SolidFill) {
                    drawer = new SolidFillDrawer();
                    drawerMap.put(styleNode, drawer);
                }  else if (styleNode instanceof PenStroke) {
                    drawer = new PenStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof WobbleStroke) {
                    drawer = new WobbleStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }
}
