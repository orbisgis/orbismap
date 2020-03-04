/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle.stroke;

import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.coremap.renderer.featureStyle.label.LineLabelDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.label.LineLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.TextStroke;

/**
 *
 * @author ebocher
 */
public class TextStrokeDrawer implements ISymbolizerDraw<TextStroke>{

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, TextStroke styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
  
        LineLabel lineLabel = styleNode.getLineLabel();
        if (lineLabel != null) {
            new LineLabelDrawer().draw(sp, g2, mapTransform, lineLabel, properties);
        }
    }
    
}
