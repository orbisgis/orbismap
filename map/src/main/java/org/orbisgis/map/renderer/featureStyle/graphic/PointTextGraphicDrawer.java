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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.label.PointLabelDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.PointTextGraphic;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 * Drawer for the element <code>PointTextGraphic</code>
 * @author Erwan Bocher, CNRS (2020)
 */
public class PointTextGraphicDrawer implements IGraphicDrawer<PointTextGraphic> {

    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Rectangle2D getShape(MapTransform mapTransform, PointTextGraphic styleNode) throws ParameterException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointTextGraphic styleNode) throws ParameterException {
       if (affineTransform != null) {
            PointLabel pointLabel = styleNode.getPointLabel();
            if (pointLabel != null) {
                AffineTransform at = new AffineTransform(affineTransform);
                double px = 0;
                double py = 0;
                Uom uom = styleNode.getUom();
                Float x = (Float) styleNode.getX().getValue();
                if (x != null) {
                    px = UomUtils.toPixel(x, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }
                Float y = (Float) styleNode.getY().getValue();
                if (y != null) {
                    py = UomUtils.toPixel(y, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }

                Rectangle2D.Double bounds = new Rectangle2D.Double(px - 5, py - 5, 10, 10);
                Shape atShp = at.createTransformedShape(bounds);
                PointLabelDrawer drawer = new PointLabelDrawer();
                drawer.setShape(atShp);
                drawer.draw(g2, mapTransform, pointLabel);
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
}
