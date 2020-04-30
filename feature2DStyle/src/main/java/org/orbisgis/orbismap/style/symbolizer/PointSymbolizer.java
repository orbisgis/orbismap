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
package org.orbisgis.orbismap.style.symbolizer;

import java.util.ArrayList;
import java.util.List;

import org.orbisgis.orbismap.style.*;
import org.orbisgis.orbismap.style.common.Description;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.IGraphicNode;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.graphic.Graphic;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;

/**
 * {@code PointSymbolizer} are used to draw a graphic at a point. As a
 * symbolizer, it depends on :
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
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 *
 */
public class PointSymbolizer extends StyleNode implements IGraphicNode, Comparable, IFeatureSymbolizer, IUom {

    private GraphicCollection graphics;
    private boolean onVertex = false;
    private GeometryParameter geometryExpression;
    private String name;
    private Description description = new Description();
    private int level = 0;
    public static final String DEFAULT_NAME = "Point symbolizer";
    private Uom uom;

    /**
     * Build a new default {@code PointSymbolizer}. It contains a
     * {@link GraphicCollection} that contains a single default
     * {@code MarkGraphic}. Its UOM is {@link Uom#MM}.
     */
    public PointSymbolizer() {
        super();
        this.name = DEFAULT_NAME;
        this.level = 0;
        this.uom = Uom.PX;
        onVertex = false;
        this.graphics = new GraphicCollection();

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

    @Override
    public GraphicCollection getGraphics() {
        return graphics;
    }

    @Override
    public void setGraphics(GraphicCollection graphic) {
        this.graphics = graphic;
        if (this.graphics != null) {
            this.graphics.setParent(this);
        }
    }

    @Override
    public void addGraphic(Graphic graphic) {
        if (graphics != null) {
            this.graphics.add(graphic);
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
        if (this.getGeometryParameter() != null) {
            ls.add(this.getGeometryParameter());
        }
        ls.add(graphics);
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

    @Override
    public ParameterValue getPerpendicularOffset() {
        return new NullParameterValue();
    }

    @Override
    public void setPerpendicularOffset(ParameterValue parameterValue) {
    }

    @Override
    public void initDefault() {
        setName("Point Symbolizer");        
        setOnVertex(false);
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.initDefault();
        addGraphic(markGraphic);
    }

}
