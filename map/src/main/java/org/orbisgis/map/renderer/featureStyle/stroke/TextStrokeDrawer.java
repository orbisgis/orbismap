/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.map.renderer.featureStyle.label.LineLabelDrawer;
import org.orbisgis.style.label.LineLabel;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.stroke.TextStroke;

/**
 *
 * @author ebocher
 */
public class TextStrokeDrawer implements ISymbolizerDraw<TextStroke>{

    private Shape shape;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, TextStroke styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
  
        LineLabel lineLabel = styleNode.getLineLabel();
        if (lineLabel != null) {
            new LineLabelDrawer().draw(g2, mapTransform, lineLabel, properties);
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
    
}