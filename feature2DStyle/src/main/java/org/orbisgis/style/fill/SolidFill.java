/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.ColorUtils;

/**
 * A solid fill fills a shape with a solid color (+opacity)
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class SolidFill extends StyleNode implements IFill, UomNode {
    
    private ParameterValue color = new NullParameterValue();
    private ParameterValue opacity = new NullParameterValue();

    /**
     * Default value for opacity : {@value SolidFill#DEFAULT_OPACITY}
     */
    public static final float DEFAULT_OPACITY = 1;
    /**
     * Default colour value : {@value SolidFill#GRAY50}
     */
    public static final float GRAY50 = 128.0f;

    /**
     * Default colour value as an int :
     */
    public static final int GRAY50_INT = 128;
    
    private Uom uom = Uom.PX;

    /**
     * Fill with random color and default opacity.
     */
    public SolidFill() {
    }

    /**
     * Set the colour value for this SolidFill.
     *
     * @param color
     */
    public void setColor(String color) {
        setColor(new Literal(color));
    }

    /**
     * Set the colour value for this SolidFill.
     *
     * @param color
     */
    public void setColor(Color color) {
        setColor(new Literal(ColorUtils.toHex(color)));
    }

    /**
     * Set the colour value for this SolidFill.
     *
     * @param color
     */
    public void setColor(ParameterValue color) {
        if (color == null) {
            this.color = new NullParameterValue();            
            this.color.setParent(this);
        } else {            
            this.color = color;
            this.color.setParent(this);
            this.color.format(String.class);
        }
    }

    /**
     * Get the current colour value for this SolidFill.
     *
     * @return
     */
    public ParameterValue getColor() {
        return color;
    }

    /**
     * Set the opacity value for this SolidFill.
     *
     * @param opacity
     */
    public void setOpacity(float opacity) {
        setOpacity(new Literal(opacity));
    }

    /**
     * Set the opacity value for this SolidFill.
     *
     * @param opacity
     */
    public void setOpacity(ParameterValue opacity) {
        if (opacity == null) {
            this.opacity = new NullParameterValue();
            this.opacity.setParent(this);
        } else {
            this.opacity = opacity;
            this.opacity.setParent(this);
            this.opacity.format(Float.class, "value >=0 and value <=1");
        }
    }

    /**
     * Get the current opacity associated to this SolidFill.
     *
     * @return
     */
    public ParameterValue getOpacity() {
        return opacity;
    }
    
    @Override
    public String toString() {
        return "Color: " + color + " alpha: " + opacity;
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (color != null) {
            ls.add(color);
        }
        if (opacity != null) {
            ls.add(opacity);
        }
        return ls;
    }
    
    @Override
    public Uom getUom() {
        return uom == null ? ((UomNode) getParent()).getUom() : uom;
    }
    
    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }
    
    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void initDefault() {
        setColor(Color.BLACK);
        setOpacity(1f);
    }
}
