/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.fill;

import java.awt.*;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;

import static org.orbisgis.style.fill.SolidFill.GRAY50;
import org.orbisgis.style.parameter.color.ColorHelper;

/**
 *
 * @author ebocher
 */
public class SolidFillDrawer implements IFillDrawer<SolidFill> {

    
    
    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, SolidFill styleNode,Map<String, Object> properties ) throws ParameterException, SQLException {
        Shape shape = (Shape) properties.get("shape");
        if (shape != null) {
            g2.setPaint(getPaint(sp, styleNode, properties, mapTransform));
            g2.fill(shape);
        }
    }

    @Override
    public Paint getPaint(JdbcSpatialTable sp, SolidFill solidFill, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        //Color ac = null; // ac stands 4 colour + alpha channel
        String colorValue = ValueHelper.getString(sp, solidFill.getColor());
        Color color = null;
        if (colorValue!= null) {
            color = Color.decode(colorValue);
        }
        if (color == null) {
            //We must cast the colours to int values, because we want to use
            //GRAY50 to build RGB value - As it equals 128.0f, we need a cast
            //because Color(float, float, float) needs values between 0 and 1.
            color = new Color((int) GRAY50, (int) GRAY50, (int) GRAY50);
        }

        double opacity = ValueHelper.getDouble(sp,solidFill.getOpacity());
       
        return  ColorHelper.getColorWithAlpha(color, opacity);
    }
    
}
