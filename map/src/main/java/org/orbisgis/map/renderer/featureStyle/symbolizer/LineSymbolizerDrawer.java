/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author ebocher
 */
public class LineSymbolizerDrawer implements ISymbolizerDraw<LineSymbolizer> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }
    private Shape shape;    
    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, LineSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {

        Stroke stroke = symbolizer.getStroke();
        if (stroke != null) {
            Uom uom = symbolizer.getUom();
            Shape shp = getShape();
            double offset = 0.0;
            /*ExpressionParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
            if (perpendicularOffset != null) {
                offset = UomUtils.toPixel(perpendicularOffset.getValue(rs, fid),
                        getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
            }*/
            if (shp != null) {
                properties.put("offset", offset);
                if (drawerMap.containsKey(stroke.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(stroke.getClass());
                    drawer.setShape(shape);
                    drawer.draw( g2, mapTransform, stroke, properties);
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
        this.bi=bufferedImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
     return bi;
    }

    @Override
    public void setGraphics2D(Graphics2D g2) {
        this.g2_bi=g2;
   }

    @Override
    public Graphics2D getGraphics2D() {
        return g2_bi;
    }

    @Override
    public void dispose(Graphics2D g2) {
        g2_bi.dispose();      
        g2_bi=null;
        g2.drawImage(bi, null, null);
    }
}
