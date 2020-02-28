/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.api;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Erwan Bocher
 * @param <M>
 */
public interface IMapTransform<M extends IMapEnvelope> {

    /**
     * @return The currently configured dot-per-inch measure.
     */
    public double getDpi();

    /**
     * Gets the scale denominator. If the scale is 1:1000 this method returns
     * 1000. The scale is not absolutely precise and errors of 2% have been
     * measured.
     *
     *
     * @return the scale as double value
     */
    public double getScaleDenominator();

    /**
     * Gets the current {@code RenderingHints}
     *
     * @return the current {@link RenderingHints}
     */
    public RenderingHints getRenderingHints();

    /**
     * Gets the current {@code AffineTransform}
     *
     * @return the current {@link AffineTransform}
     */
    public AffineTransform getAffineTransform();

    /**
     * Sets the extent of the transformation. This extent is not used directly
     * to calculate the transformation but is adjusted to obtain an extent with
     * the same ratio than the image
     *
     * @param newExtent The new base extent.
     */
    public void setExtent(M newExtent);

    /**
     * Gets the extent
     *
     * @return
     */
    public M getExtent();
    

}
