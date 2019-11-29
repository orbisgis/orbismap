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

import org.locationtech.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.visitors.FeaturesVisitor;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;

/**
 * Entry point for all kind of symbolizer
 * This abstract class contains only the name, the way to retrieve the geometry
 * and a description of the symbolizer.
 * @todo Add a general draw method that fit well for vectors and raster; implement fetch default geometry
 * @author Maxence Laurent, Alexis Guéganno
 */
public abstract class Symbolizer extends StyleNode implements  Comparable {

    /**
     * The default name affected to a new Symbolizer instance.
     */
    public static final String DEFAULT_NAME = "Default Symbolizer";
    /**
     * The current version of the Symbolizer
     */
    public static final String VERSION = "2.0.0";
    protected String name;
    protected String desc;
    //protected GeometryAttribute the_geom;
    protected int level;
    private Set<String> features;
    private Map<String,Object> featuresMap;
    private FeaturesVisitor featuresVisitor = new FeaturesVisitor();

    /**
     * Build an empty Symbolizer, with the default name and no description.
     */
    public Symbolizer() {
        name = Symbolizer.DEFAULT_NAME;
        desc = "";
        level = -1;
    }    

    /**
     * Gets the name of this Symbolizer.
     * @return 
     *  the name of the Symbolizer.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the name of this <code>Symbolizer</code>.
     * @return 
     *  the name of the <code>Symbolizer</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Set <code>name</code> as the name of this <code>Symbolizer</code>
     * @param name 
     */
    public void setName(String name) {
        if (name == null || name.equalsIgnoreCase("")) {
            this.name = Symbolizer.DEFAULT_NAME;
        } else {
            this.name = name;
        }
    }

    /**
     * Get the description associated to this <code>Symbolizer</code>.
     * @return 
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Set the description associated to this <code>Symbolizer</code>.
     * @param description 
     */
    public void setDescription(String description) {
        desc = description;
    }

    /**
     * Get the display level of this <code>Symbolizer</code>
     * @return 
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the display level of this <code>Symbolizer</code>
     * @param level 
     */
    public void setLevel(int level) {
        this.level = level;
    }   
    

    /**
     * Makes a comparison between this and o. Be aware that <b>this operation is absolutely
     * not concistent with <code>equals(Object o)</code> !!!</b>
     * @param o
     * @return 
     * <ul><li>-1 if <code>(!o instanceof Symbolizer) || o.level &lt; this.level </code></li>
     * <li>0 if <code>(o instanceof Symbolizer) &amp;&amp; s.level == this.level</code></li>
     * <li>1 otherwise</li>
     * </ul> 
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Symbolizer) {
            Symbolizer s = (Symbolizer) o;

            if (s.level < this.level) {
                return 1;
            } else if (s.level == this.level) {
                return 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Go through parents and return the rule
     */
    public FeatureRule getRule() {
        IStyleNode pIt = getParent();
        while (pIt != null && !(pIt instanceof FeatureRule)) {
            pIt = pIt.getParent();
        }

        return (FeatureRule) pIt;
    }

    /**
     * Gets the features that are needed to build this Symbolizer in a {@code
     * Map<String,Object>}. This method is based on {@see
     * SymbolizerNode#dependsOnFeature()}. Using the field names retrieved with
     * this method, we search for {@code Values} at index {@code fid} in {@code
     * sds}.
     * @param sds
     * @param fid
     * @return
     * @throws SQLException
     */
    public Map<String,Object> getFeaturesMap(ResultSet sds, long fid) throws SQLException{
        if(features==null){
            acceptVisitor(featuresVisitor);
            features = featuresVisitor.getResult();
        }
        if(featuresMap == null){
            featuresMap = new HashMap<String,Object>();
        }
        if(sds != null) {
            for(String s : features){
                featuresMap.put(s, sds.getObject(s));
            }
        }
        return featuresMap;
    }

    public void refreshFeatures(){
            features = null;
    }

    @Override
    public void update(){
            refreshFeatures();
            if(getParent() != null){
                getParent().update();
            }
    }
    /**
     * Draw the symbols in g2, using infos that are found in sds at index fid.
     * @param g2
     * @param rs
     * @param fid
     * @param selected
     * @param mt
     * @param theGeom
     * @param perm
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public abstract void draw(Graphics2D g2, ResultSet rs, long fid,
            boolean selected, MapTransform mt, Geometry theGeom)
            throws ParameterException, IOException, SQLException;
    
    

}
