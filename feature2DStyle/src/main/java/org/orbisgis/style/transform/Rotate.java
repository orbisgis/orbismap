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
 * {@code Rotate} is a transformation that performs a rotation of the affected
 * object. It is built using : 
 * <ul><li>The X-coordinate of the rotation center. This value takes place in 
 * the coordinate system of the graphic this {@code Rotate} is used on.</li>
 * <li>The Y-coordinate of the rotation center. This value takes place in 
 * the coordinate system of the graphic this {@code Rotate} is used on.</li>
 * <li>The rotation angle, in clockwise degrees.</li></ul>
 * @author Maxence Laurent
 */
public final class Rotate extends StyleNode implements Transformation {

        private ParameterValue x;
        private ParameterValue y;
        private ParameterValue rotation;

        /**
         * Build a new {@code Rotate} with angle value set to {@code rotation}.
         * @param rotation 
         */
        public Rotate(ParameterValue rotation) {
                setRotation(rotation);
                setX(null);
                setY(null);
        }

        /**
         * Build a new {@code Rotate} with angle value set to {@code rotation}, 
         * and with the rotation center placed at (ox, oy) (in the containing
         * graphic coordinate system).
         * @param rotation
         * @param ox
         * @param oy 
         */
        public Rotate(ParameterValue rotation, ParameterValue ox, ParameterValue oy) {
                setRotation(rotation);
                setX(ox);
                setY(oy);
        }

        

        /**
         * Get the rotation defined in this {@code Rotate} instance.
         * @return 
         * The rotation, in clockwise degrees. Indeed, as the Y axis is oriented
         * bottom up, it is the direct rotation sense...
         */
        public ParameterValue getRotation() {
                return rotation;
        }

        /**
         * Set the rotation defined in this {@code Rotate} instance.
         * @param rotation 
         */
        public void setRotation(ParameterValue rotation) {
                this.rotation = rotation;
                if (rotation != null) {
                        this.rotation.setParent(this);
                }
        }

        /**
         * Get the x-coordinate of the rotation center.
         * @return 
         * The x-coordinate as a {@code RealParameter} instance. Note that the 
         * returned coordinate is placed in the coordinate system associated to
         * the graphic this {@code Rotate} operation is applied on.
         */
        public ParameterValue getX() {
                return x;
        }

        /**
         * Set the x-coordinate of this {@code Rotate} center.
         * Note that this coordinate is placed in the coordinate system 
         * associated to the graphic this {@code Rotate} operation is applied on.
         * @param x 
         * A {@code RealParameter} that is placed by this method in a 
         * {@link RealParameterContext#REAL_CONTEXT}
         */
        public void setX(ParameterValue x) {
                this.x = x;
                if (this.x != null) {
                        this.x.setParent(this);
                }
        }

        /**
         * Get the y-coordinate of the rotation center.
         * @return 
         * The y-coordinate as a {@code RealParameter} instance. Note that the 
         * returned coordinate is placed in the coordinate system associated to
         * the graphic this {@code Rotate} operation is applied on.
         */
        public ParameterValue getY() {
                return y;
        }

        /**
         * Set the y-coordinate of this {@code Rotate} center.
         * Note that this coordinate is placed in the coordinate system 
         * associated to the graphic this {@code Rotate} operation is applied on.
         * @param y 
         * A {@code RealParameter} that is placed by this method in a 
         * {@link RealParameterContext#REAL_CONTEXT}
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
                if (rotation != null) {
                        ls.add(rotation);
                }
                return ls;
        }        

        @Override
        public String toString() {
                return "Rotate";
        }

    @Override
    public void initDefault() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
