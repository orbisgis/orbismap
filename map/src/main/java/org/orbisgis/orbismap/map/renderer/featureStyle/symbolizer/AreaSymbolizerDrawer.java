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
package org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer;

import org.orbisgis.orbismap.map.renderer.featureStyle.fill.DensityFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.SolidFillDrawer;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.DotMapFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.fill.HatchedFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.graphic.GraphicFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.fill.DotMapFill;
import org.orbisgis.orbismap.style.fill.GraphicFill;
import org.orbisgis.orbismap.style.fill.HatchedFill;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;

/**
 * Drawer for the element <code>AreaSymbolizer</code>
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class AreaSymbolizerDrawer extends AbstractDrawerFinder<IStyleDrawer, IStyleNode> implements ISymbolizerDraw<AreaSymbolizer> {

    private Shape shape;
    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, AreaSymbolizer symbolizer) throws ParameterException {
        if (symbolizer.getTransform() != null) {
            shape = symbolizer.getTransform().getAffineTransform(mapTransform.getDpi(), mapTransform.getScaleDenominator(),
                    (float) mapTransform.getWidth(), (float) mapTransform.getHeight()).createTransformedShape(getShape());
        }

        IFill fill = symbolizer.getFill();
        IStyleDrawer fillDrawer = getDrawer(fill);
        if (fillDrawer != null) {
            fillDrawer.setShape(shape);
            fillDrawer.draw(g2, mapTransform, fill);
        }
        Stroke stroke = symbolizer.getStroke();
        IStyleDrawer strokeDrawer = getDrawer(stroke);
        if (strokeDrawer != null) {
            strokeDrawer.setShape(shape);
            strokeDrawer.draw(g2, mapTransform, stroke);
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
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bi = bufferedImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bi;
    }

    @Override
    public void setGraphics2D(Graphics2D g2) {
        this.g2_bi = g2;
    }

    @Override
    public Graphics2D getGraphics2D() {
        return g2_bi;
    }

    @Override
    public void dispose(Graphics2D g2) {
        //The g2 can be null when the Graphics2D has been already disposed.
        if (g2_bi != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, 0, 0, null);
            bi.flush();
            bi=null;
        }
    }

    @Override
    public IStyleDrawer getDrawer(IStyleNode styleNode) {
        if (styleNode != null) {
            IStyleDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof SolidFill) {
                    drawer = new SolidFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof PenStroke) {
                    drawer = new PenStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof HatchedFill) {
                    drawer = new HatchedFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof DotMapFill) {
                    drawer = new DotMapFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof SolidFill) {
                    drawer = new DensityFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof GraphicFill) {
                    drawer = new GraphicFillDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }

}
