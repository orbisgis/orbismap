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
import org.orbisgis.orbismap.style.color.HexaColor;
import org.orbisgis.orbismap.style.color.RGBColor;
import org.orbisgis.orbismap.style.color.WellknownNameColor;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.utils.*;
import java.util.HashMap;

/**
 * SolidFill converter
 * @author Erwan Bocher, CNRS (2020)
 */
public class SolidFillConverter implements Converter {

    public SolidFillConverter() {
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {
        SolidFill solidFill = (SolidFill) value;
        writer.startNode(Feature2DStyleTerms.SOLIDFILL);
        Feature2DStyleIO.convertAnother(mc,solidFill.getColor());
        Feature2DStyleIO.marshalParameterValue(Feature2DStyleTerms.OPACITY, solidFill.getOpacity(), writer);
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        SolidFill solidFill =  new SolidFill();
        while (reader.hasMoreChildren()) {
            reader.moveDown();            
            if (Feature2DStyleTerms.COLOR.equalsIgnoreCase(reader.getNodeName())) {
                    String colorValue = reader.getValue();
                    if(colorValue!=null && colorValue.startsWith("#")){
                        HexaColor hexaColor = new HexaColor();
                        hexaColor.setHexaColor(new Literal(colorValue));
                        solidFill.setColor(hexaColor);
                    }
                    else if(colorValue!=null && !colorValue.isEmpty()){
                        if(colorValue.startsWith(Feature2DStyleTerms.RGB)) {
                            HashMap<String, ParameterValue> rgbValues = ColorUtils.parseRGB(colorValue);
                            if (rgbValues != null) {
                                RGBColor rgbColor = new RGBColor();
                                rgbColor.setRed(rgbValues.get(Feature2DStyleTerms.RED));
                                rgbColor.setGreen(rgbValues.get(Feature2DStyleTerms.GREEN));
                                rgbColor.setBlue(rgbValues.get(Feature2DStyleTerms.BLUE));
                                solidFill.setColor(rgbColor);
                            }
                        } else if(colorValue.toLowerCase().startsWith(Feature2DStyleTerms.EXPRESSION.toLowerCase())){
                                HexaColor hexaColor = new HexaColor();
                                hexaColor.setHexaColor(Feature2DStyleIO.createParameterValueFromString(reader));
                                solidFill.setColor(hexaColor);
                        }else{
                            WellknownNameColor wellknownNameColor = new WellknownNameColor();
                            wellknownNameColor.setWellknownName(Feature2DStyleIO.createParameterValueFromString(reader));
                            solidFill.setColor(wellknownNameColor);
                        }
                    }
                    else if(colorValue!=null && colorValue.toLowerCase().startsWith(Feature2DStyleTerms.EXPRESSION.toLowerCase())){
                        HexaColor hexaColor = new HexaColor();
                        hexaColor.setHexaColor(Feature2DStyleIO.createParameterValueFromString(reader));
                        solidFill.setColor(hexaColor);
                    }
            }
            else if (Feature2DStyleTerms.OPACITY.equalsIgnoreCase(reader.getNodeName())) {
                solidFill.setOpacity(Feature2DStyleIO.createParameterValue(reader));
            }
            reader.moveUp();
        }
        return solidFill;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(SolidFill.class);
    }

}
