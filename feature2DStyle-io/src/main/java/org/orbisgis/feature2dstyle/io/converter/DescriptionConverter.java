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
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors; 
import org.orbisgis.style.common.Description;
import org.orbisgis.style.common.Keywords;

/**
 *
 * @author ebocher
 */
public class DescriptionConverter implements Converter {

    public DescriptionConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
       Description description = (Description) value;
            Locale local = Locale.getDefault();
            String title = description.getTitle(local);
            if (title != null) {
                writer.startNode("Title");
                writer.setValue(title);
                writer.endNode();
            }
            String resum = description.getAbstract(local);
            if (resum != null) {
                writer.startNode("Abstract");
                writer.setValue(resum);
                writer.endNode();
            }

            HashMap<URI, Keywords> keywords = description.getKeywords();
            if(keywords!=null){
            for (Map.Entry<URI, Keywords> en : keywords.entrySet()) {
                URI key = en.getKey();
                writer.startNode("Keywords");
                writer.startNode("Uri");
                writer.setValue(key.toString());
                String val = en.getValue().getKeywords(local).stream().map( n -> n.getValue() ).collect(Collectors.joining( "," ) );
                writer.addAttribute("values", val);
                writer.endNode();
                writer.endNode();
            }
            }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Description.class);
    }

}
