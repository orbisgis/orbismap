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
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author ebocher
 */
public class TextSymbolizerConverter implements Converter {

    public TextSymbolizerConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        TextSymbolizer textSymbolizer = (TextSymbolizer) value;
        writer.startNode("TextSymbolizer");
        Feature2DStyleWriter.marshalSymbolizerMetadata(textSymbolizer, writer, mc);
        Feature2DStyleWriter.convertAnother(mc, textSymbolizer.getLabel());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new TextSymbolizer();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(TextSymbolizer.class);
    }

}
