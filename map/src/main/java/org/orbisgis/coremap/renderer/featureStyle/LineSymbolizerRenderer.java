/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.LineSymbolizer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class LineSymbolizerRenderer implements ISymbolizerDraw<MapTransform>{

    private final LineSymbolizer symbolizer;

    public LineSymbolizerRenderer(LineSymbolizer lineSymbolizer) {
        this.symbolizer=lineSymbolizer;
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform) throws ParameterException, SQLException {
           
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
                StrokeRender.drawPenStroke(sp, g2, (PenStroke) stroke, shp, mapTransform, offset);
            }
        }
    }
    
}
