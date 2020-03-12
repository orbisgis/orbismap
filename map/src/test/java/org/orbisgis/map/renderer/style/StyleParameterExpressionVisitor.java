/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.map.renderer.featureStyle.visitor.ParameterValueVisitor;

/**
 *
 * @author ebocher
 */
public class StyleParameterExpressionVisitor {
    
     /**
     * Test parser
     */
    @Test
    public void parseExpressionParameter1() {        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerStyleColorExpression();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));        
        System.out.println(pvv.getExpressionParametersAsString());
        //System.out.println(pvv.getLiteralParameters().values().stream().collect(Collectors.joining(",")));
       
    }
    
     /**
     * Test the {@link DataSourceLocation#asType(Class)} method.
     */
    @Test
    public void parseExpressionParameter2() {        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerHatched();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));        
        System.out.println(pvv.getExpressionParametersAsString());       
    }
    
     /**
     * Test the {@link DataSourceLocation#asType(Class)} method.
     */
    @Test
    public void parseExpressionParameter3() {        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerRuleExpression();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));        
        System.out.println(pvv.getExpressionParametersAsString());       
    }
}
