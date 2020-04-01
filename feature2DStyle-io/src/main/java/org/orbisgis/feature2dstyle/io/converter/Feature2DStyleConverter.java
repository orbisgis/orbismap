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
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;

/**
 *
 * @author ebocher
 */
public class Feature2DStyleConverter implements Converter {

    public Feature2DStyleConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        if (value != null) {
            Feature2DStyle feature2DStyle = (Feature2DStyle) value;
            String name = feature2DStyle.getName();
            if (name != null) {
                writer.startNode("Name");
                writer.setValue(name);
                writer.endNode();
            }
            Feature2DStyleWriter.convertAnother(mc,feature2DStyle.getDescription());

            for (Feature2DRule symbolizer : feature2DStyle.getRules()) {

                Feature2DStyleWriter.convertAnother(mc,symbolizer);
            }

        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return new Feature2DStyle();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Feature2DStyle.class);
    }

}
