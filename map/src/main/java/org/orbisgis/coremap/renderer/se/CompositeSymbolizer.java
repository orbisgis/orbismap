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
package org.orbisgis.coremap.renderer.se;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.renderer.se.common.Uom;

/**
 * This is the entry point of the <code>Symbolizer</code>'s structure in a <code>Rule</code>
 * A <code>CompositeSymbolize</code> is a collection that embedded other <code>Symbolizer</code>s,
 * and that is not directly associated to any rendering or representation hint.
 * @author Maxence Laurent
 */
public final class CompositeSymbolizer extends AbstractSymbolizerNode implements UomNode {

        private ArrayList<Symbolizer> symbolizers;
        
        /**
         * Build a new, empty, CompositeSymbolizer.
         */
        public CompositeSymbolizer() {
                symbolizers = new ArrayList<Symbolizer>();
        }
        

        /**
         * Get the list of <code>Symbolizer</code>s contained in this <code>CompositeSymbolizer</code>
         * @return 
         */
        public List<Symbolizer> getSymbolizerList() {
                return this.symbolizers;
        }

        /**
         * Add a <code>Symbolizer</code> to the list contained in this <code>CompositeSymbolizer</code>
         * @param s 
         */
        public void addSymbolizer(Symbolizer s) {
                symbolizers.add(s);
                s.setParent(this);
                if (s.getLevel() < 0) {
                        s.setLevel(symbolizers.size());
                }
        }

        /**
         * Adds a <code>Symbolizer</code> to the list contained in this
         * <code>CompositeSymbolizer</code> at position i.
         * @param s
         */
        public void addSymbolizer(int i, Symbolizer s) {
                symbolizers.add(i, s);
                s.setParent(this);
                if (s.getLevel() < 0) {
                        s.setLevel(symbolizers.size());
                }
        }

        /**
         * Sets the value of the ith {@code Symbolizer} to {@code s}.
         * @param i
         * @param s
         */
        public void setSymbolizer(int i, Symbolizer s){
            s.setParent(this);
            symbolizers.set(i, s);
        }

        /**
         * @deprecated
         */
        public void moveSymbolizerDown(Symbolizer s) {
                int index = symbolizers.indexOf(s);
                if (index > -1 && index < symbolizers.size() - 1) {
                        symbolizers.remove(index);
                        symbolizers.add(index + 1, s);
                }
        }

        /**
         * @deprecated
         */
        public void moveSymbolizerUp(Symbolizer s) {
                int index = symbolizers.indexOf(s);
                if (index > 0) {
                        symbolizers.remove(index);
                        symbolizers.add(index - 1, s);
                }
        }

        /**
         * Remove the <code>Symbolizer s</code> to the list contained in this <code>CompositeSymbolizer</code>
         * @param s 
         */
        public void removeSymbolizer(Symbolizer s) {
                symbolizers.remove(s);
        }

        /**
         * As a collection of <code>Symbolizer</code>s, a <code>CompositeSymbolize</code>
         * is not associated to any Uom. Returns always <code>null</code>
         * @return 
         *      null
         */
        @Override
        public Uom getUom() {
                return null;
        }

        @Override
        public Uom getOwnUom() {
                return null;
        }

        @Override
        public void setUom(Uom unit){}


        @Override
        public List<SymbolizerNode> getChildren() {
                List<SymbolizerNode> ls = new ArrayList<SymbolizerNode>();
                ls.addAll(symbolizers);
                return ls;
        }

}
