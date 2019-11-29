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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
//import org.slf4j.*;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.common.Description;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.slf4j.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Usable representation of SE styles. This is the upper node of the symbology
 * encoding implementation. It offers validation and edition mechanisms as well
 * as the ability to render maps from a SE style.
 * @author Maxence Laurent
 * @author Alexis Guéganno
 */
public final class Style extends StyleNode {

    public static final String PROP_VISIBLE = "visible";
    private static final String DEFAULT_NAME = "Unnamed Style";
    private static final Logger LOGGER = LoggerFactory.getLogger(Style.class);
    private String name;
    private ArrayList<FeatureRule> rules;
    private ILayer layer;
    private boolean visible = true;
    private Description description = new Description();

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);    
    
    /**
     * Create a new {@code Style} associated to the given {@code ILayer}. If the
     * given boolean is tru, a default {@code Rule} will be added to the Style.
     * If not, the {@code Style} will be let empty.
     * @param layer
     * @param addDefaultRule
     */
    public Style(ILayer layer, boolean addDefaultRule) {
        rules = new ArrayList<FeatureRule>();
        this.layer = layer;
        name = DEFAULT_NAME;
        if (addDefaultRule) {
            this.addRule(new FeatureRule(layer));
        }
    }
    
    /**
     * Gets the description associated to this style.
     * @return The description associated to this style.
     */
    public Description getDescription(){
        return description;
    }

    /**
     *  This method copies all rules from given style and merge them within the current
     * style. Resulting style is done by stacking new rules over rules from current style.
     * (i.e. symbolizer level of new style > level from current one)
     *
     * This may alter the behaviour of ElseRules !
     * @todo let the layer have several style ?
     *
     * @param style
     */
    public void merge(Style style) {
        int offset = findBiggestLevel();

        for (FeatureRule r : style.getRules()) {
            this.addRule(r);
            for (Symbolizer s : r.getCompositeSymbolizer().getSymbolizerList()) {
                s.setLevel(s.getLevel() + offset);
            }
        }
    }

    private int findBiggestLevel() {
        int level = 0;

        for (FeatureRule r : rules) {
            for (Symbolizer s : r.getCompositeSymbolizer().getSymbolizerList()) {
                level = Math.max(level, s.getLevel());
            }
        }
        return level;
    }

    /**
     * This method remove everything in this feature type style
     */
    public void clear() {
        this.rules.clear();
    }
    
    /**
     * Return all symbolizers from rules with a filter but not those from
     * a ElseFilter (i.e. fallback) rule
     *
     * @param mt
     * @param layerSymbolizers
     * @param overlaySymbolizers
     *
     * @param rules
     * @param fallbackRules
     * @todo take into account domain constraint
     */
    public void getSymbolizers(MapTransform mt,
            List<Symbolizer> layerSymbolizers,
            //ArrayList<Symbolizer> overlaySymbolizers,
            List<FeatureRule> rules,
            List<FeatureRule> fallbackRules) {
        if(visible){
            for (FeatureRule r : this.rules) {
            // Only process visible rules with valid domain
                if (r.isDomainAllowed(mt)) {
                    // Split standard rules and elseFilter rules
                    
                        rules.add(r);
                    
                        r.getCompositeSymbolizer().getSymbolizerList().forEach((s) -> {
                            // Extract TextSymbolizer into specific set =>
                            // Label are always drawn on top
                            //if (s instanceof TextSymbolizer) {
                            //overlaySymbolizers.add(s);
                            //} else {
                            layerSymbolizers.add(s);
                            //}
                    });
                }
            }
        }
    }

    public void resetSymbolizerLevels() {
        int level = 1;

        for (FeatureRule r : rules) {
            for (Symbolizer s : r.getCompositeSymbolizer().getSymbolizerList()) {
                if (s instanceof TextSymbolizer) {
                    s.setLevel(Integer.MAX_VALUE);
                } else {
                    s.setLevel(level);
                    level++;
                }
            }
        }
    }

    /**
     * Gets the {@code Layer} associated to this {@code Style}.
     * @return
     */
    public ILayer getLayer() {
        return layer;
    }

    /**
     * Sets the {@code Layer} associated to this {@code Style}.
     * @param layer
     */
    public void setLayer(ILayer layer) {
        this.layer = layer;
    }

    @Override
    public IStyleNode getParent() {
        return null;
    }

    @Override
    public void setParent(IStyleNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
    }

    /**
     * Gets the name of this Style.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
    * Sets the name of this Style.
    * @param name
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of {@link FeatureRule} contained in this Style.
     * @return
     */
    public List<FeatureRule> getRules() {
        return rules;
    }

    /**
     * Moves the ith {@link FeatureRule} to position i-1 in the list of rules.
     * @param i
     * @return
     */
    public boolean moveRuleUp(int i) {
        try {
            if (i > 0) {
                FeatureRule r = rules.remove(i);
                rules.add(i - 1, r);
                return true;
            }
        } catch (IndexOutOfBoundsException ex) {
        }
        return false;
    }

    /**
     * Moves the ith {@link FeatureRule} to position i+1 in the list of rules.
     * @param i
     * @return
     */
    public boolean moveRuleDown(int i) {
        try {
            if (i < rules.size() - 1) {
                FeatureRule r = rules.remove(i);
                rules.add(i + 1, r);
                return true;
            }

        } catch (IndexOutOfBoundsException ex) {
        }
        return false;
    }

    /**
     * Add a {@link FeatureRule} to this {@code Style}.
     * @param r
     */
    public void addRule(FeatureRule r) {
        if (r != null) {
            r.setParent(this);
            rules.add(r);
        }
    }

    /**
     * Add a {@link FeatureRule} to this {@code Style} at position {@code index}.
     * @param index
     * @param r
     */
    public void addRule(int index, FeatureRule r) {
        if (r != null) {
            r.setParent(this);
            rules.add(index, r);
        }
    }

    /**
     * Delete the ith {@link FeatureRule} from this {@code Style}.
     * @param i
     * @return
     */
    public boolean deleteRule(int i) {
        try {
            rules.remove(i);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
            List<IStyleNode> ls = new ArrayList<IStyleNode>();
            ls.addAll(rules);
            return ls;
    }

    /**
     *
     * @return
     * True if the Rule is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * If set to true, the rule is visible.
     * @param visible
     */
    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        this.visible = visible;
        propertyChangeSupport.firePropertyChange(PROP_VISIBLE, oldValue, visible);
    }
    

    /**
    * Add a property-change listener for all properties.
    * The listener is called for all properties.
    * @param listener The PropertyChangeListener instance
    * @note Use EventHandler.create to build the PropertyChangeListener instance
    */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
    * Add a property-change listener for a specific property.
    * The listener is called only when there is a change to 
    * the specified property.
    * @param prop The static property name PROP_..
    * @param listener The PropertyChangeListener instance
    * @note Use EventHandler.create to build the PropertyChangeListener instance
    */
    public void addPropertyChangeListener(String prop,PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(prop, listener);
    }
    
    /**
    * Remove the specified listener from the list
    * @param listener The listener instance
    */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
    * Remove the specified listener for a specified property from the list
    * @param prop The static property name PROP_..
    * @param listener The listener instance
    */
    public void removePropertyChangeListener(String prop,PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(prop,listener);
    }    
}
