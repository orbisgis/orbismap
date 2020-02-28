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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.color.ColorParameter;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.ExpressionParameter;

/**
 * A solid fill fills a shape with a solid color (+opacity)
 *
 * @author Maxence Laurent
 */
public final class SolidFill extends Fill {

    private ColorParameter color;
    private ExpressionParameter opacity;

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

    /**
     * Fill with random color and default opacity.
     */
    public SolidFill() {
        this(new ColorParameter(), new ExpressionParameter("-1"));
    }

    /**
     * Fill with specified color and default opacity
     *
     * @param c
     */
    public SolidFill(Color c) {
        this(new ColorParameter(c), new ExpressionParameter("-1"));
    }

    /**
     * Fill with specified color and opacity
     *
     * @param c
     * @param opacity
     */
    public SolidFill(Color c, double opacity) {
        this(new ColorParameter(c), new ExpressionParameter(String.valueOf(opacity)));
    }

    /**
     * Fill with specified color and opacity
     *
     * @param c
     * @param opacity
     */
    public SolidFill(ColorParameter c, ExpressionParameter opacity) {
        this.setColor(c);
        this.setOpacity(opacity);
    }

    /**
     * Set the colour value for this SolidFill.
     *
     * @param color
     */
    public void setColor(ColorParameter color) {
        this.color = color;
        if (this.color != null) {
            this.color.setParent(this);
        }
    }

    /**
     * Get the current colour value for this SolidFill.
     *
     * @return
     */
    public ColorParameter getColor() {
        return color;
    }

    /**
     * Set the opacity value for this SolidFill.
     *
     * @param opacity
     */
    public void setOpacity(ExpressionParameter opacity) {
        this.opacity = opacity == null ? new ExpressionParameter("1") : opacity;
        this.opacity.setParent(this);
    }

    /**
     * Get the current opacity associated to this SolidFill.
     *
     * @return
     */
    public ExpressionParameter getOpacity() {
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
    public void draw(Graphics2D g2, Map<String, Object> map, Shape shp, IMapTransform mt) throws ParameterException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paint getPaint(Map<String, Object> map, IMapTransform mt) throws ParameterException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
