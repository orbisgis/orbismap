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
package org.orbisgis.map.renderer.featureStyle.fill;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ShapeHelper;
import org.orbisgis.style.IFill;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 * Drawer for the element <code>Halo</code>
 * 
 * @author Erwan Bocher, CNRS (2020)
 */
public class HaloDrawer extends AbstractDrawerFinder<IFillDrawer, IFill>  implements IFillDrawer<Halo> {

    
    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Paint getPaint(Halo styleNode, MapTransform mt) throws ParameterException {
        return null;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, Halo styleNode) throws ParameterException {
        if (shape != null) {
            IFill fill = styleNode.getFill();
            IFillDrawer drawer = getDrawer(fill);
            if (styleNode.getRadius() != null && drawer != null) {
                    //Optimisation
                    Float radius = (Float) styleNode.getRadius().getValue();
                    if (radius == 0 && radius <= 0) {
                        throw new ParameterException("The radius parameter of the halo cannot be null and greater that 0");
                    }
                    if (shape instanceof Arc2D) {
                        Arc2D shp = (Arc2D) shape;
                        float r = getHaloRadius(radius, styleNode.getUom(), mapTransform);
                        double x = shp.getX() - r / 2;
                        double y = shp.getY() - r / 2;
                        double height = shp.getHeight() + r;
                        double width = shp.getWidth() + r;
                        Shape origin = new Arc2D.Double(x, y, width, height, shp.getAngleStart(), shp.getAngleExtent(), shp.getArcType());
                        Shape halo = getAffineTransform().createTransformedShape(origin);
                        fillHalo(drawer, fill, shape, halo, g2, mapTransform);
                    } else {
                        double r = getHaloRadius(radius, styleNode.getUom(), mapTransform);
                        if (r > 0.0) {
                            for (Shape shapeHalo : ShapeHelper.perpendicularOffset(shape, r)) {
                                fillHalo(drawer, fill, shapeHalo, shape, g2, mapTransform);
                            }
                        }
                    }
                
            }
        }
    }

    /**
     * Return the halo radius in pixel
     *
     * @param mt
     * @return
     * @throws ParameterException
     */
    public float getHaloRadius(float radius, Uom uom, MapTransform mt) throws ParameterException {
        return UomUtils.toPixel(radius, uom, mt.getDpi(), mt.getScaleDenominator()); // TODO 100%
    }

    private void fillHalo(IFillDrawer fillDrawer, IFill fill, Shape halo, Shape initialShp, Graphics2D g2,
            MapTransform mapTransform)
            throws ParameterException {
        if (halo != null && initialShp != null) {
            Area initialArea = new Area(initialShp);
            Area aHalo = new Area(halo);
            aHalo.subtract(initialArea);
            fillDrawer.setShape(aHalo);
            fillDrawer.draw(g2, mapTransform, fill);
        } else {
            //LOGGER.error("Perpendicular offset failed");
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
    public IFillDrawer getDrawer(IFill styleNode) {
        if (styleNode != null) {
            IFillDrawer drawer = drawerMap.get(styleNode);
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
                } else if (styleNode instanceof DensityFill) {
                    drawer = new DensityFillDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof DotMapFill) {
                    drawer = new DotMapFillDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }
}
