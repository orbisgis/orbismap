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
 * This class is the root concept of the Symbology Conceptual Core Model. This
 * class organizes the rules of symbolizing instructions to be applied by a
 * rendering engine on a layer of geographic features (e.g., vector based
 * spatial data or raster data). As an abstract class, it is designed to be
 * extended (e.g., the FeatureTypeStyle extension for vector data).
 *
 * @author Erwan Bocher
 */
public interface IStyle extends IDescription, IName{
    
           


    /**
     * Gets the list of {@link IRule} contained in this Style.
     *
     * @return
     */
    List<IRule> getRules();  
    
    /**
     * Add a {@link IRule} to this {@code IStyle}.
     * @param rule to add
     */
    void addRule(IRule rule);
    
    /**
     * Add a {@link IRule} to this {@code IStyle} at position {@code index}.
     * @param index of the rule
     * @param rule to add
     */
     void addRule(int index, IRule rule);
     
     /**
     * Delete the {@link IRule} from this {@code IStyle}.
     * @param index of the rule 
     * @return true if the rule has been deleted
     */
     boolean deleteRule(int index);

}
