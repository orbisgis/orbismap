/**
 * Map is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.stroke;

import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.*;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.*;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.WobbleStroke;
import org.orbisgis.orbismap.style.utils.UomUtils;

import java.awt.*;

/**
 * Drawer for the element <code>WobbleStroke</code>
 * @author Erwan Bocher, CNRS (2020)
 */
public class WobbleStrokeDrawer extends AbstractDrawerFinder<IFillDrawer, IFill> implements IStrokeDrawer<WobbleStroke> {

    private Shape shape;
    private Stroke stroke;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, WobbleStroke styleNode) throws ParameterException {
        IFill fill = styleNode.getFill();
        Float width = (Float) styleNode.getWidth().getValue();
        if (fill != null && width != null) {
                Uom uom = styleNode.getUom();
                IFillDrawer fillToDraw = getDrawer(fill);
                Paint paint = fillToDraw.getPaint(fill, mapTransform);
                //Find dashArray info
                float[] dashLengths = null;
                Float dashOffset = 0f;
                String dashArray = (String) styleNode.getDashArray().getValue();
                if (dashArray != null) {
                    if (!dashArray.isEmpty()) {
                        dashLengths = parseDashExpression(dashArray, uom, mapTransform);
                        dashOffset = (Float) styleNode.getDashOffset().getValue();
                        if (dashOffset == null) {
                            dashOffset = 0f;
                        }
                    }
                }
            BasicStroke currentStroke = null;
            if (dashLengths != null ) {
                if(stroke==null) {
                    currentStroke = createDashStroke(styleNode, mapTransform, UomUtils.toPixel(width, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator()),
                            dashLengths, dashOffset);
                }
            } else {
                if(stroke==null) {
                    currentStroke = createBasicStroke(styleNode, mapTransform, UomUtils.toPixel(width, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator()));
                }
            }
            Float amplitude = (Float)styleNode.getAmplitude().getValue();
            Float detail = (Float)styleNode.getDetail().getValue();
            if(detail!=null && amplitude!=null){
                stroke = new WobbleStrokeImpl(currentStroke, amplitude, detail);
            }else{
                stroke = new WobbleStrokeImpl(currentStroke);
            }
            g2.setPaint(paint);
            g2.setStroke(stroke);
            if (paint != null) {
                g2.draw(shape);
            } else {
                //    // Others can't -> create the ares to fill
                fillToDraw.setShape(stroke.createStrokedShape(shape));
                fillToDraw.draw(g2, mapTransform, styleNode);
            }
        }
    }

    /**
     *
     * @param dashArray
     * @param uom
     * @param mapTransform
     * @return
     * @throws ParameterException
     */
    private float[] parseDashExpression(String dashArray, Uom uom, MapTransform mapTransform) throws ParameterException {
        String[] split = dashArray.split("\\s+");
        int splitSize = split.length;
        float[] dashLengths = new float[splitSize];
        for (int i = 0; i < splitSize; i++) {
            float dash = Float.parseFloat(split[i]);
            if (dash < 0) {
                throw new IllegalArgumentException("Dash array must be made "
                        + "of positive numbers separated with spaces.");
            }
            dashLengths[i] = (float) UomUtils.toPixel(dash, uom,
                    mapTransform.getDpi(), mapTransform.getScaleDenominator());
        }
        return dashLengths;
    }

    /**
     *
     * @param wobbleStroke
     * @param mt
     * @param v100p
     * @param dashArray
     * @param dashOffset
     * @return
     * @throws ParameterException
     */
    private static BasicStroke createDashStroke(WobbleStroke wobbleStroke, MapTransform mt, float v100p, float[] dashArray, float dashOffset) throws ParameterException {
        Uom uom = wobbleStroke.getUom();
        int cap;
        if (wobbleStroke.getLineCap() == null) {
            cap = BasicStroke.CAP_BUTT;
        } else {
            switch (wobbleStroke.getLineCap()) {
                case ROUND:
                    cap = BasicStroke.CAP_ROUND;
                    break;
                case SQUARE:
                    cap = BasicStroke.CAP_SQUARE;
                    break;
                default:
                case BUTT:
                    cap = BasicStroke.CAP_BUTT;
                    break;
            }
        }

        int join;
        if (wobbleStroke.getLineJoin() == null) {
            join = BasicStroke.JOIN_ROUND;
        } else {
            switch (wobbleStroke.getLineJoin()) {
                case BEVEL:
                    join = BasicStroke.JOIN_BEVEL;
                    break;
                case MITRE:
                    join = BasicStroke.JOIN_MITER;
                    break;
                case ROUND:
                default:
                    join = BasicStroke.JOIN_ROUND;
                    break;
            }
        }
        double w = UomUtils.toPixel(v100p, uom, mt.getDpi(), mt.getScaleDenominator()); // 100% based on view box height or width ? TODO

        double dashO = 0.0;
        if (dashOffset > 0) {
            dashO = UomUtils.toPixel(dashOffset, uom,
                    mt.getDpi(), mt.getScaleDenominator(), v100p);
        }
        return new BasicStroke((float) w, cap, join, 10.0f, dashArray, (float) dashO);

    }

    private BasicStroke createBasicStroke(WobbleStroke wobbleStroke, MapTransform mt, float v100p) throws ParameterException {
        Uom uom = wobbleStroke.getUom();
        int cap;
        if (wobbleStroke.getLineCap() == null) {
            cap = BasicStroke.CAP_BUTT;
        } else {
            switch (wobbleStroke.getLineCap()) {
                case ROUND:
                    cap = BasicStroke.CAP_ROUND;
                    break;
                case SQUARE:
                    cap = BasicStroke.CAP_SQUARE;
                    break;
                default:
                case BUTT:
                    cap = BasicStroke.CAP_BUTT;
                    break;
            }
        }

        int join;
        if (wobbleStroke.getLineJoin() == null) {
            join = BasicStroke.JOIN_ROUND;
        } else {
            switch (wobbleStroke.getLineJoin()) {
                case BEVEL:
                    join = BasicStroke.JOIN_BEVEL;
                    break;
                case MITRE:
                    join = BasicStroke.JOIN_MITER;
                    break;
                case ROUND:
                default:
                    join = BasicStroke.JOIN_ROUND;
                    break;
            }
        }
        double w = UomUtils.toPixel(v100p, uom, mt.getDpi(), mt.getScaleDenominator()); // 100% based on view box height or width ? TODO

        return new BasicStroke((float) w, cap, join);
    }

    /**
     *
     * @param wobbleStroke
     * @param mt
     * @return
     */
    @Override
    public Double getNaturalLength(WobbleStroke wobbleStroke, MapTransform mt) {
        String dashArray = (String) wobbleStroke.getDashArray().getValue();
        if (dashArray != null) {
            // A dashed wobbleStroke has a length
            // This is required to compute hatches tile but will break the compound stroke natural length logic
            // for infinite PenStroke element ! For this reason, compound stroke use getNaturalLengthForCompound
            try {
                double sum = 0.0;
                if (!dashArray.isEmpty()) {
                    String[] splitDash = dashArray.split(" ");
                    int size = splitDash.length;
                    for (int i = 0; i < size; i++) {
                        sum += UomUtils.toPixel(Float.parseFloat(splitDash[i]), wobbleStroke.getUom(), mt.getDpi(), mt.getScaleDenominator());
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
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public IFillDrawer getDrawer(IFill styleNode) {
        if (styleNode != null) {
            IFillDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof Halo) {
                    drawer = new HaloDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof SolidFill) {
                    drawer = new SolidFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof HatchedFill) {
                    drawer = new HatchedFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof DensityFill) {
                    drawer = new DensityFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof DotMapFill) {
                    drawer = new DotMapFillDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }
}
