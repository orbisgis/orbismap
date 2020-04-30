/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.utils;

import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.Uom;

/**
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class UomUtils {

    public static final double PT_IN_INCH = 72.0;
    public static final double MM_IN_INCH = 25.4;
    public static final double IN_IN_FOOT = 12;
    public static final double ONE_THOUSAND = 1000;
    public static final double ONE_HUNDRED = 100;

    /**
     * Convert a value to the corresponding value in pixel
     *
     * Note that converting ground unit to pixel is done by using a constant
     * scale
     *
     * @param value the value to convert
     * @param uom unit of measure for value
     * @param dpi the current resolution
     * @param scale the current scale (for converting ground meters and ground
     * feet to media units)
     * @param v100p the value to return when uom is "percent" and value is 100
     * (%)
     * @return
     * @throws ParameterException
     *
     * @todo return integer !!!
     */
    public static float toPixel(float value, Uom uom, double dpi, double scale, float v100p) throws ParameterException {
        if (uom == null) {
            return value; // no uom ? => return as Pixel !
        }

        if (dpi <= 0  && uom != Uom.PX) {
            throw new ParameterException("DPI is invalid");
        }

        switch (uom) {
            case IN:
                return (float) (value * dpi); // [IN] * [PX]/[IN] => [PX]
            case MM:
                return (float) ((value / MM_IN_INCH) * dpi); // [MM] * [IN]/[MM] * [PX]/[IN] => [PX]
            case PT: // 1PT == 1/72[IN] whatever dpi is
                return (float) ((value / PT_IN_INCH) * dpi); // 1/72[IN] * 72 *[PX]/[IN] => [PX]
            case GM:
                if (scale <= 0) {
                    throw new ParameterException("Scale is invalid");
                }
                return (float) ((value * ONE_THOUSAND * dpi) / (scale * MM_IN_INCH));
            case GFT:
                if (scale <= 0){
                    throw new ParameterException("Scale is invalid");
                }
                return (float) ((value * IN_IN_FOOT * dpi) / (scale));
            case PERCENT:
                return (float) (value * v100p / ONE_HUNDRED);
            case PX:
            default:
                return value; // [PX]
        }
    }
    
    /**
     * Convert a value to the corresponding value in pixel
     *
     * Note that converting ground unit to pixel is done by using a constant
     * scale
     *
     * @param value the value to convert
     * @param uom unit of measure for value
     * @param dpi the current resolution
     * @param scale the current scale (for converting ground meters and ground
     * feet to media units)
     * (%)
     * @return
     * @throws ParameterException
     *
     * @todo return integer !!!
     */
    public static float toPixel(float value, Uom uom, double dpi, double scale) throws ParameterException {
        if (uom == null) {
            return value; // no uom ? => return as Pixel !
        }

        if (dpi <= 0  && uom != Uom.PX) {
            throw new ParameterException("DPI is invalid");
        }
        
        if(value ==0){
            return value;
        }

        switch (uom) {
            case IN:
                return (float) (value * dpi); // [IN] * [PX]/[IN] => [PX]
            case MM:
                return (float) ((value / MM_IN_INCH) * dpi); // [MM] * [IN]/[MM] * [PX]/[IN] => [PX]
            case PT: // 1PT == 1/72[IN] whatever dpi is
                return (float) ((value / PT_IN_INCH) * dpi); // 1/72[IN] * 72 *[PX]/[IN] => [PX]
            case GM:
                if (scale <= 0) {
                    throw new ParameterException("Scale is invalid");
                }
                return (float) ((value * ONE_THOUSAND * dpi) / (scale * MM_IN_INCH));
            case GFT:
                if (scale <= 0){
                    throw new ParameterException("Scale is invalid");
                }
                return (float) ((value * IN_IN_FOOT * dpi) / (scale));
            case PERCENT:
                return value;
            case PX:
            default:
                return value; // [PX]
        }
    }

}
