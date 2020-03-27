/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.stroke;

import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class PenStrokeDrawer implements IStrokeDrawer<PenStroke> {

    final static Map<Class, IFillDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
    }
    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PenStroke styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        IFill fill = styleNode.getFill();
        Float width = (Float) styleNode.getWidth().getValue();
        if (fill != null && width != null) {
            Uom uom = styleNode.getUom();
            if (drawerMap.containsKey(fill.getClass())) {
                IFillDrawer fillToDraw = drawerMap.get(fill.getClass());
                Paint paint = fillToDraw.getPaint(fill, properties, mapTransform);
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
                BasicStroke stroke;
                if (dashLengths != null) {
                    stroke = createDashStroke(styleNode, mapTransform, width, dashLengths, dashOffset);

                } else {
                    stroke = createBasicStroke(styleNode, mapTransform, width);
                }
                g2.setPaint(paint);
                g2.setStroke(stroke);
                if (paint != null) {
                    g2.draw(shape);
                } else {
                    //    // Others can't -> create the ares to fill
                    fillToDraw.setShape(stroke.createStrokedShape(shape));
                    fillToDraw.draw(g2, mapTransform, styleNode, properties);
                }
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
     * @param sp
     * @param shp
     * @param penStroke
     * @param mt
     * @param v100p
     * @param dashArray
     * @param dashOffset
     * @return
     * @throws ParameterException
     * @throws SQLException
     */
    private static BasicStroke createDashStroke(PenStroke penStroke, MapTransform mt, float v100p, float[] dashArray, float dashOffset) throws ParameterException, SQLException {
        Uom uom = penStroke.getUom();
        int cap;
        if (penStroke.getLineCap() == null) {
            cap = BasicStroke.CAP_BUTT;
        } else {
            switch (penStroke.getLineCap()) {
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
        if (penStroke.getLineJoin() == null) {
            join = BasicStroke.JOIN_ROUND;
        } else {
            switch (penStroke.getLineJoin()) {
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

    private BasicStroke createBasicStroke(PenStroke penStroke, MapTransform mt, float v100p) throws ParameterException, SQLException {
        Uom uom = penStroke.getUom();
        int cap;
        if (penStroke.getLineCap() == null) {
            cap = BasicStroke.CAP_BUTT;
        } else {
            switch (penStroke.getLineCap()) {
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
        if (penStroke.getLineJoin() == null) {
            join = BasicStroke.JOIN_ROUND;
        } else {
            switch (penStroke.getLineJoin()) {
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
     * @param penStroke
     * @param mt
     * @param properties
     * @return
     */
    public Double getNaturalLength(PenStroke penStroke, MapTransform mt, Map<String, Object> properties) {
        String dashArray = (String) penStroke.getDashArray().getValue();
        if (dashArray != null) {
            // A dashed PenStroke has a length
            // This is required to compute hatches tile but will break the compound stroke natural length logic
            // for infinite PenStroke element ! For this reason, compound stroke use getNaturalLengthForCompound
            try {
                double sum = 0.0;
                if (!dashArray.isEmpty()) {
                    String[] splitDash = dashArray.split(" ");
                    int size = splitDash.length;
                    for (int i = 0; i < size; i++) {
                        sum += UomUtils.toPixel(Float.parseFloat(splitDash[i]), penStroke.getUom(), mt.getDpi(), mt.getScaleDenominator());
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

}
