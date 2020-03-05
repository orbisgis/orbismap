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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.string.StringParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class PenStrokeDrawer implements ISymbolizerDraw<PenStroke>{
    
    final static Map<Class, IFillDrawer> drawerMap = new HashMap<>();
    static {
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, PenStroke styleNode,Map<String, Object> properties) throws ParameterException, SQLException {
        IFill fill = styleNode.getFill();        
        double offset = (double) properties.get("offset");
        double width = ValueHelper.getDouble(sp, styleNode.getWidth());
        Shape shape = (Shape) properties.get("shape");
        if (fill != null && width > 0) {
            List<Shape> shapes;
            Uom uom =  styleNode.getUom();
            // if not using offset rapport, compute perpendicular offset first
            if (!styleNode.isOffsetRapport() && Math.abs(offset) > 0.0) {
                shapes = ShapeHelper.perpendicularOffset(shape, offset);
                // Setting offset to 0.0 let be sure the offset will never been applied twice!
                offset = 0.0;
            } else {
                shapes = new ArrayList<Shape>();
                shapes.add(shape);
            }
            
             if(drawerMap.containsKey(fill.getClass())){
                IFillDrawer fillToDraw = drawerMap.get(fill.getClass());
                Paint paint = fillToDraw.getPaint(sp, fill, properties, mapTransform);                
                //Find dashArray info
                float[] dashLengths =null;
                float dashOffset = 0;
                String dashArray = ValueHelper.getString(sp, styleNode.getDashArray());
                if (dashArray != null){
                    if(!dashArray.isEmpty() && Math.abs(offset) > 0.0) {
                       dashLengths = parseDashExpression(dashArray,uom, mapTransform);
                       dashOffset = ValueHelper.getFloat(sp, styleNode.getDashOffset());
                    }
                }
              
            for (Shape shp : shapes) {
                if (dashLengths!=null) {                    
                    Shape chute = shp;
                    List<Shape> fragments = new ArrayList<>();
                    if (styleNode.isLengthRapport()) {
                        scaleDashArrayLength(dashLengths, shp);
                    }
                    BasicStroke bs = createDashStroke(sp, shp, styleNode, mapTransform, width, dashLengths, dashOffset);                    

                    int i = 0;
                    int j = 0;

                    int split = dashLengths.length;
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

                        j = (j + 1) % split;
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
                                    properties.put("shape", outline);
                                    fillToDraw.draw(sp, g2, mapTransform, styleNode, properties);
                                }
                            }
                        }
                    }
                } else {
                    BasicStroke stroke = createBasicStroke(sp, styleNode, mapTransform, width);
                    g2.setPaint(paint);
                    g2.setStroke(stroke);

                    if (Math.abs(offset) > 0.0) {
                        List<Shape> ses = ShapeHelper.perpendicularOffset(shp, offset);
                        for (Shape oShp : ses) {
                            if (oShp != null) {
                                if (paint != null) {
                                    //g2.setStroke(stroke);
                                    //g2.setPaint(paint);
                                    g2.draw(oShp);
                                } else {
                                    Shape outline = stroke.createStrokedShape(oShp);
                                    properties.put("shape", outline);
                                    fillToDraw.draw(sp, g2, mapTransform, styleNode, properties);
                                }
                            }
                        }
                    } else {
                        if (paint != null) {
                            // Some fill type can be converted to a texture paint or a solid color
                            //g2.setStroke(stroke);
                            //g2.setPaint(paint);
                            g2.draw(shape);
                        } else {
                        //    // Others can't -> create the ares to fill
                            Shape outline = stroke.createStrokedShape(shp);
                            properties.put("shape", outline);
                            fillToDraw.draw(sp, g2, mapTransform, styleNode, properties);
                        }
                    }
                }
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
    private float[] parseDashExpression(String dashArray,Uom uom, MapTransform mapTransform) throws ParameterException {
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
                    mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
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
    private static BasicStroke createDashStroke(JdbcSpatialTable sp, Shape shp, PenStroke penStroke, MapTransform mt, double v100p, float[] dashArray, float dashOffset) throws ParameterException, SQLException {
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
        double w = UomUtils.toPixel(v100p, uom, mt.getDpi(), mt.getScaleDenominator(), null); // 100% based on view box height or width ? TODO

        double dashO = 0.0;
        if (dashOffset > 0) {
            dashO = UomUtils.toPixel(dashOffset, uom,
                    mt.getDpi(), mt.getScaleDenominator(), v100p);
        }
        return new BasicStroke((float) w, cap, join, 10.0f, dashArray, (float) dashO);

    }
        
        
    private  BasicStroke createBasicStroke(JdbcSpatialTable sp, PenStroke penStroke, MapTransform mt, double v100p) throws ParameterException, SQLException {
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
        double w = UomUtils.toPixel(v100p, uom, mt.getDpi(), mt.getScaleDenominator(), null); // 100% based on view box height or width ? TODO

        return new BasicStroke((float) w, cap, join);    
    }
    
    
     static private void  scaleDashArrayLength(float[] dashes, Shape shp) {
        if (shp == null) {
            return;
        }

        double lineLength = ShapeHelper.getLineLength(shp);

        double sum = 0.0;
        for (float dash : dashes) {
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
     
     /**
      * 
      * @param rs
      * @param penStroke
      * @param mt
      * @param properties
      * @return 
      */
     public Double getNaturalLength(JdbcSpatialTable rs, PenStroke penStroke, MapTransform mt,Map<String, Object> properties) throws SQLException {
        String dashArray = ValueHelper.getString(rs, penStroke.getDashArray());
        if (dashArray != null) {
            // A dashed PenStroke has a length
            // This is required to compute hatches tile but will break the compound stroke natural length logic
            // for infinite PenStroke element ! For this reason, compound stroke use getNaturalLengthForCompound
            try {
                double sum = 0.0;
                if(!dashArray.isEmpty()){
                    String[] splitDash = dashArray.split(" ");
                    int size = splitDash.length;
                    for (int i = 0; i < size; i++) {
                        sum += UomUtils.toPixel(Double.parseDouble(splitDash[i]), penStroke.getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
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
    
}
