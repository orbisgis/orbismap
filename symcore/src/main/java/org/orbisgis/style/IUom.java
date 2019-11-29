/*
 * Copyright (C) 2019 Lab-STICC - UMR CNRS 6285
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.orbisgis.style;

/**
 *
 * @author Erwan Bocher
 */
public interface IUom extends IStyleNode{
    
    /**
         * Associates a unit of measure to this style node
         * @param u 
         */
	void setUom(Uom u);
        /**
         * Get the Uom associated to this node. It differs from {@code getUom}
         * in the sense that the method in SymbolizerNode will search for the nearest
         * Uom int the tree of Nodes, if this node does not contain one, while this
         * method is expected to return null if it can't find an Uom directly.
         * @return 
         * A Uom instance, if this has got one, null otherwise.
         */
	Uom getOwnUom();
        /**
         * Get the unit of measure associated with the current node.
         * @return
         */
        Uom getUom();
}
