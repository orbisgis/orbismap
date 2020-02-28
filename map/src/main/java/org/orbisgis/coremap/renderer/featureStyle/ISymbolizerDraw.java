/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.Graphics2D;
import java.sql.SQLException;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 * @param <MT>
 */
public interface ISymbolizerDraw< MT extends IMapTransform> {
 
    
    void draw(ISpatialTable sp, Graphics2D g2, MT mapTransform  ) throws ParameterException, SQLException;
}
