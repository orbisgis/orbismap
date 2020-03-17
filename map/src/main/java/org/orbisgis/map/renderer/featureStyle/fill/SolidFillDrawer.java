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
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;

import static org.orbisgis.style.fill.SolidFill.GRAY50;
import org.orbisgis.style.utils.ColorHelper;

/**
 *
 * @author ebocher
 */
public class SolidFillDrawer implements IFillDrawer<SolidFill> {

    private Shape shape;

    
    
    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, SolidFill styleNode,Map<String, Object> properties ) throws ParameterException, SQLException {
        if (shape != null) {
            g2.setPaint(getPaint( styleNode, properties, mapTransform));
            g2.fill(shape);
        }
    }

    @Override
    public Paint getPaint( SolidFill solidFill, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        //Color ac = null; // ac stands 4 colour + alpha channel
        String colorValue = (String) solidFill.getColor().getValue();
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
       
        
        Float opacity =  (Float) solidFill.getOpacity().getValue();
       
        if(opacity==null){
            return color;
        }        
        return  ColorHelper.getColorWithAlpha(color, opacity);
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
}
