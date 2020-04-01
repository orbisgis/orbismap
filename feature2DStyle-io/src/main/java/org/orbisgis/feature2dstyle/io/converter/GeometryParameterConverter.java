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
import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 *
 * @author ebocher
 */
public class GeometryParameterConverter implements Converter {

    public GeometryParameterConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        GeometryParameter geometryParameter = (GeometryParameter) value;
        writer.startNode("Geometry");
        writer.setValue(geometryParameter.getExpression());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(GeometryParameter.class);
    }

}
