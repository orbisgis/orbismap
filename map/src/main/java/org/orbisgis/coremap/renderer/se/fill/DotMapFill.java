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
package org.orbisgis.coremap.renderer.se.fill;

import org.slf4j.*;

import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.GraphicNode;
import org.orbisgis.coremap.renderer.se.graphic.GraphicCollection;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameterContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.orbisgis.style.IStyleNode;

/**
 * Descriptor for dot maps. Each point represents a given quantity. Points are randomly placed
 * in the polygon that contains them.<br/>
 * A DotMapFill is defined with three things : <br/>
 *   * The quantity represented by a single dot<br/>
 *   * The total quantity to represent<br/>
 *   * The symbol associated to each single dot.
 * @author Alexis Guéganno
 */
public final class DotMapFill extends Fill implements GraphicNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(DotMapFill.class);
    
    static final int MAX_ATTEMPT = 100;

    private GraphicCollection mark;
    private RealParameter quantityPerMark;
    private RealParameter totalQuantity;
    private Random rand;

    /**
     * Creates a new DotMapFill, with uninstanciated values.
     */
    public DotMapFill() {
        rand = new Random();
    }

    
    @Override
    public void setGraphicCollection(GraphicCollection mark) {
        if (mark != null) {
            this.mark = mark;
            mark.setParent(this);
        }
    }

    @Override
    public GraphicCollection getGraphicCollection() {
        return mark;
    }

    /**
     * Set the quantity represented by a single dot.
     * @param quantityPerMark 
     */
    public void setQuantityPerMark(RealParameter quantityPerMark) {
        if (quantityPerMark != null) {
            this.quantityPerMark = quantityPerMark;
            this.quantityPerMark.setContext(RealParameterContext.REAL_CONTEXT);
            this.quantityPerMark.setParent(this);
        }
    }

    /**
     * Get the quantity represented by a single dot.
     * @return The quantity represented by a single dot
     */
    public RealParameter getQantityPerMark() {
        return quantityPerMark;
    }

    /**
     * Get the total quantity to be represented for this symbolizer.
     * @param totalQuantity 
     */
    public void setTotalQuantity(RealParameter totalQuantity) {
        if (totalQuantity != null) {
            this.totalQuantity = totalQuantity;
            this.totalQuantity.setContext(RealParameterContext.REAL_CONTEXT);
            this.totalQuantity.setParent(this);
        }
    }

    /**
     * Set the total quantity to be represented for this symbolizer.
     * @return 
     */
    public RealParameter getTotalQantity() {
        return totalQuantity;
    }

    /**
     * Return null since an hatched fill cannot be converted into a native java fill
     * @param map
     * @param selected
     * @param mt
     * @return null
     * @throws ParameterException
     */
    @Override
    public Paint getPaint(Map<String,Object> map,
            boolean selected, MapTransform mt) throws ParameterException {
        return null;
    }

    @Override
    public void draw(Graphics2D g2, Map<String,Object> map, Shape shp, boolean selected, MapTransform mt)
            throws ParameterException, IOException {

        //RenderedImage m = this.mark.getGraphic(map, selected, mt).createRendering(mt.getCurrentRenderContext());


        Double perMark = null;
        if (quantityPerMark != null) {
            perMark = this.quantityPerMark.getValue(map);
        }

        Double total = null;
        if (totalQuantity != null) {
            total = this.totalQuantity.getValue(map);
        }

        if (perMark == null || total == null) {
            throw new ParameterException("Dot Map Fill: missing parameters !!!");
        }

        int nb = (int) Math.round(total / perMark);

        //Area area = new Area(shapes.get(0));
        Area area = new Area(shp);

        // setting the seed to the scale denom will ensure that mark will not move when panning
        rand.setSeed((long) mt.getScaleDenominator());
        for (int i = 0; i < nb; i++) {
            Point2D.Double pos = findMarkPosition(area);
            if (pos != null) {
                mark.draw(g2, map, selected, mt, AffineTransform.getTranslateInstance(pos.x, pos.y));
            } else {
                LOGGER.error("Could not find position for mark within area");
            }
        }
    }

    /**
     * Ugly version to find a random point which stand within the area
     * @param area
     * @return
     */
    private Point2D.Double findMarkPosition(Area area) {
        Rectangle2D bounds2D = area.getBounds2D();

        for (int i = 0; i < MAX_ATTEMPT; i++) {
            double x = rand.nextDouble() * bounds2D.getWidth() + bounds2D.getMinX();
            double y = rand.nextDouble() * bounds2D.getHeight() + bounds2D.getMinY();

            if (area.contains(x, y)) {
                return new Point2D.Double(x, y);
            }
        }
        return null;
    }

    

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (mark != null) {
            ls.add(mark);
        }
        if (quantityPerMark != null) {
            ls.add(quantityPerMark);
        }
        if (totalQuantity != null) {
            ls.add(totalQuantity);
        }
        return ls;
    }

    
}
