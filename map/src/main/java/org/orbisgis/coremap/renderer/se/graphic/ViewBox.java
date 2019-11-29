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
package org.orbisgis.coremap.renderer.se.graphic;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.coremap.renderer.se.Utils.UomUtils;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameterContext;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;

/**
 * {@code ViewBox} supplies a simple and convenient method to change the view box of a graphic,
 * in a {@link MarkGraphic} for instance.
 * {@code ViewBox} is built using the following parameters :
 * <ul><li>X : the width of the box.</li>
 * <li>Y : the height of the box.</li></ul>
 * If only one of these two is given, they are considered to be equal.</p>
 * <p>The main difference between this class and Scale is that a {@code Scale}
 * will use a reference graphic, that already has a size, and process an affine transformation
 * on it, while here the size of the graphic will be defined directly using its height
 * and width.</p>
 * <p>The values given for the height and the width can be negative. If that
 * happens, the coordinate of the rendered graphic will be flipped.
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class ViewBox extends  StyleNode {
        private RealParameter x;
        private RealParameter y;

        /**
         * Build a new {@code ViewBox}, with empty parameters.
         */
        public ViewBox() {
                setWidth(null);
                setHeight(null);
        }

        /**
         * Build a new {@code ViewBox}, using the given width.
         */
        public ViewBox(RealParameter width) {
            setWidth(width);
        }

        /**
         * Build a new {@code ViewBox}, using the given width and height.
         */
        public ViewBox(RealParameter width, RealParameter height) {
            setWidth(width);
            setHeight(height);
        }

        
        /**
         * A {@code ViewBox} can be used if and only if one, at least, of its two parameters
         * has been set.
         * @return 
         */
        public boolean usable() {
                return this.x != null || this.y != null;
        }

        /**
         * Set the wifth of this {@code ViewBox}.
         * @param width 
         */
        public void setWidth(RealParameter width) {
                x = width;
                if (x != null) {
                        x.setContext(RealParameterContext.REAL_CONTEXT);
                        x.setParent(this);
                }
        }
        /**
         * Get the wifth of this {@code ViewBox}.
         * @return 
         */
        public RealParameter getWidth() {
            return x;
                //return x == null ? y : x;
        }

        /**
         * Set the height of this {@code ViewBox}.
         * @param height 
         */
        public void setHeight(RealParameter height) {
                y = height;
                if (y != null) {
                        y.setContext(RealParameterContext.REAL_CONTEXT);
                        y.setParent(this);
                }
        }

        /**
         * Get the height of this {@code ViewBox}.
         * @return 
         */
        public RealParameter getHeight() {
            return y;
                //return y == null ? x : y;
        }

        /**
         * Return the final dimension described by this view box, in [px].
         * @param map map
         * @param scale required final ratio (if either width or height isn't defined)
         * @return
         * @throws ParameterException
         */
        public Point2D getDimensionInPixel(Map<String,Object> map, double height,
                    double width, Double scale, Double dpi) throws ParameterException {
                double dx, dy;

                double ratio = height / width;

                if (x != null && y != null) {
                        dx = x.getValue(map);
                        dy = y.getValue(map);
                } else if (x != null) {
                        dx = x.getValue(map);
                        dy = dx * ratio;
                } else if (y != null) {
                        dy = y.getValue(map);
                        dx = dy / ratio;
                } else { // nothing is defined
                        dx = width;
                        dy = height;
                        //return null; 
                }


                dx = UomUtils.toPixel(dx, ((IUom)getParent()).getUom(), dpi, scale, width);
                dy = UomUtils.toPixel(dy, ((IUom)getParent()).getUom(), dpi, scale, height);

                if (Math.abs(dx) <= 0.00021 || Math.abs(dy) <= 0.00021) {
                        dx=0;
                        dy=0;
                }

                return new Point2D.Double(dx, dy);
        }


        /**
         * Gets a String representation of this {@code ViewBox}.
         * @return
         * A String containing the wifth and height of the {@code ViewBox}..
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
