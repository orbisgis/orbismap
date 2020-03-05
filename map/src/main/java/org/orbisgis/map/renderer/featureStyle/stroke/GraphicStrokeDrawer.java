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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.GraphicStroke;
import static org.orbisgis.style.stroke.GraphicStroke.MIN_LENGTH;

/**
 *
 * @author ebocher
 */
public class GraphicStrokeDrawer implements ISymbolizerDraw<GraphicStroke>{

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, GraphicStroke styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        /*List<Shape> shapes;

        if (!this.isOffsetRapport() && Math.abs(offset) > 0.0) {
            shapes = ShapeHelper.perpendicularOffset(shape, offset);
            // Setting offset to 0.0 let be sure the offset will never been applied twice!
            offset = 0.0;
        } else {
            shapes = new ArrayList<Shape>();
            // TODO : Extract holes as separate shape !
            shapes.add(shape);
        }


        double gWidth = getGraphicWidth(map, mt);
        for (Shape shp : shapes) {
            double segLength = getNaturalLength(map, shp, mt);
            double lineLength = ShapeHelper.getLineLength(shp);

            if (segLength > lineLength){
                segLength = lineLength;
            }
                
            RelativeOrientation rOrient = this.getRelativeOrientation();
            List<Shape> segments = null;

            double nbSegments;

            //int nbToDraw;

            if (this.isLengthRapport()) {
                nbSegments = (int) ((lineLength / segLength) + 0.5);
                segments = ShapeHelper.splitLine(shp, (int) nbSegments);
                //segLength = lineLength / nbSegments;
                //nbToDraw = (int) nbSegments;
            } else {
                nbSegments = lineLength / segLength;
                if (nbSegments == 0 && getParent() instanceof StrokeElement) {
                    nbSegments = 1;
                }
                if (nbSegments > 0) {
                    // TODO remove half of extra space at the beginning of the line
                    //shp = ShapeHelper.splitLine(shp, (nbSegments - nbToDraw)/2.0).get(1);
                    segments = ShapeHelper.splitLineInSeg(shp, segLength);
                }
            }

            if (segments != null) {
                for (Shape seg : segments) {
                    List<Shape> oSegs;
                    if (this.isOffsetRapport() && Math.abs(offset) > 0.0) {
                        oSegs = ShapeHelper.perpendicularOffset(seg, offset);
                    } else {
                        oSegs = new ArrayList<Shape>();
                        oSegs.add(seg);
                    }

                    for (Shape oSeg : oSegs) {
                        if (oSeg != null) {
                            double realSegLength = ShapeHelper.getLineLength(oSeg);
                            // Is there enough space on the real segment ?  otherwise is the graphic part of a compound stroke ?
                            if (realSegLength > 0.9 * segLength || (getParent() instanceof StrokeElement && segLength == 0.0)) {
                                Point2D.Double pt;
                                double relativePos = 0.5;

                                if (relativePosition != null) {
                                    relativePos = relativePosition.getValue(map);
                                }

                                if (segLength < MIN_LENGTH) {
                                    pt = ShapeHelper.getPointAt(oSeg, 0);
                                } else {
                                    // TODO Replace with relative position !
                                    pt = ShapeHelper.getPointAt(oSeg, realSegLength * relativePos);
                                }
                                AffineTransform at = AffineTransform.getTranslateInstance(pt.x, pt.y);

                                if (rOrient != RelativeOrientation.PORTRAYAL) {
                                    Point2D.Double ptA;
                                    Point2D.Double ptB;

                                    if (segLength < MIN_LENGTH) {
                                        ptA = pt;
                                        ptB = ShapeHelper.getPointAt(oSeg, gWidth);
                                    } else {
                                        ptA = ShapeHelper.getPointAt(oSeg, relativePos * realSegLength - (gWidth*0.5));
                                        ptB = ShapeHelper.getPointAt(oSeg, relativePos * realSegLength + (gWidth*0.5));
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

                                //TODO uncoment
                                //graphic.draw(g2, map, mt, at);
                            }
                        }
                    }
                }
            }
        }*/
    }
    
}
