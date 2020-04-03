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
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.graphic.GraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.shape.PointsShape;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class PointSymbolizerDrawer implements ISymbolizerDraw<PointSymbolizer> {

    final static Map<Class, IGraphicCollectionDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(GraphicCollection.class, new GraphicCollectionDrawer());
    }
    private Shape shape;

    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, PointSymbolizer symbolizer) throws ParameterException {
            GraphicCollection graphic = symbolizer.getGraphics();
            if (graphic != null) {
                if (drawerMap.containsKey(graphic.getClass())) {
                    IGraphicCollectionDrawer graphicDrawer = drawerMap.get(graphic.getClass());
                    if (shape instanceof PointsShape) {
                        PointsShape shapes = (PointsShape) getShape();
                        for (int i = 0; i < shapes.size(); i++) {
                            Rectangle2D b = shapes.get(i).getBounds2D();
                            graphicDrawer.setAffineTransform( AffineTransform.getTranslateInstance(b.getX(), b.getY()));                        
                            graphicDrawer.draw(g2, mapTransform, graphic);
                        }
                    } else {
                        Rectangle2D b = getShape().getBounds2D();
                        graphicDrawer.setAffineTransform(AffineTransform.getTranslateInstance(b.getX(), b.getY()));
                        graphicDrawer.draw(g2, mapTransform, graphic);
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
        if (g2 != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, null, null);
            bi = null;
        }
    }

}
