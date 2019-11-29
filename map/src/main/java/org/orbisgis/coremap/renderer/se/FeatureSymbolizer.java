/**
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
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2017
 * CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.coremap.renderer.se;

import java.awt.Graphics2D;
import org.locationtech.jts.geom.*;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.*;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.geometry.GeometryAttribute;
import org.h2gis.utilities.SpatialResultSet;
import org.orbisgis.coremap.renderer.se.visitors.FeaturesVisitor;
import org.orbisgis.style.ISymbolizer;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;

/**
 * This class contains the common elements shared by
 * <code>PointSymbolizer</code>,<code>LineSymbolizer</code>
 * ,<code>AreaSymbolizer</code> and <code>TextSymbolizer</code>. Those vector
 * layers all contains the elements defined in <code>Symbolizer</code>, and :
 * <ul>
 * <li> - a unit of measure (Uom)</li>
 * <li> - an affine transformation def (transform)</li>
 * </ul>
 *
 * @author Maxence Laurent, Alexis Guéganno
 */
public abstract class FeatureSymbolizer extends StyleNode implements Comparable, ISymbolizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureSymbolizer.class);
    private Uom uom;
    private GeometryAttribute theGeom;
    /**
     * The default name affected to a new Symbolizer instance.
     */
    public static final String DEFAULT_NAME = "Default Symbolizer";
    private String name;
    private String desc;
    private int level;
    
    
    private Set<String> features;
    private Map<String,Object> featuresMap;
    
    private FeaturesVisitor featuresVisitor = new FeaturesVisitor();

    /**
     * Default constructor for this abstract class. Only set the inner unit of
     * measure to {@code Uom.MM}.
     */
    protected FeatureSymbolizer() {
        name = DEFAULT_NAME;
        desc = "";
        level = 0;
        setUom(Uom.PX);
    }

    /**
     * Gets the name of this Symbolizer.
     *
     * @return the name of the Symbolizer.
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.equalsIgnoreCase("")) {
            this.name = DEFAULT_NAME;
        } else {
            this.name = name;
        }
    }

    /**
     * Get the description associated to this <code>Symbolizer</code>.
     *
     * @return
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Set the description associated to this <code>Symbolizer</code>.
     *
     * @param description
     */
    public void setDescription(String description) {
        desc = description;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public final Uom getUom() {
        return uom == null ? Uom.PX : uom;
    }
    
    public final Uom getOwnUom() {
        return uom;
    }

    @Override
    public final void setUom(Uom uom) {
        if (uom != null) {
            this.uom = uom;
        } else {
            this.uom = Uom.PX;
        }
    }

    /**
     * Makes a comparison between this and o. Be aware that <b>this operation is
     * absolutely not concistent with <code>equals(Object o)</code> !!!</b>
     *
     * @param o
     * @return
     * <ul><li>-1 if <code>(!o instanceof Symbolizer) || o.level &lt; this.level
     * </code></li>
     * <li>0 if
     * <code>(o instanceof Symbolizer) &amp;&amp; s.level == this.level</code></li>
     * <li>1 otherwise</li>
     * </ul>
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof ISymbolizer) {
            ISymbolizer s = (ISymbolizer) o;

            if (s.getLevel() < this.level) {
                return 1;
            } else if (s.getLevel() == this.level) {
                return 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Get the name of the column where the geometry data will be retrieved.
     *
     * @return
     */
    public final GeometryAttribute getGeometryAttribute() {
        return theGeom;
    }

    /**
     * Set the name of the column where the geometry data will be retrieved.
     *
     * @param theGeom
     */
    public final void setGeometryAttribute(GeometryAttribute theGeom) {
        this.theGeom = theGeom;
    }

    /**
     * Draw the symbols in g2, using infos that are found in sds at index fid.
     *
     * @param g2
     * @param rs
     * @param fid
     * @param selected
     * @param mt
     * @param theGeom
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public abstract void draw(Graphics2D g2, ResultSet rs, long fid,
            boolean selected, MapTransform mt, Geometry theGeom)
            throws ParameterException, IOException, SQLException;

    /**
     * Get the {@code Geometry} stored in {@code sds} at index {@code fid}.
     *
     * @param rs
     * @param fid
     * @return
     * @throws ParameterException
     * @throws SQLException
     */
    public Geometry getGeometry(ResultSet rs, Long fid) throws ParameterException, SQLException {
        if (theGeom != null) {
            return theGeom.getTheGeom(rs, fid);
        } else {
            return rs.unwrap(SpatialResultSet.class).getGeometry();
        }
    }

    /**
     * If {@code theGeom} is null, get the {@code Geometry} stored in
     * {@code sds} at index {@code fid}.Otherwise, return {@code theGeom}.
     *
     * @param rs
     * @param fid
     * @param theGeom
     * @return
     * @throws ParameterException
     * @throws SQLException
     */
    public Geometry getGeometry(ResultSet rs, Long fid, Geometry theGeom) throws ParameterException, SQLException {
        if (theGeom == null) {
            return this.getGeometry(rs, fid);
        } else {
            return theGeom;
        }
    }

    /**
     * Convert a spatial feature into a LiteShape, should add parameters to
     * handle the scale and to perform a scale dependent generalization !
     *
     * @param rs the data source
     * @param fid the feature id
     * @return
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public Shape getShape(ResultSet rs, long fid,
            MapTransform mt, Geometry theGeom, boolean generalize) throws ParameterException, IOException, SQLException {

        Geometry geom = getGeometry(rs, fid, theGeom);
        //ArrayList<Shape> shapes = new ArrayList<Shape>();

        //shapes.add();
        /* ArrayList<Geometry> geom2Process = new ArrayList<Geometry>();
                
                geom2Process.add(geom);
                
                while (!geom2Process.isEmpty()) {
                geom = geom2Process.remove(0);
                if (geom != null) {
                if (geom instanceof GeometryCollection) {
                int numGeom = geom.getNumGeometries();
                for (int i = 0; i < numGeom; i++) {
                geom2Process.add(geom.getGeometryN(i));
                }
                } else {
                Shape shape = mt.getShape(geom,generalize);
                if (shape != null) {
                shapes.add(shape);
                }
                }
                }
                }*/
        return mt.getShape(geom, generalize);
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

}
