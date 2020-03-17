/**
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
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2017
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
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
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.ParameterValueHelper;
import org.orbisgis.style.parameter.Literal;

/**
 * A solid fill fills a shape with a solid color (+opacity)
 *
 * @author Maxence Laurent
 */
public  class SolidFill extends StyleNode implements IFill , UomNode {

    private ParameterValue color;
    private ParameterValue opacity;

    /**
     * Default value for opacity : {@value SolidFill#DEFAULT_OPACITY}
     */
    public static final double DEFAULT_OPACITY = 1;
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
        this(ParameterValueHelper.randomColor(), new Literal(1f));
    }

    /**
     * Fill with specified color and default opacity
     *
     * @param c
     */
    public SolidFill(Color c) {
        this(ParameterValueHelper.toExpression(c), new Literal(1f));
    }

    /**
     * Fill with specified color and opacity
     *
     * @param c
     * @param opacity
     */
    public SolidFill(Color c, double opacity) {
        this(ParameterValueHelper.toExpression(c), new Literal(opacity));
    }

    /**
     * Fill with specified color and opacity
     *
     * @param c
     * @param opacity
     */
    public SolidFill(ParameterValue c, ParameterValue opacity) {
        this.setColor(c);
        this.setOpacity(opacity);
    }

    /**
     * Set the colour value for this SolidFill.
     *
     * @param color
     */
    public void setColor(ParameterValue color) {
        this.color = color;
        ParameterValueHelper.validateAsString(this.color);
        //this.color.setDataType(String.class);
        if (this.color != null) {
            this.color.setParent(this);
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
    public void setOpacity(ParameterValue opacity) {
        this.opacity = opacity == null ? new Literal(1f) : opacity;
        ParameterValueHelper.validateAsFloat(this.opacity);
        this.opacity.setParent(this);
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
        return uom == null ? ((UomNode)getParent()).getUom() : uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom =uom;
     }    

    @Override
    public Uom getOwnUom() {
        return uom;
    }
}
