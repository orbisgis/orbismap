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
package org.orbisgis.style.visitors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.orbisgis.style.parameter.reference.ValueReference;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IStyleNodeVisitor;

/**
 * Search for the names of the features that are used in the visited tree of
 * {@link SymbolizerNode} instances.
 * @author Alexis Guéganno
 */
public class FeaturesVisitor implements IStyleNodeVisitor {

        private Set<String> res = new HashSet<String>();

        /**
         * Recursively visits {@code sn} and all its children, searching for
         * feature-dependant nodes.
         * @param sn
         */
        @Override
        public void visitSymbolizerNode(IStyleNode sn) {
                if(!res.isEmpty()){
                        res = new HashSet<String>();
                }
                visitImpl(sn);
        }

        /**
         * The method that does the work... It is not callable directly by the
         * clients, as it does not clean the inner HashSet. If you want
         * to use it directly, inherit this class.
         * @param sn
         */
        protected void visitImpl(IStyleNode sn){
                List<IStyleNode> children = sn.getChildren();
                if(sn instanceof ValueReference){
                        res.add(((ValueReference)sn).getReference());
                }
                children.forEach((c) -> {
                    visitImpl(c);
            });
        }

        /**
         * Gets the {@code HashSet<String>} instance that contains all the field
         * names needed to use safely the last visited {@code SymbolizerNode}.
         * @return
         */
        public Set<String> getResult(){
                return res;
        }

}
