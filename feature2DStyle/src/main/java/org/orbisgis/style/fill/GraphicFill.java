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
package org.orbisgis.style.fill;


import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;

/**
 * A "GraphicFill" defines repeated-graphic filling (stippling) pattern for an area geometry.
 * It is defined with a GraphicCollection (that will be used to draw the filling symbol), a Uom,
 * and a gap vector. The gap vector is represented by its two (X and Y) coordinates,
 * stored as <code>RealParameter</code> instances.
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class GraphicFill extends Fill {

    private GraphicCollection graphic;
    /**
     * Distance between two graphics in the fill, in X direction.
     */
    private RealParameter gapX;
    /**
     * Distance between two graphics in the fill, in Y direction.
     */
    private RealParameter gapY;

    /**
     * Creates a new GraphicFill, with the gap's measures set to null.
     */
    public GraphicFill() {
        this.setGapX(null);
        this.setGapY(null);
    }

    
    
    /**
     * Set the GraphicCollection embedded in this GraphicFill. This is set as the parent of <code>graphic</code>
     * @return 
     */
    public void setGraphic(GraphicCollection graphic) {
        this.graphic = graphic;
        graphic.setParent(this);
    }

    /**
     * Get the GraphicCollection embedded in this GraphicFill.
     * @return 
     */
    public GraphicCollection getGraphic() {
        return graphic;
    }

    /**
     * Set the gap, upon X direction, between two symbols.
     * @param gap 
     */
    public void setGapX(RealParameter gap) {
        gapX = gap;
        if (gap != null) {
            gap.setContext(RealParameterContext.NON_NEGATIVE_CONTEXT);
            gap.setParent(this);
        }
    }

    /**
     * Set the gap, upon Y direction, between two symbols.
     * @param gap 
     */
    public void setGapY(RealParameter gap) {
        gapY = gap;
        if (gap != null) {
            gap.setContext(RealParameterContext.NON_NEGATIVE_CONTEXT);
            gap.setParent(this);
        }
    }


    /**
     * Get the gap, upon X direction, between two symbols.
     * @param gap 
     */
    public RealParameter getGapX() {
        return gapX;
    }

    /**
     * Get the gap, upon Y direction, between two symbols.
     * @param gap 
     */
    public RealParameter getGapY() {
        return gapY;
    }

    /**
     * see Fill
     */
    @Override
    public void draw(Graphics2D g2, Map<String,Object> map, Shape shp, IMapTransform mt) throws ParameterException, IOException {
        Paint stipple = this.getPaint(map, mt);

        // TODO handle selected ! 
        if (stipple != null) {
            g2.setPaint(stipple);
            g2.fill(shp);
        }
    }

    /**
     * Create a new TexturePaint according to this GraphicFill
     * 
     * @param ds DataSet
     * @param fid feature id
     * @return a TexturePain ready to be used
     * @throws ParameterException
     * @throws IOException
     */
    @Override
    public Paint getPaint(Map<String,Object> map, IMapTransform mt) throws ParameterException, IOException {
        double gX = 0.0;
        double gY = 0.0;

        if (gapX != null) {
            gX = gapX.getValue(map);
            if (gX < 0.0) {
                gX = 0.0;
            }
        }

        if (gapY != null) {
            gY = gapY.getValue(map);
            if (gY < 0.0) {
                gY = 0.0;
            }
        }

        Rectangle2D bounds = graphic.getBounds(map, mt);
        gX = UomUtils.toPixel(gX, getUom(), mt.getDpi(), mt.getScaleDenominator(), bounds.getWidth());
        gY = UomUtils.toPixel(gY, getUom(), mt.getDpi(), mt.getScaleDenominator(), bounds.getHeight());

        return getPaint(map, mt, graphic, gX, gY, bounds);
    }

    public static Paint getPaint(Map<String,Object> map,
            IMapTransform mt, GraphicCollection graphic, double gX, double gY, Rectangle2D bounds)
            throws ParameterException, IOException {

        if (bounds != null) {

            Point2D.Double geoRef = new Point2D.Double(0, 0);
            Point2D ref = mt.getAffineTransform().transform(geoRef, null);

            int tWidth = (int) (bounds.getWidth() + gX);
            int tHeight = (int) (bounds.getHeight() + gY);

            int deltaX = (int) (ref.getX() - Math.ceil(ref.getX() / tWidth) * tWidth);
            int deltaY = (int) (ref.getY() - Math.ceil(ref.getY() / tHeight) * tHeight);


            BufferedImage i = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D tile = i.createGraphics();
            tile.setRenderingHints(mt.getRenderingHints());

            int ix;
            int iy;
            for (ix = 0; ix < 2; ix++) {
                for (iy = 0; iy < 2; iy++) {
                    graphic.draw(tile, map, mt,
                            AffineTransform.getTranslateInstance(
                            -bounds.getMinX() + gX / 2.0 + deltaX + tWidth * ix,
                            -bounds.getMinY() + gY / 2.0 + deltaY + tHeight * iy));
                }
            }

            return new TexturePaint(i, new Rectangle2D.Double(0, 0, i.getWidth(), i.getHeight()));
        } else {
            return null;
        }

    }

    

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphic != null) {
            ls.add(graphic);
        }
        if (gapX != null) {
            ls.add(gapX);
        }
        if (gapY != null) {
            ls.add(gapY);
        }
        return ls;
    }

   
}
