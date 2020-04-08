/**
 * Map is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.utils;

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
        /*public static AffineTransform getAffineTranslate(Translate translate, Uom uom,
            Map<String,Object> properties, MapTransform mt, Double width100p, Double height100p) throws ParameterException, SQLException {
                double tx = 0.0;
               Float x = ValueHelper.getAsFloat(properties, translate.getX());
            if (x != null) {
                tx = UomUtils.toPixel(x, uom, mt.getDpi(), mt.getScaleDenominator(), width100p);
            }

            double ty = 0.0;
            Float y = ValueHelper.getAsFloat(properties, translate.getY());
            if (y != null) {
                ty = UomUtils.toPixel(y, uom, mt.getDpi(), mt.getScaleDenominator(), height100p);
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
