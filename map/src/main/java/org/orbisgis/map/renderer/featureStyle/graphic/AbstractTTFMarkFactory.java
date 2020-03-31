/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public abstract class AbstractTTFMarkFactory extends AbstractShapeFactory {

    String ttf_file_path = null;

    Font font;

    public AbstractTTFMarkFactory(String factory_prefix, String ttf_file_path) {
        super(factory_prefix);
        this.ttf_file_path = ttf_file_path;
    }

    @Override
    public Shape getShape(ViewBox viewBox, Double scale, Double dpi, Uom uom) throws ParameterException {
        return getTrueTypeGlyph(viewBox, scale, dpi, uom, getShapeName());
    }

    private Shape getTrueTypeGlyph(ViewBox viewBox, Double scale,
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

            if (viewBox != null && viewBox.usable()) {
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
            Rectangle2D gb = glyphOutline.getBounds2D();
            at = AffineTransform.getTranslateInstance(-gb.getCenterX(), -gb.getCenterY());

            return at.createTransformedShape(glyphOutline);

        } catch (FontFormatException | IOException ex) {
            throw new ParameterException(ex);
        }

    }

}
