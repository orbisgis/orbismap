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
package org.orbisgis.style.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IColor;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 *
 *
 * https://www.w3.org/TR/css-color-3/#rgb-color
 *
 * Reference : Multimedia systems and equipment - Colour measurement and
 * management - Part 2-1: Colour management - Default RGB colour space - sRGB.
 * IEC 61966-2-1 (1999-10) ISBN: 2-8318-4989-6 - ICS codes: 33.160.60, 37.080 -
 * TC 100 - 51 pp. as amended by Amendment A1:2003. URL:
 * https://webstore.iec.ch/publication/6168
 *
 * @author Erwan Bocher, CNRS (2020)
 *
 */
public class RGBColor extends StyleNode implements IColor {

    ParameterValue red;
    ParameterValue green;
    ParameterValue blue;
    private Uom uom;

    public RGBColor() {

    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if ((blue != null && !(blue instanceof NullParameterValue))
                && (red != null && !(red instanceof NullParameterValue))
                && (green != null && !(green instanceof NullParameterValue))) {
            ls.add(blue);
            ls.add(red);
            ls.add(green);
        }
        return ls;
    }

    /**
     * Blue color parameter Integer values from 0 to 255.
     *
     * @return
     */
    public ParameterValue getBlue() {
        return blue;
    }

    /**
     * Green color parameter Integer values from 0 to 255.
     *
     * @return
     */
    public ParameterValue getGreen() {
        return green;
    }

    /**
     * Red color parameter Integer values from 0 to 255.
     *
     * @return
     */
    public ParameterValue getRed() {
        return red;
    }

    /**
     * Set the red color parameter
     *
     *
     * @param red
     */
    public void setRed(ParameterValue red) {
        if (red == null) {
            this.red = new NullParameterValue();
            this.red.setParent(this);
        } else {
            this.red = red;
            this.red.setParent(this);
            this.red.format(Integer.class, "value >=0 and value <=255");
        }
    }

    /**
     * Set the green color parameter
     *
     *
     * @param green
     */
    public void setGreen(ParameterValue green) {
        if (green == null) {
            this.green = new NullParameterValue();
            this.green.setParent(this);
        } else {
            this.green = green;
            this.green.setParent(this);
            this.green.format(Integer.class, "value >=0 and value <=255");
        }
    }

    /**
     * Set the blue color parameter
     *
     *
     * @param blue
     */
    public void setBlue(ParameterValue blue) {
        if (blue == null) {
            this.blue = new NullParameterValue();
            this.blue.setParent(this);
        } else {
            this.blue = blue;
            this.blue.setParent(this);
            this.blue.format(Integer.class, "value >=0 and value <=255");
        }
    }

    @Override
    public Color getColor() {
        return new Color((Integer) red.getValue(), (Integer) green.getValue(), (Integer) blue.getValue());
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
