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
import org.orbisgis.style.fill.Halo;

/**
 *
 * @author ebocher
 */
public class HaloConverter implements Converter {

    public HaloConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
            Halo halo = (Halo) value;
            writer.startNode("Halo");
            Feature2DStyleWriter.marshalParameterValue("Radius", halo.getRadius(), writer);
            Feature2DStyleWriter.convertAnother(mc,halo.getFill());
            writer.endNode();      
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new Halo();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Halo.class);
    }

}
