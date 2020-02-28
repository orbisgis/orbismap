/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.fill.SolidFill;
import static org.orbisgis.style.fill.SolidFill.GRAY50;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class FillRenderer {
    
    /**
     * Return a Java Color according to this SE Solid Fill
     *
     * @param mt
     * @return A java.awt.Color
     * @throws ParameterException
     */
    public static Paint getPaint(ResultSet rs, SolidFill solidFill, IMapTransform mt) throws ParameterException, SQLException {

        Color ac = null; // ac stands 4 colour + alpha channel
        String colorIdenifier = solidFill.getColor().getIdentifier();
        Color color = null;
        if(colorIdenifier!=null && !colorIdenifier.isEmpty()){
         color = Color.getColor(rs.getString(solidFill.getColor().getIdentifier()));
        }
        
        if (color == null) {
            //We must cast the colours to int values, because we want to use 
            //GRAY50 to build RGB value - As it equals 128.0f, we need a cast
            //because Color(float, float, float) needs values between 0 and 1.
            color = new Color((int) GRAY50, (int) GRAY50, (int) GRAY50);
        }

        double opacity = rs.getDouble(solidFill.getOpacity().getIdentifier());
        /*if (opacity) {
            ac = ColorHelper.getColorWithAlpha(color, opacity);
        } else {
            ac = color;
        }*/
        
        ac=color;

        // Add opacity to the color
        return ac;
    }

    public static void drawSolidFill(ResultSet rs, Graphics2D g2, SolidFill solidFill, Shape shp,
            IMapTransform mt) throws ParameterException, SQLException {
        g2.setPaint(getPaint(rs, solidFill, mt));
        g2.fill(shp);
    }
}
