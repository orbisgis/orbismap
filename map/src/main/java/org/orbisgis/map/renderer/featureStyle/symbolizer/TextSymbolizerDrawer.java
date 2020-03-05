/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.label.PointLabelDrawer;
import static org.orbisgis.map.renderer.featureStyle.symbolizer.AreaSymbolizerDrawer.drawerMap;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.label.Label;
import org.orbisgis.style.label.PointLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class TextSymbolizerDrawer implements ISymbolizerDraw<TextSymbolizer>{

    final static Map<Class, ILabelDrawer> drawerMap = new HashMap<>();
    static {
        drawerMap.put(PointLabel.class, new PointLabelDrawer());
    }
    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, TextSymbolizer symbolizer, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shape = mapTransform.getShape(sp.getGeometry(symbolizer.getGeometryParameter().getIdentifier()), true);
        if (shape != null) {
            List<Shape> shps;
            RealParameter perpendicularOffset = symbolizer.getPerpendicularOffset();
            if (perpendicularOffset != null) {
                Double pOffset = perpendicularOffset.getValue(properties);
                shps = ShapeHelper.perpendicularOffset(shape, pOffset);
            } else {
                shps = new LinkedList<Shape>();
                shps.add(shape);
            }
        
            Label label = symbolizer.getLabel();
            if (drawerMap.containsKey(label.getClass())) {
                properties.put("offset", 0.0);
                ILabelDrawer labelDrawer = drawerMap.get(label.getClass());
                for (Shape s : shps) {
                    properties.put("shape", s);
                    labelDrawer.draw(sp, g2, mapTransform, label, properties);
                }
            }
        }
    }
    
}
