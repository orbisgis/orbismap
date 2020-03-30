/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.GraphicStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class GraphicStrokeDrawer implements IStrokeDrawer<GraphicStroke> {

    final static Map<Class, IGraphicDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicStroke styleNode) throws ParameterException {
        GraphicCollection graphics = styleNode.getGraphics();
        Uom uom = styleNode.getUom();
        if (graphics != null) {
            RelativeOrientation rOrient = styleNode.getRelativeOrientation();
            if(rOrient==null){
                rOrient = RelativeOrientation.PORTRAYAL;
            }
            Float distance = (Float) styleNode.getDistance().getValue();
            if (distance != null && Math.abs(distance) > 0) {
                MarkGraphicStroke markGraphicStroke = new MarkGraphicStroke(graphics, mapTransform,
                        UomUtils.toPixel(distance, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator()));
                markGraphicStroke.draw(shape, g2, rOrient);
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

    public class MarkGraphicStroke {

        private ArrayList<Shape> shapes;
        private ArrayList<MarkGraphic> markGraphics;
        private float advance;
        private boolean repeat = true;
        private AffineTransform t = new AffineTransform();
        private static final float FLATNESS = 1;
        private MarkGraphicDrawer drawer;
        private final MapTransform mapTransform;

        public MarkGraphicStroke(GraphicCollection graphics, MapTransform mapTransform,float advance) throws ParameterException {
            this.mapTransform = mapTransform;
            this.advance = advance;
            this.shapes = new ArrayList<>();
            this.markGraphics = new ArrayList<>();
            for (int i = 0; i < graphics.getNumGraphics(); i++) {
                Graphic graphic = graphics.get(i);
                if (drawerMap.containsKey(graphic.getClass())) {
                    drawer = (MarkGraphicDrawer) drawerMap.get(graphic.getClass());
                    Shape currentShape = drawer.getShape((MarkGraphic) graphic, mapTransform);
                    this.shapes.add(currentShape);
                    this.markGraphics.add((MarkGraphic) graphic);
                }
            }
        }

        public void draw(Shape shape, Graphics2D g2, RelativeOrientation rOrient) throws ParameterException {
            GeneralPath result = new GeneralPath();
            PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
            float points[] = new float[6];
            float moveX = 0, moveY = 0;
            float lastX = 0, lastY = 0;
            float thisX = 0, thisY = 0;
            int type = 0;
            boolean first = false;
            float next = 0;
            int currentShape = 0;
            int length = shapes.size();

            while (currentShape < length && !it.isDone()) {
                type = it.currentSegment(points);
                switch (type) {
                    case PathIterator.SEG_MOVETO:
                        moveX = lastX = points[0];
                        moveY = lastY = points[1];
                        result.moveTo(moveX, moveY);
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
                            while (currentShape < length && distance >= next) {
                                float x = lastX + next * dx * r;
                                float y = lastY + next * dy * r;
                                t.setToTranslation(x, y);    
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
                                //t.rotate(angle);
                                drawer.setAffineTransform(t);
                                drawer.draw(g2, mapTransform, markGraphics.get(currentShape));
                                next += advance;
                                currentShape++;
                                if (repeat) {
                                    currentShape %= length;
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

    }

}
