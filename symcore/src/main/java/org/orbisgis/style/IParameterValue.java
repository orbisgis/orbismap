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
 * The ​ ParameterValue class represents a gateway that provides the value to be
 * used by a parameter in a styling context of use (almost all styling
 * parameters such as width, opacity, displacement, etc are "parameter-values").
 * This class has a similar meaning to Expression as defined in the OGC Filter
 * Encoding 2.0 standard. As an interface, it is designed to be extended
 * (e.g., Literal).
 *
 * @author ebocher
 */
public interface IParameterValue extends IStyleNode{
    
}
