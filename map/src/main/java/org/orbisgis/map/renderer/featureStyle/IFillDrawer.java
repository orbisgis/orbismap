/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 * @param <T>
 */
public interface IFillDrawer <T extends IStyleNode> extends IStyleDrawer <T>{
    
    
    public Paint getPaint(T styleNode, MapTransform mt) throws ParameterException; 
    
    public AffineTransform getAffineTransform();

   
    public void setAffineTransform(AffineTransform affineTransform); 
    
}
