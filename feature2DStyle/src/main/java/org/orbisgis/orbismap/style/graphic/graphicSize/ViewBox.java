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
package org.orbisgis.orbismap.style.graphic.graphicSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.IUom;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

/**
 * {@code ViewBox} supplies a simple and convenient method to change the view
 * box of a graphic, in a {@link MarkGraphic} for instance. {@code ViewBox} is
 * built using the following parameters :
 * <ul><li>X : the width of the box.</li>
 * <li>Y : the height of the box.</li></ul>
 * If only one of these two is given, they are considered to be equal.</p>
 * <p>
 * The main difference between this class and Scale is that a {@code Scale} will
 * use a reference graphic, that already has a size, and process an affine
 * transformation on it, while here the size of the graphic will be defined
 * directly using its height and width.</p>
 * <p>
 * The values given for the height and the width can be negative. If that
 * happens, the coordinate of the rendered graphic will be flipped.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class ViewBox extends GraphicSize {
    
    private ParameterValue width = new NullParameterValue();
    private ParameterValue height = new NullParameterValue();
    private Uom uom;

    //In mm
    public static float DEFAULT_SIZE = 3;

    /**
     * Build a new {@code ViewBox}, with empty parameters.
     */
    public ViewBox() {
    }

    /**
     * Build a new {@code ViewBox}, with empty parameters.
     */
    public ViewBox(float width) {
        setWidth(width);
    }

    /**
     * Set the wifth of this {@code ViewBox}.
     *
     * @param width
     */
    public void setWidth(float width) {
        setWidth(new Literal(width));
    }

    /**
     * Set the wifth of this {@code ViewBox}.
     *
     * @param width
     */
    public void setWidth(ParameterValue width) {
        if (width == null) {
            this.width = new NullParameterValue();
            this.width.setParent(this);
        } else {
            this.width = width;
            this.width.setParent(this);
            this.width.format(Float.class, "value>=0");
        }
    }

    /**
     * A {@code ViewBox} can be used if and only if one, at least, of its two
     * parameters has been set.
     *
     * @return
     */
    public boolean usable() {
        return this.width != null || this.height != null;
    }

    /**
     * Get the wifth of this {@code ViewBox}.
     *
     * @return
     */
    public ParameterValue getWidth() {
        return width.getValue() == null ? height : width;
    }

    /**
     * Set the height of this {@code ViewBox}.
     *
     * @param height
     */
    public void setHeight(float height) {
        setHeight(new Literal(height));
    }

    /**
     * Set the height of this {@code ViewBox}.
     *
     * @param height
     */
    public void setHeight(ParameterValue height) {
        if (height == null) {
            this.height = new NullParameterValue();
            this.height.setParent(this);
        } else {
            this.height = height;
            this.height.setParent(this);
            this.height.format(Float.class, "value>=0");
        }
    }

    /**
     * Get the height of this {@code ViewBox}.
     *
     * @return
     */
    public ParameterValue getHeight() {
        return height.getValue() == null ? width : height;
    }

    /**
     * Gets a String representation of this {@code ViewBox}.
     *
     * @return A String containing the wifth and height of the {@code ViewBox}..
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ViewBox:");
        if (this.width != null) {
            result.append("  Width: ").append(width.toString());
        }
        if (this.height != null) {
            result.append("  Height: ").append(height.toString());
        }
        return result.toString();
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (height != null) {
            ls.add(height);
        }
        if (width != null) {
            ls.add(width);
        }
        return ls;
    }
    
    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }
    
    @Override
    public Uom getOwnUom() {
        return uom;
    }
    
    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }
    
    @Override
    public void initDefault() {
        this.uom = Uom.MM;
        this.height = new Literal(DEFAULT_SIZE);
        this.width = new Literal(DEFAULT_SIZE);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Literal)) {
            return false;
        }
        ViewBox other = (ViewBox) o;
        
        if (!getUom().equals(other.getUom())) {
            return false;
        }
        
        if (!getHeight().equals(other.getHeight())) {
            return false;
        }
        if (!getWidth().equals(other.getWidth())) {
            return false;
        }
        return true;
        
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.width);
        hash = 83 * hash + Objects.hashCode(this.height);
        hash = 83 * hash + Objects.hashCode(this.uom);
        return hash;
    }
    
}
