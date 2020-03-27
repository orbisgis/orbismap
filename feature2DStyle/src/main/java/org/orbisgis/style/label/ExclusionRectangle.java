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
package org.orbisgis.style.label;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * An {@code ExclusionZone} where the forbidden area is defined as a rectangle.
 * It is defined thanks to a x and y values. Their meaning is of course
 * dependant of the inner UOM instance.
 *
 * @author Alexis Guéganno, Maxence Laurent
 */
public class ExclusionRectangle extends ExclusionZone {

    private ParameterValue x = new NullParameterValue();
    private ParameterValue y = new NullParameterValue();

    /**
     * Build a {@code ExclusionZone} with default width and length set to 3.
     */
    public ExclusionRectangle() {
        this.setX(new Literal(3f));
        this.setY(new Literal(3f));
    }

    /**
     * Get the x-length of the rectangle.
     *
     * @return the x-length as a {@code RealParameter}
     */
    public ParameterValue getX() {
        return x;
    }

    /**
     * Set the x-length of the rectangle.
     *
     * @param x
     */
    public void setX(ParameterValue x) {
        if (x == null) {
            this.x = new NullParameterValue();
            this.x = x;
        } else {
            this.x = x;
            this.x.setParent(this);
            this.x.format(Float.class, "value>=0");
        }
    }

    /**
     * Get the y-length of the rectangle.
     *
     * @return the y-length as a {@code RealParameter}
     */
    public ParameterValue getY() {
        return y;
    }

    /**
     * Set the y-length of the rectangle.
     *
     * @param y
     */
    public void setY(ParameterValue y) {
        if (y == null) {
            this.y = new NullParameterValue();
            this.y.setParent(this);
        } else {
            this.y = y;
            this.y.setParent(this);
            this.y.format(Float.class, "value>=0");
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (x != null) {
            ls.add(x);
        }
        if (y != null) {
            ls.add(y);
        }
        return ls;
    }

}
