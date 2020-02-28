/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.style.AreaSymbolizer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.Fill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author ebocher
 */
public class AreaSymbolizerRenderer implements ISymbolizerDraw<MapTransform> {

    private final AreaSymbolizer symbolizer ;

    public AreaSymbolizerRenderer(AreaSymbolizer areaSymbolizer) {
            this.symbolizer=areaSymbolizer;;
    }

    @Override
    public void draw(ISpatialTable sp, Graphics2D g2, MapTransform mapTransform) throws ParameterException, SQLException {
        Uom uom = symbolizer.getUom();
        Shape shp = mapTransform.getShape(sp.getGeometry(symbolizer.getGeometryParameter().getIdentifier()), true);
        if (symbolizer.getTranslate() != null) {
            shp = AffineTransformUtils.getAffineTranslate((ResultSet) sp, symbolizer.getTranslate(), uom, mapTransform,
                    (double) mapTransform.getWidth(), (double) mapTransform.getHeight()).createTransformedShape(shp);
        }
        if (shp != null) {
            Fill fill = symbolizer.getFill();
            if (fill != null) {
                if(fill instanceof SolidFill){
                    FillRenderer.drawSolidFill((ResultSet) sp, g2, (SolidFill) fill, shp, mapTransform);
                }
            }

            Stroke stroke = symbolizer.getStroke();
            if (stroke != null) {
                double offset = 0.0;   
                ExpressionParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
                /*if (perpendicularOffset != null) {
                    offset = Uom.toPixel(perpendicularOffset.getValue(rs, fid),
                            uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                }*/
                //stroke.draw(g2, map, shp, selected, mt, offset);
            }
        }

    }
}
