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
package org.orbisgis.orbismap.map.renderer.featureStyle.label;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.SolidFillDrawer;
import static org.orbisgis.orbismap.map.renderer.featureStyle.label.StyleTextUtil.getOutline;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.Halo;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.label.ExclusionRadius;
import org.orbisgis.orbismap.style.label.ExclusionRectangle;
import org.orbisgis.orbismap.style.label.ExclusionZone;
import org.orbisgis.orbismap.style.label.Label;
import org.orbisgis.orbismap.style.label.PointLabel;
import org.orbisgis.orbismap.style.label.StyleFont;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;
import org.orbisgis.orbismap.style.utils.UomUtils;

/**
 * Drawer for the element <code>PointLabel</code>
 * 
 * TODO : improve this part
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class PointLabelDrawer extends AbstractDrawerFinder<IStyleDrawer, IStyleNode> implements ILabelDrawer<PointLabel> {

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointLabel styleNode) throws ParameterException {
        if (shape != null) {
            double x;
            double y;
            Float deltaX = 0f;
            Float deltaY = 0f;
            Uom uom = styleNode.getUom();
            String styleText = (String) styleNode.getLabelText().getValue();
            if (styleText != null && !styleText.isEmpty()) {
                Rectangle2D bounds = StyleTextUtil.getBounds(g2, styleText, mapTransform, (StyleFont) styleNode.getFont());

                x = shape.getBounds2D().getCenterX() + getHorizontalDisplacement(bounds, styleNode.getHorizontalAlign());
                y = shape.getBounds2D().getCenterY() + bounds.getHeight() / 2;

                ExclusionZone exclusionZone = styleNode.getExclusionZone();
                if (exclusionZone != null) {
                    if (exclusionZone instanceof ExclusionRadius) {
                        Float radius = (Float) ((ExclusionRadius) (exclusionZone)).getRadius().getValue();
                        if (radius == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }
                        radius = UomUtils.toPixel(radius, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                        deltaX = radius;
                        deltaY = radius;
                    } else {
                        deltaX = (Float) ((ExclusionRectangle) (exclusionZone)).getX().getValue();
                        if (deltaX == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }
                        deltaY = (Float) ((ExclusionRectangle) (exclusionZone)).getY().getValue();

                        if (deltaY == null) {
                            throw new ParameterException("The radius parameter for the exclusion zone cannot be null");
                        }

                        deltaX = UomUtils.toPixel(deltaX, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                        deltaY = UomUtils.toPixel(deltaY, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                    }
                }

                AffineTransform at = AffineTransform.getTranslateInstance(x + deltaX, y + deltaY);

                Label.VerticalAlignment va  = styleNode.getVerticalAlign();

                if (va  == null) {
                    va  = Label.VerticalAlignment.TOP;
                }

                ArrayList<Shape> outlines = new ArrayList<Shape>();
                outlines.add(getOutline(g2, styleText, mapTransform, at, va, styleNode));
                drawOutlines(g2, outlines, mapTransform, styleNode);
            }
        }
    }

    /**
     * Gets the horizontal displacement to the given bounds according to the
     * currently configured HorizontalAlignment.
     *
     * @param bounds The bounds of the text to be drawn
     * @return The displacement.
     */
    private double getHorizontalDisplacement(Rectangle2D bounds, Label.HorizontalAlignment ha) {
        switch (ha) {
            case CENTER:
                return -bounds.getWidth() / 2.0;
            case LEFT:
                return -bounds.getWidth();
            default:
                return 0.0;
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

    /**
     * Draw the list of given "outlines", that is the list of characters already
     * transformed to {@code Shape} instances.We'll use for that, of course, the
     * inner {@code Fill}, {@code Halo} and {@code Stroke} instances.If they are
     * not set, a simple default {@code SolidFill} will be used.
     *
     * @param g2 The graphics we draw with
     *
     * @param outlines The list of needed outlines
     * @param mapTransform
     * @param styleNode
     * @throws ParameterException
     */
    public void drawOutlines(Graphics2D g2, ArrayList<Shape> outlines,
            MapTransform mapTransform, PointLabel styleNode) throws ParameterException {
        Halo halo = styleNode.getHalo();
        IStyleDrawer haloDrawer = getDrawer(halo);
        if (haloDrawer != null) {
            for (Shape outline : outlines) {
                haloDrawer.setShape(outline);
                haloDrawer.draw(g2, mapTransform, halo);
            }
        }

        IFill fill = styleNode.getFill();
        Stroke stroke = styleNode.getStroke();
        IStyleDrawer strokeDrawer = getDrawer(stroke);
        IStyleDrawer fillDrawer = getDrawer(fill);

        for (Shape outline : outlines) {
            /**
             * No fill and no stroke : apply default SolidFill !
             */
            if (fillDrawer == null && strokeDrawer == null) {
                SolidFill sf = new SolidFill();
                sf.setColor(Color.BLACK);
                sf.setOpacity(1f);
                sf.setParent(styleNode);
                SolidFillDrawer drawer = new SolidFillDrawer();
                drawer.setShape(outline);
                drawer.draw(g2, mapTransform, sf);
            }
            if (fillDrawer != null) {
                fillDrawer.setShape(outline);
                fillDrawer.draw(g2, mapTransform, fill);
            }
            if (strokeDrawer != null) {
                strokeDrawer.setShape(outline);
                strokeDrawer.draw(g2, mapTransform, stroke);
            }
        }

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
