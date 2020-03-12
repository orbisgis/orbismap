/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class PointSymbolizerDrawer implements ISymbolizerDraw<PointSymbolizer> {
    
    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }
    private Shape shape;
    
    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
        if (shape != null) {
            Graphic graphic = symbolizer.getGraphic();
            if (graphic != null) {
                if (drawerMap.containsKey(graphic.getClass())) {
                    IStyleDrawer graphicDrawer = drawerMap.get(graphic.getClass());     
                    Rectangle2D b = shape.getBounds2D();
                    AffineTransform a = AffineTransform.getTranslateInstance(b.getX(), b.getY());
                    properties.put("affinetransform", a);
                    graphicDrawer.draw(g2, mapTransform, graphic, properties);
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
}
