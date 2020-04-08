/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.style.transform;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * {@code Scale} is used to apply an homothetic transformation on a Graphic. It
 * depends on the following parameter :
 * <ul><li>X : The horizontal multiplication factor</li>
 * <li>Y : The vertical multiplication factor</li></ul>
 *
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class Scale extends StyleNode implements Transformation {

    private ParameterValue x;
    private ParameterValue y;

    /**
     * Build a new {@code Scale} with the given horizontal and vertical factors.
     * @param x The horizontal factor.
     * @param y The vertical factor.
     */
    public Scale(ParameterValue x, ParameterValue y) {
        setX(x);
        setY(y);
    }

    /**
     * Build a new {@code Scale} with the given factor, that will be used for
     * both vertical and horizontal values.
     * @param xy
     */
    public Scale(ParameterValue xy) {
        setX(xy);
        setY(xy);
    }

    

    /**
     * Get the horizontal multiplication factor.
     * @return
     * A {@code RealParameter} in a {@link RealParameterContext#REAL_CONTEXT}.
     */
    public ParameterValue getX() {
        return x;
    }

    /**
     * Set the horizontal multiplication factor.
     * @param x
     * A {@code RealParameter} that is placed by this method in a
     * {@link RealParameterContext#REAL_CONTEXT}.
     */
    public void setX(ParameterValue x) {
        this.x = x;
        if (this.x != null) {
            this.x.setParent(this);
        }
    }

    /**
     * Get the vertical multiplication factor.
     * @return
     * A {@code RealParameter} in a {@link RealParameterContext#REAL_CONTEXT}.
     */
    public ParameterValue getY() {
        return y;
    }

    /**
     * Set the vertical multiplication factor.
     * @param y
     * A {@code RealParameter} that is placed by this method in a
     * {@link RealParameterContext#REAL_CONTEXT}.
     */
    public void setY(ParameterValue y) {
        this.y = y;
        if (this.y != null) {
            this.y.setParent(this);
        }
    }


    @Override
    public boolean allowedForGeometries() {
        return false;
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
    

    @Override
    public String toString() {
        return "Scale";
    }

    @Override
    public void initDefault() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
