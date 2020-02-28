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

import java.util.List;

/**
 * Rule is used to organize symbolizing instructions and potentially to define
 * conditions of application of these associated symbolizers (e.g.,
 * feature-property conditions or map scales).
 *
 * @author Erwan Bocher
 * @param <T>
 */
public interface IRule<T extends ISymbolizer> extends IName, IStyleNode{
        
    
    /**
     * Gets the list of {@link ISymbolizer} contained in this Rule.
     *
     * @return
     */
    List<T> getSymbolizers();

    /**
     * Adds a <code>ISymbolizer</code> to the list of symbolizer at position
     * index.
     *
     * @param index of the symbolizer
     * @param symbolizer to add
     */
    void addSymbolizer(int index, T symbolizer);

    void addSymbolizer(T symbolizer);
}
