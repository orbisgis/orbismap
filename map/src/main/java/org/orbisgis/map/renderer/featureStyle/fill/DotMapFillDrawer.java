/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.fill;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.IStyleDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;

/**
 *
 * @author ebocher
 */
public class DotMapFillDrawer implements IFillDrawer<DotMapFill> {

    private Random rand;
    static final int MAX_ATTEMPT = 100;

    final static Map<Class, IStyleDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }

    @Override
    public Paint getPaint(JdbcSpatialTable sp, DotMapFill styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        return null;
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, DotMapFill styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shp = (Shape) properties.get("shape");
        Graphic graphic = styleNode.getGraphic();
        if (drawerMap.containsKey(graphic.getClass())) {
            IStyleDrawer graphicDrawer = drawerMap.get(graphic.getClass());
            if(graphicDrawer!=null){
            Double perMark = null;
            if (styleNode.getQuantityPerMark() != null) {
                perMark = styleNode.getQuantityPerMark().getValue(properties);
            }

            Double total = null;
            if (styleNode.getTotalQuantity() != null) {
                total = styleNode.getTotalQuantity().getValue(properties);
            }

            if (perMark == null || total == null) {
                throw new ParameterException("Dot Map Fill: missing parameters !!!");
            }

            int nb = (int) Math.round(total / perMark);

            Area area = new Area(shp);

            if (rand == null) {
                rand = new Random();
            }
            // setting the seed to the scale denom will ensure that mark will not move when panning
            rand.setSeed((long) mapTransform.getScaleDenominator());
            for (int i = 0; i < nb; i++) {
                Point2D.Double pos = findMarkPosition(area);
                if (pos != null) {
                    properties.put("affinetransform",  AffineTransform.getTranslateInstance(pos.x, pos.y));
                    graphicDrawer.draw(sp, g2, mapTransform, graphic, properties);
                } else {
                    //TODO : log this message ("Could not find position for mark within area");
                }
            }
            }
        }
    }

    /**
     * Ugly version to find a random point which stand within the area
     *
     * @param area
     * @return
     */
    private Point2D.Double findMarkPosition(Area area) {
        Rectangle2D bounds2D = area.getBounds2D();

        for (int i = 0; i < MAX_ATTEMPT; i++) {
            double x = rand.nextDouble() * bounds2D.getWidth() + bounds2D.getMinX();
            double y = rand.nextDouble() * bounds2D.getHeight() + bounds2D.getMinY();

            if (area.contains(x, y)) {
                return new Point2D.Double(x, y);
            }
        }
        return null;
    }

}
