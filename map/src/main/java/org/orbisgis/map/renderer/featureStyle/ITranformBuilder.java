/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import java.awt.geom.AffineTransform;
import java.util.Map;
import org.orbisgis.style.parameter.TransformParameter;

/**
 *
 * @author ebocher
 */
public interface ITranformBuilder {
 
    
    AffineTransform getAffineTransform(TransformParameter transformParameter, Map<String, Object> properties);
}
