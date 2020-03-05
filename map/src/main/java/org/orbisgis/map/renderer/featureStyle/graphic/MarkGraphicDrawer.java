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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.transform.TransformBuilder;
import org.orbisgis.map.renderer.featureStyle.utils.WellKnownNameUtils;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.IFill;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.WellKnownName;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author ebocher
 */
public class MarkGraphicDrawer implements IGraphicDrawer<MarkGraphic> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(Halo.class, new HaloDrawer());
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }
    // cached shape : only available with shape that doesn't depends on features
    private Shape shape;

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, MarkGraphic styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shp;
        shape = (Shape) properties.get("shape");
        if (shape == null) {
            shp = getShape(properties, mapTransform);
        } else {
            shp = shape;

        }
        if (shp == null) {
            ExpressionParameter wknEwpression = styleNode.getWkn();
            WellKnownName wknValue = WellKnownName.CIRCLE;
            if (wknEwpression != null) {
                Object value = sp.getObject(wknEwpression.getIdentifier());
                if (value instanceof Geometry) {
                    Geometry geom = (Geometry) value;
                    shp = WellKnownNameUtils.getShape(mapTransform.getShape(geom, false), styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
                            mapTransform.getDpi());
                } else if (value instanceof String) {
                    WellKnownName wknSupported = WellKnownName.fromString((String) value);
                    if (wknSupported == null) {
                        shp = WellKnownNameUtils.getShape(wknSupported, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
                                mapTransform.getDpi());
                    }
                }

            } else {
                shp = WellKnownNameUtils.getShape(wknValue, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
                        mapTransform.getDpi());
            }
        }
        if (shp != null) {
            AffineTransform at = new AffineTransform((AffineTransform) properties.get("affinetransform"));

            if (styleNode.getTransform() != null) {
                //TODO : Put in cache...
                TransformBuilder transformBuilder = new TransformBuilder();
                properties.put("shape", shp);
                at.concatenate(transformBuilder.getAffineTransform(styleNode.getTransform(), properties));
            }
            Shape atShp = at.createTransformedShape(shp);

            //We give the raw shape to the drawHalo method in order not to lose the
            //type of the original Shape - It will be easier to compute the halo.
            //We give the transformed shape too... This way we are sure we won't
            //compute it twice, as it is a complicated operation.
            Halo halo = styleNode.getHalo();
            if (halo != null) {
                if (drawerMap.containsKey(halo.getClass())) {
                    properties.put("shape", atShp);
                    drawerMap.get(halo.getClass()).draw(sp, g2, mapTransform, halo, properties);
                }
            }
            IFill fill = styleNode.getFill();
            if (fill != null) {
                if (drawerMap.containsKey(fill.getClass())) {
                    properties.put("shape", atShp);
                    drawerMap.get(fill.getClass()).draw(sp, g2, mapTransform, fill, properties);
                }
            }

            Stroke stroke = styleNode.getStroke();
            if (stroke != null) {
                double offset = 0.0;
                RealParameter perpendicularOffset = styleNode.getPerpendicularOffset();
                /*(perpendicularOffset != null) {
                    offset = Uom.toPixel(perpendicularOffset.getValue(rs, fid),
                            uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                }*/
                properties.put("offset", offset);
                if (drawerMap.containsKey(stroke.getClass())) {
                    properties.put("shape", atShp);
                    drawerMap.get(stroke.getClass()).draw(sp, g2, mapTransform, stroke, properties);
                }
            }
        }

    }

    /**
     * TODO : implements
     *
     * @param map
     * @param mt
     * @return
     * @throws ParameterException
     */
    private Shape getShape(Map<String, Object> map, MapTransform mt) throws ParameterException {

        Double dpi = null;
        Double scaleDenom = null;

        if (mt != null) {
            dpi = mt.getDpi();
            scaleDenom = mt.getScaleDenominator();
        }

        //MarkGraphicSource source = getSource(map);

        /*if (source != null) {
            //TODO implemennt
            //return source.getShape(viewBox, map, scaleDenom, dpi, markIndex, mimeType);
            return null;
        } else {
            return null;
        }*/
        return null;
    }

    @Override
    public Rectangle2D getBounds(JdbcSpatialTable sp, MapTransform mapTransform, MarkGraphic styleNode, Map<String, Object> properties) throws ParameterException {
        Shape shp;
        // If the shape doesn't depends on feature (i.e. not null), we used the cached one
        if (shape == null) {
            shp = getShape(properties, mapTransform);
        } else {
            shp = shape;
        }

        if (shp == null) {
            WellKnownNameUtils.getShape(WellKnownName.CIRCLE, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
                    mapTransform.getDpi());
        }

        /*if (transform != null) {
            return this.transform.getGraphicalAffineTransform(false, map, mt, shp.getBounds().getWidth(),
                    shp.getBounds().getHeight()).createTransformedShape(shp).getBounds2D();
        } else {*/
        return shp.getBounds2D();/*
        }*/

    }

}
