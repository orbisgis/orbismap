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
package org.orbisgis.style.color;

import org.orbisgis.style.*;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of basic color names : aqua, black, blue, fuchsia, gray... 
 * The color names are case-insensitive.
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class WellknownNameColor extends StyleNode implements IColor  {

    private ParameterValue wellknownName;


    enum  ColorNames {BLACK, SILVER, GRAY, WHITE, MAROON, RED, PURPLE,
    FUCHSIA, GREEN, LIME, OLIVE, YELLOW, NAVY, BLUE,
    TEAL, AQUA};

    private Uom uom;

    public WellknownNameColor() {

    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (wellknownName != null && !(wellknownName instanceof NullParameterValue)) {
            ls.add(wellknownName);
        }
        return ls;
    }

    /**
     * Set a color name
     *
     * @param wellknownName
     */
    public void setWellknownName(ParameterValue wellknownName){
        if (wellknownName == null) {
            this.wellknownName = new NullParameterValue();
            this.wellknownName.setParent(this);
        } else {
            this.wellknownName = wellknownName;
            this.wellknownName.setParent(this);
            this.wellknownName.format(String.class);
        }
    }

    /**
     * Return a color name
     *
     * @return
     */
    public ParameterValue getWellknownName() {
        return wellknownName;
    }

    /**
     * Build a new {@code Color} from a {@code String colorName}.
     *
     * @param colorName
     * @return A {@code Color} value.
     */
    public static Color getColor(String colorName) {
        String token = colorName == null ? "" : colorName;
        if (token.equalsIgnoreCase("BLACK")) {
            return Color.BLACK;
        } else if (token.equalsIgnoreCase("SILVER")) {
            return new Color(192, 192, 192);
        } else if (token.equalsIgnoreCase("GRAY")) {
            return Color.GRAY;
        } else if (token.equalsIgnoreCase("WHITE")) {
            return Color.WHITE;
        } else if (token.equalsIgnoreCase("MAROON")) {
            return new Color(128, 0, 0);
        } else if (token.equalsIgnoreCase("RED")) {
            return Color.RED;
        } else if (token.equalsIgnoreCase("PURPLE")) {
            return new Color(128, 0, 128);
        } else if (token.equalsIgnoreCase("FUCHSIA")) {
            return new Color(255, 0, 255);
        } else if (token.equalsIgnoreCase("GREEN")) {
            return new Color(0, 128, 0);
        } else if (token.equalsIgnoreCase("LIME")) {
            return new Color(0, 255, 0);
        } else if (token.equalsIgnoreCase("OLIVE")) {
            return new Color(128, 128, 0);
        } else if (token.equalsIgnoreCase("YELLOW")) {
            return new Color(255, 255, 0);
        } else if (token.equalsIgnoreCase("NAVY")) {
            return new Color(0, 0, 128);
        } else if (token.equalsIgnoreCase("BLUE")) {
            return new Color(0, 0, 255);
        } else if (token.equalsIgnoreCase("TEAL")) {
            return new Color(0, 128, 128);
        } else if (token.equalsIgnoreCase("AQUA")) {
            return new Color(0, 255, 255);
        } else {
            return null;
        }
    }

    @Override
    public Color getColor() {
        return getColor((String) wellknownName.getValue());
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
