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
import java.util.HashMap;
import java.util.Map;

import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.HaloDrawer;
import org.orbisgis.map.renderer.featureStyle.fill.SolidFillDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;

/**
 *
 * @author Erwan Bocher
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
    private AffineTransform affineTransform;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        Shape shp = null;
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;

        }

        if (shp != null) {
            AffineTransform at = new AffineTransform(getAffineTransform());
            if (styleNode.getTransform() != null) {
                //TODO : Put in cache...
                //TransformBuilder transformBuilder = new TransformBuilder();
                //at.concatenate(transformBuilder.getAffineTransform(styleNode.getTransform(), properties));
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
                    drawer.draw(g2, mapTransform, halo);
                }
            }
            IFill fill = styleNode.getFill();
            if (fill != null) {
                if (drawerMap.containsKey(fill.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(fill.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, fill);
                }
            }
            Stroke stroke = styleNode.getStroke();
            if (stroke != null) {
                if (drawerMap.containsKey(stroke.getClass())) {
                    IStyleDrawer drawer = drawerMap.get(stroke.getClass());
                    drawer.setShape(atShp);
                    drawer.draw(g2, mapTransform, stroke);
                }
            }
        }

    }

    /**
     * TODO : implements
     *
     * @param markGraphic
     * @param mapTransform
     * @return
     * @throws ParameterException
     */
    public Shape getShape(MarkGraphic markGraphic, MapTransform mapTransform) throws ParameterException {
       return ShapeFinder.getShape((String) markGraphic.getWellKnownName().getValue(), markGraphic.getGraphicSize(),  mapTransform.getScaleDenominator(),  mapTransform.getDpi(), markGraphic.getUom());           
   }

    @Override
    public Rectangle2D getBounds(MapTransform mapTransform, MarkGraphic styleNode) throws ParameterException {
        Shape shp = null;
        // If the shape doesn't depends on feature (i.e. not null), we used the cached one
        if (shape == null) {
            shp = getShape(styleNode, mapTransform);
        } else {
            shp = shape;
        }

        /*if (transform != null) {
            return this.transform.getGraphicalAffineTransform(false, map, mt, shp.getBounds().getWidth(),
                    shp.getBounds().getHeight()).createTransformedShape(shp).getBounds2D();
        } else {*/
        return shp.getBounds2D();/*
        }*/

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
