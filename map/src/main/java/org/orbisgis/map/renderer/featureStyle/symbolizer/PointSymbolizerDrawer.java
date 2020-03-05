/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.utils.GeometryUtils;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
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

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, PointSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
        Geometry geom = sp.getGeometry(symbolizer.getGeometryParameter().getIdentifier());
        Graphic graphic = symbolizer.getGraphic();
        if (graphic != null) {
                if(drawerMap.containsKey(graphic.getClass())){
                    IStyleDrawer graphicDrawer = drawerMap.get(graphic.getClass());                
                double x,y;
                if (symbolizer.isOnVertex()) {
                    List<Point2D> points = GeometryUtils.getPoints(mapTransform, geom);
                    for (Point2D pt : points) {
                    x = pt.getX();
                    y = pt.getY();
                    properties.put("affinetransform", AffineTransform.getTranslateInstance(x, y));
                    graphicDrawer.draw(sp, g2, mapTransform, symbolizer, properties);
                    }
                } else {
                    Point2D pt = GeometryUtils.getInteriorPoint(mapTransform, geom);
                    x = pt.getX();
                    y = pt.getY();
                    // Draw the graphic right over the point !
                    properties.put("affinetransform", AffineTransform.getTranslateInstance(x, y));
                    graphicDrawer.draw(sp, g2, mapTransform, graphic, properties);
                }
                }
        }
    }
}
