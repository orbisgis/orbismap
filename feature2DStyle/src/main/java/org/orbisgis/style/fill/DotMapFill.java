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
 * @author Alexis Guéganno
 */
public class DotMapFill extends StyleNode implements IGraphicNode, IFill, UomNode {

    private Graphic mark;
    private ParameterValue quantityPerMark = new NullParameterValue();
    private ParameterValue totalQuantity = new NullParameterValue();
    private Random rand;
    private Uom uom;

    /**
     * Creates a new DotMapFill, with uninstanciated values.
     */
    public DotMapFill() {
    }

    @Override
    public void setGraphic(Graphic mark) {
        if (mark != null) {
            this.mark = mark;
            mark.setParent(this);
        }
    }

    @Override
    public Graphic getGraphic() {
        return mark;
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
        if (mark != null) {
            ls.add(mark);
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
}
