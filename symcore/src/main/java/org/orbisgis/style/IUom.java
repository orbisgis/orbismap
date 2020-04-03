/**
 * SYMCORE is part of the OrbisGIS platform.
 *
 * It's implement the OGC Symbology Conceptual Model: Core part.
 *
 * Internal reference number of this OGC ® document: 18-067
 *
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
 * SYMCORE is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * SYMCORE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * SYMCORE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SYMCORE. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style;

/**
 * For styling parameters that define sizing and positioning of graphical
 * objects (width, displacement, etc.) the unit of measure needs to be provided
 * for the rendering engine. Therefore, for different levels of elements (eg.
 * Symbolizer, Stroke, Fill, GraphicSize...) the model allows using different ​
 * uom codes. Consequently, either the unit of measure is determined through the
 * ​ uom code directly associated to each element or it is determined by the
 * innermost parent ​ uom code (e.g., an uom code defined at the Symbolizer
 * level implies that this unit is applied for all sizing and positioning values
 * inside the Symbolizer).
 * 
 * The IUom class offers a way to implement their own UOM.
 * 
 * @see Uom.class
 *
 * @author Ertz Olivier, HEIG-VD (2010-2020)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public interface IUom extends IStyleNode {

    /**
     * Associates a unit of measure to this style node
     *
     * @param u
     */
    void setUom(Uom u);

    /**
     * Get the Uom associated to this node. It differs from {@code getUom} in
     * the sense that the method in SymbolizerNode will search for the nearest
     * Uom int the tree of Nodes, if this node does not contain one, while this
     * method is expected to return null if it can't find an Uom directly.
     *
     * @return A Uom instance, if this has got one, null otherwise.
     */
    Uom getOwnUom();

    /**
     * Get the unit of measure associated with the current node.
     *
     * @return
     */
    Uom getUom();
}
