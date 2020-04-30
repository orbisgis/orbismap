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
package org.orbisgis.orbismap.style.graphic;

/**
 * {@code WellKnownName} instances are used to build simple vectorial
 * {@link MarkGraphic}. There are six types of {@code WellKnownName} :
 * <ul><li>SQUARE</li>
 * <li>CIRCLE</li>
 * <li>HALFCIRCLE</li>
 * <li>TRIANGLE</li>
 * <li>STAR</li>
 * <li>CROSS</li>
 * <li>X</li>
 * <li>VERTLINE</li>
 * </ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public enum WellKnownName {
    SQUARE, CIRCLE, HALFCIRCLE, TRIANGLE, STAR, CROSS, X, VERTLINE;

    /**
     * Default size to be used to render graphics based on well-known names.
     */
    public static final double DEFAULT_SIZE = 10.0;

    /**
     * Get all the {@code String} values that can be used to build a
     * {@code WellKnownName}.
     *
     * @return An array of lega {@code String} values
     */
    public static String[] getValues() {
        String[] list = new String[WellKnownName.values().length];
        int i = 0;
        for (WellKnownName wkn : WellKnownName.values()) {
            list[i] = wkn.toString();
            i++;
        }
        return list;
    }

    /**
     * Gets an array containing the localized strings associated to the string
     * representation of all the units of mesure contained in this enum.
     *
     * @return
     */
    public static String[] getLocalizedStrings() {
        WellKnownName[] vals = WellKnownName.values();
        String[] ls = new String[vals.length];
        for (int i = 0; i < vals.length; i++) {
            ls[i] = vals[i].toLocalizedString();
        }
        return ls;
    }

    /**
     * Gets the localized representation of this.
     *
     * @return
     */
    public String toLocalizedString() {
        switch (this) {
            case SQUARE:
                return "SQUARE";
            case HALFCIRCLE:
                return "HALFCIRCLE";
            case TRIANGLE:
                return "TRIANGLE";
            case STAR:
                return "STAR";
            case CROSS:
                return "CROSS";
            case X:
                return "X";
            case VERTLINE:
                return "VERTLINE";
            default:
                return "CIRCLE";

        }
    }

    /**
     * Build a new {@code WellKnownName} from a {@code String token}.
     *
     * @param tok
     * @return A {@code WellKnownName} value. Defaults to {@link #CIRCLE} if the
     * name does not match anything.
     */
    public static WellKnownName fromString(String tok) {
        String token = tok == null ? "" : tok;
        if (token.equalsIgnoreCase("SQUARE")) {
            return SQUARE;
        } else if (token.equalsIgnoreCase("HALFCIRCLE")) {
            return HALFCIRCLE;
        } else if (token.equalsIgnoreCase("TRIANGLE")) {
            return TRIANGLE;
        } else if (token.equalsIgnoreCase("STAR")) {
            return STAR;
        } else if (token.equalsIgnoreCase("CROSS")) {
            return CROSS;
        } else if (token.equalsIgnoreCase("X")) {
            return X;
        }  else if (token.equalsIgnoreCase("VERTLINE")) {
            return VERTLINE;
        }else {
            return CIRCLE;
        }
    }

}
