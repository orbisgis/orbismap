/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import org.orbisgis.style.StyleNode;

/**
 *
 * @author ebocher
 */
public interface IStyleExpression <T extends StyleNode>{
    
    T getStyleNode();
    
}
