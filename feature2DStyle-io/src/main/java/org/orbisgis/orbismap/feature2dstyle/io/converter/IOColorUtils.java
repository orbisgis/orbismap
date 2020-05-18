/**
 * Feature2DStyle-IO is part of the OrbisGIS platform
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
 * Feature2DStyle-IO  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle-IO  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle-IO  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle-IO . If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.feature2dstyle.io.converter;

import java.util.HashMap;
import org.orbisgis.orbismap.style.IColor;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.orbisgis.orbismap.style.Feature2DStyleTerms;
import org.orbisgis.orbismap.style.color.HexaColor;
import org.orbisgis.orbismap.style.color.RGBColor;
import org.orbisgis.orbismap.style.color.WellknownNameColor;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.utils.ColorUtils;

/**
 * Class to build the color element
 * 
 * @author Erwan Bocher, CNRS (2020)
 * @author Sylvain Palominos, UBS (2020)
 */

public class IOColorUtils {

    private static Pattern EXPRESSION_PATTERN;

    /**
     *
     * @param value
     * @return
     */
    public static IColor createColorStyleElement(String value) {
        if (EXPRESSION_PATTERN == null) {
            EXPRESSION_PATTERN = Pattern.compile("\\s*(?:expression\\s*\\(\\s*(.+)?\\)|([^\\s]+))\\s*", Pattern.CASE_INSENSITIVE);
        }
        if (value != null && !value.isEmpty()) {
            Matcher matcher = EXPRESSION_PATTERN.matcher(value);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                if (group1 != null) {
                    if (group2 != null && !group2.isEmpty()) {
                        return null;
                    } else {
                        if (ColorUtils.isHexa(group1)) {
                            HexaColor hexaColor = new HexaColor();
                            hexaColor.setHexaColor(new Expression(group1));
                            return hexaColor;

                        } else {
                            WellknownNameColor wellknownNameColor = new WellknownNameColor();
                            wellknownNameColor.setWellknownName(new Expression(group1));
                            return wellknownNameColor;
                        }
                    }
                } else {
                    if (group2 != null && !group2.isEmpty()) {
                        HashMap<String, ParameterValue> rgbValues = ColorUtils.parseRGB(group2);
                        if (rgbValues != null && !rgbValues.isEmpty()) {
                            RGBColor rgbColor = new RGBColor();
                            rgbColor.setRed(rgbValues.get(Feature2DStyleTerms.RED));
                            rgbColor.setGreen(rgbValues.get(Feature2DStyleTerms.GREEN));
                            rgbColor.setBlue(rgbValues.get(Feature2DStyleTerms.BLUE));
                            return rgbColor;

                        }
                        if (ColorUtils.isHexa(group2)) {
                            HexaColor hexaColor = new HexaColor();
                            hexaColor.setHexaColor(new Literal(group2));
                            return hexaColor;

                        } else {
                            WellknownNameColor wellknownNameColor = new WellknownNameColor();
                            wellknownNameColor.setWellknownName(new Literal(group2));
                            return wellknownNameColor;
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
