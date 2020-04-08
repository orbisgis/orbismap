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
import java.util.Random;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IGraphicNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * Descriptor for dot maps. Each point represents a given quantity. Points are
 * randomly placed in the polygon that contains them.<br/>
 * A DotMapFill is defined with three things : <br/>
 *   * The quantity represented by a single dot<br/>
 *   * The total quantity to represent<br/>
 *   * The symbol associated to each single dot.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class DotMapFill extends StyleNode implements IGraphicNode, IFill, UomNode {

    private GraphicCollection graphics;
    private ParameterValue quantityPerMark = new NullParameterValue();
    private ParameterValue totalQuantity = new NullParameterValue();
    private Random rand;
    private Uom uom;

    /**
     * Creates a new DotMapFill
     */
    public DotMapFill() {
        this.graphics = new GraphicCollection();
    }

    @Override
    public void setGraphics(GraphicCollection mark) {
        if (mark != null) {
            this.graphics = mark;
            mark.setParent(this);
        }
    }

    @Override
    public GraphicCollection getGraphics() {
        return graphics;
    }

    /**
     * Set the quantity represented by a single dot.
     *
     * @param quantityPerMark
     */
    public void setQuantityPerMark(int quantityPerMark) {
        setQuantityPerMark(new Literal(quantityPerMark));
    }

    /**
     * Set the quantity represented by a single dot.
     *
     * @param quantityPerMark
     */
    public void setQuantityPerMark(ParameterValue quantityPerMark) {
        if (quantityPerMark == null) {
            this.quantityPerMark = new NullParameterValue();
            this.quantityPerMark.setParent(this);
        } else {
            this.quantityPerMark = quantityPerMark;
            this.quantityPerMark.setParent(this);
            this.quantityPerMark.format(Integer.class, "value>=0");
        }
    }

    /**
     * Get the quantity represented by a single dot.
     *
     * @return The quantity represented by a single dot
     */
    public ParameterValue getQuantityPerMark() {
        return quantityPerMark;
    }

    /**
     * Get the total quantity to be represented for this symbolizer.
     *
     * @param totalQuantity
     */
    public void setTotalQuantity(int totalQuantity) {
        setTotalQuantity(new Literal(totalQuantity));
    }

    /**
     * Get the total quantity to be represented for this symbolizer.
     *
     * @param totalQuantity
     */
    public void setTotalQuantity(ParameterValue totalQuantity) {
        if (totalQuantity == null) {
            this.totalQuantity = new NullParameterValue();
            this.totalQuantity.setParent(this);
        } else {
            this.totalQuantity = totalQuantity;
            this.totalQuantity.setParent(this);
            this.totalQuantity.format(Integer.class, "value>=0");
            this.totalQuantity = totalQuantity;
        }
    }

    /**
     * Set the total quantity to be represented for this symbolizer.
     *
     * @return
     */
    public ParameterValue getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphics != null) {
            ls.add(graphics);
        }
        if (quantityPerMark != null) {
            ls.add(quantityPerMark);
        }
        if (totalQuantity != null) {
            ls.add(totalQuantity);
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
