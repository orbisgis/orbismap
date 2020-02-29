/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.fill.Fill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class StrokeRender {

    static void drawPenStroke(JdbcSpatialTable rs, Graphics2D g2, PenStroke penStroke, Shape shape, MapTransform mapTransform, double offset) throws SQLException, ParameterException {

        Fill fill = penStroke.getFill();

        double width = (double) rs.getDouble(penStroke.getWidth().getIdentifier());

        if (fill != null && width > 0) {
            if (fill instanceof SolidFill) {
                List<Shape> shapes;
                // if not using offset rapport, compute perpendicular offset first
                if (!penStroke.isOffsetRapport() && Math.abs(offset) > 0.0) {
                    shapes = ShapeHelper.perpendicularOffset(shape, offset);
                    // Setting offset to 0.0 let be sure the offset will never been applied twice!
                    offset = 0.0;
                } else {
                    shapes = new ArrayList<Shape>();
                    shapes.add(shape);
                }

                Paint paint = FillRenderer.getPaint(rs, (SolidFill) fill, mapTransform);

                for (Shape shp : shapes) {
                    /*if (this.dashArray != null && !this.dashArray.getValue(map).isEmpty() && Math.abs(offset) > 0.0) {
                    String value = dashArray.getValue(map);
                    String[] split = value.split("\\s+");
                    Shape chute = shp;
                    List<Shape> fragments = new ArrayList<Shape>();
                    BasicStroke bs = createBasicStroke(map, shp, mt, null, false);

                    int splitSize = split.length;
                    double dashLengths[] = new double[splitSize];
                    for (int i = 0; i < splitSize; i++) {
                        dashLengths[i] = UomUtils.toPixel(Double.parseDouble(split[i]), getUom(),
                                mt.getDpi(), mt.getScaleDenominator(), null);
                    }

                    if (this.isLengthRapport()) {
                        scaleDashArrayLength(dashLengths, shp);
                    }

                    int i = 0;
                    int j = 0;

                    //while (ShapeHelper.getLineLength(chute) > 0) {
                    while (chute != null) {
                        List<Shape> splitLine = ShapeHelper.splitLine(chute, dashLengths[j]);
                        Shape seg = splitLine.remove(0);
                        if (splitLine.size() > 0) {
                            chute = splitLine.remove(0);
                        } else {
                            chute = null;
                        }
                        if (i % 2 == 0) {
                            // i.e seg to draw
                            fragments.add(seg);
                        } // else means blank space

                        j = (j + 1) % split.length;
                        i++;
                    }

                    if (paint != null) {
                        g2.setPaint(paint);
                        g2.setStroke(bs);
                    }

                    for (Shape seg : fragments) {
                        List<Shape> ses = ShapeHelper.perpendicularOffset(seg, offset);
                        for (Shape oSeg : ses) {
                            if (oSeg != null) {
                                if (paint != null) {
                                    g2.draw(oSeg);
                                } else {
                                    Shape outline = bs.createStrokedShape(oSeg);
                                    fill.draw(g2, map, outline, mt);
                                }
                            }
                        }
                    }
                } else {*/

                    BasicStroke stroke = createBasicStroke(rs, penStroke, mapTransform, 0, true);
                    //g2.setPaint(paint);
                    g2.setStroke(stroke);

                    /*if (Math.abs(offset) > 0.0) {
                        List<Shape> ses = ShapeHelper.perpendicularOffset(shp, offset);
                        for (Shape oShp : ses) {
                            if (oShp != null) {
                                if (paint != null) {
                                    g2.setStroke(stroke);
                                    g2.setPaint(paint);
                                    g2.draw(oShp);
                                } else {
                                    Shape outline = stroke.createStrokedShape(oShp);
                                    fill.draw(g2, map, outline, mt);
                                }
                            }
                        }
                    } else {*/
                        if (paint != null) {
                            // Some fill type can be converted to a texture paint or a solid color
                            g2.setStroke(stroke);
                            g2.setPaint(paint);
                            g2.draw(shp);
                        } else {
                            // Others can't -> create the ares to fill
                            Shape outline = stroke.createStrokedShape(shp);                            
                            FillRenderer.drawSolidFill(rs, g2, (SolidFill) fill, outline, mapTransform);
                        }
                    //}
                    // }
                }
            }
        }
    }

    private static BasicStroke createBasicStroke(JdbcSpatialTable rs, PenStroke penStroke, MapTransform mt, double v100p, boolean useDash) throws ParameterException, SQLException {

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

        double w = penStroke.DEFAULT_WIDTH_PX;

        ExpressionParameter width = penStroke.getWidth();
        if (width != null) {
            w = rs.getDouble(width.getIdentifier());
            w = UomUtils.toPixel(w, uom, mt.getDpi(), mt.getScaleDenominator(), null); // 100% based on view box height or width ? TODO
        }


        /*if (useDash && this.dashArray != null && !this.dashArray.getValue(map).isEmpty()) {

            double dashO = 0.0;
            double[] dashA;

            String sDash = this.dashArray.getValue(map);
            String[] splitedDash = sDash.split(" ");
            int dashSize = splitedDash.length;
            dashA = new double[dashSize];
            for (int i = 0; i < dashSize; i++) {
                dashA[i] = UomUtils.toPixel(Double.parseDouble(splitedDash[i]), uom,
                        mt.getDpi(), mt.getScaleDenominator(), v100p);
            }

            if (this.dashOffset != null) {
                dashO = UomUtils.toPixel(this.dashOffset.getValue(map), uom,
                        mt.getDpi(), mt.getScaleDenominator(), v100p);
            }

            if (this.isLengthRapport()) {
                scaleDashArrayLength(dashA, shp);
            }

            float[] dashes = new float[dashA.length];
            int dashesSize = dashes.length;
            for (int i = 0; i < dashesSize; i++) {
                dashes[i] = (float) dashA[i];
                if(dashes[i] < 0){
                        throw new IllegalArgumentException("Dash array must be made "
                                + "of positive numbers separated with spaces.");
                }
            }
            return new BasicStroke((float) w, cap, join, 10.0f, dashes, (float) dashO);
        } else {*/
        return new BasicStroke((float) w, cap, join);
        //}
    }

}
