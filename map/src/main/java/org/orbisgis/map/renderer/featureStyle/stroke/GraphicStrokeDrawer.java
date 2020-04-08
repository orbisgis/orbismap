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
package org.orbisgis.map.renderer.featureStyle.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.PointTextGraphicDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.label.RelativeOrientation;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.PointTextGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.GraphicStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 * Drawer for the element <code>GraphicStroke</code>
 * 
 * @author Erwan Bocher, CNRS (2020)
 */
public class GraphicStrokeDrawer implements IStrokeDrawer<GraphicStroke> {

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicStroke styleNode) throws ParameterException {
        GraphicCollection graphics = styleNode.getGraphics();
        Uom uom = styleNode.getUom();
        if (graphics != null) {
            RelativeOrientation rOrient = styleNode.getRelativeOrientation();
            if (rOrient == null) {
                rOrient = RelativeOrientation.PORTRAYAL;
            }
            Float distance = (Float) styleNode.getDistance().getValue();
            boolean isOverlap = (boolean) styleNode.isOverlap().getValue();
            if (distance != null && Math.abs(distance) > 0) {
                MarkGraphicStroke markGraphicStroke = new MarkGraphicStroke(graphics, mapTransform,
                        UomUtils.toPixel(distance, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator()));
                markGraphicStroke.draw(shape, g2, rOrient, isOverlap);
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

    @Override
    public Double getNaturalLength(GraphicStroke stroke, MapTransform mapTransform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    public class MarkGraphicStroke {

        public Map<Graphic, IGraphicDrawer> drawerMap;
        private float advance;
        private boolean repeat = true;
        private AffineTransform t = new AffineTransform();
        private static final float FLATNESS = 1;
        private final MapTransform mapTransform;
        private final GraphicCollection graphics;

        public MarkGraphicStroke(GraphicCollection graphics, MapTransform mapTransform, float advance) throws ParameterException {
            this.mapTransform = mapTransform;
            this.advance = advance;
            this.graphics = graphics;
            initDrawer(graphics);
        }

        /**
         * Draw the graphics along the shape
         *
         * @param shape
         * @param g2
         * @param rOrient
         * @param isOverlap
         * @throws ParameterException
         */
        public void draw(Shape shape, Graphics2D g2, RelativeOrientation rOrient, boolean isOverlap) throws ParameterException {
            PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
            float points[] = new float[6];
            float moveX = 0, moveY = 0;
            float lastX = 0, lastY = 0;
            float thisX = 0, thisY = 0;
            int type = 0;
            boolean first = false;
            float next = 0;
            int numbGraphics = drawerMap.size();

            while (!it.isDone()) {
                type = it.currentSegment(points);
                switch (type) {
                    case PathIterator.SEG_MOVETO:
                        moveX = lastX = points[0];
                        moveY = lastY = points[1];
                        first = true;
                        next = 0;
                        break;

                    case PathIterator.SEG_CLOSE:
                        points[0] = moveX;
                        points[1] = moveY;
                    // Fall into....

                    case PathIterator.SEG_LINETO:
                        thisX = points[0];
                        thisY = points[1];
                        float dx = thisX - lastX;
                        float dy = thisY - lastY;
                        float distance = (float) Math.sqrt(dx * dx + dy * dy);
                        if (distance >= next) {
                            float r = 1.0f / distance;
                            float angle = (float) Math.atan2(dy, dx);
                            while (distance >= next) {
                                float x = lastX + next * dx * r;
                                float y = lastY + next * dy * r;
                                t.setToTranslation(x, y);
                                buildGraphicOrientation(t, angle, rOrient);
                                if (isOverlap) {
                                    double graphicWidth = advance ;
                                    for (Map.Entry<Graphic, IGraphicDrawer> entry : drawerMap.entrySet()) {
                                        Graphic graphic = entry.getKey();
                                        IGraphicDrawer drawer = entry.getValue();
                                        double currentWidth = getGraphicWidth(drawer, graphic, rOrient, mapTransform);
                                        if(currentWidth>graphicWidth){
                                            graphicWidth=currentWidth;
                                        }
                                        drawer.setAffineTransform(t);
                                        drawer.draw(g2, mapTransform, graphic);
                                    }
                                    next += advance + graphicWidth;
                                } else {
                                    int counter = 1;                                    
                                    for (Map.Entry<Graphic, IGraphicDrawer> entry : drawerMap.entrySet()) {
                                        Graphic graphic = entry.getKey();
                                        IGraphicDrawer drawer = entry.getValue();
                                        double graphicWidth = getGraphicWidth(drawer, graphic, rOrient, mapTransform);
                                        if (counter == 1) {
                                            drawer.setAffineTransform(t);
                                            drawer.draw(g2, mapTransform, graphic);
                                            next += advance + graphicWidth;
                                        } else if (counter < numbGraphics) {
                                            //Move the next graphic
                                            x = lastX + next * dx * r;
                                            y = lastY + next * dy * r;
                                            t.setToTranslation(x, y);
                                            buildGraphicOrientation(t, angle, rOrient);
                                            drawer.setAffineTransform(t);
                                            drawer.draw(g2, mapTransform, graphic);
                                            next += advance + graphicWidth;
                                        }
                                        else{
                                            //Move the last graphic
                                            x = lastX + next * dx * r;
                                            y = lastY + next * dy * r;
                                            t.setToTranslation(x, y);
                                            buildGraphicOrientation(t, angle, rOrient);
                                            drawer.setAffineTransform(t);
                                            drawer.draw(g2, mapTransform, graphic);
                                            next += advance*2;
                                        }
                                        counter++;
                                    }
                                }
                            }
                        }
                        next -= distance;
                        first = false;
                        lastX = thisX;
                        lastY = thisY;
                        break;
                }
                it.next();
            }
        }

        public void buildGraphicOrientation(AffineTransform t, float angle, RelativeOrientation rOrient) {
            if (rOrient != RelativeOrientation.PORTRAYAL) {
                switch (rOrient) {
                    case LINE:
                        angle += 0.5 * Math.PI;
                        break;
                    case NORMAL_UP:
                        if (angle < -Math.PI / 2 || angle > Math.PI / 2) {
                            angle += Math.PI;
                        }
                        break;
                    //TODO : Implements
                    /*case NORMAL:
                                            
                                            if (angle < -Math.PI / 2 || angle > Math.PI / 2) {
                                                angle += Math.PI;
                                            }
                                            break;*/
                }
                t.concatenate(AffineTransform.getRotateInstance(angle));
            }
        }

        /**
         * Find the drawer
         *
         * @param graphics
         */
        public void initDrawer(GraphicCollection graphics) {
            drawerMap = new LinkedHashMap<>();
            for (int i = 0; i < graphics.getNumGraphics(); i++) {
                Graphic graphic = graphics.get(i);
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
                }
            }
        }

        /**
         * Graphic width
         *
         * @param drawer
         * @param rOrient
         * @return
         * @throws ParameterException
         * @throws IOException
         */
        private double getGraphicWidth(IGraphicDrawer drawer, Graphic graphic, RelativeOrientation rOrient, MapTransform mapTransform) throws ParameterException {
            Rectangle2D bounds = drawer.getShape(mapTransform, graphic).getBounds2D();
            double gWidth = bounds.getWidth();
            double gHeight = bounds.getHeight();

            switch (rOrient) {
                case LINE:
                    return gHeight;
                case NORMAL:
                case NORMAL_UP:
                case PORTRAYAL:
                default:
                    return gWidth;
            }
        }

    }

}
