/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.utils.AffineTransformUtils;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.fill.DotMapFillDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HatchedFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author ebocher
 */
public class AreaSymbolizerDrawer implements ISymbolizerDraw<AreaSymbolizer> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();
    static {
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
        drawerMap.put(HatchedFill.class, new HatchedFillDrawer());
        drawerMap.put(DotMapFill.class, new DotMapFillDrawer());
    }
    
    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, AreaSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
        Uom uom = symbolizer.getUom();
        Shape shp = mapTransform.getShape(sp.getGeometry(symbolizer.getGeometryParameter().getIdentifier()), true);
        if (symbolizer.getTranslate() != null) {
            shp = AffineTransformUtils.getAffineTranslate( sp, symbolizer.getTranslate(), uom, mapTransform,
                    (double) mapTransform.getWidth(), (double) mapTransform.getHeight()).createTransformedShape(shp);
        }
        if (shp != null) {
            IFill fill = symbolizer.getFill();
            if (fill != null) {
                if(drawerMap.containsKey(fill.getClass())){
                    properties.put("shape", shp);
                    drawerMap.get(fill.getClass()).draw(sp, g2, mapTransform, fill, properties);
                }
            }

            Stroke stroke = symbolizer.getStroke();
            if (stroke != null) {
                double offset = 0.0;   
                ExpressionParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
                /*(perpendicularOffset != null) {
                    offset = Uom.toPixel(perpendicularOffset.getValue(rs, fid),
                            uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                }*/
                properties.put("offset", offset);
                if(drawerMap.containsKey(stroke.getClass())){                    
                    properties.put("shape", shp);
                    drawerMap.get(stroke.getClass()).draw(sp, g2, mapTransform, stroke, properties);
                }
            }
            
            
        }

    }

}
