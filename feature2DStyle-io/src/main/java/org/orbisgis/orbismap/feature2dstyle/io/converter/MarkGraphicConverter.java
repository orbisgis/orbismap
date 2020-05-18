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
 * Feature2DStyle-IO is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle-IO is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle-IO is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle-IO . If not, see <http://www.gnu.org/licenses/>.
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
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.Halo;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.graphic.graphicSize.Size;
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.stroke.PenStroke;

/**
 *
 * @author ebocher
 */
public class MarkGraphicConverter implements Converter {

    public MarkGraphicConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        MarkGraphic markGraphic = (MarkGraphic) value;
        writer.startNode(Feature2DStyleTerms.MARKGRAPHIC);
        Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.WELLKNOWNNAME, markGraphic.getWellKnownName(), writer);
        Feature2DStyleIO.convertAnother(mc, markGraphic.getGraphicSize());
        Feature2DStyleIO.convertAnother(mc, markGraphic.getStroke());
        Feature2DStyleIO.convertAnother(mc, markGraphic.getFill());
        Feature2DStyleIO.convertAnother(mc, markGraphic.getHalo());
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        MarkGraphic symbolizer = new MarkGraphic();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String node = reader.getNodeName();
            if (Feature2DStyleTerms.UOM.equalsIgnoreCase(node)) {
                Uom uom = (Uom) context.convertAnother(reader, Uom.class);
                symbolizer.setUom(uom);
            } else if (Feature2DStyleTerms.PENSTROKE.equalsIgnoreCase(node)) {
                PenStroke penStroke = (PenStroke) context.convertAnother(reader, PenStroke.class);
                symbolizer.setStroke(penStroke);
            } else if (Feature2DStyleTerms.SOLIDFILL.equalsIgnoreCase(node)) {
                SolidFill fill = (SolidFill) context.convertAnother(reader, SolidFill.class);
                symbolizer.setFill(fill);
            } else if (Feature2DStyleTerms.HALO.equalsIgnoreCase(node)) {
                Halo halo = (Halo) context.convertAnother(reader, Halo.class);
                symbolizer.setHalo(halo);
            } else if (Feature2DStyleTerms.WELLKNOWNNAME.equalsIgnoreCase(node)) {
                symbolizer.setWellKnownName((ParameterValue) context.convertAnother(reader, ParameterValue.class));
            } else if (Feature2DStyleTerms.VIEWBOX.equalsIgnoreCase(node)) {
                ViewBox viewBox = (ViewBox) context.convertAnother(reader, ViewBox.class);
                symbolizer.setGraphicSize(viewBox);
            }else if (Feature2DStyleTerms.SIZE.equalsIgnoreCase(node)) {
                Size size = new Size();
                size.setSize((ParameterValue) context.convertAnother(reader, ParameterValue.class));
                symbolizer.setGraphicSize(size);
            }
            reader.moveUp();
        }
        return symbolizer;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(MarkGraphic.class);
    }

}
