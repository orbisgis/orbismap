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
import org.orbisgis.style.IFeatureSymbolizer;

/**
 *
 * @author ebocher
 */
public class Feature2DRuleConverter implements Converter {

    public Feature2DRuleConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
            Feature2DRule feature2DRule = (Feature2DRule) value;  
            writer.startNode("Rule");
                String name = feature2DRule.getName();
                if(name!=null){
                    writer.startNode("Name");
                    writer.setValue(name);
                    writer.endNode();
                }
                Feature2DStyleWriter.convertAnother(mc,feature2DRule.getDescription());                
                Feature2DStyleWriter.marshalParameterValue("Filter", feature2DRule.getFilterExpression(),writer);
                Double maxScale = feature2DRule.getMaxScaleDenom();
                if(maxScale!=null){
                    writer.startNode("MaxScaleDenominator");
                    writer.setValue(String.valueOf(maxScale));
                    writer.endNode();
                }
                Double minScale =  feature2DRule.getMinScaleDenom();   
                if(name!=null){
                    writer.startNode("MinScaleDenominator");
                    writer.setValue(String.valueOf(minScale));
                    writer.endNode();
                }                
                for (IFeatureSymbolizer symbolizer : feature2DRule.getSymbolizers()) {                    
                    Feature2DStyleWriter.convertAnother(mc,symbolizer);
                }
            
            writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
            return new Feature2DRule();
    }

    @Override
    public boolean canConvert(Class type) {
            return type.equals(Feature2DRule.class);
    }
    
}
