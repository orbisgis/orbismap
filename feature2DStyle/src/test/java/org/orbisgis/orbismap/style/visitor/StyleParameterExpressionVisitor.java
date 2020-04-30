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
package org.orbisgis.orbismap.style.visitor;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.orbisgis.orbismap.style.Feature2DStyle;
import static org.junit.jupiter.api.Assertions.*;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.color.HexaColor;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.visitor.ParameterValueVisitor;

/**
 *
 * @author ebocher
 */
public class StyleParameterExpressionVisitor {
    
     /**
     * Test parse parameter
     */
    @Test
    public void parseExpressionParameterStyleElement() {        
        Feature2DStyle style = createAreaSymbolizerStyleColorExpression("CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END");        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        Optional<String> formatedExpression = pvv.getExpressionParameters().keySet().stream().findFirst();
        assertTrue(formatedExpression.isPresent());
        assertEquals("CASE WHEN ST_AREA(THE_GEOM) > 10000 THEN '#ff6d6d' ELSE '#6d86ff' END", formatedExpression.get());
    } 
    
     /**
     * Test parse parameter
     */
    @Test
    public void parseExpressionColumnParameterStyleElement() {        
        Feature2DStyle style = createAreaSymbolizerStyleColorExpression("MyColumn");        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        Optional<String> formatedExpression = pvv.getExpressionParameters().keySet().stream().findFirst();
        assertTrue(formatedExpression.isPresent());
        assertEquals("MyColumn", formatedExpression.get());
    }
    
     /**
     * Test parse parameter
     */
    @Test
    public void parseWrongExpressionParameterStyleElement() {    
        Assertions.assertThrows(RuntimeException.class, () -> {
        Feature2DStyle style = createAreaSymbolizerStyleColorExpression("SELECT * FROM");        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        });
      }  
        
      
    
    /**
     * Create a style with one <code>AreaSymbolizer</code> and a SolidFill color
     * expression
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerStyleColorExpression(String expression) {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        Expression colorExpression = new Expression(expression);
        HexaColor hexaColor =  new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(hexaColor);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        ps.initDefault();
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

}
