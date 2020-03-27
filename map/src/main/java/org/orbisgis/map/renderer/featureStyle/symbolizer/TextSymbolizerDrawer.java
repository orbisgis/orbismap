/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.label.PointLabelDrawer;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class TextSymbolizerDrawer implements ISymbolizerDraw<TextSymbolizer> {

    final static Map<Class, ILabelDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(PointLabel.class, new PointLabelDrawer());
    }
    private Shape shape;

    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, TextSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
        List<Shape> shps;
        //RealParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
        /*if (perpendicularOffset != null) {
                Double pOffset = perpendicularOffset.getValue(properties);
                shps = ShapeHelper.perpendicularOffset(shape, pOffset);
            } else {*/
        shps = new LinkedList<Shape>();
        shps.add(getShape());
        //}

        Label label = symbolizer.getLabel();
        if (drawerMap.containsKey(label.getClass())) {
            properties.put("offset", 0.0);
            ILabelDrawer labelDrawer = drawerMap.get(label.getClass());
            for (Shape s : shps) {
                labelDrawer.setShape(s);
                labelDrawer.draw(g2, mapTransform, label, properties);
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
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bi = bufferedImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bi;
    }

    @Override
    public void setGraphics2D(Graphics2D g2) {
        this.g2_bi = g2;
    }

    @Override
    public Graphics2D getGraphics2D() {
        return g2_bi;
    }

    @Override
    public void dispose(Graphics2D g2) {
        if (g2 != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, null, null);
            bi = null;
        }
    }

}
