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
package org.orbisgis.style.graphic;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;

/**
 * Collection of graphics
 * 
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GraphicCollection extends StyleNode implements IUom {

    ArrayList<Graphic> graphics;

    public GraphicCollection() {
        graphics = new ArrayList<>();
    }
    
    /**
     * Add a graphic in this collection, at index i if<code>i &lt;= getNumGraphics()-1 &amp;&amp; i &gt;= 0
     * </code>, or in the end of the collection (ie at index n+1, if the
     * collection contains n elements before the insertion) if this condition is
     * no satisfied.
     *
     * @param graphic
     * @param index
     */
    public void add(Graphic graphic, int index) {
        if (graphic != null) {
            if (index >= 0 && index < graphics.size()) {
                graphics.add(index, graphic);
            } else {
                graphics.add(graphic);
            }
            graphic.setParent(this);
        }
    }

    /**
     * Get the number of inner graphic symbols.
     *
     * @return
     */
    public int getNumGraphics() {
        return graphics.size();
    }

    public ArrayList<Graphic> getGraphics() {
        return graphics;
    }

    public boolean add(Graphic graphic) {
        if (graphic != null) {
            graphics.add(graphic);
            graphic.setParent(this);
            return true;
        }
        return false;
    }

    public Graphic get(int index) {
        return graphics.get(index);
    }

    public boolean remove(Graphic graphic) {
        return graphics.remove(graphic);
    }

    public Graphic remove(int index) {
        return graphics.remove(index);
    }

    /**
     * Move the graphic at index i (if any) down in the collection, ie at
     * position i+1. If <code> i >= n-1 </code>, where n is the size of the
     * collection, or if <code> 0 > i </code>, nothing is done.
     *
     * @param index
     * @return
     */
    public boolean moveGraphicDown(int index) {
        if (index >= 0 && index < graphics.size() - 1) {
            Graphic g = graphics.get(index);
            graphics.set(index, graphics.get(index + 1));
            graphics.set(index + 1, g);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Move the graphic at index i (if any) up in the collection, ie at position
     * i-1. If <code>0 >= i</code> or <code>i > n-1 </code>, where n is the size
     * of the collection, nothing is done.
     *
     * @param index
     * @return
     */
    public boolean moveGraphicUp(int index) {
        if (index > 0 && index < graphics.size()) {
            Graphic g = graphics.get(index);
            graphics.set(index, graphics.get(index - 1));
            graphics.set(index - 1, g);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        ls.addAll(graphics);
        return ls;
    }

    /**
     * Won't do anything : graphic collections are not intended to have a
     * dedicated unit of measure.
     *
     * @param u
     */
    @Override
    public void setUom(Uom u) {
    }

    /**
     * Returns {@link Uom#PX}, as it is the default value for {@code Uom}
     * instances.
     *
     * @return
     */
    @Override
    public Uom getOwnUom() {
        return Uom.PX;
    }

    @Override
    public Uom getUom() {
        if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }

    /**
     * Return a <code>Graphic</code> in the collection
     * @param index
     * @return 
     */
    public Graphic getGraphic(int index) {
        return graphics.get(index);
    }

}
