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
import org.orbisgis.style.fill.SolidFill;

/**
 *
 * @author ebocher
 */
public class SolidFillConverter implements Converter {

    public SolidFillConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        SolidFill solidFill = (SolidFill) value;
        writer.startNode("SolidFill");
        Feature2DStyleWriter.marshalParameterValue("Color", solidFill.getColor(), writer);
        Feature2DStyleWriter.marshalParameterValue("Opacity", solidFill.getOpacity(), writer);
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new SolidFill();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(SolidFill.class);
    }

}
