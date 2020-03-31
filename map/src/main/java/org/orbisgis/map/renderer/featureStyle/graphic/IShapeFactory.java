/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Shape;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public interface IShapeFactory {
    
    String getIdentifier();
    
    Shape getShape(ViewBox viewBox,
            Double scale, Double dpi, Uom uom ) throws  ParameterException;

    void setShapeName(String shapeName);
    
    String getShapeName();
}
