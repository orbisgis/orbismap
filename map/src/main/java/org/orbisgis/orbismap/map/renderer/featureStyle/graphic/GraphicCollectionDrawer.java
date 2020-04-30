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
package org.orbisgis.orbismap.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.orbismap.style.graphic.Graphic;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.graphic.PointTextGraphic;
import org.orbisgis.orbismap.style.parameter.ParameterException;

/**
 * Drawer for the element <code>GraphicCollection</code>
 * 
 * @author Erwan Bocher, CNRS (2020)
 */
public class GraphicCollectionDrawer extends AbstractDrawerFinder<IGraphicDrawer, Graphic> implements IGraphicCollectionDrawer<GraphicCollection> {

    private Shape shape;

    private AffineTransform affineTransform;
    

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicCollection styleNode) throws ParameterException {
        for (Graphic graphic : styleNode.getGraphics()) {
            IGraphicDrawer drawer = getDrawer(graphic);
            drawer.setAffineTransform(getAffineTransform());
            drawer.draw(g2, mapTransform, graphic);
        }
    }

    
    @Override
    public IGraphicDrawer getDrawer(Graphic graphic) {
        if (graphic != null) {
            IGraphicDrawer graphicDrawer = drawerMap.get(graphic);
            if (graphicDrawer == null) {
                if (graphic instanceof MarkGraphic) {
                    graphicDrawer = new MarkGraphicDrawer();
                    drawerMap.put(graphic, graphicDrawer);
                } else if (graphic instanceof PointTextGraphic) {
                    graphicDrawer = new PointTextGraphicDrawer();
                    drawerMap.put(graphic, graphicDrawer);
                }
            }
            return graphicDrawer;
        }
        return null;
    }

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, GraphicCollection styleNode) throws ParameterException {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        // First, retrieve all graphics composing the collection
        // and fetch the min/max x, y values
        Iterator<Graphic> it = styleNode.getGraphics().iterator();
        while (it.hasNext()) {
            Graphic g = it.next();
            Rectangle2D bounds = getDrawer(g).getShape(mapTransform, g).getBounds2D();
            if (bounds != null) {
                double mX = bounds.getMinX();
                double w = bounds.getWidth();
                double mY = bounds.getMinY();
                double h = bounds.getHeight();

                if (mX < xmin) {
                    xmin = mX;
                }
                if (mY < ymin) {
                    ymin = mY;
                }
                if (mX + w > xmax) {
                    xmax = mX + w;
                }
                if (mY + h > ymax) {
                    ymax = mY + h;
                }
            }
        }
        double width = xmax - xmin;
        double height = ymax - ymin;

        if (width > 0 && height > 0) {
            return new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
        } else {
            return null;
        }
    }

    @Override
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }
    
   

}
