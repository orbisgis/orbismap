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

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointLabel styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        if (shape != null) {
            double x;
            double y;
            Float deltaX =0f;
            Float deltaY=0f;
            Uom uom = styleNode.getUom();
            StyledText styleText = styleNode.getLabel();

            if (styleText != null) {
                StyleTextDrawer styleTextDrawer = new StyleTextDrawer();
                String text = ValueHelper.getAsString(properties, styleText.getText());
                if(text!=null && !text.isEmpty()) {
                    Rectangle2D bounds = styleTextDrawer.getBounds( g2, text, properties, mapTransform, styleText);


                    x = shape.getBounds2D().getCenterX() + getHorizontalDisplacement(bounds, styleNode.getHorizontalAlign());
                    y = shape.getBounds2D().getCenterY() + bounds.getHeight() / 2;

                    ExclusionZone exclusionZone = styleNode.getExclusionZone();
                    if (exclusionZone != null) {
                        if (exclusionZone instanceof ExclusionRadius) {
                            Float radius = ValueHelper.getAsFloat(properties,((ExclusionRadius) (exclusionZone)).getRadius());
                            if(radius==null){
                                throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                            }
                            radius = UomUtils.toPixel(radius, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                            deltaX = radius;
                            deltaY = radius;
                        } else {
                             deltaX = ValueHelper.getAsFloat(properties,((ExclusionRectangle) (exclusionZone)).getX());
                            if(deltaX==null){
                                throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                            }
                             deltaY = ValueHelper.getAsFloat(properties,((ExclusionRectangle) (exclusionZone)).getY());

                            if(deltaY==null){
                                throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                            }
                            
                            deltaX = UomUtils.toPixel(deltaX, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                            deltaY = UomUtils.toPixel(deltaY, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                        }
                    }

                    AffineTransform at = AffineTransform.getTranslateInstance(x + deltaX, y + deltaY);

                    properties.put("affinetransform", at);
                    properties.put("verticalalignment", styleNode.getVerticalAlign());
                    styleTextDrawer.draw(g2, mapTransform, styleText, properties);
                    properties.remove("affinetransform");
                    properties.remove("verticalalignment");
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
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
