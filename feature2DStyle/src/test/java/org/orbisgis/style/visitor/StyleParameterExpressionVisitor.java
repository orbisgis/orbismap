/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.visitor;

import java.util.Optional;
import org.junit.jupiter.api.Test;

import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.factory.StyleFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ebocher
 */
public class StyleParameterExpressionVisitor {
    
     /**
     * Test parser
     */
    @Test
    public void parseExpressionParameterStyleElement() {        
        Feature2DStyle style = StyleFactory.createAreaSymbolizerStyleColorExpression();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        Optional<String> formatedExpression = pvv.getExpressionParameters().keySet().stream().findFirst();
        assertTrue(formatedExpression.isPresent());
        assertEquals("CASE WHEN ST_AREA(THE_GEOM) > 10000 THEN '#ff6d6d' ELSE '#6d86ff' END", formatedExpression.get());
    }
    
    
    
     /**
     * Test the {@link DataSourceLocation#asType(Class)} method.
     */
    @Test
    public void parseExpressionParameterRule() {        
        Feature2DStyle style = StyleFactory.createAreaSymbolizerRuleExpression();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));        
        Optional<String> formatedExpression = pvv.getExpressionParameters().keySet().stream().findFirst();
        assertTrue(formatedExpression.isPresent());
        assertEquals("st_area(the_geom) < 5000", formatedExpression.get());           
    }
}
