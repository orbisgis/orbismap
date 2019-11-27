/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer;

import org.orbisgis.coremap.layerModel.LayerException;

/**
 *
 * @author Erwan Bocher
 */
public interface IRenderer {
    
    
    void draw() throws LayerException ;
    
}
