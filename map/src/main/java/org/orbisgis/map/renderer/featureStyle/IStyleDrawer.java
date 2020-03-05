/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 * @param <T>
 */
public interface IStyleDrawer<T extends IStyleNode>{
    
    void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform , T styleNode, Map<String, Object> properties ) throws ParameterException, SQLException;
}
