/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.io.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.orbisgis.feature2dstyle.io.Feature2DStyleWriter;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.ViewBox;

/**
 *
 * @author ebocher
 */
public class MarkGraphicConverter implements Converter {

    public MarkGraphicConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        MarkGraphic markGraphic = (MarkGraphic) value;
        writer.startNode("MarkGraphic");
        Feature2DStyleWriter.marshalParameterValue("WellKnownName", markGraphic.getWkn(), writer);
        ViewBox viewBox = markGraphic.getViewBox();
        if (viewBox != null) {
            writer.startNode("ViewBox");
            Feature2DStyleWriter.marshalParameterValue("Width", viewBox.getWidth(), writer);
            Feature2DStyleWriter.marshalParameterValue("Height", viewBox.getHeight(), writer);
            writer.endNode();
        }
        Feature2DStyleWriter.convertAnother(mc, markGraphic.getStroke());
        Feature2DStyleWriter.convertAnother(mc, markGraphic.getStroke());
        Feature2DStyleWriter.convertAnother(mc, markGraphic.getFill());
        Feature2DStyleWriter.convertAnother(mc, markGraphic.getHalo());
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new MarkGraphic();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(MarkGraphic.class);
    }

}
