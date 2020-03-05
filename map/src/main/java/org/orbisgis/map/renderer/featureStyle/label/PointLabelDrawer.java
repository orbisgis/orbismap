/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.Uom;
import org.orbisgis.style.label.ExclusionRadius;
import org.orbisgis.style.label.ExclusionRectangle;
import org.orbisgis.style.label.ExclusionZone;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.label.StyledText;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class PointLabelDrawer implements ILabelDrawer<PointLabel> {

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, PointLabel styleNode, Map<String, Object> properties) throws ParameterException, SQLException {

        Shape shape = (Shape) properties.get("shape");
        if (shape != null) {
            double x;
            double y;

            // TODO RenderPermission !
            double deltaX = 0;
            double deltaY = 0;
            Uom uom = styleNode.getUom();
            StyledText styleText = styleNode.getLabel();

            if (styleText != null) {
                StyleTextDrawer styleTextDrawer = new StyleTextDrawer();
                String text = ValueHelper.getString(sp, styleText.getText());
                if(text!=null && !text.isEmpty()) {
                    Rectangle2D bounds = styleTextDrawer.getBounds(sp, g2, text, properties, mapTransform, styleText);


                    x = shape.getBounds2D().getCenterX() + getHorizontalDisplacement(bounds, styleNode.getHorizontalAlign());
                    y = shape.getBounds2D().getCenterY() + bounds.getHeight() / 2;

                    ExclusionZone exclusionZone = styleNode.getExclusionZone();
                    if (exclusionZone != null) {
                        if (exclusionZone instanceof ExclusionRadius) {
                            double radius = ((ExclusionRadius) (exclusionZone)).getRadius().getValue(properties);
                            radius = UomUtils.toPixel(radius, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                            deltaX = radius;
                            deltaY = radius;
                        } else {
                            deltaX = ((ExclusionRectangle) (exclusionZone)).getX().getValue(properties);
                            deltaY = ((ExclusionRectangle) (exclusionZone)).getY().getValue(properties);

                            deltaX = UomUtils.toPixel(deltaX, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                            deltaY = UomUtils.toPixel(deltaY, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                        }
                    }

                    AffineTransform at = AffineTransform.getTranslateInstance(x + deltaX, y + deltaY);

                    properties.put("affinetransform", at);
                    properties.put("verticalalignment", styleNode.getVerticalAlign());
                    styleTextDrawer.draw(sp, g2, mapTransform, styleText, properties);
                }
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

}
