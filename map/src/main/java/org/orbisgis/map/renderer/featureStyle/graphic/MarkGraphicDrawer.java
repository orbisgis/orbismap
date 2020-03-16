/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.transform.TransformBuilder;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.graphic.WellKnownName;
import static org.orbisgis.style.graphic.WellKnownName.DEFAULT_SIZE;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class MarkGraphicDrawer implements IGraphicDrawer<MarkGraphic> {

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(Halo.class, new HaloDrawer());
        drawerMap.put(SolidFill.class, new SolidFillDrawer());
        drawerMap.put(PenStroke.class, new PenStrokeDrawer());
    }
    // cached shape : only available with shape that doesn't depends on features
    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, MarkGraphic styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shp = null;
        if (shape == null) {
            try {
                shp = getShape(styleNode, mapTransform, properties);
            } catch (Exception ex) {
                Logger.getLogger(MarkGraphicDrawer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            shp = shape;

        }
//        if (shp == null) {
//            ExpressionParameter wknEwpression = styleNode.getWkn();
//            WellKnownName wknValue = WellKnownName.CIRCLE;
//            if (wknEwpression != null) {
//                Object value = sp.getObject(wknEwpression.getIdentifier());
//                if (value instanceof Geometry) {
//                    Geometry geom = (Geometry) value;                    
//                    shp = getShape(mapTransform.getShape(geom, false), styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
//                            mapTransform.getDpi());
//                } else if (value instanceof String) {
//                    WellKnownName wknSupported = WellKnownName.fromString((String) value);
//                    if (wknSupported == null) {
//                        shp = WellKnownNameUtils.getShape(wknSupported, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
//                                mapTransform.getDpi());
//                    }
//                }
//
//            } else {
//                shp = WellKnownNameUtils.getShape(wknValue, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
//                        mapTransform.getDpi());
//            }
//     }
        if (shp != null) {
            AffineTransform at = new AffineTransform((AffineTransform) properties.get("affinetransform"));
            if (styleNode.getTransform() != null) {
                //TODO : Put in cache...
                TransformBuilder transformBuilder = new TransformBuilder();
                properties.put("shape", shp);
                at.concatenate(transformBuilder.getAffineTransform(styleNode.getTransform(), properties));
            }
            Shape atShp = at.createTransformedShape(shp);

            //We give the raw shape to the drawHalo method in order not to lose the
            //type of the original Shape - It will be easier to compute the halo.
            //We give the transformed shape too... This way we are sure we won't
            //compute it twice, as it is a complicated operation.
            Halo halo = styleNode.getHalo();
            if (halo != null) {
                if (drawerMap.containsKey(halo.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(halo.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, halo, properties);
                }
            }
            IFill fill = styleNode.getFill();
            if (fill != null) {
                if (drawerMap.containsKey(fill.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(fill.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, fill, properties);
                }
            }

            Stroke stroke = styleNode.getStroke();
            if (stroke != null) {
                double offset = 0.0;
                //TODO : RealParameter perpendicularOffset = styleNode.getPerpendicularOffset();
                /*(perpendicularOffset != null) {
                    offset = Uom.toPixel(perpendicularOffset.getValue(rs, fid),
                            uom, mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);
                }*/
                properties.put("offset", offset);
                if (drawerMap.containsKey(stroke.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(stroke.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, stroke, properties);
                }
                properties.remove("offset");

            }
        }

    }

    /**
     * TODO : implements
     *
     * @param map
     * @param mt
     * @return
     * @throws ParameterException
     */
    private Shape getShape(MarkGraphic markGraphic, MapTransform mapTransform, Map<String, Object> properties) throws ParameterException, Exception {

        String wkn = ValueHelper.getAsString(properties, markGraphic.getWkn());
        if (wkn != null && !wkn.isEmpty()) {
            return getShape(WellKnownName.fromString(wkn), markGraphic.getViewBox(), properties, mapTransform.getScaleDenominator(),
                    mapTransform.getDpi(), markGraphic.getUom());
        } else {
            throw new IllegalArgumentException("");
        }

        /*if (source != null) {
            //TODO implemennt
            //return source.getShape(viewBox, map, scaleDenom, dpi, markIndex, mimeType);
            return null;
        } else {
            return null;
        }*/
    }

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, MarkGraphic styleNode, Map<String, Object> properties) throws ParameterException {
        Shape shp = null;
        // If the shape doesn't depends on feature (i.e. not null), we used the cached one
        if (shape == null) {
            try {
                shp = getShape(styleNode, mapTransform, properties);
            } catch (Exception ex) {
                Logger.getLogger(MarkGraphicDrawer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            shp = shape;
        }

        if (shp == null) {
            getShape(WellKnownName.CIRCLE, styleNode.getViewBox(), properties, mapTransform.getScaleDenominator(),
                    mapTransform.getDpi(), styleNode.getUom());
        }

        /*if (transform != null) {
            return this.transform.getGraphicalAffineTransform(false, map, mt, shp.getBounds().getWidth(),
                    shp.getBounds().getHeight()).createTransformedShape(shp).getBounds2D();
        } else {*/
        return shp.getBounds2D();/*
        }*/

    }

    public static Shape getShape(Shape shape, ViewBox viewBox, Map<String, Object> map,
            Double scale, Double dpi) throws ParameterException, SQLException {
        double x = 1, y = 1; // The size of the shape, [final unit] => [px]
        if (viewBox != null) {
            Float height = ValueHelper.getAsFloat(map, viewBox.getHeight());
            Float width = ValueHelper.getAsFloat(map, viewBox.getWidth());
            if (height != null || width != null) {
                Point2D box = getDimensionInPixel(((UomNode) viewBox.getParent()).getUom(), height, width, scale, dpi);
                x = box.getX();
                y = box.getY();
            }
        }
        return AffineTransform.getScaleInstance(x, y).createTransformedShape(shape);

    }

    /**
     * Return the final dimension described by this view box, in [px].
     *
     * @param uom
     * @param height
     * @param scale required final ratio (if either width or height isn't
     * defined)
     * @param width
     * @param dpi
     * @return
     * @throws ParameterException
     */
    public static Point2D getDimensionInPixel(Uom uom, double height,
            double width, Double scale, Double dpi) throws ParameterException {
        float dx, dy;

        double ratio = height / width;

        if (width < 0 && height < 0) {
            dx = MarkGraphic.DEFAULT_SIZE;
            dy = MarkGraphic.DEFAULT_SIZE;
        } else if (width > 0 && height < 0) {
            dx = (float) width;
            dy = (float) (dx * ratio);
        } else if (height > 0 && width < 0) {
            dy = (float) height;
            dx = (float) (dy / ratio);
        } else { // nothing is defined
            dx = (float) width;
            dy = (float) height;
            //return null; 
        }

        dx = UomUtils.toPixel(dx, uom, dpi, scale, (float) width);
        dy = UomUtils.toPixel(dy, uom, dpi, scale, (float) height);

        if (Math.abs(dx) <= 0.00021 || Math.abs(dy) <= 0.00021) {
            dx = 0;
            dy = 0;
        }

        return new Point2D.Double(dx, dy);
    }

    public static Shape getShape(WellKnownName wellKnownName, ViewBox viewBox, Map<String, Object> map,
            Double scale, Double dpi, Uom uom) throws ParameterException {
        double x = DEFAULT_SIZE, y = DEFAULT_SIZE; // The size of the shape, [final unit] => [px]

        if (viewBox != null) {
            Float height = ValueHelper.getAsFloat(map, viewBox.getHeight());
            Float width = ValueHelper.getAsFloat(map, viewBox.getWidth());
            if (height != null || width != null) {
                Point2D box = getDimensionInPixel(uom, height, width, scale, dpi);
                x = box.getX();
                y = box.getY();
            }
        }

        int x2 = (int) (x / 2.0);
        int y2 = (int) (y / 2.0);
        int minxy6 = (int) Math.min(x / 6, y / 6);

        switch (wellKnownName) {
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

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
