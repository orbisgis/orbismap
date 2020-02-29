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
package org.orbisgis.style;

import org.locationtech.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.style.utils.FeatureUtils;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.GeometryParameter;

/**
 * {@code PointSymbolizer} are used to draw a graphic at a point. As a symbolizer, 
 * it depends on :
 * <ul><li>A version</li>
 * <li>A name<li>
 * <li>A Description</li>
 * <li>A LegendGraphic</li></ul>
 * 
 * It has additional requirements :
 * <ul><li>A geometry, ie a value reference containing the geometry to style. It 
 * is optional, but shall appear if several geometries are defined in the data
 * type.</li>
 * <li>A unit of measure. If not set, the UOM of the parent will be used.</li>
 * <li>Graphic : the graphic to draw at the point. Compulsory.</li></ul>
 * 
 * An additional parameter can be given. It is used to determine if the symbol 
 * must be drawn on the vertex of a geometry, rather than at its center.
 * 
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class PointSymbolizer extends StyleNode  implements GraphicNode,Comparable, IFeatureSymbolizer {
    private static final String MODE_VERTEX = "vertex";
    private GraphicCollection graphic;
    private boolean onVertex;
    private GeometryParameter geometryExpression = new GeometryParameter("the_geom");
    private String name;
    private String desc;
    private int level;
    public static final String DEFAULT_NAME = "Point Symbolizer";
    private Uom uom;

    /**
     * Build a new default {@code PointSymbolizer}. It contains a 
     * {@link GraphicCollection} that contains a single default {@code MarkGraphic}.
     * Its UOM is {@link Uom#MM}.
     */
    public PointSymbolizer() {
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.PX;
        setGraphicCollection(new GraphicCollection());
        MarkGraphic mark = new MarkGraphic();
        graphic.addGraphic(mark);
        onVertex = false;
        
    }    
    
    @Override
    public GeometryParameter getGeometryParameter() {
        return geometryExpression;
    }

    @Override
    public void setGeometryParameter(String geometryExpression) {
        this.geometryExpression = new GeometryParameter(geometryExpression);
    }

    @Override
    public GraphicCollection getGraphicCollection() {
        return graphic;
    }

    @Override
    public void setGraphicCollection(GraphicCollection graphic) {
        this.graphic = graphic;
        graphic.setParent(this);
    }

    //@Override
    public void draw(Graphics2D g2, ResultSet rs, long fid,
             IMapTransform mt, Geometry the_geom)
            throws IOException, SQLException, ParameterException {
            if (graphic != null && graphic.getNumGraphics() > 0) {
                double x,y;
                Map<String,Object> map = null;//getFeaturesMap(rs, fid);
                if (onVertex) {
                    List<Point2D> points = FeatureUtils.getPoints(mt, the_geom);
                    for (Point2D pt : points) {
                    x = pt.getX();
                    y = pt.getY();
                    graphic.draw(g2, map, mt, AffineTransform.getTranslateInstance(x, y));
                    }
                } else {
                    Point2D pt = FeatureUtils.getPointShape( mt, the_geom);

                    x = pt.getX();
                    y = pt.getY();

                    // Draw the graphic right over the point !
                    graphic.draw(g2, map, mt, AffineTransform.getTranslateInstance(x, y));
                }
        }
    }  

    public boolean isOnVertex() {
        return onVertex;
    }

    public void setOnVertex(boolean onVertex) {
        this.onVertex = onVertex;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if(this.getGeometryParameter()!=null){
            ls.add(this.getGeometryParameter());
        }
        ls.add(graphic);
        return ls;
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
     * <ul><li>-1 if <code>(!o instanceof PointSymbolizer) || o.level &lt; this.level
     * </code></li>
     * <li>0 if
     * <code>(o instanceof PointSymbolizer) &amp;&amp; s.level == this.level</code></li>
     * <li>1 otherwise</li>
     * </ul>
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof PointSymbolizer) {
            PointSymbolizer s = (PointSymbolizer) o;

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

    
}