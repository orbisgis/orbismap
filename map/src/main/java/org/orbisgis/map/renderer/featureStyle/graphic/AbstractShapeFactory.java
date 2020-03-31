/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.geom.Point2D;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public abstract class AbstractShapeFactory implements IShapeFactory{

     /**
     * Default size to be used to render graphics based on well-known names.
     */
    public static final double DEFAULT_SIZE = 10.0;
    private String name;
    String shapeName;
    
    public AbstractShapeFactory(String name){
        this.name=name;
    }
    
    /*
     * Return the final dimension described by this view box, in [px].
     *
     * @param uom
     * @param height
     * @param scale required final ratio (if either width or height isn't
     * defined)
     * @param width
     * @param dpi
     * @return
     * @throws ParameterException
     */
    public static Point2D getDimensionInPixel(Uom uom, double height,
            double width, Double scale, Double dpi) throws ParameterException {
        float dx, dy;
        double ratio = height / width;
        if (width < 0 && height < 0) {
            dx = MarkGraphic.DEFAULT_SIZE;
            dy = MarkGraphic.DEFAULT_SIZE;
        } else if (width > 0 && height < 0) {
            dx = (float) width;
            dy = (float) (dx * ratio);
        } else if (height > 0 && width < 0) {
            dy = (float) height;
            dx = (float) (dy / ratio);
        } else { // nothing is defined
            dx = (float) width;
            dy = (float) height;
        }

        dx = UomUtils.toPixel(dx, uom, dpi, scale, (float) width);
        dy = UomUtils.toPixel(dy, uom, dpi, scale, (float) height);

        if (Math.abs(dx) <= 0.00021 || Math.abs(dy) <= 0.00021) {
            dx = 0;
            dy = 0;
        }

        return new Point2D.Double(dx, dy);
    }

    /**
     * Return the final dimension described by this view box, in [px].
     *
     * @param scale required final ratio (if either width or height isn't
     * defined)
     * @return
     * @throws ParameterException
     */
    public Point2D getDimensionInPixel(Uom uom, ViewBox viewBox, double height,
            double width, Double scale, Double dpi) throws ParameterException {
        float dx, dy;

        Float x = (Float) viewBox.getWidth().getValue();
        Float y = (Float) viewBox.getHeight().getValue();
        double ratio = height / width;

        if (x != null && y != null) {
            dx = x;
            dy = y;
        } else if (x != null) {
            dx = x;
            dy = (float) (dx * ratio);
        } else if (y != null) {
            dy = y;
            dx = (float) (dy / ratio);
        } else { // nothing is defined
            dx = (float) width;
            dy = (float) height;
            //return null; 
        }

        dx = UomUtils.toPixel(dx, uom, dpi, scale, (float) width);
        dy = UomUtils.toPixel(dy, uom, dpi, scale, (float) height);

        if (Math.abs(dx) <= 0.00021 || Math.abs(dy) <= 0.00021) {
            dx = 0;
            dy = 0;
        }

        return new Point2D.Double(dx, dy);
    }
    
    @Override
    public String getIdentifier() {
        return  this.name;
    }    

    @Override
    public void setShapeName(String shapeName) {
        this.shapeName=shapeName;
    }

    @Override
    public String getShapeName() {
        return this.shapeName;
    }

   
    
    
}
