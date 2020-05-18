/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.orbismap.feature2dstyle.io.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.orbisgis.orbismap.feature2dstyle.io.Feature2DStyleIO;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

/**
 * Expression converter for json
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class ParameterValueConverter implements Converter {

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        ParameterValue parameterValue = (ParameterValue) value;
        if (parameterValue instanceof Literal) {
            writer.setValue(String.valueOf(parameterValue.getValue()));
        } else if (parameterValue instanceof Expression) {
            String valuetoWrite = String.valueOf(((Expression) parameterValue).getExpression());            
            writer.setValue("expression(" + valuetoWrite + ")");
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return Feature2DStyleIO.createParameterValueFromString(reader.getValue());
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(ParameterValue.class)|| type.equals(Literal.class) || type.equals(Expression.class);
    }

}
