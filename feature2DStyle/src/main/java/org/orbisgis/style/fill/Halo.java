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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 * A {@code Halo} is a type of {@code Fill} that is applied to the background of
 * font glyphs. It is mainly used to improve the readability of text labels on
 * the map.
 *
 * @author Alexis Guéganno, CNRS
 * @author Erwan Bocher, CNRS
 */
public class Halo extends StyleNode implements IUom, FillNode {

    /**
     * The default radius for new {@code Halo} instances. Set to 1.0, and UOM
     * dependant.
     */
    public static final float DEFAULT_RADIUS = 1.0f;

    private Uom uom;
    private ParameterValue radius;
    private IFill fill;

    /**
     * Build a new default {@code Halo}, with a solid fill and a radius set to
     * {@code DEFAULT_RADIUS}
     */
    public Halo() {
        setFill(getDefaultFill());
        setRadius(ParameterValueHelper.createFloatLiteral(DEFAULT_RADIUS));
    }

    /**
     * Build a new {@code Halo} with the given {@code Fill} and a radius set to
     * {@code radius}
     *
     * @param fill
     * @param radius
     */
    public Halo(IFill fill, ParameterValue radius) {
        setFill(fill);
        setRadius(radius);
    }

    @Override
    public Uom getUom() {
        if (uom == null) {
            return ((IUom) getParent()).getUom();
        } else {
            return uom;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void setFill(IFill fill) {
        this.fill = fill == null ? getDefaultFill() : fill;
        this.fill.setParent(this);
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    /**
     * Get the radius of this {@code Halo}.
     *
     * @return The radius of this {@code Halo} as a {@code RealParameter}.
     */
    public ParameterValue getRadius() {
        return radius;
    }

    /**
     * Set the radius of this {@code Halo}.
     *
     * @param radius
     */
    public void setRadius(ParameterValue radius) {
        if (radius != null) {
            ParameterValueHelper.validateAsFloat(radius);
            this.radius = radius;
        } else {
            this.radius = ParameterValueHelper.createFloatLiteral(DEFAULT_RADIUS);
        }
        this.radius.setParent(this);
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        ls.add(radius);
        ls.add(fill);
        return ls;
    }

    /**
     * Default fill for the halo must be white and 100% opaque.
     *
     * @return
     */
    private IFill getDefaultFill() {
        return new SolidFill(Color.WHITE, 1);
    }

    @Override
    public void setUom(org.orbisgis.style.Uom uom) {
        this.uom = uom;
    }

}
