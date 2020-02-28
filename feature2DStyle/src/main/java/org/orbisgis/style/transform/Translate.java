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
import org.orbisgis.style.parameter.ExpressionParameter;

/**
 * Represents a translation in an euclidean plane. As it can be represented with
 * a 2D vector, it is defined by two <code>RealParameter</code>s.
 * @author Maxence Laurent
 */
public class Translate extends StyleNode implements Transformation {

        private ExpressionParameter x;
        private ExpressionParameter y;

        /**
         * Create a new <code>Translate</code>
         * @param x The translation about X-axis
         * @param y The translation about Y-axis
         */
        public Translate(ExpressionParameter x, ExpressionParameter y) {
                setX(x);
                setY(y);
        }

        /**
         * Create an new empty <code>Translate</code>
         */
        public Translate(){
        }

        

        @Override
        public boolean allowedForGeometries() {
                return true;
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

        

        /**
         * Get the translation about the X-axis
         * @return The translation about the X-axis
         */
        public ExpressionParameter getX() {
                return x;
        }

        /**
         * Get the translation about the Y-axis
         * @return The translation about the Y-axis
         */
        public ExpressionParameter getY() {
                return y;
        }

        /**
         * Set the translation about the Y-axis
         * @param y 
         */
        public final void setY(ExpressionParameter y) {
                this.y = y;
                if (y != null) {
                        this.y.setParent(this);
                }
        }

        /**
         * Set the translation about the X-axis
         * @param x
         * @param y 
         */
        public final void setX(ExpressionParameter x) {
                this.x = x;
                if (x != null) {
                        this.x.setParent(this);
                }
        }

        @Override
        public String toString() {
                return "Translate";
        }
}
