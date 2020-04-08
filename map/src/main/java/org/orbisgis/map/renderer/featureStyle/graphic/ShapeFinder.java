/**
 * Map is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Shape;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.GraphicSize;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * Get the shape from the a wellKnownNameParameter
 * Look into a factory
 * @author Erwan Bocher, CNRS (2020)
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
     * @param wellKnownNameParameter
     * @param graphicSize
     * @param scale
     * @param dpi
     * @param uom
     * @return
     * @throws ParameterException 
     */
    public static Shape getShape(ParameterValue wellKnownNameParameter, GraphicSize graphicSize,  Double scale, Double dpi, Uom uom) throws ParameterException  {
        if (wellKnownNameParameter != null && !(wellKnownNameParameter instanceof NullParameterValue)) {
           String[] prefix = getPrefix((String) wellKnownNameParameter.getValue());
           IShapeFactory factory = shapeFactories.get(prefix[0]);
           factory.setShapeName(prefix[1]);
           return factory.getShape(graphicSize, scale,  dpi,  uom);            
        }else{
            throw  new ParameterException();
        }
    }

    /**
     * 
     * @param wkn
     * @return 
     */
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
