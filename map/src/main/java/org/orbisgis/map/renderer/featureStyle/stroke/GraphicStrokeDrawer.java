/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.GraphicStroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class GraphicStrokeDrawer implements IStrokeDrawer<GraphicStroke> {

    final static Map<Class, IGraphicDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicStroke styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Graphic graphic = styleNode.getGraphic();
        Uom uom = styleNode.getUom();
        if (graphic != null) {
            if (drawerMap.containsKey(graphic.getClass())) {
                IGraphicDrawer markStyle = drawerMap.get(graphic.getClass());
                Rectangle2D bounds = markStyle.getBounds(mapTransform, graphic, properties);
                RelativeOrientation rOrient = styleNode.getRelativeOrientation();
                double gWidth = bounds.getWidth();
                if (rOrient == RelativeOrientation.LINE) {
                    gWidth = bounds.getHeight();
                }
                double lineLength;
                Float length = (Float) styleNode.getLength().getValue();
                if (length == null) {
                    length = 0f;
                    lineLength = gWidth;
                } else {
                    lineLength = ShapeHelper.getLineLength(shape);
                }
                double segLength = getNaturalLength(length, (float) lineLength, uom, mapTransform);
                if (segLength > lineLength) {
                    segLength = lineLength;
                }

                List<Shape> segments = null;

                double nbSegments;

                Float relativePosition = (Float) styleNode.getRelativePosition().getValue();

                //int nbToDraw;
                if (styleNode.isLengthRapport()) {
                    nbSegments = (int) ((lineLength / segLength) + 0.5);
                    segments = ShapeHelper.splitLine(shape, (int) nbSegments);
                    //segLength = lineLength / nbSegments;
                    //nbToDraw = (int) nbSegments;
                } else {
                    nbSegments = lineLength / segLength;
                    if (nbSegments > 0) {
                        // TODO remove half of extra space at the beginning of the line
                        //shp = ShapeHelper.splitLine(shp, (nbSegments - nbToDraw)/2.0).get(1);
                        segments = ShapeHelper.splitLineInSeg(shape, segLength);
                    }

                    if (segments != null) {
                        for (Shape seg : segments) {
                            List<Shape> oSegs = new ArrayList<Shape>();
                            oSegs.add(seg);

                            for (Shape oSeg : oSegs) {
                                if (oSeg != null) {
                                    double realSegLength = ShapeHelper.getLineLength(oSeg);
                                    // Is there enough space on the real segment ?  otherwise is the graphic part of a compound stroke ?
                                    if (realSegLength > 0.9 * segLength) {
                                        Point2D.Double pt;
                                        float relativePos = 0.5f;

                                        if (relativePosition != null) {
                                            relativePos = relativePosition;
                                        }

                                        if (segLength < GraphicStroke.MIN_LENGTH) {
                                            pt = ShapeHelper.getPointAt(oSeg, 0);
                                        } else {
                                            // TODO Replace with relative position !
                                            pt = ShapeHelper.getPointAt(oSeg, realSegLength * relativePos);
                                        }
                                        AffineTransform at = AffineTransform.getTranslateInstance(pt.x, pt.y);

                                        if (rOrient != RelativeOrientation.PORTRAYAL) {
                                            Point2D.Double ptA;
                                            Point2D.Double ptB;

                                            if (segLength < GraphicStroke.MIN_LENGTH) {
                                                ptA = pt;
                                                ptB = ShapeHelper.getPointAt(oSeg, gWidth);
                                            } else {
                                                ptA = ShapeHelper.getPointAt(oSeg, relativePos * realSegLength - (gWidth * 0.5));
                                                ptB = ShapeHelper.getPointAt(oSeg, relativePos * realSegLength + (gWidth * 0.5));
                                            }

                                            double theta = Math.atan2(ptB.y - ptA.y, ptB.x - ptA.x);
                                            switch (rOrient) {
                                                case LINE:
                                                    theta += 0.5 * Math.PI;
                                                    break;
                                                case NORMAL_UP:
                                                    if (theta < -Math.PI / 2 || theta > Math.PI / 2) {
                                                        theta += Math.PI;
                                                    }
                                                    break;
                                            }
                                            at.concatenate(AffineTransform.getRotateInstance(theta));
                                        }

                                        properties.put("affinetransform", at);
                                        markStyle.draw(g2, mapTransform, styleNode, properties);
                                        
                                    }
                                }
                            }
                        }
                    }
                }
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

    public Double getNaturalLength(float length, float lineLength, Uom uom, MapTransform mt) throws ParameterException {
        double naturalLength;
        naturalLength = UomUtils.toPixel(length, uom, mt.getDpi(), mt.getScaleDenominator(), lineLength);
        //if (naturalLength <= GraphicStroke.MIN_LENGTH || naturalLength > lineLength) {
        if (naturalLength < 1e-5 || Double.isInfinite(naturalLength)) {
            return Double.POSITIVE_INFINITY;
        }
        if (naturalLength > lineLength) {
            naturalLength = lineLength;
        }
        return naturalLength;
    }

}
