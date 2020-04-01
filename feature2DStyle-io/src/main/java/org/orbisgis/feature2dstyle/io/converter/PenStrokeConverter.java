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
import org.orbisgis.style.stroke.PenStroke;

/**
 *
 * @author ebocher
 */
public class PenStrokeConverter implements Converter {

    public PenStrokeConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {     
            PenStroke penStroke = (PenStroke) value;
            writer.startNode("PenStroke");
            Feature2DStyleWriter.convertAnother(mc,penStroke.getFill());
            Feature2DStyleWriter.marshalParameterValue("Width", penStroke.getWidth(), writer);
            Feature2DStyleWriter.marshalParameterValue("DashArray", penStroke.getDashArray(), writer);
            Feature2DStyleWriter.marshalParameterValue("DashOffset", penStroke.getDashOffset(), writer);
            PenStroke.LineCap lineCap = penStroke.getLineCap();
            if(lineCap!=null){
                 writer.startNode("LineCap");
                 writer.setValue(lineCap.name());
                 writer.endNode();
            }
            PenStroke.LineJoin lineJoin = penStroke.getLineJoin();
            if(lineJoin!=null){
                 writer.startNode("LineJoin");
                 writer.setValue(lineJoin.name());
                 writer.endNode();
            }
            writer.endNode();
        
     }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return  new PenStroke();
    }

    @Override
    public boolean canConvert(Class type) {
       return type.equals(PenStroke.class);
    }
    
}
