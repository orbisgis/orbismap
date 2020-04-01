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
import org.orbisgis.style.symbolizer.AreaSymbolizer;

/**
 *
 * @author ebocher
 */
public class AreaSymbolizerConverter implements Converter {

    public AreaSymbolizerConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
            AreaSymbolizer areaSymbolizer = (AreaSymbolizer) value;  
            writer.startNode("AreaSymbolizer");
            Feature2DStyleWriter.marshalSymbolizerMetadata(areaSymbolizer, writer, mc);
            Feature2DStyleWriter.convertAnother(mc,areaSymbolizer.getStroke());
            Feature2DStyleWriter.convertAnother(mc,areaSymbolizer.getFill());
            writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
            return new AreaSymbolizer();
    }

    @Override
    public boolean canConvert(Class type) {
            return type.equals(AreaSymbolizer.class);
    }
    
}
