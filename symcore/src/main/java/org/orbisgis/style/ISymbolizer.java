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
 * This class describes how to portray geographic data given a shape (e.g., area
 * fill, line stroke, point marker, etc.) and graphical properties (e.g., color,
 * opacity, font-family, etc.). As an abstract class, it is designed to be
 * extended.
 *
 * @author Erwan Bocher
 */
interface ISymbolizer extends IName, IStyleNode {

    /**
     * Unit of measure
     *
     * @return
     */
    Uom getUom();

    /**
     *
     * @param uom
     */
    void setUom(Uom uom);

    /**
     * Return a level order for this symbol
     *
     * @return
     */
    int getLevel();

    /**
     * Set a level order for this symbol
     *
     * @param level
     */
    void setLevel(int level);
    
    /**
     * The parameter value that defines
     * the geometry to apply the symbol
     * @return a parameter value
     */
    IParameterValue getGeometryParameter();
   
     /**
     * A symbolizer must be related to a parameter value that defines
     * the geometry to apply the symbol
     */
    void setGeometryParameter(IParameterValue parameterValue);

}
