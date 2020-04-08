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
package org.orbisgis.map.renderer.featureStyle;


import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.map.renderer.featureStyle.graphic.ShapeFinder;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;
import static org.junit.jupiter.api.Assertions.*;
import org.orbisgis.style.parameter.Literal;

/**
 *
 * @author ebocher
 */
public class ShapeFinderTest {
    
    
    @Test
    public void readDefaultShapeFactory(TestInfo testInfo) throws ParameterException{        
        ViewBox viewBox = new ViewBox(50f);
        Shape shape = ShapeFinder.getShape(new Literal("square"), viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width);        
        assertTrue(shape instanceof Rectangle2D);
    }
    
    @Test
    public void readWeatherFactory(TestInfo testInfo) throws ParameterException{        
        ViewBox viewBox = new ViewBox(50f);
        Shape shape = ShapeFinder.getShape(new Literal("weather://F003"), viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width); 
        shape = ShapeFinder.getShape(new Literal("weather://cloudy"), viewBox, 1d, 96d, Uom.PX);        
        assertEquals(50,  shape.getBounds().height);
        assertEquals(50,  shape.getBounds().width); 
    }
    
}
