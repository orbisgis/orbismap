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
package org.orbisgis.style.transform;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.ITransform;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;

/**
 * Represents a translation in an euclidean plane. As it can be represented with
 * a 2D vector, it is defined by two <code>ParameterValue</code>s.
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class Translate extends StyleNode implements ITransform {

    private ParameterValue x;
    private ParameterValue y;

    /**
     * Create a new <code>Translate</code>
     *
     * @param x The translation about X-axis
     * @param y The translation about Y-axis
     */
    public Translate(float x, float y) {
        setX(new Literal(x));
        setY(new Literal(y));
    }
    

    /**
     * Create an new empty <code>Translate</code>
     */
    public Translate() {
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<>();
        if (x != null) {
            ls.add(x);
        }
        if (y != null) {
            ls.add(y);
        }
        return ls;
    }

    /**
     * Get the translation about the X-axis
     *
     * @return The translation about the X-axis
     */
    public ParameterValue getX() {
        return x;
    }

    /**
     * Get the translation about the Y-axis
     *
     * @return The translation about the Y-axis
     */
    public ParameterValue getY() {
        return y;
    }

    /**
     * Set the translation about the Y-axis
     *
     * @param y
     */
    public final void setY(ParameterValue y) {
        if (y == null) {
            this.y = new NullParameterValue();
            this.y.setParent(this);
        } else {
            this.y = y;
            this.y.setParent(this);
            this.y.format(Float.class);
        }
    }

    /**
     * Set the translation about the X-axis
     *
     * @param x
     */
    public final void setX(ParameterValue x) {
        if (x == null) {
            this.x = new NullParameterValue();
            this.x.setParent(this);
        } else {
            this.x = x;
            this.x.setParent(this);
            this.x.format(Float.class);
        }
    }

    @Override
    public String toString() {
        return "Translate";
    }

    @Override
    public AffineTransform getAffineTransform(Uom uom, double dpi,
            double scaleDenominator, Float width100p, Float height100p) throws ParameterException {
        double tx = 0.0;
        Float xValue = (Float) x.getValue();
        if (xValue != null) {
            tx = UomUtils.toPixel(xValue, uom, dpi, scaleDenominator, width100p);
        }
        double ty = 0.0;
        Float yValue = (Float) y.getValue();
        if (yValue != null) {
            ty = UomUtils.toPixel(yValue, uom, dpi, scaleDenominator, height100p);
        }
        return AffineTransform.getTranslateInstance(tx, ty);
    }
}
