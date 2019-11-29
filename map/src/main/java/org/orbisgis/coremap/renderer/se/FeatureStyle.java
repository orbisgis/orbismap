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
import java.util.Locale;
//import org.slf4j.*;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.common.Description;
import org.orbisgis.style.IRule;
import org.orbisgis.style.IStyle;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.ISymbolizer;
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
public final class FeatureStyle extends StyleNode implements IStyle{

    public static final String PROP_VISIBLE = "visible";
    private static final String DEFAULT_NAME = "Unnamed Style";
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureStyle.class);
    private String name;
    private ArrayList<IRule> rules;
    private boolean visible = true;
    private Description description = new Description();

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);    
    
    /**
     * Create a new {@code Style} with a default {@code Rule}.
     */
    public FeatureStyle() {        
        name = DEFAULT_NAME;
        rules = new ArrayList<IRule>();        
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
            List<ISymbolizer> layerSymbolizers,
            //ArrayList<Symbolizer> overlaySymbolizers,
            List<FeatureRule> rules,
            List<FeatureRule> fallbackRules) {
        if(visible){
            for (IRule r : this.rules) {
               FeatureRule featureRule = (FeatureRule) r;
            // Only process visible rules with valid domain
                if (featureRule.isDomainAllowed(mt)) {
                    // Split standard rules and elseFilter rules
                    
                        rules.add(featureRule);
                    
                        featureRule.getSymbolizers().forEach((s) -> {
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

    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    
    @Override
    public List<IRule> getRules() {
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
                IRule r = rules.remove(i);
                rules.add(i - 1, r);
                return true;
            }
        } catch (IndexOutOfBoundsException ex) {
        }
        return false;
    }

    @Override
    public boolean moveRuleDown(int i) {
        try {
            if (i < rules.size() - 1) {
                IRule r = rules.remove(i);
                rules.add(i + 1, r);
                return true;
            }

        } catch (IndexOutOfBoundsException ex) {
        }
        return false;
    }

   
    @Override
    public void addRule(IRule rule) {
        if (rule != null) {
            rule.setParent(this);
            rules.add(rule);
        }
    }

    
    @Override
    public void addRule(int index, IRule rule) {
        if (rule != null) {
            rule.setParent(this);
            rules.add(index, rule);
        }
    }

    
    @Override
    public boolean deleteRule(int index) {
        try {
            rules.remove(index);
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

    @Override
    public String getTitle(Locale locale) {
            return description.getTitle(locale);
    }

    @Override
    public String addTitle(Locale locale, String text) {
        return description.addTitle(locale, text);
    }

    @Override
    public String getAbstract(Locale locale) {
        return description.getAbstract(locale);
    }

    @Override
    public String addAbstract(Locale locale, String text) {
         return description.addAbstract(locale, text);
    }
}
