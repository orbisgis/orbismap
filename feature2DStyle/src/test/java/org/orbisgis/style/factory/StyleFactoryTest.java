/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.factory;

import java.awt.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.Literal;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class StyleFactoryTest {

    @Test
    public void createStyleInvalidParameterTest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            SolidFill solidFill = StyleFactory.createSolidFill(Color.yellow, 12);
            solidFill.getOpacity().getValue();
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            SolidFill solidFill = new SolidFill(new Literal(""), new Literal(1.0));
        });   
        
        Assertions.assertThrows(RuntimeException.class, () -> {
            SolidFill solidFill = new SolidFill(new Literal(""), new Literal("orbisgis"));
        }); 
        
    }

}
