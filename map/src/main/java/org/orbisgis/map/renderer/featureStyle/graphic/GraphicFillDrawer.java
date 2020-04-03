/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class GraphicFillDrawer implements IGraphicDrawer<GraphicFill> {

    final static Map<Class, IGraphicCollectionDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(GraphicCollection.class, new GraphicCollectionDrawer());
    }
    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, GraphicFill styleNode) throws ParameterException {
        Paint stipple = getPaint(styleNode, mapTransform);
        if (stipple != null) {
            g2.setPaint(stipple);
            g2.fill(shape);
        }
    }

    public Paint getPaint(GraphicFill styleNode, MapTransform mt) throws ParameterException {
        float gX = 0.0f;
        float gY = 0.0f;
        Uom uom = styleNode.getUom();
        Float gapX = (Float) styleNode.getGapX().getValue();
        if (gapX != null) {
            gX = gapX;
            if (gX < 0.0) {
                gX = 0.0f;
            }
        }
        Float gapY = (Float) styleNode.getGapY().getValue();
        if (gapY != null) {
            gY = gapY;
            if (gY < 0.0) {
                gY = 0.0f;
            }
        }
        GraphicCollection graphic = styleNode.getGraphics();
        if (graphic != null) {
            if (drawerMap.containsKey(graphic.getClass())) {
                IGraphicCollectionDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                Rectangle2D bounds = graphicDrawer.getBounds(mt, graphic);
                gX = UomUtils.toPixel(gX, uom, mt.getDpi(), mt.getScaleDenominator(), (float) bounds.getWidth());
                gY = UomUtils.toPixel(gY, uom, mt.getDpi(), mt.getScaleDenominator(), (float) bounds.getHeight());
                return getPaint(graphicDrawer, mt, graphic, gX, gY, bounds);
            }
        }

        return null;
    }

    public static Paint getPaint(IGraphicCollectionDrawer graphicDrawer,
            MapTransform mt, GraphicCollection graphics, double gX, double gY, Rectangle2D bounds)
            throws ParameterException {

        if (bounds != null) {

            Point2D.Double geoRef = new Point2D.Double(0, 0);
            Point2D ref = mt.getAffineTransform().transform(geoRef, null);

            int tWidth = (int) (bounds.getWidth() + gX);
            int tHeight = (int) (bounds.getHeight() + gY);

            int deltaX = (int) (ref.getX() - Math.ceil(ref.getX() / tWidth) * tWidth);
            int deltaY = (int) (ref.getY() - Math.ceil(ref.getY() / tHeight) * tHeight);

            if (tWidth > 0 && tHeight > 0) {

                BufferedImage i = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tile = i.createGraphics();
                tile.setRenderingHints(mt.getRenderingHints());
                int ix;
                int iy;
                for (ix = 0; ix < 2; ix++) {
                    for (iy = 0; iy < 2; iy++) {
                        graphicDrawer.setAffineTransform(AffineTransform.getTranslateInstance(
                                -bounds.getMinX() + gX / 2.0 + deltaX + tWidth * ix,
                                -bounds.getMinY() + gY / 2.0 + deltaY + tHeight * iy));
                        graphicDrawer.draw(tile, mt, graphics);
                    }
                }
                tile.dispose();
                return new TexturePaint(i, new Rectangle2D.Double(0, 0, i.getWidth(), i.getHeight()));
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, GraphicFill styleNode) throws ParameterException {
        GraphicCollection graphic = styleNode.getGraphics();
        if (graphic != null) {
            if (drawerMap.containsKey(graphic.getClass())) {
                IGraphicCollectionDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                return graphicDrawer.getBounds(mapTransform, styleNode);
            }
        }
        return null;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

     @Override
    public AffineTransform getAffineTransform() {
        return  affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform=affineTransform;
    }

}
