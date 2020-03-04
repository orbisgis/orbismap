/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.renderer.style;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.visitors.ParameterValueVisitor;

/**
 *
 * @author ebocher
 */
public class StyleParameterExpressionVisitor {
    
     /**
     * Test the {@link DataSourceLocation#asType(Class)} method.
     */
    @Test
    public void parseExpressionParameter1() {
        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerStyleColorExpression();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        
        System.out.println(pvv.getResultAsString());
       
    }
    
     /**
     * Test the {@link DataSourceLocation#asType(Class)} method.
     */
    @Test
    public void parseExpressionParameter2() {
        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerHatchedStyle();        
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(style.getRules().get(0));
        
        System.out.println(pvv.getResultAsString());
       
    }
}
