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

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StrokeNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.common.Description;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.transform.Translate;
import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 * A "AreaSymbolizer" specifies the rendering of a polygon or other area/surface
 * geometry, including its interior fill and border stroke.</p>
 * <p>
 * In addition of the properties inherited from <code>VectorSymbolizer</code> an <code>
 * AreaSymbolizer</code> is defined with a perpendicular offset, a
 * <code>Stroke</code> (to draw its limit, and as a <code>StrokeNode</code>) and
 * a <code>Fill</code> (to paint its interior, and as a <code>FillNode</code>).
 *
 * @author Maxence Laurent, Alexis Guéganno
 */
public class AreaSymbolizer extends StyleNode implements FillNode, StrokeNode, IFeatureSymbolizer, UomNode {

    private Translate translate;
    private ParameterValue perpendicularOffset = new NullParameterValue();
    private Stroke stroke;
    private IFill fill;
    private GeometryParameter geometryExpression;
    private String name;
    private Description description = new Description();
    private int level = 0;
    public static final String DEFAULT_NAME = "Area symbolizer";
    private Uom uom;

    /**
     * Build a new AreaSymbolizer, named "Area Symbolizer". It is defined with a
     * <code>SolidFill</code> and a standard <code>PenStroke</code>
     */
    public AreaSymbolizer() {
        super();
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.PX;
    }

    @Override
    public GeometryParameter getGeometryParameter() {
        return geometryExpression;
    }

    @Override
    public void setGeometryParameter(GeometryParameter geometryExpression) {
        this.geometryExpression = geometryExpression;
        if (this.geometryExpression != null) {
            this.geometryExpression.setParent(this);
        }
    }

    @Override
    public void setStroke(Stroke stroke) {
        if (stroke != null) {
            stroke.setParent(this);
        }
        this.stroke = stroke;
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setFill(IFill fill) {
        if (fill != null) {
            fill.setParent(this);
        }
        this.fill = fill;
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    /**
     * Retrieve the geometric transformation that must be applied to the
     * geometries.
     *
     * @return The transformation associated to this Symbolizer.
     */
    public Translate getTranslate() {
        return translate;
    }

    /**
     * Get the geometric transformation that must be applied to the geometries.
     *
     * @param translate
     */
    public void setTranslate(Translate translate) {
        this.translate = translate;
        //translate.setParent(this);
    }

    /**
     * Get the current perpendicular offset associated to this Symbolizer. It
     * allows to draw polygons larger or smaller than their actual geometry. The
     * meaning of the value is dependant of the <code>Uom</code> instance
     * associated to this <code>Symbolizer</code>.
     *
     * @return The offset as a <code>RealParameter</code>. A positive value will
     * cause the polygons to be drawn larger than their original size, while a
     * negative value will cause the drawing of smaller polygons.
     */
    public ParameterValue getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Set the current perpendicular offset associated to this Symbolizer. It
     * allows to draw polygons larger or smaller than their actual geometry. The
     * meaning of the value is dependant of the <code>Uom</code> instance
     * associated to this <code>Symbolizer</code>.
     *
     * @param perpendicularOffset The offset as a <code>RealParameter</code>. A
     * positive value will cause the polygons to be drawn larger than their
     * original size, while a negative value will cause the drawing of smaller
     * polygons.
     */
    @Override
    public void setPerpendicularOffset(ParameterValue perpendicularOffset) {
        if (perpendicularOffset == null) {
            this.perpendicularOffset = new NullParameterValue();
            this.perpendicularOffset.setParent(this);
        } else {
            this.perpendicularOffset = perpendicularOffset;
            this.perpendicularOffset.setParent(this);
            this.perpendicularOffset.format(Double.class);
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>(4);
        if (this.getGeometryParameter() != null) {
            ls.add(this.getGeometryParameter());
        }
        if (translate != null) {
            ls.add(translate);
        }
        if (fill != null) {
            ls.add(fill);
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
    @Override
    public Description getDescription() {
        return description;
    }

    /**
     * Set the description associated to this <code>Symbolizer</code>.
     *
     * @param description
     */
    @Override
    public void setDescription(Description description) {
        this.description = description;
        if (this.description != null) {
            this.description.setParent(this);
        }
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

    @Override
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
     * <ul><li>-1 if <code>(!o instanceof AreaSymbolizer) || o.level &lt; this.level
     * </code></li>
     * <li>0 if
     * <code>(o instanceof AreaSymbolizer) &amp;&amp; s.level == this.level</code></li>
     * <li>1 otherwise</li>
     * </ul>
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof AreaSymbolizer) {
            AreaSymbolizer s = (AreaSymbolizer) o;
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
