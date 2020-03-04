/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class GraphicFillDrawer implements IGraphicDrawer<GraphicFill> {

    final static Map<Class, IGraphicDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, GraphicFill styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shape = (Shape) properties.get("shape");
        if (shape != null) {
            Paint stipple = getPaint(sp, styleNode, properties, mapTransform);
            if (stipple != null) {
                g2.setPaint(stipple);
                g2.fill(shape);
            }
        }
    }

    public Paint getPaint(JdbcSpatialTable sp, GraphicFill styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        double gX = 0.0;
        double gY = 0.0;
        Uom uom = styleNode.getUom();
        RealParameter gapX = styleNode.getGapX();
        if (gapX != null) {
            gX = gapX.getValue(properties);
            if (gX < 0.0) {
                gX = 0.0;
            }
        }
        RealParameter gapY = styleNode.getGapY();
        if (gapY != null) {
            gY = gapY.getValue(properties);
            if (gY < 0.0) {
                gY = 0.0;
            }
        }
        Graphic graphic = styleNode.getGraphic();        
        if (graphic != null) {
            if (drawerMap.containsKey(graphic.getClass())) {
                IGraphicDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                Rectangle2D bounds = graphicDrawer.getBounds(sp, mt, graphic, properties);
                gX = UomUtils.toPixel(gX, uom, mt.getDpi(), mt.getScaleDenominator(), bounds.getWidth());
                gY = UomUtils.toPixel(gY, uom, mt.getDpi(), mt.getScaleDenominator(), bounds.getHeight());
                return getPaint(sp, graphicDrawer, properties, mt, graphic, gX, gY, bounds);                
            }
        }
        
        return null;
    }
    
    public static Paint getPaint( JdbcSpatialTable sp, IGraphicDrawer graphicDrawer, Map<String,Object> properties,
            MapTransform mt, Graphic graphic, double gX, double gY, Rectangle2D bounds)
            throws ParameterException, SQLException {

        if (bounds != null) {

            Point2D.Double geoRef = new Point2D.Double(0, 0);
            Point2D ref = mt.getAffineTransform().transform(geoRef, null);

            int tWidth = (int) (bounds.getWidth() + gX);
            int tHeight = (int) (bounds.getHeight() + gY);

            int deltaX = (int) (ref.getX() - Math.ceil(ref.getX() / tWidth) * tWidth);
            int deltaY = (int) (ref.getY() - Math.ceil(ref.getY() / tHeight) * tHeight);


            BufferedImage i = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D tile = i.createGraphics();
            tile.setRenderingHints(mt.getRenderingHints());

            int ix;
            int iy;
            for (ix = 0; ix < 2; ix++) {
                for (iy = 0; iy < 2; iy++) {
                    properties.put("affinetransform", AffineTransform.getTranslateInstance(
                            -bounds.getMinX() + gX / 2.0 + deltaX + tWidth * ix,
                            -bounds.getMinY() + gY / 2.0 + deltaY + tHeight * iy));
                    graphicDrawer.draw(sp, tile,mt, graphic, properties);
                }
            }

            return new TexturePaint(i, new Rectangle2D.Double(0, 0, i.getWidth(), i.getHeight()));
        } else {
            return null;
        }
    }

    @Override
    public Rectangle2D getBounds(JdbcSpatialTable sp, MapTransform mapTransform, GraphicFill styleNode, Map<String, Object> properties) throws ParameterException {
        Graphic graphic = styleNode.getGraphic();
        if(graphic !=null){
            if (drawerMap.containsKey(graphic.getClass())) {
                IGraphicDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                return graphicDrawer.getBounds(sp, mapTransform, styleNode, properties);
            }
        }
        return null;
    }

}
