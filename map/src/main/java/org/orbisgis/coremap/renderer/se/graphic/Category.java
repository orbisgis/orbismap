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

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.renderer.se.FillNode;
import org.orbisgis.coremap.renderer.se.GraphicNode;
import org.orbisgis.coremap.renderer.se.StrokeNode;
import org.orbisgis.coremap.renderer.se.fill.Fill;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.stroke.Stroke;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;

/**
 * A {@code Category} is a part of an {@link AxisChart}. It embeds a value, and
 * hints that must be used to render it. It depends on the following parameters :
 * <ul>
 * <li>A measure, ie the value to represent with this {@code Category}.</li>
 * <li>A {@link Fill} to fill its representation.</li>
 * <li>A {@link Stroke} that will be used to draw its boundaries.</li>
 * <li>A {@link GraphicCollection} to represent it.</li>
 * <li>A name (as a String).</li>
 * </ul>
 * @author Maxence Laurent
 * @todo add support for stacked bar (means category fill / stroke are mandatory) and others are forbiden
 */
public final class Category  extends StyleNode implements FillNode, StrokeNode, GraphicNode {

        private RealParameter measure;

        /* in order to draw bars, optionnal */
        private Fill fill;
        private Stroke stroke;

        /* In order to draw points, optionnal */
        private GraphicCollection graphic;
        private String name;

        /**
         * Build a new, empty, {@code Category}.
         */
        public Category() {
                graphic = new GraphicCollection();
                name = "";
                graphic.setParent(this);
        }

        
        @Override
        public Fill getFill() {
                return fill;
        }

        @Override
        public void setFill(Fill fill) {
                this.fill = fill;
                fill.setParent(this);
        }

        @Override
        public Stroke getStroke() {
                return stroke;
        }

        @Override
        public void setStroke(Stroke stroke) {
                this.stroke = stroke;
                stroke.setParent(this);
        }

        @Override
        public GraphicCollection getGraphicCollection() {
                return graphic;
        }

        @Override
        public void setGraphicCollection(GraphicCollection graphic) {
                this.graphic = graphic;
        }

        /**
         * Set the name of this {@code Category}.
         * @param name
         */
        public void setName(String name) {
                this.name = name;
        }

        /**
         * Get the name of this {@code Category}.
         * @return
         * The name of this {@code Category}, as a String instance.
         */
        public String getName() {
                return name;
        }

        /**
         * The measure associated to this {@code Category}.
         * @return
         * A {@link RealParameter}. That means this {@code Category} can
         * be linked to a value in a table, for instance.
         */
        public RealParameter getMeasure() {
                return measure;
        }

        /**
         * Set the {@link RealParameter} used to retrieve the value associated
         * to this {@code Category}.
         * @param measure
         */
        public void setMeasure(RealParameter measure) {
                this.measure = measure;
                this.measure.setParent(this);
        }

        
        @Override
        public List<IStyleNode> getChildren() {
            List<IStyleNode> ls = new ArrayList<IStyleNode>();
            if (this.getFill() != null) {
                ls.add(this.getFill());
            }
            if (this.getStroke() != null) {
                ls.add(this.getStroke());
            }
            if (this.getGraphicCollection() != null) {
                ls.add(this.getGraphicCollection());
            }
            if (this.getMeasure() != null) {
                ls.add(this.getMeasure());
            }
            return ls;
        }
}
