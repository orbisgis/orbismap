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
package org.orbisgis.orbismap.style.color;

import org.orbisgis.orbismap.style.IColor;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.orbismap.style.IUom;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;

/**
 * The HexaColor extension is a concrete implementation of the Color class to
 * expressed a RGB color model. The format of the color is in hexadecimal
 * notation. It is a ‘`#`’ immediately followed by either three or six
 * hexadecimal characters.
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class HexaColor extends StyleNode implements IColor {

    ParameterValue hexaColor;
    private Uom uom;

    public HexaColor() {

    }

    public HexaColor(String color) {
        this.hexaColor = new Literal(color);
    }

    /**
     * Return the hexacolor value
     *
     * @return
     */
    public ParameterValue getHexaColor() {
        return hexaColor;
    }

    /**
     * Set an hexacolor value
     *
     * @param hexaColor
     */
    public void setHexaColor(ParameterValue hexaColor) {
        if (hexaColor == null) {
            this.hexaColor = new NullParameterValue();
            this.hexaColor.setParent(this);
        } else {
            this.hexaColor = hexaColor;
            this.hexaColor.setParent(this);
            this.hexaColor.format(String.class);
        }
    }

    @Override
    public Color getColor() {
        Color color = null;
        if (hexaColor.getValue() != null) {
            color = Color.decode((String) hexaColor.getValue());
        }
        return color;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (hexaColor != null && !(hexaColor instanceof NullParameterValue)) {
            ls.add(hexaColor);
        }
        return ls;
    }

    @Override
    public Uom getUom() {
        return uom == null ? ((IUom) getParent()).getUom() : uom;
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
