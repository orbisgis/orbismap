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
package org.orbisgis.style.graphic;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 * {@code ViewBox} supplies a simple and convenient method to change the view
 * box of a graphic, in a {@link MarkGraphic} for instance. {@code ViewBox} is
 * built using the following parameters :
 * <ul><li>X : the width of the box.</li>
 * <li>Y : the height of the box.</li></ul>
 * If only one of these two is given, they are considered to be equal.</p>
 * <p>
 * The main difference between this class and Scale is that a {@code Scale} will
 * use a reference graphic, that already has a size, and process an affine
 * transformation on it, while here the size of the graphic will be defined
 * directly using its height and width.</p>
 * <p>
 * The values given for the height and the width can be negative. If that
 * happens, the coordinate of the rendered graphic will be flipped.
 *
 * @author Alexis Guéganno, Maxence Laurent
 */
public class ViewBox extends StyleNode {

    private ParameterValue x;
    private ParameterValue y;

    /**
     * Build a new {@code ViewBox}, with empty parameters.
     */
    public ViewBox() {
        setWidth(new NullParameterValue());
        setHeight(new NullParameterValue());
    }

    /**
     * Build a new {@code ViewBox}, using the given width.
     *
     * @param width
     */
    public ViewBox(ParameterValue width) {
        setWidth(width);
    }

    /**
     * Build a new {@code ViewBox}, using the given width and height.
     */
    public ViewBox(ParameterValue width, ParameterValue height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Set the wifth of this {@code ViewBox}.
     *
     * @param width
     */
    public void setWidth(ParameterValue width) {
        ParameterValueHelper.validateAsFloat(width);
        x = width;
        if (x != null) {
            x.setParent(this);
        }
    }

    /**
     * Get the wifth of this {@code ViewBox}.
     *
     * @return
     */
    public ParameterValue getWidth() {
        return x == null ? y : x;
    }

    /**
     * Set the height of this {@code ViewBox}.
     *
     * @param height
     */
    public void setHeight(ParameterValue height) {
        ParameterValueHelper.validateAsFloat(height);
        y = height;
        if (y != null) {
            y.setParent(this);
        }
    }

    /**
     * Get the height of this {@code ViewBox}.
     *
     * @return
     */
    public ParameterValue getHeight() {
        return y == null ? x : y;
    }

    /**
     * Gets a String representation of this {@code ViewBox}.
     *
     * @return A String containing the wifth and height of the {@code ViewBox}..
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ViewBox:");
        if (this.x != null) {
            result.append("  Width: ").append(x.toString());
        }
        if (this.y != null) {
            result.append("  Height: ").append(y.toString());
        }
        return result.toString();
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (y != null) {
            ls.add(y);
        }
        if (x != null) {
            ls.add(x);
        }
        return ls;
    }
}
