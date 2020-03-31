/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Shape;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class ShapeFinder {
    
     /**
     * Default size to be used to render graphics based on well-known names.
     */
    public static float DEFAULT_SIZE = 10.0f;

    public static HashMap<String, IShapeFactory> shapeFactories = new HashMap<>();
    
    static {
        shapeFactories.put(WKNFactory.FACTORY_PREFIX, new WKNFactory());
        shapeFactories.put(WeatherMarkFactory.FACTORY_PREFIX, new WeatherMarkFactory());
    }
    
    
    /**
     * 
     * @param wkn
     * @param viewBox
     * @param scale
     * @param dpi
     * @param uom
     * @return
     * @throws ParameterException 
     */
    public static Shape getShape(String wkn, ViewBox viewBox,  Double scale, Double dpi, Uom uom) throws ParameterException  {
        if (wkn != null && !wkn.isEmpty()) {
            String[] prefix = getPrefix(wkn);
           IShapeFactory factory = shapeFactories.get(prefix[0]);
           factory.setShapeName(prefix[1]);
           return factory.getShape(viewBox, scale,  dpi,  uom);
            
        }else{
        throw  new ParameterException();
        }
    }

    private static String[] getPrefix(String wkn) {
        try {
            URI uri = new URI(wkn);
            String sheme = uri.getScheme();
            String authority = uri.getAuthority();
            if(sheme !=null){
                return new String[]{sheme, authority};
            }
            else{
                return new String[]{WKNFactory.FACTORY_PREFIX, wkn};
            }
        } catch (URISyntaxException ex) {
            return new String[]{WKNFactory.FACTORY_PREFIX, wkn};
        }
 
    }   
    
}
