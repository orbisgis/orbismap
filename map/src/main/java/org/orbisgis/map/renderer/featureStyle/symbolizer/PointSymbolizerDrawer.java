/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.graphic.GraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.shape.PointsShape;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class PointSymbolizerDrawer implements ISymbolizerDraw<PointSymbolizer> {

    final static Map<Class, IGraphicCollectionDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(GraphicCollection.class, new GraphicCollectionDrawer());
    }
    private Shape shape;

    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointSymbolizer symbolizer) throws ParameterException {
            GraphicCollection graphic = symbolizer.getGraphics();
            if (graphic != null) {
                if (drawerMap.containsKey(graphic.getClass())) {
                    IGraphicCollectionDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                    if (shape instanceof PointsShape) {
                        PointsShape shapes = (PointsShape) getShape();
                        for (int i = 0; i < shapes.size(); i++) {
                            Rectangle2D b = shapes.get(i).getBounds2D();
                            graphicDrawer.setAffineTransform( AffineTransform.getTranslateInstance(b.getX(), b.getY()));                        
                            graphicDrawer.draw(g2, mapTransform, graphic);
                        }
                    } else {
                        Rectangle2D b = getShape().getBounds2D();
                        graphicDrawer.setAffineTransform(AffineTransform.getTranslateInstance(b.getX(), b.getY()));
                        graphicDrawer.draw(g2, mapTransform, graphic);
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

    @Override
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bi = bufferedImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bi;
    }

    @Override
    public void setGraphics2D(Graphics2D g2) {
        this.g2_bi = g2;
    }

    @Override
    public Graphics2D getGraphics2D() {
        return g2_bi;
    }

    @Override
    public void dispose(Graphics2D g2) {
        if (g2 != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, null, null);
            bi = null;
        }
    }

}
