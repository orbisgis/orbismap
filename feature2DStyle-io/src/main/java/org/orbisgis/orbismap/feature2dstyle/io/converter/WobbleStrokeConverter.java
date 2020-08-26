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
package org.orbisgis.orbismap.feature2dstyle.io.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.orbisgis.orbismap.feature2dstyle.io.Feature2DStyleIO;
import org.orbisgis.orbismap.style.Feature2DStyleTerms;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.stroke.LineCap;
import org.orbisgis.orbismap.style.stroke.LineJoin;
import org.orbisgis.orbismap.style.stroke.WobbleStroke;

/**
 *
 * @author ebocher
 */
public class WobbleStrokeConverter implements Converter {

    public WobbleStrokeConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {     
            WobbleStroke wobbleStroke = (WobbleStroke) value;
            writer.startNode(Feature2DStyleTerms.WOBBLESTROKE);
            Feature2DStyleIO.convertAnother(mc,wobbleStroke.getFill());
            Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.WIDTH, wobbleStroke.getWidth(), writer);
            Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.AMPLITUDE, wobbleStroke.getAmplitude(), writer);
            Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.DETAIL, wobbleStroke.getDetail(), writer);
            Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.DASHARRAY, wobbleStroke.getDashArray(), writer);
            Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.DASHOFFSET, wobbleStroke.getDashOffset(), writer);
            LineCap lineCap = wobbleStroke.getLineCap();
            if(lineCap!=null){
                 writer.startNode(Feature2DStyleTerms.LINECAP);
                 writer.setValue(lineCap.name());
                 writer.endNode();
            }
            LineJoin lineJoin = wobbleStroke.getLineJoin();
            if(lineJoin!=null){
                 writer.startNode(Feature2DStyleTerms.LINEJOIN);
                 writer.setValue(lineJoin.name());
                 writer.endNode();
            }
            writer.endNode();
        
     }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        WobbleStroke wobbleStroke =  new WobbleStroke();
        while (reader.hasMoreChildren()) {
            reader.moveDown();            
            if (Feature2DStyleTerms.SOLIDFILL.equalsIgnoreCase(reader.getNodeName())) {
               SolidFill fill = (SolidFill) context.convertAnother(reader, SolidFill.class);
               wobbleStroke.setFill(fill);
            }
            else if (Feature2DStyleTerms.WIDTH.equalsIgnoreCase(reader.getNodeName())) {
                wobbleStroke.setWidth((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            }
            else if (Feature2DStyleTerms.AMPLITUDE.equalsIgnoreCase(reader.getNodeName())) {
                wobbleStroke.setAmplitude((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            }
            else if (Feature2DStyleTerms.DETAIL.equalsIgnoreCase(reader.getNodeName())) {
                wobbleStroke.setDetail((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            }
            else if (Feature2DStyleTerms.DASHARRAY.equalsIgnoreCase(reader.getNodeName())) {
                wobbleStroke.setDashArray((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            }
            else if (Feature2DStyleTerms.DASHOFFSET.equalsIgnoreCase(reader.getNodeName())) {
                wobbleStroke.setDashOffset((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            }
            else if (Feature2DStyleTerms.LINECAP.equalsIgnoreCase(reader.getNodeName())) {
                LineCap lineCap = LineCap.fromString(reader.getValue());
                if (lineCap != null) {
                    wobbleStroke.setLineCap(lineCap);
                }
            }
            else if (Feature2DStyleTerms.LINEJOIN.equalsIgnoreCase(reader.getNodeName())) {
                LineJoin lineJoin = LineJoin.fromString(reader.getValue());
                if (lineJoin != null) {
                    wobbleStroke.setLineJoin(lineJoin);
                }
            }
            reader.moveUp();
        }
        return wobbleStroke;
    }

    @Override
    public boolean canConvert(Class type) {
       return type.equals(WobbleStroke.class);
    }
    
}
