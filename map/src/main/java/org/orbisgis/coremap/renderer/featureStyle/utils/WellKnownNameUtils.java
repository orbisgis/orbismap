/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle.utils;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.graphic.WellKnownName;
import static org.orbisgis.style.graphic.WellKnownName.DEFAULT_SIZE;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.visitors.ParameterValueVisitor;

/**
 *
 * @author ebocher
 */
public class WellKnownNameUtils {
    
    
    public static Shape getShape(WellKnownName wellKnownName, ViewBox viewBox, Map<String,Object> map,
            Double scale, Double dpi, RealParameter markIndex,
            String mimeType) throws ParameterException {
        if (map == null && viewBox != null){
            ParameterValueVisitor fv = new ParameterValueVisitor();
            viewBox.acceptVisitor(fv);
            if(!fv.getResult().isEmpty()){
                return null;
            }
        }

        double x=DEFAULT_SIZE, y=DEFAULT_SIZE; // The size of the shape, [final unit] => [px]

        if (viewBox != null && viewBox.usable()) {
            Point2D box = viewBox.getDimensionInPixel(map, MarkGraphic.DEFAULT_SIZE, MarkGraphic.DEFAULT_SIZE, scale, dpi);
            x = box.getX();
            y = box.getY();
        }

        int x2 = (int)(x / 2.0);
        int y2 = (int)(y / 2.0);
		int minxy6 = (int)Math.min(x/6, y/6);

        switch (wellKnownName) {
            case HALFCIRCLE:
                if(x2>=0){
                    return new Arc2D.Double(-x2, -Math.abs(y2), Math.abs(x), Math.abs(y), -90, -180, Arc2D.CHORD);
                } else {
                    return new Arc2D.Double(x2, -Math.abs(y2), Math.abs(x), Math.abs(y), -90, 180, Arc2D.CHORD);
                }
            case CIRCLE:
                return new Ellipse2D.Double(-Math.abs(x2), -Math.abs(y2), Math.abs(x), Math.abs(y));
            case TRIANGLE: {
                int h3 = (int) (y / 3);
                Polygon polygon = new Polygon();
                polygon.addPoint((int) x2, h3);
                polygon.addPoint((int) -x2, h3);
                polygon.addPoint(0, -2 * h3);
                polygon.addPoint((int) x2, h3);
                return polygon;
            }
            case STAR: // 5 branches
            {
                double crx = x2 * (2.0 / 5.0);
                double cry = y2 * (2.0 / 5.0);

                Polygon star = new Polygon();

                final double cos1 = 0.587785252292472915058851867798;
                final double cos2 = 0.951056516295153531181938433292;
                final double sin1 = 0.809016994374947562285171898111;
                final double sin2 = 0.309016994374947617796323129369;

                star.addPoint(0, (int) (cry - 0.5));
                // alpha = 234
                star.addPoint((int) (-cos1 * x2 + 0.5), (int) (sin1 * y2 - 0.5));

                // alpha = 198
                star.addPoint((int) (-cos2 * crx + 0.5), (int) (sin2 * cry - 0.5));

                // alpha = 162
                star.addPoint((int) (-cos2 * x2 + 0.5), (int) (-sin2 * y2 - 0.5));

                // alpha = 126
                star.addPoint((int) (-cos1 * crx + 0.5), (int) (-sin1 * cry - 0.5));

                // alpha = 90
                star.addPoint(0, (int) (-y2 - 0.5));

                // alpha = 54
                star.addPoint((int) (cos1 * crx + 0.5), (int) (-sin1 * cry - 0.5));

                // alpha = 18
                star.addPoint((int) (cos2 * x2 + 0.5), (int) (-sin2 * y2 - 0.5));

                // alpha = 342
                star.addPoint((int) (cos2 * crx + 0.5), (int) (sin2 * cry - 0.5));

                // alpha = 306
                star.addPoint((int) (cos1 * x2 + 0.5), (int) (sin1 * y2 - 0.5));

                // alpha = 270
                star.addPoint(0, (int) (cry - 0.5));

                //return star;
                //return new Area(star);
                //return new Polygon()
                return new GeneralPath(star);
            }
            case CROSS:// TODO IMPLEMENT

                Polygon cross = new Polygon();

				cross.addPoint(-minxy6, -y2);
				cross.addPoint(minxy6, -y2);

				cross.addPoint(minxy6, -minxy6);

				cross.addPoint(x2, -minxy6);
				cross.addPoint(x2, minxy6);

				cross.addPoint(minxy6, minxy6);

				cross.addPoint(minxy6, y2);
				cross.addPoint(-minxy6, y2);

				cross.addPoint(-minxy6, minxy6);

				cross.addPoint(-x2, minxy6);
				cross.addPoint(-x2, -minxy6);

				cross.addPoint(-minxy6, -minxy6);

				cross.addPoint(-minxy6, -y2);

				return cross;

            case X: // TODO IMPLEMENT

                Polygon xShape = new Polygon();

				xShape.addPoint(0, -minxy6);

				xShape.addPoint(x2 - minxy6, - y2);
				xShape.addPoint(x2, - y2 +minxy6);

				xShape.addPoint(minxy6, 0);

				xShape.addPoint(x2, y2 - minxy6);
				xShape.addPoint(x2 - minxy6, y2);

				xShape.addPoint(0, minxy6);

				xShape.addPoint(- x2 + minxy6, y2);
				xShape.addPoint(- x2, y2 - minxy6);

				xShape.addPoint(-minxy6, 0);

				xShape.addPoint(- x2, - y2 +minxy6);
				xShape.addPoint(- x2 + minxy6, - y2);

				xShape.addPoint(0, -minxy6);

				return xShape;
            case SQUARE:
            default:
                return new Rectangle2D.Double(-Math.abs(x2), -Math.abs(y2), Math.abs(x), Math.abs(y));
        }
    }
}
