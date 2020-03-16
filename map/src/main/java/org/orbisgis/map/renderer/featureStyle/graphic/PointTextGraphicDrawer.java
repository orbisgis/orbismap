/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.label.PointLabelDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.PointTextGraphic;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class PointTextGraphicDrawer implements IGraphicDrawer<PointTextGraphic> {

    private Shape shape;

    @Override
    public Rectangle2D getBounds( MapTransform mapTransform, PointTextGraphic styleNode, Map<String, Object> properties) throws ParameterException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, PointTextGraphic styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        AffineTransform fat = (AffineTransform) properties.get("affinetransform");
        if (fat != null) {
            PointLabel pointLabel = styleNode.getPointLabel();
            if(pointLabel!=null){
            AffineTransform at = new AffineTransform(fat);
            double px = 0;
            double py = 0;
            Uom uom = styleNode.getUom();
            Float x = ValueHelper.getAsFloat(properties, styleNode.getX());
            if (x != null) {
                px = UomUtils.toPixel(x, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
            }
            Float y = ValueHelper.getAsFloat(properties, styleNode.getY());
            if (y != null) {
                py = UomUtils.toPixel(y, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
            }

            Rectangle2D.Double bounds = new Rectangle2D.Double(px - 5, py - 5, 10, 10);
            Shape atShp = at.createTransformedShape(bounds);
            properties.put("shape", atShp);
            new PointLabelDrawer().draw(g2, mapTransform, pointLabel, properties);
            }
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
