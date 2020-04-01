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
import java.util.ArrayList;
import org.orbisgis.feature2dstyle.io.Feature2DStyleWriter;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.symbolizer.PointSymbolizer;

/**
 *
 * @author ebocher
 */
public class PointSymbolizerConverter implements Converter {

    public PointSymbolizerConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {     
        PointSymbolizer pointSymbolizer = (PointSymbolizer) value;
        writer.startNode("PointSymbolizer");
        Feature2DStyleWriter.marshalSymbolizerMetadata(pointSymbolizer, writer, mc);
        ArrayList<Graphic> grahics = pointSymbolizer.getGraphics().getGraphics();
        for (Graphic grahic : grahics) {
            Feature2DStyleWriter.convertAnother(mc,grahic);
        }
        writer.endNode();
       
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new PointSymbolizer();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(PointSymbolizer.class);
    }

}
