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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.fill;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IGraphicNode;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * A "GraphicFill" defines repeated-graphic filling (stippling) pattern for an
 * area geometry. It is defined with a GraphicCollection (that will be used to
 * draw the filling symbol), a Uom, and a gap vector. The gap vector is
 * represented by its two (X and Y) coordinates, stored as
 * <code>ParameterValue</code> instances.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GraphicFill extends StyleNode implements IGraphicNode, IFill, UomNode {
    
    private GraphicCollection graphics;
    /**
     * Distance between two graphics in the fill, in X direction.
     */
    private ParameterValue gapX = new NullParameterValue();
    /**
     * Distance between two graphics in the fill, in Y direction.
     */
    private ParameterValue gapY = new NullParameterValue();
    private Uom uom;

    /**
     * Creates a new GraphicFill, with the gap's measures set to null.
     */
    public GraphicFill() {
        this.graphics = new GraphicCollection();
    }

    /**
     * Set the GraphicCollection embedded in this GraphicFill.This is set as the
     * parent of <code>graphic</code>
     *
     * @param graphic
     */
    @Override
    public void setGraphics(GraphicCollection graphic) {
        this.graphics = graphic;
        graphic.setParent(this);
    }

    /**
     * Get the GraphicCollection embedded in this GraphicFill.
     *
     * @return
     */
    @Override
    public GraphicCollection getGraphics() {
        return graphics;
    }

    /**
     * Set the gap, upon X direction, between two symbols.
     *
     * @param gapX
     */
    public void setGapX(float gapX) {
        setGapX(new Literal(gapX));
    }

    /**
     * Set the gap, upon X direction, between two symbols.
     *
     * @param gapX
     */
    public void setGapX(ParameterValue gapX) {
        if (gapX == null) {
            this.gapX = new NullParameterValue();
            this.gapX.setParent(this);
        } else {
            this.gapX = gapX;
            this.gapX.setParent(this);
            this.gapX.format(Float.class, "value >=0");
        }
    }

    /**
     * Set the gap, upon Y direction, between two symbols.
     *
     * @param gapY
     */
    public void setGapY(float gapY) {
        setGapY(new Literal(gapY));
    }

    /**
     * Set the gap, upon Y direction, between two symbols.
     *
     * @param gapY
     */
    public void setGapY(ParameterValue gapY) {
        if (gapY == null) {
            this.gapY = new NullParameterValue();
            this.gapY.setParent(this);
        } else {
            this.gapY = gapY;
            this.gapY.setParent(this);
            this.gapY.format(Float.class, "value >=0");
        }
    }

    /**
     * Get the gap, upon X direction, between two symbols.
     *
     * @return
     */
    public ParameterValue getGapX() {
        return gapX;
    }

    /**
     * Get the gap, upon Y direction, between two symbols.
     *
     * @return
     */
    public ParameterValue getGapY() {
        return gapY;
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphics != null) {
            ls.add(graphics);
        }
        if (gapX != null) {
            ls.add(gapX);
        }
        if (gapY != null) {
            ls.add(gapY);
        }
        return ls;
    }
    
    @Override
    public Uom getUom() {
        return uom == null ? ((UomNode) getParent()).getUom() : uom;
    }
    
    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }
    
    @Override
    public Uom getOwnUom() {
        return uom;
    }
    
    @Override
    public void addGraphic(Graphic graphic) {
        if (graphics != null) {
            this.graphics.add(graphic);
        }
    }

    @Override
    public void initDefault() {
    }
    
}
