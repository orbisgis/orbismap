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
package org.orbisgis.orbismap.map.renderer.featureStyle.fill;

import org.orbisgis.orbismap.map.renderer.featureStyle.graphic.GraphicFillDrawer;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.graphic.GraphicCollectionDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.style.fill.DensityFill;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.utils.UomUtils;

/**
 * Drawer for the element <code>DensityFill</code>
 * 
 * @author Erwan Bocher, CNRS (2020)
 */
public class DensityFillDrawer extends AbstractDrawerFinder<IGraphicCollectionDrawer, GraphicCollection> implements IFillDrawer<DensityFill> {

    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Paint getPaint(DensityFill styleNode, MapTransform mt) throws ParameterException {
        Float percentage = (Float) styleNode.getPercentageCovered().getValue();
        if (percentage == null) {
            percentage = 0f;
        }
        percentage = percentage * styleNode.ONE_HUNDRED;
        if (percentage > styleNode.ONE_HUNDRED) {
            percentage = styleNode.ONE_HUNDRED;
        }

        if (percentage > styleNode.ONE_HALF) {
            Paint painter = null;
            GraphicCollection marks = styleNode.getGraphics();
            if (styleNode.isHatched() && styleNode.getHatches() != null) {
                return null;
            } else if (marks != null) {
                IGraphicCollectionDrawer markStyle = getDrawer(styleNode.getGraphics());
                Rectangle2D bounds = markStyle.getBounds(mt, marks);
                double ratio = Math.sqrt(styleNode.ONE_HUNDRED / percentage);
                double gapX = bounds.getWidth() * ratio - bounds.getWidth();
                double gapY = bounds.getHeight() * ratio - bounds.getHeight();
                painter = GraphicFillDrawer.getPaint(markStyle, mt, marks, gapX, gapY, bounds);
            } else {
                throw new ParameterException("Neither marks or hatches are defined");
            }
            return painter;
        }
        return null;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, DensityFill styleNode) throws ParameterException {
        if (shape != null) {
            if (styleNode.isHatched()) {
                Float alpha = (Float) styleNode.getHatchesOrientation().getValue();
                float pDist;
                if (alpha == null) {
                    throw new ParameterException("The orientation parameter cannot be null");
                }
                // Stroke width in pixel
                Float sWidth = (Float) styleNode.getHatches().getWidth().getValue();

                if (sWidth == null) {
                    throw new ParameterException("The hatches size parameter cannot be null");
                }

                float widthInPixel = UomUtils.toPixel(sWidth, styleNode.getUom(), mapTransform.getDpi(), mapTransform.getScaleDenominator());

                Float percentage = (Float) styleNode.getPercentageCovered().getValue();

                if (percentage == null) {
                    throw new ParameterException("The percentage covered parameter cannot be null");
                }

                float percentageNormalized = percentage * styleNode.ONE_HUNDRED;

                if (percentageNormalized > styleNode.ONE_HUNDRED) {
                    percentageNormalized = styleNode.ONE_HUNDRED;
                }
                // Perpendiculat dist bw two hatches
                pDist = styleNode.ONE_HUNDRED * widthInPixel / percentageNormalized;
                HatchedFillDrawer.drawHatch(g2, shape, mapTransform, alpha, pDist, styleNode.getHatches(), new PenStrokeDrawer(), 0.0);
            } else {

                Paint painter = getPaint(styleNode, mapTransform);

                if (painter != null) {
                    g2.setPaint(painter);
                    g2.fill(shape);
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

    @Override
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    @Override
    public IGraphicCollectionDrawer getDrawer(GraphicCollection styleNode) {
        if (styleNode != null) {
            IGraphicCollectionDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof GraphicCollection) {
                    drawer = new GraphicCollectionDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }
}
