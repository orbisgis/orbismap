/**
 * Feature2DStyle-IO is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Feature2DStyle-IO  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle-IO  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle-IO  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle-IO . If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.feature2dstyle.io.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.orbisgis.feature2dstyle.io.Feature2DStyleIO;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class Feature2DRuleConverter implements Converter {
    
    public Feature2DRuleConverter() {
    }
    
    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        Feature2DRule feature2DRule = (Feature2DRule) value;        
        writer.startNode("Rule");
        String name = feature2DRule.getName();
        if (name != null) {
            writer.startNode("Name");
            writer.setValue(name);
            writer.endNode();
        }
        Feature2DStyleIO.convertAnother(mc, feature2DRule.getDescription());        
        Feature2DStyleIO.marshalParameterValue("Filter", feature2DRule.getFilter(), writer);
        Double maxScale = feature2DRule.getMaxScaleDenom();
        if (maxScale != null) {
            writer.startNode("MaxScaleDenominator");
            writer.setValue(String.valueOf(maxScale));
            writer.endNode();
        }
        Double minScale = feature2DRule.getMinScaleDenom();        
        if (minScale != null) {
            writer.startNode("MinScaleDenominator");
            writer.setValue(String.valueOf(minScale));
            writer.endNode();
        }        
        for (IFeatureSymbolizer symbolizer : feature2DRule.getSymbolizers()) {            
            Feature2DStyleIO.convertAnother(mc, symbolizer);
        }
        
        writer.endNode();
    }
    
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Feature2DRule feature2DRule = new Feature2DRule();
        while (reader.hasMoreChildren()) {
            reader.moveDown();            
            if ("name".equalsIgnoreCase(reader.getNodeName())) {
                feature2DRule.setName(reader.getValue());
            }
            else if ("areasymbolizer".equalsIgnoreCase(reader.getNodeName())) {
                AreaSymbolizer symbolizer = (AreaSymbolizer) context.convertAnother(reader, AreaSymbolizer.class);
                feature2DRule.addSymbolizer(symbolizer);
            }
            else if ("linesymbolizer".equalsIgnoreCase(reader.getNodeName())) {
                LineSymbolizer symbolizer = (LineSymbolizer) context.convertAnother(reader, LineSymbolizer.class);
                feature2DRule.addSymbolizer(symbolizer);
            }
            else if ("pointsymbolizer".equalsIgnoreCase(reader.getNodeName())) {
                PointSymbolizer symbolizer = (PointSymbolizer) context.convertAnother(reader, PointSymbolizer.class);
                feature2DRule.addSymbolizer(symbolizer);
            }
            else if ("textsymbolizer".equalsIgnoreCase(reader.getNodeName())) {
                TextSymbolizer symbolizer = (TextSymbolizer) context.convertAnother(reader, TextSymbolizer.class);
                feature2DRule.addSymbolizer(symbolizer);
            }
            reader.moveUp();
        }
        return feature2DRule;
    }
    
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Feature2DRule.class);
    }
    
}
