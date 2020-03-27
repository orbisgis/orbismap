/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Erwan Bocher
 */
public class DomainExpressionTest {

    @Test
    public void domainExpresionNumberValue() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser(10);
        assertTrue(domainExpression.evaluate(""));
        assertFalse(domainExpression.evaluate("value > 12"));
        assertTrue(domainExpression.evaluate("value > 9"));
        assertTrue(domainExpression.evaluate("value > 9.5"));
        assertFalse(domainExpression.evaluate("value > 0 and value <1"));
        assertTrue(domainExpression.evaluate("value > 0 and value >1"));
        assertTrue(domainExpression.evaluate("value = 10"));
        assertFalse(domainExpression.evaluate("value = 1"));
        assertTrue(domainExpression.evaluate("value > 0 or value <1"));
        assertFalse(domainExpression.evaluate("(value > 12) or (value > 15)"));
        assertTrue(domainExpression.evaluate("(value < 12) or (value > 15)"));
        assertTrue(domainExpression.evaluate("value in (12,13,10)"));
    }

    @Test
    public void domainExpresionStringValueError() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser("10");
        Assertions.assertThrows(RuntimeException.class, () -> {
            domainExpression.evaluate("value > 12");
        });
    }
    
    @Test
    public void domainExpresionStringValue() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser("orbisgis");
        assertTrue(domainExpression.evaluate("value = 'orbisgis'"));
        assertFalse(domainExpression.evaluate("value = 'Orbisgis'"));       
        assertTrue(domainExpression.evaluate("value in ('orbisgis', 12, 'super')"));
        assertTrue(domainExpression.evaluate("value in ('orbisgis', 'vannes', 'super')")); 
    }

}
