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
package org.orbisgis.style.transform;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.ITransform;

/**
 * {@code Rotate} is a transformation that performs a rotation of the affected
 * object. It is built using :
 * <ul><li>The X-coordinate of the rotation center. This value takes place in
 * the coordinate system of the graphic this {@code Rotate} is used on.</li>
 * <li>The Y-coordinate of the rotation center. This value takes place in the
 * coordinate system of the graphic this {@code Rotate} is used on.</li>
 * <li>The rotation angle, in clockwise degrees.</li></ul>
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public final class Rotate extends StyleNode implements ITransform {

    private ParameterValue x;
    private ParameterValue y;
    private ParameterValue rotation;

    /**
     * Build a new {@code Rotate} with angle value set to {@code rotation}.
     *
     * @param rotation
     */
    public Rotate(ParameterValue rotation) {
        setRotation(rotation);
        setX(null);
        setY(null);
    }

    /**
     * Build a new {@code Rotate} with angle value set to {@code rotation}, and
     * with the rotation center placed at (ox, oy) (in the containing graphic
     * coordinate system).
     *
     * @param rotation
     * @param ox
     * @param oy
     */
    public Rotate(ParameterValue rotation, ParameterValue ox, ParameterValue oy) {
        setRotation(rotation);
        setX(ox);
        setY(oy);
    }

    /**
     * Get the rotation defined in this {@code Rotate} instance.
     *
     * @return The rotation, in clockwise degrees. Indeed, as the Y axis is
     * oriented bottom up, it is the direct rotation sense...
     */
    public ParameterValue getRotation() {
        return rotation;
    }

    /**
     * Set the rotation defined in this {@code Rotate} instance.
     *
     * @param rotation
     */
    public void setRotation(ParameterValue rotation) {        
        if (rotation == null) {
            this.rotation = new NullParameterValue();
            this.rotation.setParent(this);
        } else {
            this.rotation = rotation;
            this.rotation.setParent(this);
            this.rotation.format(Float.class, "value>=0 and value<=180");
        }
    }

    /**
     * Get the x-coordinate of the rotation center.
     *
     * @return The x-coordinate as a {@code RealParameter} instance. Note that
     * the returned coordinate is placed in the coordinate system associated to
     * the graphic this {@code Rotate} operation is applied on.
     */
    public ParameterValue getX() {
        return x;
    }

    /**
     * Set the x-coordinate of this {@code Rotate} center. Note that this
     * coordinate is placed in the coordinate system associated to the graphic
     * this {@code Rotate} operation is applied on.
     *
     * @param x A {@code RealParameter} that is placed by this method in a
     * {@link RealParameterContext#REAL_CONTEXT}
     */
    public void setX(ParameterValue x) {
        if (x == null) {
            this.x = new NullParameterValue();
            this.x.setParent(this);
        } else {
            this.x = x;
            this.x.setParent(this);
            this.x.format(Float.class);
        }
    }

    /**
     * Get the y-coordinate of the rotation center.
     *
     * @return The y-coordinate as a {@code RealParameter} instance. Note that
     * the returned coordinate is placed in the coordinate system associated to
     * the graphic this {@code Rotate} operation is applied on.
     */
    public ParameterValue getY() {
        return y;
    }

    /**
     * Set the y-coordinate of this {@code Rotate} center. Note that this
     * coordinate is placed in the coordinate system associated to the graphic
     * this {@code Rotate} operation is applied on.
     *
     * @param y A {@code RealParameter} that is placed by this method in a
     * {@link RealParameterContext#REAL_CONTEXT}
     */
    public void setY(ParameterValue y) {
        if (y == null) {
            this.y = new NullParameterValue();
            this.y.setParent(this);
        } else {
            this.y = y;
            this.y.setParent(this);
            this.y.format(Float.class);
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (x != null) {
            ls.add(x);
        }
        if (y != null) {
            ls.add(y);
        }
        if (rotation != null) {
            ls.add(rotation);
        }
        return ls;
    }

    @Override
    public String toString() {
        return "Rotate";
    }


    @Override
    public AffineTransform getAffineTransform(Uom uom, double dpi, double scaleDenominator, Float width, Float height) throws ParameterException {
        double ox = 0.0;
        Float xValue = (Float) x.getValue();
        if (xValue != null) {
            ox = UomUtils.toPixel(xValue, uom, dpi, scaleDenominator, width);
        }

        double oy = 0.0;
        Float yValue = (Float) y.getValue();
        if (yValue != null) {
            oy = UomUtils.toPixel(yValue, uom, dpi, scaleDenominator, height);
        }

        double theta = 0.0;
        Float rotationValue = (Float) rotation.getValue();
        if (rotationValue != null) {
            theta = rotationValue * Math.PI / 180.0; // convert to rad
        }
        return AffineTransform.getRotateInstance(theta, ox, oy);
    }
}
