/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.graphic.shapeFactory;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.graphic.graphicSize.GraphicSize;
import org.orbisgis.orbismap.style.graphic.graphicSize.Size;
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox;
import org.orbisgis.orbismap.style.graphic.WellKnownName;

/**
 * WellKnownName factory
 * @author Erwan Bocher, CNRS (2020)
 */
public class WKNFactory extends AbstractShapeFactory {

    public static final String FACTORY_PREFIX = "wellknownname";

    public HashMap<GraphicSize, Shape> shapes;

    public WKNFactory() {
        super(FACTORY_PREFIX);
        shapes = new HashMap<>();

    }

    @Override
    public Shape getShape(GraphicSize graphicSize, Double scale, Double dpi, Uom uom) throws ParameterException {
        WellKnownName wellKnownName = WellKnownName.fromString(getShapeName());
        if (graphicSize != null) {
            if (graphicSize instanceof ViewBox) {
                ViewBox viewBox = (ViewBox) graphicSize;
                ParameterValue heigthParameter = viewBox.getHeight();
                ParameterValue widthParameterValue = viewBox.getWidth();
                if (!heigthParameter.isLiteral() || !widthParameterValue.isLiteral()) {
                    return buildShape(wellKnownName, viewBox, scale, dpi, uom);
                } else {
                    Shape shape = shapes.get(viewBox);
                    if (shape == null) {
                        shape = buildShape(wellKnownName, viewBox, scale, dpi, uom);
                        shapes.put(viewBox, shape);
                        return shape;
                    }else{
                        return shape;
                    }
                }

            } else if (graphicSize instanceof Size) {
                Size size = (Size) graphicSize;
                if (!size.getSize().isLiteral()) {
                    return buildShape(wellKnownName, size, scale, dpi, uom);
                } else {
                    Shape shape = shapes.get(size);
                    if (shape == null) {
                        shape = buildShape(wellKnownName, size, scale, dpi, uom);
                        shapes.put(size, shape);
                         return shape;
                    }else{
                        return shape;
                    }
                }
            }
        }
        return null;

    }

    /**
     * Build the corresponding shape from the viewbox
     *
     * @param wellKnownName
     * @param x
     * @param y
     * @return
     */
    private Shape buildShape(WellKnownName wellKnownName, Size size, Double scale, Double dpi, Uom uom) throws ParameterException  {
        Float sizeValue = (Float) size.getSize().getValue();
        if (sizeValue != null) {
            Point2D box = getDimensionInPixel(uom, sizeValue, sizeValue, scale, dpi);
            return  buildShape(wellKnownName, box.getX(), box.getY());
        }
        return null;
    }

    /**
     * Build the corresponding shape from the viewbox
     *
     * @param wellKnownName
     * @param x
     * @param y
     * @return
     */
    private Shape buildShape(WellKnownName wellKnownName, ViewBox viewBox, Double scale, Double dpi, Uom uom) throws ParameterException {
        Float height = (Float) viewBox.getHeight().getValue();
        Float width = (Float) viewBox.getWidth().getValue();
        if (viewBox.usable()) {
            Point2D box = getDimensionInPixel(uom, height, width, scale, dpi);
            return buildShape(wellKnownName, box.getX(), box.getY());
        }
        return null;
    }

    /**
     * Build the corresponding shape
     *
     * @param wellKnownName
     * @param x
     * @param y
     * @return
     */
    private Shape buildShape(WellKnownName wellKnownName, double x, double y) {
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
