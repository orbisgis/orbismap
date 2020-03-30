/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Shape;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.style.graphic.ExternalGraphic;
import org.orbisgis.style.graphic.OnlineResource;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class ExternalGraphicDrawer implements IStyleDrawer<ExternalGraphic>{

    private Shape shape;

    
        
    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, ExternalGraphic styleNode) throws ParameterException {
        
        OnlineResource onlineRessource = styleNode.getOnlineResource();
        
        if(onlineRessource!=null){
            String mimeType = styleNode.getMimeType();
            if (mimeType != null && mimeType.equalsIgnoreCase("image/svg+xml")) {
            //TODO : drawSVG(g2, at, opacity);
            } else {
            //TODO : drawJAI(g2, at, mt, opacity);
            }
        }    
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
}
