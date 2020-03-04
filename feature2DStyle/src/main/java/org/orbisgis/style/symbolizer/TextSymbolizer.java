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
package org.orbisgis.style.symbolizer;

import org.locationtech.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.GeometryParameter;

/**
 * {@code TextSymbolizer} instances are used to style text labels. In addition
 * to the {@link Feature2DSymbolizer} parameters, it is computed given these
 * arguments :
 * <ul><li>Perpendicular Offset : Transformation according a line parallel to
 * the original geometry</li>
 * <li>A {@link Label} that gathers all the informations needed to print the
 * text. This element is compulsory.</li></ul>
 *
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class TextSymbolizer extends StyleNode implements IFeatureSymbolizer {

    private RealParameter perpendicularOffset;
    private Label label;

    private GeometryParameter geometryExpression = new GeometryParameter("the_geom");
    private String name;
    private String desc;
    private int level;
    public static final String DEFAULT_NAME = "Text Symbolizer";
    private Uom uom;

    /**
     * Build a new {@code TextSymbolizer}, named {@code Label}. It is defined
     * using a default {@link PointLabel#PointLabel() PointLabel}, and is
     * measured in {@link Uom#MM}.
     */
    public TextSymbolizer() {
        super();
        setName("Label");
        setLabel(new PointLabel());
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.MM;
    }
    
    @Override
    public GeometryParameter getGeometryParameter() {
        return geometryExpression;
    }

    @Override
    public void setGeometryParameter(String geometryExpression) {
        this.geometryExpression = new GeometryParameter(geometryExpression);
    }

    /**
     * Set the label contained in this {@code TextSymbolizer}.
     *
     * @param label The new {@code Label} contained in this
     * {@code TextSymbolizer}. Must be non-{@code null}.
     */
    public void setLabel(Label label) {
        label.setParent(this);
        this.label = label;
    }

    /**
     * Get the label contained in this {@code TextSymbolizer}.
     *
     * @return The label currently contained in this {@code TextSymbolizer}.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Get the offset currently associated to this {@code TextSymbolizer}.
     *
     * @return The current perpendicular offset as a {@code RealParameter}. If
     * null, the offset is considered to be equal to {@code 0}.
     */
    public RealParameter getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Set the perpendicular offset associated to this {@code TextSymbolizer}.
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

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (this.getGeometryParameter() != null) {
            ls.add(this.getGeometryParameter());
        }
        if (perpendicularOffset != null) {
            ls.add(perpendicularOffset);
        }
        if (label != null) {
            ls.add(label);
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
        if (o instanceof TextSymbolizer) {
            TextSymbolizer s = (TextSymbolizer) o;

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
