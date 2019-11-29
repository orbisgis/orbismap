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
 * IFill defines the graphical symbolizing parameters required to draw the
 * filling of a two-dimensional shape such as a polygon. As an abstract class
 * and part of the base of the core graphical concepts, ​ FillClass is a global
 * point of extension for specifying concrete definitions for shape fill
 * operations (e.g., the ​ SolidFill ​ and ​ GraphicFill ​ extensions).
 *
 * @author Erwan Bocher
 */
public interface IFill extends IStyleNode {
    
    /**
     * Unit of measure
     * @return 
     */
     Uom getUom();

     
    /**
     * 
     * @param uom 
     */
     void setUom(Uom uom);
    
    
}
