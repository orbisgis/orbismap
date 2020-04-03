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
package org.orbisgis.map.renderer.featureStyle.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.label.ExclusionRadius;
import org.orbisgis.style.label.ExclusionRectangle;
import org.orbisgis.style.label.ExclusionZone;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.label.StyleFont;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class PointLabelDrawer implements ILabelDrawer<PointLabel> {

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointLabel styleNode) throws ParameterException {
        if (shape != null) {
            double x;
            double y;
            Float deltaX = 0f;
            Float deltaY = 0f;
            Uom uom = styleNode.getUom();
            String styleText = (String) styleNode.getLabelText().getValue();
            if (styleText != null && !styleText.isEmpty()) {
                StyleTextDrawer styleTextDrawer = new StyleTextDrawer();
                Rectangle2D bounds = styleTextDrawer.getBounds(g2, styleText, mapTransform, (StyleFont) styleNode.getFont());

                x = shape.getBounds2D().getCenterX() + getHorizontalDisplacement(bounds, styleNode.getHorizontalAlign());
                y = shape.getBounds2D().getCenterY() + bounds.getHeight() / 2;

                ExclusionZone exclusionZone = styleNode.getExclusionZone();
                if (exclusionZone != null) {
                    if (exclusionZone instanceof ExclusionRadius) {
                        Float radius = (Float) ((ExclusionRadius) (exclusionZone)).getRadius().getValue();
                        if (radius == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }
                        radius = UomUtils.toPixel(radius, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                        deltaX = radius;
                        deltaY = radius;
                    } else {
                        deltaX = (Float) ((ExclusionRectangle) (exclusionZone)).getX().getValue();
                        if (deltaX == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }
                        deltaY = (Float) ((ExclusionRectangle) (exclusionZone)).getY().getValue();

                        if (deltaY == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }

                        deltaX = UomUtils.toPixel(deltaX, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                        deltaY = UomUtils.toPixel(deltaY, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                    }
                }

                AffineTransform at = AffineTransform.getTranslateInstance(x + deltaX, y + deltaY);
                styleTextDrawer.setAffineTransform(at);
                styleTextDrawer.setVerticalalignment(styleNode.getVerticalAlign());
                styleTextDrawer.draw(g2, mapTransform, styleNode);
            }
        }
    }

    /**
     * Gets the horizontal displacement to the given bounds according to the
     * currently configured HorizontalAlignment.
     *
     * @param bounds The bounds of the text to be drawn
     * @return The displacement.
     */
    private double getHorizontalDisplacement(Rectangle2D bounds, Label.HorizontalAlignment ha) {
        switch (ha) {
            case CENTER:
                return -bounds.getWidth() / 2.0;
            case LEFT:
                return -bounds.getWidth();
            default:
                return 0.0;
        }
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
