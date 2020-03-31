/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;


import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.map.renderer.featureStyle.graphic.ShapeFinder;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ebocher
 */
public class ShapeFinderTest {
    
    
    @Test
    public void readDefaultShapeFactory(TestInfo testInfo) throws ParameterException{        
        ViewBox viewBox = new ViewBox(new Literal(50f));
        Shape shape = ShapeFinder.getShape("square", viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width);        
        assertTrue(shape instanceof Rectangle2D);
    }
    
    @Test
    public void readWeatherFactory(TestInfo testInfo) throws ParameterException{        
        ViewBox viewBox = new ViewBox(new Literal(50f));
        Shape shape = ShapeFinder.getShape("weather://F003", viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width); 
        shape = ShapeFinder.getShape("weather://cloudy", viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width); 
    }
    
}
