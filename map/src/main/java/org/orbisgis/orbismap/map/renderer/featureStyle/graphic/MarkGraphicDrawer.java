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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.graphic;

import org.orbisgis.orbismap.style.graphic.shapeFactory.ShapeFinder;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.HatchedFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.fill.Halo;
import org.orbisgis.orbismap.style.fill.HatchedFill;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.graphic.AnchorPosition;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;

/**
 * Drawer for the element <code>MarkGraphic</code>
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class MarkGraphicDrawer extends AbstractDrawerFinder<IStyleDrawer, IStyleNode> implements IGraphicDrawer<MarkGraphic> {

    private AffineTransform affineTransform;
    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        Shape shp;
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;
        }

        if (shp != null) {
            AffineTransform at = new AffineTransform(getAffineTransform());
            if (styleNode.getTransform() != null) {
                Rectangle2D bounds = shp.getBounds2D();
                at.concatenate(styleNode.getTransform().getAffineTransform(mapTransform.getDpi(),
                        mapTransform.getScaleDenominator(), (float) bounds.getWidth(), (float) bounds.getHeight()));
            }

            AnchorPosition anchorPosition = styleNode.getAnchorPosition();
            if (anchorPosition != AnchorPosition.CENTER) {
                Rectangle bounds = shp.getBounds();
                switch (anchorPosition) {
                    case UPPER_LEFT:
                        at.concatenate(AffineTransform.getTranslateInstance(-bounds.getMinX(), -bounds.getMinY()));
                        break;
                    case UPPER_RIGHT:
                        at.concatenate(AffineTransform.getTranslateInstance(-bounds.getMaxX(), -bounds.getMinY()));
                        break;
                    case LOWER_LEFT:
                        at.concatenate(AffineTransform.getTranslateInstance(-bounds.getMinX(), -bounds.getMaxY()));
                        break;
                    case LOWER_RIGHT:
                        at.concatenate(AffineTransform.getTranslateInstance(-bounds.getMaxX(), -bounds.getMaxY()));
                        break;
                }
            }

            Shape atShp = at.createTransformedShape(shp);

            //We give the raw shape to the drawHalo method in order not to lose the
            //type of the original Shape - It will be easier to compute the halo.
            //We give the transformed shape too... This way we are sure we won't
            //compute it twice, as it is a complicated operation.
            Halo halo = styleNode.getHalo();
            if (halo != null) {
                IStyleDrawer drawer = getDrawer(halo);
                drawer.setShape(atShp);
                drawer.draw(g2, mapTransform, halo);
            }
            IStyleDrawer drawerFill = getDrawer(styleNode.getFill());
            if (drawerFill != null) {
                drawerFill.setShape(atShp);
                drawerFill.draw(g2, mapTransform, styleNode.getFill());
            }
            Stroke stroke = styleNode.getStroke();
            if (stroke != null) {
                IStyleDrawer drawer = getDrawer(stroke);
                drawer.setShape(atShp);
                drawer.draw(g2, mapTransform, stroke);
            }
        }

    }

    /**
     * Shape build from a MarkGraphic factory.
     *
     *
     * @param markGraphic
     * @param mapTransform
     * @return
     * @throws ParameterException
     */
    private Shape getShape(MarkGraphic markGraphic, MapTransform mapTransform) throws ParameterException {
        return ShapeFinder.getShape(markGraphic.getWellKnownName(), markGraphic.getGraphicSize(), mapTransform.getScaleDenominator(), mapTransform.getDpi(), markGraphic.getUom());
    }

    @Override
    public Shape getShape(MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        // If the shape doesn't depends on feature (i.e. not null), we used the cached one
        Shape shp;
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;
        }
        /*if (transform != null) {
            return this.transform.getGraphicalAffineTransform(false, map, mt, shp.getBounds().getWidth(),
                    shp.getBounds().getHeight()).createTransformedShape(shp).getBounds2D();
        } else {*/
        return shp;

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
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    @Override
    public IStyleDrawer getDrawer(IStyleNode styleNode) {
        if (styleNode != null) {
            IStyleDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof Halo) {
                    drawer = new HaloDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof SolidFill) {
                    drawer = new SolidFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof HatchedFill) {
                    drawer = new HatchedFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof PenStroke) {
                    drawer = new PenStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }
}
