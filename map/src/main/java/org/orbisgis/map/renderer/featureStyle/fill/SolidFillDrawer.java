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
package org.orbisgis.map.renderer.featureStyle.fill;

import java.awt.*;
import java.awt.geom.AffineTransform;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;

import static org.orbisgis.style.fill.SolidFill.GRAY50;
import org.orbisgis.style.utils.ColorUtils;

/**
 * Drawer for the element <code>SolidFill</code>
 * @author Erwan Bocher, CNRS (2020)
 */
public class SolidFillDrawer implements IFillDrawer<SolidFill> {

    private Shape shape;
    private AffineTransform affineTransform;    
    
    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, SolidFill styleNode) throws ParameterException {
        if (shape != null) {
            g2.setPaint(getPaint( styleNode, mapTransform));
            g2.fill(shape);
        }
    }

    @Override
    public Paint getPaint(SolidFill solidFill,MapTransform mt) throws ParameterException {
       Color color = solidFill.getAWTColor();
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
        return  ColorUtils.getColorWithAlpha(color, opacity);
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    @Override
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }
    
}
