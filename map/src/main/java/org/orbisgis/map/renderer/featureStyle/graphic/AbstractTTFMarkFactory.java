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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import static org.orbisgis.map.renderer.featureStyle.graphic.AbstractShapeFactory.getDimensionInPixel;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.GraphicSize;
import org.orbisgis.style.graphic.Size;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;

/**
 * Abstract class to read TTF font and return a shape from its index
 * 
 * @author Erwan Bocher, CNRS (2020)
 */
public abstract class AbstractTTFMarkFactory extends AbstractShapeFactory {

    String ttf_file_path = null;

    Font font;

    public AbstractTTFMarkFactory(String factory_prefix, String ttf_file_path) {
        super(factory_prefix);
        this.ttf_file_path = ttf_file_path;
    }

    @Override
    public Shape getShape(GraphicSize graphicSize, Double scale, Double dpi, Uom uom) throws ParameterException {
        return getTrueTypeGlyph(graphicSize, scale, dpi, uom, getShapeName());
    }

    private Shape getTrueTypeGlyph(GraphicSize graphicSize, Double scale,
            Double dpi, Uom uom, String unicode) throws ParameterException {
        try {
            if (font == null) {
                try (InputStream iStream = AbstractTTFMarkFactory.class.getResourceAsStream(ttf_file_path)) {
                    font = Font.createFont(Font.TRUETYPE_FONT, iStream);
                }
            }

            // Scale is used to have an high resolution
            AffineTransform at = AffineTransform.getTranslateInstance(0, 0);

            FontRenderContext fontCtx = new FontRenderContext(at, true, true);
            TextLayout tl = new TextLayout(unicode, font, fontCtx);

            Shape glyphOutline = tl.getOutline(at);

            Rectangle2D bounds2D = glyphOutline.getBounds2D();

            double width = bounds2D.getWidth();
            double height = bounds2D.getHeight();

            if (graphicSize != null) {
                if (graphicSize instanceof ViewBox) {
                    ViewBox viewBox = (ViewBox) graphicSize;
                    if (viewBox.usable()) {
                        Point2D dim = getDimensionInPixel(uom, viewBox, height, width, scale, dpi);
                        if (Math.abs(dim.getX()) <= 0 || Math.abs(dim.getY()) <= 0) {
                            return null;
                        }

                        at = AffineTransform.getScaleInstance(dim.getX() / width,
                                dim.getY() / height);

                        fontCtx = new FontRenderContext(at, true, true);
                        tl = new TextLayout(unicode, font, fontCtx);
                        glyphOutline = tl.getOutline(at);
                    }

                } else if (graphicSize instanceof Size) {
                    Size size = (Size) graphicSize;
                    Float sizeValue = (Float) size.getSize().getValue();
                    if (sizeValue != null) {
                        Point2D dim = getDimensionInPixel(uom, sizeValue, sizeValue, scale, dpi);
                        if (Math.abs(dim.getX()) <= 0 || Math.abs(dim.getY()) <= 0) {
                            return null;
                        }
                        at = AffineTransform.getScaleInstance(dim.getX() / width,
                                dim.getY() / height);

                        fontCtx = new FontRenderContext(at, true, true);
                        tl = new TextLayout(unicode, font, fontCtx);
                        glyphOutline = tl.getOutline(at);
                    }
                }
            }
            Rectangle2D gb = glyphOutline.getBounds2D();
            at = AffineTransform.getTranslateInstance(-gb.getCenterX(), -gb.getCenterY());

            return at.createTransformedShape(glyphOutline);

        } catch (FontFormatException | IOException ex) {
            throw new ParameterException(ex);
        }
    }

}
