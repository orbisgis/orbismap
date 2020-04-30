/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.label;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.style.label.Label;
import org.orbisgis.orbismap.style.label.StyleFont;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.utils.UomUtils;

/**
 * TODO : change this part
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class StyleTextUtil {

    /**
     * Get the minimal {@code Rectangle2D} that contains this
     * {@code StyledText}.
     *
     * @param g2 The graphics we draw with
     * @param text The text for which we need the bounds.
     * @param mt The current IMapTransform
     * @param styleFont
     * @return The bounds of the text
     * @throws ParameterException
     */
    public static Rectangle2D getBounds(Graphics2D g2, String text,
            MapTransform mt, StyleFont styleFont) throws ParameterException {
        Font font = getFont(mt, styleFont);
        FontMetrics metrics = g2.getFontMetrics(font);
        return metrics.getStringBounds(text, null);
    }

    /**
     * Gets the outline of the given {@code String} as a shape.This shapes is
     * made of the boundary(ies) of the text, that will have to be stroked and
     * fill.
     *
     * @param g2 The {@code Graphics2D} instance used to render the map we are
     * drawing.
     * @param text The text we want to compute the outline of.
     * @param affineTransform
     * @param mt Used to compute the font's size.
     * @param styleNode
     * @param va The {@code Label.VerticalAlignment} we must use to determine
     * where to put the baseline of {@code text}.
     * @return The needed Shape
     * @throws ParameterException If we fail to retrieve a parameter used to
     * configure this {@code
     * StyledText}.
     */
    public static Shape getOutline(Graphics2D g2, String text,
            MapTransform mt, AffineTransform affineTransform, Label.VerticalAlignment va, Label styleNode)
            throws ParameterException {
        Font font = getFont(mt, (StyleFont) styleNode.getFont());
        TextLayout tl = new TextLayout(text, font, g2.getFontRenderContext());
        FontMetrics metrics = g2.getFontMetrics(font);
        double dy = 0;
        switch (va) {
            case BASELINE:
                break;
            case BOTTOM:
                dy = metrics.getAscent();
                break;
            case TOP:
                dy = -metrics.getDescent();
                break;
            case MIDDLE:
            default:
                dy = (metrics.getAscent() - metrics.getDescent()) / 2.0;
        }
        AffineTransform rat;
        if (affineTransform != null) {
            rat = new AffineTransform(affineTransform);
        } else {
            rat = new AffineTransform();
        }
        //We apply the translation used to manage the height of the text on the
        //line BEFORE to apply at : we use concatenate.
        rat.concatenate(AffineTransform.getTranslateInstance(0, dy));
        return tl.getOutline(rat);
    }

    /**
     *
     * @param sp
     * @param map
     * @param mt
     * @param styleNode
     * @return
     * @throws ParameterException
     * @throws IOException
     */
    private static Font getFont(MapTransform mt, StyleFont font) throws ParameterException {
        String fontFamily = (String) font.getFontFamily().getValue();
        if (fontFamily == null) {
            throw new ParameterException("The font family cannot be null");
        }

        // TODO Family is comma delimeted list of fonts family. Choose the first available
        String fontWeight = (String) font.getFontWeight().getValue();
        if (fontWeight == null) {
            throw new ParameterException("The font weight cannot be null");
        }

        String fontStyle = (String) font.getFontStyle().getValue();
        if (fontStyle == null) {
            throw new ParameterException("The font style cannot be null");
        }

        Float fontSize = (Float) font.getFontSize().getValue();
        if (fontSize == null) {
            throw new ParameterException("The font size cannot be null");
        }
        double size = UomUtils.toPixel(fontSize, font.getFontUom(), mt.getDpi(), mt.getScaleDenominator());

        int st = Font.PLAIN;

        if (fontWeight.equalsIgnoreCase("bold")) {
            st = Font.BOLD;
        }

        if (fontStyle.equalsIgnoreCase("italic")) {
            if (st == Font.PLAIN) {
                st |= Font.ITALIC;
            } else {
                st = Font.ITALIC;
            }
        }

        return new Font(fontFamily, st, (int) size);
    }

}
