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
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.IStyleNode;

/**
 * An {@code ExclusionZone} where the forbidden area is defined as a circle. It
 * is defined thanks to radius value. Its meaning is of course dependant of the
 * inner UOM instance. The resulting exclusion zone is a circle centered on the
 * point associated to the {@code LabelPoint},
 *
 * @author Maxence Laurent
 */
public final class ExclusionRadius extends ExclusionZone {

    private RealParameter radius;

    /**
     * Build a new {@code ExclusionRadius} with radius 3.
     */
    public ExclusionRadius() {
        setRadius(new RealLiteral(3));
    }

    /**
     * Build a new {@code ExclusionRadius} with radius value set to
     * {@code radius}.
     *
     * @param radius
     */
    public ExclusionRadius(double radius) {
        setRadius(new RealLiteral(radius));
    }

    /**
     * Get the radius defining this {@code ExclusionRadius}
     *
     * @return The radius as a {@code RealParameter}.
     */
    public RealParameter getRadius() {
        return radius;
    }

    /**
     * Set the radius defining this {@code ExclusionRadius}
     *
     * @param radius
     */
    public void setRadius(RealParameter radius) {
        this.radius = radius;
        if (this.radius != null) {
            this.radius.setContext(RealParameterContext.NON_NEGATIVE_CONTEXT);
            this.radius.setParent(this);
        }
    }    

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (radius != null) {
            ls.add(radius);
        }
        return ls;
    }
}
