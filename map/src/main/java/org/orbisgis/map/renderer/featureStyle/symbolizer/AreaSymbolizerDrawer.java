/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import org.orbisgis.map.renderer.featureStyle.fill.DensityFillDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.fill.DotMapFillDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HatchedFillDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.GraphicFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author ebocher
 */
public class AreaSymbolizerDrawer implements ISymbolizerDraw<AreaSymbolizer> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
        drawerMap.put(HatchedFill.class, new HatchedFillDrawer());
        drawerMap.put(DotMapFill.class, new DotMapFillDrawer());
        drawerMap.put(DensityFill.class, new DensityFillDrawer());
        drawerMap.put(GraphicFill.class, new GraphicFillDrawer());    
    }
    private Shape shape;
    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, AreaSymbolizer symbolizer) throws ParameterException {
        Uom uom = symbolizer.getUom();
        if (symbolizer.getTranslate() != null) {
            // TODO : shp = AffineTransformUtils.getAffineTranslate(symbolizer.getTranslate(), uom,properties, mapTransform,
            //       (double) mapTransform.getWidth(), (double) mapTransform.getHeight()).createTransformedShape(shp);
        }

        IFill fill = symbolizer.getFill();
        if (fill != null) {
            if (drawerMap.containsKey(fill.getClass())) {
                IStyleDrawer drawer = drawerMap.get(fill.getClass());
                drawer.setShape(getShape());
                drawer.draw(g2, mapTransform, fill);
            }
        }

        Stroke stroke = symbolizer.getStroke();
        if (stroke != null) {
            if (drawerMap.containsKey(stroke.getClass())) {
                IStyleDrawer drawer = drawerMap.get(stroke.getClass());
                drawer.setShape(getShape());
                drawer.draw(g2, mapTransform, stroke);
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
        //The g2 can be null when the Graphics2D has been already disposed.
        if (g2_bi != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, 0, 0, null);
            bi = null;
        }
    }

}
