/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle.fill;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.featureStyle.IFillDrawer;
import org.orbisgis.coremap.renderer.featureStyle.utils.ShapeHelper;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class HaloDrawer implements IFillDrawer<Halo> {

    final static Map<Class, IFillDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
    }

    @Override
    public Paint getPaint(JdbcSpatialTable sp, Halo styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        return null;
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, Halo styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shape = (Shape) properties.get("shape");
        AffineTransform at = (AffineTransform) properties.get("affinetransform");
        if (shape != null) {
        IFill fill = styleNode.getFill();
        if (styleNode.getRadius() != null && fill != null) { 
            if (drawerMap.containsKey(fill.getClass())) {
                    IFillDrawer fillDrawer = drawerMap.get(fill.getClass());
            //Optimisation
            if (shape instanceof Arc2D) {
                Arc2D shp = (Arc2D)shape;
                double r = getHaloRadius(styleNode.getRadius().getValue(properties), styleNode.getUom(), mapTransform);                   
                double x = shp.getX() - r / 2;
                double y = shp.getY() - r / 2;
                double height = shp.getHeight() + r;
                double width = shp.getWidth() + r;
                Shape origin = new Arc2D.Double(x, y, width, height, shp.getAngleStart(), shp.getAngleExtent(), shp.getArcType());
                Shape halo = at.createTransformedShape(origin);
                fillHalo(sp, fillDrawer, fill, shape, halo, g2, properties, mapTransform);
            } else {                
                    double r = getHaloRadius(styleNode.getRadius().getValue(properties), styleNode.getUom(), mapTransform);
                    if (r > 0.0) {
                        for (Shape shapeHalo : ShapeHelper.perpendicularOffset(shape, r)) {
                            fillHalo(sp, fillDrawer, fill, shapeHalo, shape, g2, properties, mapTransform);
                        }
                    }
                }
            }
        }
        }
    }

    /**
     * Return the halo radius in pixel
     *
     * @param mt
     * @return
     * @throws ParameterException
     */
    public double getHaloRadius(double radius, Uom uom, MapTransform mt) throws ParameterException {
        return UomUtils.toPixel(radius, uom, mt.getDpi(), mt.getScaleDenominator(), null); // TODO 100%
    }

    private void fillHalo(JdbcSpatialTable sp, IFillDrawer fillDrawer, IFill fill, Shape halo, Shape initialShp, Graphics2D g2,
            Map<String, Object> properties, MapTransform mapTransform)
            throws ParameterException, SQLException {
        if (halo != null && initialShp != null) {
            Area initialArea = new Area(initialShp);
            Area aHalo = new Area(halo);           
            aHalo.subtract(initialArea);            
            properties.put("shape", aHalo);
            fillDrawer.draw(sp, g2, mapTransform, fill, properties);              
        } else {
            //LOGGER.error("Perpendicular offset failed");
        }
    }

}
