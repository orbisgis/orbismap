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
import org.orbisgis.style.symbolizer.LineSymbolizer;

/**
 *
 * @author ebocher
 */
public class LineSymbolizerConverter implements Converter {

    public LineSymbolizerConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        LineSymbolizer lineSymbolizer = (LineSymbolizer) value;
        writer.startNode("LineSymbolizer");
        Feature2DStyleWriter.marshalSymbolizerMetadata(lineSymbolizer, writer, mc);
        Feature2DStyleWriter.convertAnother(mc, lineSymbolizer.getStroke());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new LineSymbolizer();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(LineSymbolizer.class);
    }

}
