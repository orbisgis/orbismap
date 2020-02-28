/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.geom.AffineTransform;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.transform.Translate;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class AffineTransformUtils {
 
   
        //Translate transformation
    /**
     *
     * @param rs
     * @param translate
     * @param map
     * @param uom
     * @param mt
     * @param width100p
     * @param height100p
     * @return
     * @throws ParameterException
     * @throws SQLException
     */
        public static AffineTransform getAffineTranslate(ResultSet rs, Translate translate, Uom uom,
            MapTransform mt, Double width100p, Double height100p) throws ParameterException, SQLException {
                double tx = 0.0;
                ExpressionParameter x = translate.getX();
                if (x!= null) {
                        tx = UomUtils.toPixel(rs.getDouble(x.getIdentifier()), uom, mt.getDpi(), mt.getScaleDenominator(), width100p);
                }

                double ty = 0.0;
                ExpressionParameter y = translate.getY();
                if (translate.getY() != null) {
                        ty = UomUtils.toPixel(rs.getDouble(y.getIdentifier()), uom, mt.getDpi(), mt.getScaleDenominator(), height100p);
                }

                return AffineTransform.getTranslateInstance(tx, ty);
        }
        
        
        //Matrix
        /*public AffineTransform getAffineTransform(Map<String,Object> map, Uom uom,
            IMapTransform mt, Double width, Double height) throws ParameterException {
                return new AffineTransform(
                        //Uom.toPixel(a.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null),
                        a.getValue(map),
                        b.getValue(map),
                        c.getValue(map),
                        //Uom.toPixel(b.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null),
                        //Uom.toPixel(c.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null),
                        //Uom.toPixel(d.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null),
                        d.getValue(map),
                        UomUtils.toPixel(e.getValue(map), uom, mt.getDpi(), mt.getScaleDenominator(), width),
                        UomUtils.toPixel(f.getValue(map), uom, mt.getDpi(), mt.getScaleDenominator(), height));
        }
        
        //Rotate
        @Override
        public AffineTransform getAffineTransform(Map<String,Object> map, Uom uom,
                        IMapTransform mt, Double width, Double height) throws ParameterException {
                double ox = 0.0;
                if (x != null) {
                        ox = UomUtils.toPixel(x.getValue(map), uom, mt.getDpi(), mt.getScaleDenominator(), width);
                }

                double oy = 0.0;
                if (y != null) {
                        oy = UomUtils.toPixel(y.getValue(map), uom, mt.getDpi(), mt.getScaleDenominator(), height);
                }

                double theta = 0.0;
                if (rotation != null) {
                        theta = rotation.getValue(map) * Math.PI / 180.0; // convert to rad
                }
                return AffineTransform.getRotateInstance(theta, ox, oy);
        }
        
        //Scale
        @Override
    public AffineTransform getAffineTransform(Map<String,Object> map, Uom uom,
            IMapTransform mt, Double width, Double height) throws ParameterException {
        double sx = 1.0;
        if (x != null) {
            //sx = Uom.toPixel(x.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null);
            sx = x.getValue(map);
        }

        double sy = 1.0;
        if (y != null) {
            //sy = Uom.toPixel(y.getValue(feat), uom, mt.getDpi(), mt.getScaleDenominator(), null);
            sy = y.getValue(map);
        }

        //AffineTransform.getTranslateInstance(A;, sy);

        return AffineTransform.getScaleInstance(sx, sy);
    }*/
}
