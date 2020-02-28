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
package org.orbisgis.style;

import org.locationtech.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.parameter.GeometryParameter;

/**
 * A {@code LineSymbolizer} is used to style a {@code Stroke} along a linear
 * geometry type (a LineString, for instance). It is dependant upon the same
 * parameters as {@link Feature2DSymbolizer}, and upon two others :
 * <ul><li>PerpendicularOffset : Used to draw lines in parallel to the original
 * geometry</li>
 * <li>Stroke : defines the way to render the line, as described in
 * {@link Stroke} and its children</li>
 * </ul>
 *
 * @todo add perpendicular offset
 *
 * @author Erwan Bocher, CNRS
 * @author Alexis Guéganno, CNRS
 * @author Maxence Laurent, HEIG-VD
 */
public final class LineSymbolizer extends StyleNode implements Comparable, StrokeNode, IFeatureSymbolizer {

    private RealParameter perpendicularOffset;
    private Stroke stroke;
    private GeometryParameter geometryExpression = new GeometryParameter("the_geom");
    private String name;
    private String desc;
    private int level;
    public static final String DEFAULT_NAME = "Line Symbolizer";
    private Uom uom;

    /**
     * Instantiate a new default {@code LineSymbolizer}. It's named {@code
     * Line Symbolizer"}, is defined in {@link Uom#MM}, and is drawn using a
     * default {@link PenStroke}
     */
    public LineSymbolizer() {
        setStroke(new PenStroke());
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.PX;
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
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        stroke.setParent(this);
    }

    /**
     * Get the current perpendicular offset. If null, considered to be set to 0.
     *
     * @return
     */
    public RealParameter getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Set the perpendicular offset. If a {@code null} value is given, the
     * offset will be considered as equal to 0.
     *
     * @param perpendicularOffset
     */
    public void setPerpendicularOffset(RealParameter perpendicularOffset) {
        this.perpendicularOffset = perpendicularOffset;
        if (this.perpendicularOffset != null) {
            this.perpendicularOffset.setContext(RealParameterContext.REAL_CONTEXT);
            this.perpendicularOffset.setParent(this);
        }
    }

    //@Override
    public void draw(Graphics2D g2, ResultSet rs, long fid,
            IMapTransform mt, Geometry the_geom)
            throws ParameterException, IOException, SQLException {
        if (stroke != null) {
            Map<String, Object> map = null;//getFeaturesMap(rs, fid);
            Shape shp = null;//mt.getShape(the_geom, true);
            double offset = 0.0;
            if (perpendicularOffset != null) {
                offset = UomUtils.toPixel(perpendicularOffset.getValue(rs, fid),
                        getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
            }
            if (shp != null) {
                stroke.draw(g2, map, shp, mt, offset);

            }
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (this.getGeometryParameter() != null) {
            ls.add(this.getGeometryParameter());
        }
        if (perpendicularOffset != null) {
            ls.add(perpendicularOffset);
        }
        if (stroke != null) {
            ls.add(stroke);
        }
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
