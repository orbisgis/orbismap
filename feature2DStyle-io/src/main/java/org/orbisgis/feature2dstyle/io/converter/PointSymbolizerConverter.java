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
import java.util.ArrayList;
import org.orbisgis.feature2dstyle.io.Feature2DStyleIO;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.Description;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
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
        Feature2DStyleIO.marshalSymbolizerMetadata(pointSymbolizer, writer, mc);
        ArrayList<Graphic> grahics = pointSymbolizer.getGraphics().getGraphics();
        for (Graphic grahic : grahics) {
            Feature2DStyleIO.convertAnother(mc,grahic);
        }
        writer.endNode();
       
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        PointSymbolizer symbolizer = new PointSymbolizer();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if ("name".equalsIgnoreCase(reader.getNodeName())) {
                symbolizer.setName(reader.getValue());
            } else if ("level".equalsIgnoreCase(reader.getNodeName())) {
                symbolizer.setLevel(Integer.parseInt(reader.getValue()));
            } else if ("onvertex".equalsIgnoreCase(reader.getNodeName())) {
                symbolizer.setOnVertex(Boolean.parseBoolean(reader.getValue()));
            } else if ("uom".equalsIgnoreCase(reader.getNodeName())) {
                Uom uom = (Uom) context.convertAnother(reader, Uom.class);
                symbolizer.setUom(uom);
            } else if ("geometryparameter".equalsIgnoreCase(reader.getNodeName())) {
                GeometryParameter geometryParameter = (GeometryParameter) context.convertAnother(reader, GeometryParameter.class);
                symbolizer.setGeometryParameter(geometryParameter);
            } else if ("description".equalsIgnoreCase(reader.getNodeName())) {
                Description description = (Description) context.convertAnother(reader, Description.class);
                symbolizer.setDescription(description);
            }else if ("markgraphic".equalsIgnoreCase(reader.getNodeName())) {
                MarkGraphic markGraphic = (MarkGraphic) context.convertAnother(reader, MarkGraphic.class);
                symbolizer.addGraphic(markGraphic);
            }
            reader.moveUp();
        }
        return symbolizer;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(PointSymbolizer.class);
    }

}
