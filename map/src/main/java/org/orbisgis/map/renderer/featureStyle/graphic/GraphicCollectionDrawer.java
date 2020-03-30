/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class GraphicCollectionDrawer implements IGraphicCollectionDrawer<GraphicCollection> {

    private Shape shape;
    
    private AffineTransform affineTransform;

    final static Map<Class, IGraphicDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
        drawerMap.put(GraphicFill.class, new GraphicFillDrawer());
    }

    public GraphicCollectionDrawer() {

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
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicCollection styleNode) throws ParameterException {
        for (Graphic graphic : styleNode.getGraphics()) {
            if (graphic != null) {
                if (drawerMap.containsKey(graphic.getClass())) {
                    IGraphicDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                    graphicDrawer.setAffineTransform(getAffineTransform());
                    graphicDrawer.draw(g2, mapTransform, graphic);
                }
            }
        }
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
            if (drawerMap.containsKey(g.getClass())) {
                IGraphicDrawer graphicDrawer = drawerMap.get(g.getClass());
                Rectangle2D bounds = graphicDrawer.getBounds(mapTransform, g);
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
        return  affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform=affineTransform;
    }

}
