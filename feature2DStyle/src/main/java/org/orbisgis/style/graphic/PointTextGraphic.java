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
package org.orbisgis.style.graphic;

import java.util.ArrayList;
import java.util.List;

import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ExpressionParameter;

/**
 * A {@code PointTextGraphic} is used to paint a text label using a given translation. It is consequently
 * dependant on :
 * <ul><li>A x-coordinate</li>
 * <li>A y-coordinate</li>
 * <li>A {@code PointLabel}</li></ul>
 * @author Alexis Guéganno
 */
public final class PointTextGraphic extends Graphic implements IUom {

        private Uom uom;
        private PointLabel pointLabel;
        private ExpressionParameter x;
        private ExpressionParameter y;

        /**
         * Build a new {@code PointTextGraphic}, at the position of its container. 
         */
        public PointTextGraphic() {
                setPointLabel(new PointLabel());
        }
        

        @Override
        public Uom getUom() {
                if (uom != null) {
                          return uom;
                } else if(getParent() instanceof IUom){
                          return ((IUom)getParent()).getUom();
                } else {
                        return Uom.PX;
                }
        }

        @Override
        public Uom getOwnUom() {
                return uom;
        }

        @Override
        public void setUom(Uom uom) {
                this.uom = uom;
        }

        /**
         * Get the inner label, contained in this {@code PointTextGraphic}.
         * @return 
         */
        public PointLabel getPointLabel() {
                return pointLabel;
        }

        /**
         * Set the inner label, contained in this {@code PointTextGraphic}.
         * @param pointLabel 
         */
        public void setPointLabel(PointLabel pointLabel) {
                this.pointLabel = pointLabel;
                if (pointLabel != null) {
                        pointLabel.setParent(this);
                }
        }
        

        /**
         * Get the x-displacement in the associated translation.
         * @return 
         */
        public ExpressionParameter getX() {
                return x;
        }

        /**
         * Set the x-displacement in the associated translation.
         * @param x 
         */
        public void setX(ExpressionParameter x) {
                this.x = x;
                if (this.x != null) {
                        this.x.setParent(this);
                }
        }

        /**
         * Get the y-displacement in the associated translation.
         * @return 
         */
        public ExpressionParameter getY() {
                return y;
        }

        /**
         * Set the y-displacement in the associated translation.
         * @param y 
         */
        public void setY(ExpressionParameter y) {
                this.y = y;
                if (this.y != null) {
                        this.y.setParent(this);
                }
        }


        @Override
        public List<IStyleNode> getChildren() {
                List<IStyleNode> ls = new ArrayList<IStyleNode>();
                if (pointLabel != null) {
                        ls.add(pointLabel);
                }
                if (x != null) {
                        ls.add(x);
                }
                if (y != null) {
                        ls.add(y);
                }
                return ls;
        }
}
