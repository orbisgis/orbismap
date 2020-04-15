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
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.Literal;
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
            Feature2DStyleIO.convertAnother(mc,penStroke.getFill());
            Feature2DStyleIO.marshalParameterValue("Width", penStroke.getWidth(), writer);
            Feature2DStyleIO.marshalParameterValue("DashArray", penStroke.getDashArray(), writer);
            Feature2DStyleIO.marshalParameterValue("DashOffset", penStroke.getDashOffset(), writer);
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
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        PenStroke penStroke =  new PenStroke();        
        while (reader.hasMoreChildren()) {
            reader.moveDown();            
            if ("solidfill".equalsIgnoreCase(reader.getNodeName())) {
               SolidFill fill = (SolidFill) context.convertAnother(reader, SolidFill.class);
               penStroke.setFill(fill);            
            }
            else if ("width".equalsIgnoreCase(reader.getNodeName())) {
                penStroke.setWidth(new Literal(Float.parseFloat(reader.getValue())));
            }
            reader.moveUp();
        }
        return penStroke;
    }

    @Override
    public boolean canConvert(Class type) {
       return type.equals(PenStroke.class);
    }
    
}
