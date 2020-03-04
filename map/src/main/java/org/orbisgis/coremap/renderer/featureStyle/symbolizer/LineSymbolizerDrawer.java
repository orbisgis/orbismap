/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.coremap.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.coremap.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;


/**
 *
 * @author ebocher
 */
public class LineSymbolizerDrawer implements ISymbolizerDraw<LineSymbolizer>{

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();
    static {
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, LineSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
           
        Stroke stroke = symbolizer.getStroke();       
        if (stroke != null) {
             Uom uom = symbolizer.getUom();
             Shape shp = mapTransform.getShape(sp.getGeometry(symbolizer.getGeometryParameter().getIdentifier()), true);
       
            double offset = 0.0;
            /*ExpressionParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
            if (perpendicularOffset != null) {
                offset = UomUtils.toPixel(perpendicularOffset.getValue(rs, fid),
                        getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
            }*/
            if (shp != null) {
                properties.put("offset", offset);
                if(drawerMap.containsKey(stroke.getClass())){
                    properties.put("shape",shp);
                    drawerMap.get(stroke.getClass()).draw(sp, g2, mapTransform, stroke, properties);
                }
            }
        }
    }
    
}
