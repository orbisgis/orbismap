/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 * @param <T>
 */
public interface IGraphicDrawer <T extends StyleNode> extends IStyleDrawer <T>{
    
    
    public Rectangle2D getBounds(MapTransform mapTransform, T styleNode, Map<String, Object> properties) throws ParameterException;
}