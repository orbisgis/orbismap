/**
 * Feature2DStyle is part of the OrbisGIS platform
 *
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.symbolizer;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.Description;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 * {@code TextSymbolizer} instances are used to style text labels. In addition
 * to the {@link IFeatureSymbolizer} parameters, it is computed given these
 * arguments :
 * <ul><li>Perpendicular Offset : Transformation according a line parallel to
 * the original geometry</li>
 * <li>A {@link Label} that gathers all the informations needed to print the
 * text. This element is compulsory.</li></ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class TextSymbolizer extends StyleNode implements IFeatureSymbolizer {

    private ParameterValue perpendicularOffset = new NullParameterValue();
    private Label label;
    private GeometryParameter geometryExpression;
    private String name;
    private Description description = new Description();
    private int level = 0;
    public static final String DEFAULT_NAME = "Text symbolizer";
    private Uom uom;

    /**
     * Build a new {@code TextSymbolizer}, named {@code Label}. It is defined
     * using a default {@link PointLabel#PointLabel() PointLabel}, and is
     * measured in {@link Uom#MM}.
     */
    public TextSymbolizer() {
        super();
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.MM;
    }

    /**
     * Set geometry expression
     *
     * @param geometryExpression
     */
    public void setGeometryParameter(String geometryExpression) {
        setGeometryParameter(new GeometryParameter(geometryExpression));
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
    @Override
    public ParameterValue getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Set a perpendicular offset value
     *
     * @param perpendicularOffset
     */
    public void setPerpendicularOffset(double perpendicularOffset) {
        setPerpendicularOffset(new Literal(perpendicularOffset));
    }

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

    @Override
    public void initDefault() {
        setName("Point Symbolizer");        
        GeometryParameter geometryParameter = new GeometryParameter();
        geometryParameter.initDefault();
        setGeometryParameter(geometryParameter);
        PointLabel pointLabel = new PointLabel();
        pointLabel.initDefault();
        setLabel(pointLabel);
    }
}
