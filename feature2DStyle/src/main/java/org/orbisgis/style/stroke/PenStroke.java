/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.style.stroke;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.fill.Fill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.parameter.string.StringLiteral;
import org.orbisgis.style.parameter.string.StringParameter;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.ExpressionParameter;

/**
 * Basic stroke for linear features. It is designed according to :
 * <ul><li>A {@link Fill} value</li>
 * <li>A width</li>
 * <li>A way to draw the extremities of the lines</li>
 * <li>A way to draw the joins between the segments of the lines</li>
 * <li>An array of dashes, that is used to draw the lines. The array is stored as a StringParamater,
 * that contains space separated double values. This double values are used to determine
 * the length of each opaque part (even elements of the array) and the length of 
 * each transparent part (odd elements of the array). If an odd number of values is given,
 * the pattern is expanded by repeating it twice to give an even number of values.</li>
 * <li>An offset used to know where to draw the line.</li>
 * </ul>
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class PenStroke extends Stroke implements FillNode {

    public static final double DEFAULT_WIDTH_PX = 1.0;
    public static final double DEFAULT_WIDTH = .25;
    /**
     * The cap used by default. Value is {@code LineCap.BUTT}.
     */
    public static final LineCap DEFAULT_CAP = LineCap.BUTT;
    /**
     * The join used by default. Value is {@code LineCap.MITRE}.
     */
    public static final LineJoin DEFAULT_JOIN = LineJoin.MITRE;
    private Fill fill;
    private ExpressionParameter width;
    private LineJoin lineJoin;
    private LineCap lineCap;
    private StringParameter dashArray;
    private RealParameter dashOffset;

    /**
     * There are three ways to draw the end of a line : butt, round and square.
     */
    public enum LineCap {

        BUTT, ROUND, SQUARE;
       
    }

    /**
     * There are three ways to join the segments of a LineString : mitre, round, bevel.
     */
    public enum LineJoin {

        MITRE, ROUND, BEVEL;

    }

    /**
     * Create a standard 0.1mm-wide opaque black stroke without dash.
     */
    public PenStroke() {
        super();
        setFill(getDefaultFill());
        setWidth(new ExpressionParameter(DEFAULT_WIDTH));
        setUom(null);
        setDashArray(new StringLiteral(""));
        setDashOffset(new RealLiteral(0));
        setLineCap(DEFAULT_CAP);
        setLineJoin(DEFAULT_JOIN);
    }
   


    @Override
    public Double getNaturalLength(Map<String,Object> map, Shape shp, IMapTransform mt) {

        if (dashArray != null) {
            // A dashed PenStroke has a length
            // This is required to compute hatches tile but will break the compound stroke natural length logic
            // for infinite PenStroke element ! For this reason, compound stroke use getNaturalLengthForCompound
            try {
                double sum = 0.0;
                String sDash = this.dashArray.getValue(map);
                if(!sDash.isEmpty()){
                String[] splitDash = sDash.split(" ");
                    int size = splitDash.length;
                    for (int i = 0; i < size; i++) {
                        sum += UomUtils.toPixel(Double.parseDouble(splitDash[i]), getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
                    }

                    if (size % 2 == 1) {
                        // # pattern item is odd -> 2* to close the pattern
                        sum *= 2;
                    }
                    return sum;
                }
            } catch (ParameterException ex) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Double getNaturalLengthForCompound(Map<String,Object> map,
            Shape shp, IMapTransform mt) throws ParameterException, IOException {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (fill != null) {
            ls.add(fill);
        }
        if (dashOffset != null) {
            ls.add(dashOffset);
        }
        if (dashArray != null) {
            ls.add(dashArray);
        }
        if (width != null) {
            ls.add(width);
        }
        return ls;
    }

    @Override
    public Fill getFill() {
        return fill;
    }

    /**
     * Sets the fill used to draw the inside of this {@code PenStroke}.
     * @param fill The new {@link Fill}. If null, will be set to a {@link SolidFill} which color is black and opacity
     *             is 100%.
     */
    @Override
    public void setFill(Fill fill) {
        this.fill = fill == null ? getDefaultFill() : fill;
        this.fill.setParent(this);
    }

    private Fill getDefaultFill(){
        return new SolidFill(Color.BLACK, 1.0);
    }

    /**
     * Sets the way to draw the extremities of a line.
     * @param cap The new {@link LineCap}. Will be replaced by {@see DEFAULT_CAP} if null.
     */
    public void setLineCap(LineCap cap) {
        lineCap = cap == null ? DEFAULT_CAP : cap;
    }

    /**
     * Gets the way used to draw the extremities of a line.
     * @return 
     */
    public LineCap getLineCap() {
        if (lineCap != null) {
            return lineCap;
        } else {
            return DEFAULT_CAP;
        }
    }

    /**
     * Sets the ways used to draw the join between line segments.
     * @param join  The new {@link LineJoin}. Will be replaced by {@see DEFAULT_JOIN} if null.
     */
    public void setLineJoin(LineJoin join) {
        lineJoin = join == null ? DEFAULT_JOIN : join;
    }

    /**
     * Gets the ways used to draw the join between line segments.
     * @return 
     */
    public LineJoin getLineJoin() {
        if (lineJoin != null) {
            return lineJoin;
        } else {
            return DEFAULT_JOIN;
        }

    }

    /**
     * Set the width used to draw the lines with this {@code PenStroke}.
     * @param width The new width. If null, will be replaced with {@link PenStroke#DEFAULT_WIDTH}, as specified in SE 2.0.
     */
    public void setWidth(ExpressionParameter width) {
        this.width = width == null ? new ExpressionParameter(DEFAULT_WIDTH) : width;
        if (width != null) {
            width.setParent(this);
        }
    }

    /**
     * Gets the width used to draw the lines with this PenStroke.
     * @return 
     */
    public ExpressionParameter getWidth() {
        return this.width;
    }

    /**
     * Gets the offset let before drawing the first dash.
     * @return  The offset let before drawing the first dash.
     */
    public RealParameter getDashOffset() {
        return dashOffset;
    }

    /**
     * Sets the offset let before drawing the first dash.
     * @param dashOffset If null, will be defaulted to 0.
     */
    public void setDashOffset(RealParameter dashOffset) {
        this.dashOffset = dashOffset == null ? new RealLiteral(0) : dashOffset;
        this.dashOffset.setContext(RealParameterContext.REAL_CONTEXT);
        this.dashOffset.setParent(this);
    }

    /**
     * Gets the array of double values that will be used to draw a dashed line. This "array" 
     * is in fact stored as a string parameter, filled with space separated double values.</p>
     * <p>These values represent the length (in the inner UOM) of the opaque (even elements of the array)
     * and transparent (odd elements of the array) parts of the lines to draw.
     * @return 
     */
    public StringParameter getDashArray() {
        return dashArray;
    }

    /**
     * Sets the array of double values that will be used to draw a dashed line. This "array" 
     * is in fact stored as a string parameter, filled with space separated double values.</p>
     * <p>These values represent the length (in the inner UOM) of the opaque (even elements of the array)
     * and transparent (odd elements of the array) parts of the lines to draw.
     * @param dashArray The new dash array. If null, will be replaced by a StringLiteral built with the empty string.
     */
    public void setDashArray(StringParameter dashArray) {
        this.dashArray = dashArray == null ? new StringLiteral("") : dashArray;
        this.dashArray.setParent(this);
    }
   

    private void scaleDashArrayLength(double[] dashes, Shape shp) {
        if (shp == null) {
            return;
        }

        double lineLength = ShapeHelper.getLineLength(shp);

        double sum = 0.0;
        for (double dash : dashes) {
            sum += dash;
        }

        int dashesSize = dashes.length;
        // number of element is odd => x2
        if ((dashesSize % 2) == 1) {
            sum *= 2;
        }

        double nbPattern = (int) ((lineLength / sum));

        if (nbPattern > 0) {
            double f = lineLength / (sum * nbPattern);
            for (int i = 0; i < dashesSize; i++) {
                dashes[i] *= f;
            }
        }
    }

    
    @Override
    public void draw(Graphics2D g2, Map<String,Object> map, Shape shape,
             IMapTransform mt, double offset)
            throws ParameterException, IOException {

    }

    /**
     * Gets the width, in pixels, of the lines that will be drawn using this {@code PenStroke}.
     * @param map
     * @param mt
     * @return
     * @throws ParameterException 
     */
    public double getWidthInPixel(Map<String,Object> map, IMapTransform mt) throws ParameterException {
        if (this.width != null) {
            return DEFAULT_WIDTH_PX;//UomUtils.toPixel(width.getValue(map), this.getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
        } else {
            return DEFAULT_WIDTH_PX;
        }
    }

    /**
     * Get the minimal length needed to display a complete dash pattern, including
     * the dash offset.
     * @param map
     * @param mt
     * @return
     * @throws ParameterException 
     */
    public double getMinLength(Map<String,Object> map, IMapTransform mt) throws ParameterException {
        double length = 0;
        if (dashArray != null) {
            String sDash = this.dashArray.getValue(map);
            String[] splitedDash = sDash.split(" ");
            int size = splitedDash.length;
            for (int i = 0; i < size; i++) {
                length += UomUtils.toPixel(Double.parseDouble(splitedDash[i]), getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
            }
        }

        if (dashOffset != null) {
            length += dashOffset.getValue(map);
        }

        return length;
    }    
}
