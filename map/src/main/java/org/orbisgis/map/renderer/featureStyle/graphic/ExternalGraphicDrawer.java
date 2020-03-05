/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.graphic.ExternalGraphic;
import org.orbisgis.style.graphic.OnlineResource;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class ExternalGraphicDrawer implements IStyleDrawer<ExternalGraphic>{

    
        
    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, ExternalGraphic styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        
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
    
}
