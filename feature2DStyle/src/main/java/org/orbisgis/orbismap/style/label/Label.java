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
package org.orbisgis.orbismap.style.label;

import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IFont;
import org.orbisgis.orbismap.style.ILabel;
import org.orbisgis.orbismap.style.IUom;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.Halo;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.stroke.Stroke;
import org.orbisgis.orbismap.style.IStrokeNode;
import org.orbisgis.orbismap.style.IFillNode;

/**
 * Labels are used to provide text-label contents. A textSymbolizer must contain
 * a label - If not it won't be displayed.</p>
 * <p>
 * A Label instance contains a text value (as a StyledText) and informations
 * about its alignment, vertical or horizontal.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public abstract class Label extends StyleNode implements ILabel<ParameterValue>, IFillNode, IStrokeNode {

    private Uom uom;
    private HorizontalAlignment hAlign;
    private VerticalAlignment vAlign;
    private ParameterValue textLabel;
    private IFill fill;
    private IFont font;
    private Halo halo;
    private Stroke stroke;

    /**
     * Possible values for the HorizontalAlignment of a Label. It can be left,
     * centered or right aligned.
     */
    public enum HorizontalAlignment {

        LEFT, CENTER, RIGHT;

        /**
         * Creates a <code>HorizontalAlignment</code> from a <code>String</code>
         * value.
         *
         * @param token
         * @return
         * <ul><li><code>LEFT</code> if token == "left"</li>
         * <li><code>CENTER</code> if token == "center</li>
         * <li><code>RIGHT</code> of token == "right"</li>
         * <li><code>CENTER</code> otherwise (fallback value)</li></ul>
         * Comparisons are made ignoring case.
         */
        public static HorizontalAlignment fromString(String token) {
            if (token.equalsIgnoreCase("left")) {
                return LEFT;
            }

            if (token.equalsIgnoreCase("center")) {
                return CENTER;
            }

            if (token.equalsIgnoreCase("right")) {
                return RIGHT;
            }

            return CENTER; // default value
        }

        /**
         * Retrieve the possible values for <code>HorizontalAlignment</code> in
         * an array of <code>String</code>
         *
         * @return An array containing the legal values.
         */
        public static String[] getList() {
            String[] list = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                list[i] = values()[i].name();
            }
            return list;
        }
    }

    /**
     * Possible values for the VerticalAlignment of a Label. It can be top,
     * bottom, middle or baseline aligned.
     */
    public enum VerticalAlignment {

        TOP, MIDDLE, BASELINE, BOTTOM;

        /**
         * Creates a <code>VerticalAlignment</code> from a <code>String</code>
         * value.
         *
         * @param token
         * @return
         * <ul><li><code>BOTTOM</code> if token == "bottom"</li>
         * <li><code>MIDDLE</code> if token == "middle</li>
         * <li><code>BASELINE</code> of token == "baseline"</li>
         * <li><code>TOP</code> of token == "top"</li>
         * <li><code>TOP</code> otherwise (fallback value)</li></ul>
         * Comparisons are made ignoring case.
         */
        public static VerticalAlignment fromString(String token) {
            if (token.equalsIgnoreCase("bottom")) {
                return BOTTOM;
            }
            if (token.equalsIgnoreCase("middle")) {
                return MIDDLE;
            }
            if (token.equalsIgnoreCase("baseline")) {
                return BASELINE;
            }

            return TOP;
        }

        /**
         * Retrieve the possible values for <code>VerticalAlignment</code> in an
         * array of <code>String</code>
         *
         * @return An array containing the legal values.
         */
        public static String[] getList() {
            String[] list = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                list[i] = values()[i].name();
            }
            return list;
        }
    }

    /**
     * Create a new <code>Label</code> with default values as defined in the
     * default {@code StyledText} constructor (cf
     * {@link org.orbisgis.coremap.renderer.se.label.Label#Label() Label()} ).
     */
    public Label() {
    }

    @Override
    public Uom getOwnUom() {
        return uom;
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
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    /**
     * Get the current <code>HorizontalAlignment</code>
     *
     * @return The current <code>HorizontalAlignment</code>
     */
    public HorizontalAlignment getHorizontalAlign() {
        return hAlign;
    }

    /**
     * Set the current <code>HorizontalAlignment</code>
     *
     * @param hAlign
     */
    public void setHorizontalAlign(HorizontalAlignment hAlign) {
        if (hAlign != null) {
            this.hAlign = hAlign;
        }
    }

    /**
     * Get the current <code>VerticalAlignment</code>
     *
     * @return The current <code>VerticalAlignment</code>
     */
    public VerticalAlignment getVerticalAlign() {
        return vAlign;
    }

    /**
     * Set the current <code>VerticalAlignment</code>
     *
     * @param vAlign
     */
    public void setVerticalAlign(VerticalAlignment vAlign) {
        if (vAlign != null) {
            this.vAlign = vAlign;
        }
    }
    
    /**
     * Set a literal text
     * @param textLabel 
     */
    public void setLabelText(String textLabel) {
        setLabelText(new Literal(textLabel));
    }

    @Override
    public void setLabelText(ParameterValue textLabel) {
        if (textLabel == null) {
            this.textLabel = new NullParameterValue();
            this.textLabel.setParent(this);
        } else {
            this.textLabel = textLabel;
            this.textLabel.setParent(this);
            this.textLabel.format(String.class);
        }
    }

    @Override
    public ParameterValue getLabelText() {
        return this.textLabel;
    }

    @Override
    public IFont getFont() {
        return this.font;
    }

    @Override
    public void setFont(IFont font) {
        this.font = font;
        if (this.font != null) {
            this.font.setParent(this);
        }
    }
    
    @Override
    public IFill getFill() {
        return fill;
    }
    
    @Override
    public void setFill(IFill fill) {
        this.fill = fill;
        if (fill != null) {
            fill.setParent(this);
        }
    }

    /**
     * Return the halo associated to this <code>StyledText</code>.
     *
     * @return A <code>Halo</code> instance, or null if it has not been set.
     */
    public Halo getHalo() {
        return halo;
    }

    /**
     * Set the halo associated to this <code>StyledText</code>
     *
     * @param halo
     */
    public void setHalo(Halo halo) {
        this.halo = halo;
        if (halo != null) {
            halo.setParent(this);
        }
    }
    
    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (stroke != null) {
            stroke.setParent(this);
        }
    }


}
