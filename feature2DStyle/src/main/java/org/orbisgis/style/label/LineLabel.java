/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.style.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;

/**
 * A {@code LineLabel} is a text of some kinf associated to a Line (polygon or not).
 * @author Alexis Guéganno, Maxence Laurent
 * @todo implements
 */
public class LineLabel extends Label {

    private RelativeOrientation orientation;

    /**
         * Build a new default {@code LineLabel}, using the defaults in 
         * {@link org.orbisgis.coremap.renderer.se.label.StyledText#StyledText()  StyledText}.
         * The label will be centered (horizontally), and in the middle (vertically)
         * of the graphic.
         */
    public LineLabel() {
        super();
        setVerticalAlign(VerticalAlignment.MIDDLE);
        setHorizontalAlign(HorizontalAlignment.CENTER);
    }

    

    

    /**
     * Gets the orientation of the characters along the line.
     */
    public final RelativeOrientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of the characters along the line.
     * @param orientation
     */
    public final void setOrientation(RelativeOrientation orientation) {
        this.orientation = orientation;
    }

    
    @Override
    public void draw(Graphics2D g2, Map<String,Object> map,
            Shape shp, IMapTransform mt)
            throws ParameterException, IOException {

        Rectangle2D bounds = getLabel().getBounds(g2, map, mt);
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

        VerticalAlignment vA = getVerticalAlign();
        HorizontalAlignment hA = getHorizontalAlign();
        RelativeOrientation ra = getOrientation();

        if (vA == null) {
            vA = VerticalAlignment.TOP;
            //The four important lines, here, according to the SE norm, are the
            //middle line, the baseline, the ascent line and the descent line.
        }
        if (hA == null) {
            hA = HorizontalAlignment.CENTER;
        }
        if(ra == null) {
            ra= RelativeOrientation.NORMAL_UP;
        }
        double lineLength = ShapeHelper.getLineLength(shp);
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
        if (stopAt > lineLength){
            stopAt = lineLength;
        }
        Point2D.Double ptStart = ShapeHelper.getPointAt(shp, startAt);
        Point2D.Double ptStop = ShapeHelper.getPointAt(shp, stopAt);
        int way = 1;
        // Do not laid out the label upside-down !
                if (ptStart.x > ptStop.x){
                // invert line way
                way = -1;
                startAt = stopAt;
                }

        double currentPos = startAt;
        double glyphWidth;

        String text = getLabel().getText().getValue(map);
        String[] glyphs = text.split("");

        ArrayList<Shape> outlines = new ArrayList<Shape>();

        for (String glyph : glyphs) {
            if (glyph != null && !glyph.isEmpty()) {
                Rectangle2D gBounds = getLabel().getBounds(g2, glyph, map, mt);

                glyphWidth = gBounds.getWidth()*way;
                Point2D.Double pAt = ShapeHelper.getPointAt(shp, currentPos);
                Point2D.Double pAfter = ShapeHelper.getPointAt(shp, currentPos + glyphWidth);
                //We compute the angle we must use to rotate our glyph.
                double theta = Math.atan2(pAfter.y - pAt.y, pAfter.x - pAt.x);
                //We compute the place where we will draw the chatacter, and
                //the orientation it must have.
                AffineTransform at = AffineTransform.getTranslateInstance(pAt.x, pAt.y);
                at.concatenate(AffineTransform.getRotateInstance(theta));
                currentPos += glyphWidth;
                outlines.add(getLabel().getOutline(g2, glyph, map, mt, at, vA));
            } else {
                //System.out.println ("Space...");
                //currentPos += emWidth*way;
            }
        }
        getLabel().drawOutlines(g2, outlines, map, mt);
    }

   
    

        @Override
        public List<IStyleNode> getChildren() {
                List<IStyleNode> ls = new ArrayList<IStyleNode>();
                if (getLabel() != null) {
                        ls.add(getLabel());
                }
                return ls;
        }    
}