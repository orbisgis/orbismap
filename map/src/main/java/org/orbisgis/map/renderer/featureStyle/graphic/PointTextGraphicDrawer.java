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
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.label.PointLabelDrawer;
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
    private AffineTransform affineTransform;

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, PointTextGraphic styleNode) throws ParameterException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointTextGraphic styleNode) throws ParameterException {
       if (affineTransform != null) {
            PointLabel pointLabel = styleNode.getPointLabel();
            if (pointLabel != null) {
                AffineTransform at = new AffineTransform(affineTransform);
                double px = 0;
                double py = 0;
                Uom uom = styleNode.getUom();
                Float x = (Float) styleNode.getX().getValue();
                if (x != null) {
                    px = UomUtils.toPixel(x, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }
                Float y = (Float) styleNode.getY().getValue();
                if (y != null) {
                    py = UomUtils.toPixel(y, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }

                Rectangle2D.Double bounds = new Rectangle2D.Double(px - 5, py - 5, 10, 10);
                Shape atShp = at.createTransformedShape(bounds);
                PointLabelDrawer drawer = new PointLabelDrawer();
                drawer.setShape(atShp);
                drawer.draw(g2, mapTransform, pointLabel);
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

    @Override
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }
}
