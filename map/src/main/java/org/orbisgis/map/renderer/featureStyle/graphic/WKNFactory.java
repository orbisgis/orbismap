/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.graphic.WellKnownName;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class WKNFactory extends AbstractShapeFactory {

    public static final String FACTORY_PREFIX = "wellknownname";

    public WKNFactory() {
        super(FACTORY_PREFIX);

    }

    @Override
    public Shape getShape(ViewBox viewBox, Double scale, Double dpi, Uom uom) throws ParameterException{
        double x = DEFAULT_SIZE, y = DEFAULT_SIZE; // The size of the shape, [final unit] => [px]

        WellKnownName wellKnownName = WellKnownName.fromString(getShapeName());
        if (viewBox != null) {
            Float height = (Float) viewBox.getHeight().getValue();
            Float width = (Float) viewBox.getWidth().getValue();
            if (height != null || width != null) {
                Point2D box = getDimensionInPixel(uom,height, width, scale, dpi);
                x = box.getX();
                y = box.getY();
            }
        }

        int x2 = (int) (x / 2.0);
        int y2 = (int) (y / 2.0);
        int minxy6 = (int) Math.min(x / 6, y / 6);

        switch (wellKnownName) {
            case VERTLINE:
                return new Line2D.Double(0, -Math.abs(y2), 0, Math.abs(y2));
            case HALFCIRCLE:
                if (x2 >= 0) {
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

                return new GeneralPath(star);
            }
            case CROSS:

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

            case X:

                Polygon xShape = new Polygon();

                xShape.addPoint(0, -minxy6);

                xShape.addPoint(x2 - minxy6, -y2);
                xShape.addPoint(x2, -y2 + minxy6);

                xShape.addPoint(minxy6, 0);

                xShape.addPoint(x2, y2 - minxy6);
                xShape.addPoint(x2 - minxy6, y2);

                xShape.addPoint(0, minxy6);

                xShape.addPoint(-x2 + minxy6, y2);
                xShape.addPoint(-x2, y2 - minxy6);

                xShape.addPoint(-minxy6, 0);

                xShape.addPoint(-x2, -y2 + minxy6);
                xShape.addPoint(-x2 + minxy6, -y2);

                xShape.addPoint(0, -minxy6);

                return xShape;
            case SQUARE:
            default:
                return new Rectangle2D.Double(-Math.abs(x2), -Math.abs(y2), Math.abs(x), Math.abs(y));
        }
    }

}
