/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.fill;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.HatchedFill;
import static org.orbisgis.style.fill.HatchedFill.DEFAULT_ALPHA;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class HatchedFillDrawer implements IFillDrawer<HatchedFill> {
      //Useful constants.
    private static final double EPSILON = 0.01; // todo Eval, and use an external EPSILON value.
    private static final double TWO_PI_DEG = 360.0;
    private static final double PI_DEG = 180.0;
    
    public static final double DEFAULT_NATURAL_LENGTH = 100;
    
    final static Map<Class, PenStrokeDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }
    private Shape shape;

    
    @Override
    public Paint getPaint( HatchedFill styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        return null;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, HatchedFill styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        if (shape != null) {
        Uom   uom =   styleNode.getUom();
        Stroke stroke = styleNode.getStroke();
        if (stroke != null) {
            if(drawerMap.containsKey(stroke.getClass())){
                PenStrokeDrawer strokeToDraw = drawerMap.get(stroke.getClass());            
                // Perpendicular distance between two lines
            try {
                float pDist = 0;
                Float distance =  (Float) styleNode.getDistance().getValue();
                if(distance ==null ){
                    throw new ParameterException("The distance parameter for the hatched fill cannot be null");

                }
                if (distance > 0) {
                    pDist = UomUtils.toPixel(distance, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }                

                float alpha = DEFAULT_ALPHA;
                Float angle = (Float) styleNode.getAngle().getValue();
                if(angle==null){
                    throw new ParameterException("The angle parameter for the hatched fill cannot be null");
                }
                if (angle >0) {
                    alpha = angle;
                }
                double hOffset = 0.0;
                Float offset =  (Float) styleNode.getOffset().getValue();
                if(offset==null){
                   throw new ParameterException("The offset parameter for the hatched fill cannot be null");

                }
                if (offset >0) {
                    hOffset = UomUtils.toPixel(offset, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }

                drawHatch(g2, properties, shape, mapTransform, alpha, pDist, (PenStroke)stroke, strokeToDraw, hOffset);
                
            } catch (RuntimeException eee) {
                System.out.println("Error " + eee);
                eee.printStackTrace(System.out);
            }
            }
        }
        }
    }
    
    /**
     * Static method that draw hatches within provided shp
     * 
     * @param sp
     * @param g2  the g2 to write on
     * @param shp the shape to hatch
     * @param mt the well known map transform
     * @param alph hatches orientation
     * @param pDist perpendicular distance between two hatch line (stroke width 
     *         not taken into account so a 10mm wide black PenStroke + pDist=10mm
     *         will produce a full black behaviour...)
     * @param hOffset offset between the references point and the reference hatch
     * @param penStroke
     * @throws ParameterException 
     */
    public static void drawHatch( Graphics2D g2, Map<String,Object> properties, Shape shp,
             MapTransform mt, double alph, double pDist,PenStroke penStroke, PenStrokeDrawer penStrokeDrawer,
            double hOffset) throws ParameterException, SQLException {
       
        double alpha = alph;
        while (alpha < 0.0) {
            alpha += TWO_PI_DEG;
        }   // Make sure alpha is > 0
        while (alpha > TWO_PI_DEG) {
            alpha -= TWO_PI_DEG;
        } // and < 360.0
        alpha = alpha * Math.PI / PI_DEG; // and finally convert in radian
        double beta = Math.PI / 2.0 + alpha;
        double deltaOx = Math.cos(beta) * hOffset;
        double deltaOy = Math.sin(beta) * hOffset;
        Double naturalLength = penStrokeDrawer.getNaturalLength( penStroke, mt, properties);
        if (naturalLength.isInfinite()) {
            naturalLength = DEFAULT_NATURAL_LENGTH;
        }

        // the first hatch to generate is the reference one : it crosses the reference point
        Point2D.Double geoRef = new Point2D.Double(0, 0);
        // Map geo ref point within g2 space
        Point2D ref = mt.getAffineTransform().transform(geoRef, null);
        // Apply hatch offset to ref point
        ref.setLocation(ref.getX() + deltaOx, ref.getY() + deltaOy);

        // Compute some var
        double cosAlpha = Math.cos(alpha);
        double sinAlpha = Math.sin(alpha);

        if (Math.abs(sinAlpha) < EPSILON) {
            sinAlpha = 0.0;
        }

        boolean vertical = false;

        if (Math.abs(cosAlpha) < EPSILON) {
            cosAlpha = 0.0;
            vertical = true;
        }

        double deltaHx = cosAlpha * naturalLength;
        double deltaHy = sinAlpha * naturalLength;

        double deltaDx = pDist / sinAlpha;
        double deltaDy = pDist / cosAlpha;

        Rectangle2D fbox = shp.getBounds2D();


        /* the following block compute the number of times the hatching pattern shall be drawn */

        int nb2start; // how many pattern to skip from the ref point to the begining of the shape ?
        int nb2end; // how many pattern to skip from the ref point to the end of the shape ?

        if (vertical) {
            if (deltaDx >= 0.0) {
                nb2start = (int) Math.ceil((fbox.getMinX() - ref.getX()) / deltaDx);
                nb2end = (int) Math.floor(((fbox.getMaxX() - ref.getX()) / deltaDx));
            } else {
                nb2start = (int) Math.floor((fbox.getMinX() - ref.getX()) / deltaDx);
                nb2end = (int) Math.ceil(((fbox.getMaxX() - ref.getX()) / deltaDx));
            }
        } else {
            if (cosAlpha < 0) {
                nb2start = (int) Math.ceil((fbox.getMinX() - ref.getX()) / deltaHx);
                nb2end = (int) Math.floor(((fbox.getMaxX() - ref.getX()) / deltaHx));
            } else {
                nb2start = (int) Math.floor((fbox.getMinX() - ref.getX()) / deltaHx);
                nb2end = (int) Math.ceil(((fbox.getMaxX() - ref.getX()) / deltaHx));
            }
        }

        int nb2draw = nb2end - nb2start;

        double ref_yXmin;
        double ref_yXmax;

        double cos_sin = cosAlpha * sinAlpha;

        ref_yXmin = ref.getY() + nb2start * deltaHy;
        ref_yXmax = ref.getY() + nb2end * deltaHy;

        double hxmin;
        double hxmax;
        if (vertical) {
            hxmin = nb2start * deltaDx + ref.getX();
            hxmax = nb2end * deltaDx + ref.getX();
        } else {
            hxmin = nb2start * deltaHx + ref.getX();
            hxmax = nb2end * deltaHx + ref.getX();
        }

        double hymin;
        double hymax;
        double nb2drawDeltaY = nb2draw * deltaHy;

        // Compute hatches sub-set to draw (avoid all pattern which not stands within the clip area...)

        if (vertical) {
            if (deltaHy < 0.0) {
                hymin = Math.ceil((fbox.getMinY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
                hymax = Math.floor((fbox.getMaxY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
            } else {
                hymin = Math.floor((fbox.getMinY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
                hymax = Math.ceil((fbox.getMaxY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
            }
        } else {
            if (cos_sin < 0) {
                hymin = Math.floor((fbox.getMinY() - ref_yXmin) / (deltaDy)) * deltaDy + ref_yXmin;
                hymax = Math.ceil((fbox.getMaxY() - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
            } else {
                hymin = Math.floor((fbox.getMinY() - nb2drawDeltaY - ref_yXmin) / (deltaDy)) * deltaDy + ref_yXmin;

                if (deltaDy < 0) {
                    hymax = Math.floor((fbox.getMaxY() + nb2drawDeltaY - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
                } else {
                    hymax = Math.ceil((fbox.getMaxY() + nb2drawDeltaY - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
                }
            }
        }

        double y;
        double x;

        Line2D.Double l = new Line2D.Double();


        // Inform graphic2g to only draw hatches within the shape !
        g2.clip(shp);

        if (vertical) {

            if (hxmin < hxmax) {
                if (deltaDx < 0) {
                    deltaDx *= -1;
                }
                properties.put("offset", 0.0);
                for (x = hxmin; x < hxmax + deltaDx / 2.0; x += deltaDx) {
                    if (sinAlpha > 0) {
                        l.x1 = x;
                        l.y1 = hymin;
                        l.x2 = x;
                        l.y2 = hymax;
                    } else {
                        l.x1 = x;
                        l.y1 = hymax;
                        l.x2 = x;
                        l.y2 = hymin;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke, properties);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            } else {

                 properties.put("offset", 0.0);
                // Seems to been unreachable !
                for (x = hxmin; x > hxmax - deltaDx / 2.0; x += deltaDx) {
                    l.x1 = x;
                    l.y1 = hymin;
                    l.x2 = x;
                    l.y2 = hymax;

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw( g2, mt, penStroke, properties);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            }

        } else {
            if (hymin < hymax) {
                if (deltaDy < 0.0) {
                    deltaDy *= -1;
                }
                properties.put("offset", 0.0);
                for (y = hymin; y < hymax + deltaDy / 2.0; y += deltaDy) {

                    if (cosAlpha > 0) {
                        // Line goes from the left to the right
                        l.x1 = hxmin;
                        l.y1 = y;
                        l.x2 = hxmax;
                        l.y2 = y + nb2draw * deltaHy;
                    } else {
                        // Line goes from the right to the left
                        l.x1 = hxmax;
                        l.y1 = y + nb2draw * deltaHy;
                        l.x2 = hxmin;
                        l.y2 = y;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw( g2, mt, penStroke, properties);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            } else {

                if (deltaDy > 0.0) {
                    deltaDy *= -1;
                }

                properties.put("offset", 0.0);
                for (y = hymin; y > hymax - deltaDy / 2.0; y += deltaDy) {


                    if (cosAlpha > 0) {
                        // Line goes from the left to the right
                        l.x1 = hxmin;
                        l.y1 = y;
                        l.x2 = hxmax;
                        l.y2 = y + nb2draw * deltaHy;
                    } else {
                        // Line goes from the right to the left
                        l.x1 = hxmax;
                        l.y1 = y + nb2draw * deltaHy;
                        l.x2 = hxmin;
                        l.y2 = y;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke, properties);

                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);

                }
            }
        }
        g2.setClip(null);
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
