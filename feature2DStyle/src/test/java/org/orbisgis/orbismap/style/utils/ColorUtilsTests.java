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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import org.orbisgis.orbismap.style.Feature2DStyleTerms;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

public class ColorUtilsTests {

    @Test
    public void parseRGBRepresentionTest1() {
        String rgbValue = "rgb(12,120,11)";
        HashMap<String, ParameterValue> rgbValues = ColorUtils.parseRGB(rgbValue);
        assertEquals(new Literal(12), rgbValues.get(Feature2DStyleTerms.RED));
        assertEquals(new Literal(120), rgbValues.get(Feature2DStyleTerms.GREEN));
        assertEquals(new Literal(11), rgbValues.get(Feature2DStyleTerms.BLUE));
    }

    @Test
    public void parseRGBRepresentionTest2() {
        String rgbValue = "rgb(expression(the_color),120,11)";
        HashMap<String, ParameterValue> rgbValues = ColorUtils.parseRGB(rgbValue);
        assertEquals(new Expression("the_color"), rgbValues.get(Feature2DStyleTerms.RED));
        assertEquals(new Literal(120), rgbValues.get(Feature2DStyleTerms.GREEN));
        assertEquals(new Literal(11), rgbValues.get(Feature2DStyleTerms.BLUE));
    }

    @Test
    public void parseRGBRepresentionTest3() {
        String rgbValue = "rgb(120,11)";
        HashMap<String, ParameterValue> rgbValues = ColorUtils.parseRGB(rgbValue);
        assertNull(rgbValues);
    }

    @Test
    public void parseHexaRepresentionTest1() {
        String hexaColor = "#000";
        assertEquals(new Literal(hexaColor), ColorUtils.parseHexa(hexaColor));
        hexaColor = "#000000";
        assertEquals(new Literal(hexaColor), ColorUtils.parseHexa(hexaColor));
        hexaColor = "000000";
        assertEquals(new Literal(hexaColor), ColorUtils.parseHexa(hexaColor));
        hexaColor = "orange";
        assertTrue( ColorUtils.parseHexa(hexaColor) instanceof NullParameterValue);        
        hexaColor = "111";
        assertEquals(new Literal(hexaColor), ColorUtils.parseHexa(hexaColor));            
        hexaColor = "";        
        assertNull(ColorUtils.parseHexa(hexaColor)); 
        hexaColor = null;
        assertNull(ColorUtils.parseHexa(hexaColor));    
    }

}
