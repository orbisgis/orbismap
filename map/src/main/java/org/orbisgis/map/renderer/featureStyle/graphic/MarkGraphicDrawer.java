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
import java.util.HashMap;
import java.util.Map;

import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author Erwan Bocher
 */
public class MarkGraphicDrawer implements IGraphicDrawer<MarkGraphic> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(Halo.class, new HaloDrawer());
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }
    // cached shape : only available with shape that doesn't depends on features
    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        Shape shp = null;
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;

        }

        if (shp != null) {
            AffineTransform at = new AffineTransform(getAffineTransform());
            if (styleNode.getTransform() != null) {
                //TODO : Put in cache...
                //TransformBuilder transformBuilder = new TransformBuilder();
                //at.concatenate(transformBuilder.getAffineTransform(styleNode.getTransform(), properties));
            }
            Shape atShp = at.createTransformedShape(shp);

            //We give the raw shape to the drawHalo method in order not to lose the
            //type of the original Shape - It will be easier to compute the halo.
            //We give the transformed shape too... This way we are sure we won't
            //compute it twice, as it is a complicated operation.
            Halo halo = styleNode.getHalo();
            if (halo != null) {
                if (drawerMap.containsKey(halo.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(halo.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, halo);
                }
            }
            IFill fill = styleNode.getFill();
            if (fill != null) {
                if (drawerMap.containsKey(fill.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(fill.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, fill);
                }
            }
            Stroke stroke = styleNode.getStroke();
            if (stroke != null) {
                if (drawerMap.containsKey(stroke.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(stroke.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, stroke);
                }
            }
        }

    }

    /**
     * TODO : implements
     *
     * @param markGraphic
     * @param mapTransform
     * @return
     * @throws ParameterException
     */
    public Shape getShape(MarkGraphic markGraphic, MapTransform mapTransform) throws ParameterException {
       return ShapeFinder.getShape((String) markGraphic.getWkn().getValue(), markGraphic.getViewBox(),  mapTransform.getScaleDenominator(),  mapTransform.getDpi(), markGraphic.getUom());           
   }

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        Shape shp = null;
        // If the shape doesn't depends on feature (i.e. not null), we used the cached one
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;
        }

        /*if (transform != null) {
            return this.transform.getGraphicalAffineTransform(false, map, mt, shp.getBounds().getWidth(),
                    shp.getBounds().getHeight()).createTransformedShape(shp).getBounds2D();
        } else {*/
        return shp.getBounds2D();/*
        }*/

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
