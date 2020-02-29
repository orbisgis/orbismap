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
package org.orbisgis.style.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.api.IMapTransform;
import org.slf4j.*;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.fill.Fill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;


/**
 * A {@code Halo} is a type of {@code Fill} that is applied to the background of font glyphs.
 * It is mainly used to improve the readability of text labels on the map.
 * @author Alexis Guéganno
 */
public final class Halo extends StyleNode implements  IUom, FillNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(Halo.class);
        /**
         * The default radius for new {@code Halo} instances. Set to 1.0, and UOM dependant.
         */
    public static final double DEFAULT_RADIUS = 1.0;

    private Uom uom;
    private RealParameter radius;
    private Fill fill;
    
    /**
     * Build a new default {@code Halo}, with a solid fill and a radius set to {@code DEFAULT_RADIUS}
     */
    public Halo() {
        setFill(getDefaultFill());
        setRadius(new RealLiteral(DEFAULT_RADIUS));
    }

    /**
     * Build a new {@code Halo} with the given {@code Fill} and a radius set to {@code radius}
     * @param fill
     * @param radius 
     */
    public Halo(Fill fill, RealParameter radius) {
        setFill(fill);
        setRadius(radius);
    }

    
    @Override
    public Uom getUom() {
        if (uom == null) {
            return ((IUom)getParent()).getUom();
        } else {
            return uom;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

   

    @Override
    public void setFill(Fill fill) {
        this.fill = fill == null ? getDefaultFill() : fill;
        this.fill.setParent(this);
    }

    @Override
    public Fill getFill() {
        return fill;
    }

    /**
     * Get the radius of this {@code Halo}.
     * @return 
     * The radius of this {@code Halo} as a {@code RealParameter}.
     */
    public RealParameter getRadius() {
        return radius;
    }

    /**
     * Set the radius of this {@code Halo}.
     * @param radius 
     */
    public void setRadius(RealParameter radius) {
        if (radius != null) {
            this.radius = radius;
            this.radius.setContext(RealParameterContext.REAL_CONTEXT);
        } else {
            this.radius = new RealLiteral(DEFAULT_RADIUS);
        }
        this.radius.setParent(this);
    }

    /**
     * Return the halo radius in pixel
     * @param mt
     * @return
     * @throws ParameterException
     */
    public double getHaloRadius(Map<String,Object> map, IMapTransform mt) throws ParameterException {
        return UomUtils.toPixel(radius.getValue(map), getUom(), mt.getDpi(), mt.getScaleDenominator(), null); // TODO 100%
    }

    /**
     * Draw this {@code Halo} in {@code g2}. Basically compute an offseted shape
     * and fill the difference with the original one.
     * @param g2
     * @param shp
     * @param mt
     * @param substract
     * @throws ParameterException
     * @throws IOException
     */
    public void draw(Graphics2D g2, Map<String,Object> map,
            Shape shp, IMapTransform mt, boolean substract) throws ParameterException, IOException {
        if (radius != null && fill != null) {
            double r = this.getHaloRadius(map, mt);
            if (r > 0.0) {
                for (Shape halo : ShapeHelper.perpendicularOffset(shp, r)) {
                    fillHalo(halo, shp, g2, map, mt, substract);
                }
            }
        }
    }
    
    /**
     * In order to improve performance when drawing a halo on a Circle, we use a
     * dedicated method, where we won't compute all the offseted point of the
     * original shape. It's faster to compute directly a new Shape.</p>
     * <p>To achieve this goal, we use the original shape, that is supposed to be
     * an {@code Arc2D}. We compute the new {@code Arc2D} by adding the wanted
     * radius, and finally apply the current AffineTransform obtained from the
     * MapTransform.
     * @param g2
     * The {@code Graphics} where we are going to draw.
     * Our DataSet
     * The index of the current feature in sds.
     * @param shp
     * The original Shape
     * @param atShp
     * The shape obtained by applying mt to shp
     * @param mt
     * The current {@code MapTransform}.
     * @param substract
     * @param viewBox
     * @param at
     * @throws ParameterException
     * @throws IOException 
     */
    public void drawCircle(Graphics2D g2, Map<String,Object> map,
            Arc2D shp, Shape atShp, IMapTransform mt, boolean substract, 
            ViewBox viewBox, AffineTransform at) throws ParameterException, IOException {
        //We want to make a halo around a WKN.CIRCLE instance. 
        if (radius != null && fill != null) {
            double r = this.getHaloRadius(map, mt);
            double x = shp.getX() - r/2;
            double y = shp.getY() - r/2;
            double height = shp.getHeight() + r;
            double width = shp.getWidth() + r;
            Shape origin = new Arc2D.Double(x, y, width, height, shp.getAngleStart(), shp.getAngleExtent(), shp.getArcType());
            Shape halo = at.createTransformedShape(origin);
            fillHalo(halo, atShp, g2, map, mt, substract);
            
        }
    }
    
    private void fillHalo(Shape halo, Shape initialShp, Graphics2D g2, 
                Map<String,Object> map,IMapTransform mt, boolean substract)
                throws ParameterException, IOException {
        if (halo != null && initialShp != null) {
            Area initialArea = new Area(initialShp);
            Area aHalo = new Area(halo);
            if (substract){
                aHalo.subtract(initialArea);
            }
            fill.draw(g2, map, aHalo, mt);
        } else {
            LOGGER.error("Perpendicular offset failed");
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
            List<IStyleNode> ls = new ArrayList<IStyleNode>();
            ls.add(radius);
            ls.add(fill);
            return ls;
    }
   

    /**
     * Default fill for the halo must be white and 100% opaque.
     * @return
     */
    private Fill getDefaultFill() {
            return new SolidFill(Color.WHITE, 1);
    }

    @Override
    public void setUom(org.orbisgis.style.Uom uom) {
        this.uom = uom;
    }

}