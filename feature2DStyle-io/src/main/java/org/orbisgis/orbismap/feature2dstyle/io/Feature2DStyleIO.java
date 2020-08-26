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
package org.orbisgis.orbismap.feature2dstyle.io;

import org.orbisgis.orbismap.style.Feature2DStyleTerms;
import org.orbisgis.orbismap.feature2dstyle.io.converter.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.apache.commons.text.StringEscapeUtils;



/**
 * Methods to read and write Feature2DStyle in XML or JSON formats.
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class Feature2DStyleIO {

    private static Pattern EXPRESSION_PATTERN;

    /**
     * Save any style node to an xml file
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Feature2DStyle fromXML(File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream();
            registerConverter(xstream);
            return (Feature2DStyle) xstream.fromXML(new FileInputStream(file));
        } else {
            throw new RuntimeException("Invalid input file path. Use a se extension file name.");
        }
    }

    /**
     * Save any style node to an xml file
     *
     * @param styleNode
     * @param file
     * @throws FileNotFoundException
     */
    public static void toXML(StyleNode styleNode, File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream();            
            registerConverter(xstream);
            xstream.toXML(styleNode, new FileOutputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a se extension file name.");
        }
    }

    /**
     * Save any style node to a json file
     *
     * @param styleNode
     * @param file
     * @throws FileNotFoundException
     */
    public static void toJSON(StyleNode styleNode, File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "json")) {
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            registerConverter(xstream);
            xstream.toXML(styleNode, new FileOutputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a json extension file name.");
        }
    }

    /**
     * Read any style node to a json file
     *
     * @param file
     * @return 
     * @throws FileNotFoundException
     */
    public static Feature2DStyle  fromJSON(File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "json")) {
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            registerConverter(xstream);
            return (Feature2DStyle) xstream.fromXML(new FileInputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a json extension file name.");
        }
    }

    /**
     * Load Feature2Style converts
     *
     * @param xstream
     */
    private static void registerConverter( XStream xstream){
        xstream.registerConverter(new Feature2DStyleConverter());
        xstream.registerConverter(new Feature2DRuleConverter());
        xstream.registerConverter(new RuleFilterConverter());
        xstream.registerConverter(new AreaSymbolizerConverter());
        xstream.registerConverter(new LineSymbolizerConverter());
        xstream.registerConverter(new PointSymbolizerConverter());
        xstream.registerConverter(new TextSymbolizerConverter());
        xstream.registerConverter(new PenStrokeConverter());
        xstream.registerConverter(new WobbleStrokeConverter());
        xstream.registerConverter(new ViewBoxConverter());
        xstream.registerConverter(new GraphicSizeConverter());
        xstream.registerConverter(new SolidFillConverter());
        xstream.registerConverter(new MarkGraphicConverter());
        xstream.registerConverter(new HaloConverter());
        xstream.registerConverter(new DescriptionConverter());
        xstream.registerConverter(new UomConverter());
        xstream.registerConverter(new GeometryParameterConverter());
        xstream.registerConverter(new RGBColorConverter());
        xstream.registerConverter(new HexaColorConverter());
        xstream.registerConverter(new WellknownNameColorConverter());
        xstream.registerConverter(new ParameterValueConverter());
        xstream.alias(Feature2DStyleTerms.FEATURE2DSTYLE, Feature2DStyle.class);
    }

    /**
     * Method to marshall a parameter value
     *
     * @param parameterValue
     * @param writer
     */
    public static void marshalParameterValue(ParameterValue parameterValue, HierarchicalStreamWriter writer) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                String valuetoWrite = String.valueOf(parameterValue.getValue());
                if (!valuetoWrite.isEmpty()) {
                    writer.setValue(valuetoWrite);
                }
            } else if (parameterValue instanceof Expression) {
                String valuetoWrite = String.valueOf(((Expression) parameterValue).getExpression());
                if (!valuetoWrite.isEmpty()) {
                    writer.setValue(valuetoWrite);
                }
            }
        }
    }

    /**
     * Method to marshall a parameter value
     *
     * @param nodeName
     * @param parameterValue
     * @param writer
     */
    public static void marshalParameterValue(String nodeName, ParameterValue parameterValue, HierarchicalStreamWriter writer) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                String valuetoWrite = String.valueOf(parameterValue.getValue());
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(nodeName);
                    writer.setValue(valuetoWrite);
                    writer.endNode();
                }
            } else if (parameterValue instanceof Expression) {
                String valuetoWrite = String.valueOf(((Expression) parameterValue).getExpression());
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(nodeName);
                    writer.startNode("Expression");
                    writer.setValue(valuetoWrite);
                    writer.endNode();
                    writer.endNode();
                }
            }
        }
    }

    /**
     * Marshal commun symbolizer properties
     *
     * @param symbolizer
     * @param writer
     * @param mc
     */
    public static void marshalSymbolizerMetadata(IFeatureSymbolizer symbolizer, HierarchicalStreamWriter writer, MarshallingContext mc) {
        String name = symbolizer.getName();
        if (name != null && !name.isEmpty()) {
            writer.startNode(Feature2DStyleTerms.NAME);
            writer.setValue(name);
            writer.endNode();
        }
        Feature2DStyleIO.convertAnother(mc, symbolizer.getUom());
        Feature2DStyleIO.convertAnother(mc, symbolizer.getDescription());
        Feature2DStyleIO.convertAnother(mc, symbolizer.getGeometryParameter());
        Feature2DStyleIO.convertAnother(Feature2DStyleTerms.PERPENDICULAROFFSET,  writer, mc, symbolizer.getPerpendicularOffset());
        writer.startNode(Feature2DStyleTerms.LEVEL);
        writer.setValue(String.valueOf(symbolizer.getLevel()));
        writer.endNode();

    }

    /**
     *
     * @param mc
     * @param styleNode
     */
    public static void convertAnother(MarshallingContext mc, Object styleNode) {
        if (styleNode != null) {
            mc.convertAnother(styleNode);
        }
    }

    /**
     * Check if the file has the good extension
     *
     * @param file
     * @param prefix
     * @return
     */
    public static boolean isExtensionWellFormated(File file, String prefix) {
        String path = file.getAbsolutePath();
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i >= 0) {
            extension = path.substring(i + 1);
        }
        return extension.equalsIgnoreCase(prefix);
    }
    
     /**
     * 
     * @param reader
     * @param context
     * @return 
     */
    public static ParameterValue createParameterValue(HierarchicalStreamReader reader, UnmarshallingContext context) {        
        return (ParameterValue) context.convertAnother(reader, ParameterValue.class);
    }

    
    /**
     * Translator object for escaping XML 1.1. While escapeXml11(String)
     * is the expected method of use, this object allows the XML escaping functionality to be used as the foundation for a custom translator.
     * See : http://commons.apache.org/proper/commons-lang/apidocs/index.html
     * @param value
     * @return
     */
    public static String unescapeXMLString(String value){
        return StringEscapeUtils.unescapeXml(value);
    }   
    

    /**
     * 
     * @param reader
     * @return 
     */
    public static ParameterValue createParameterValueFromString(HierarchicalStreamReader reader) {            
        return createParameterValueFromString(reader.getValue());
    }


    /**
     * 
     * @param value
     * @return 
     */
    public static ParameterValue createParameterValueFromString(String value) {
        if (EXPRESSION_PATTERN == null) {
            EXPRESSION_PATTERN = Pattern.compile("\\s*(?:expression\\s*\\(\\s*(.+)?\\)|([^\\s]+))\\s*", Pattern.CASE_INSENSITIVE);
        }
        if (value != null && !value.isEmpty()) {
            value = unescapeXMLString(value);
            Matcher matcher = EXPRESSION_PATTERN.matcher(value);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                if (group1 != null) {
                    if (group2 != null && !group2.isEmpty()) {
                        return new NullParameterValue();
                    } else {
                        return new Expression(group1);
                    }
                } else {
                    if (group2 != null && !group2.isEmpty()) {
                        return new Literal(group2);
                    } else {
                        return new NullParameterValue();
                    }
                }
            }
        }
        return new NullParameterValue();
    }

    public static ViewBox createViewBox(HierarchicalStreamReader reader) {
        ViewBox viewBox = new ViewBox();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if ("height".equalsIgnoreCase(reader.getNodeName())) {
                viewBox.setHeight(new Literal(Float.parseFloat(reader.getValue())));
            } else if ("width".equalsIgnoreCase(reader.getNodeName())) {
                viewBox.setWidth(new Literal(reader.getValue()));
            }
            reader.moveUp();
        }
        return viewBox;
    }

    /**
     * Return a string representation of the parameterValue
     * @param parameterValue
     * @return
     */
    public static String getParameterValue(ParameterValue parameterValue) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                String valuetoWrite = String.valueOf(parameterValue.getValue());
                if (!valuetoWrite.isEmpty()) {
                    return unescapeXMLString(valuetoWrite);
                }
            } else if (parameterValue instanceof Expression) {
                String valuetoWrite = ((Expression) parameterValue).getExpression();
                if (!valuetoWrite.isEmpty()) {
                    return "expression("+unescapeXMLString(valuetoWrite)+")";
                }
            }
        }
        return null;
    }

    /**
     * Create a node element based on a name and add its value as a string representation
     * @param fielName
     * @param parameterValue
     * @param writer
     */
    public static void appendParameterValue(String fielName, ParameterValue parameterValue, HierarchicalStreamWriter writer) {
        if(fielName!=null && !fielName.isEmpty())
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                String valuetoWrite = String.valueOf(parameterValue.getValue());
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(fielName);
                    writer.setValue(valuetoWrite);
                    writer.endNode();
                }
            } else if (parameterValue instanceof Expression) {
                String valuetoWrite = ((Expression) parameterValue).getExpression();
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(fielName);
                    writer.setValue("expression("+ valuetoWrite+")");
                    writer.endNode();
                }
            }
        }
    }

    /**
     * Append a new node name before going to the next style node
     * @param nodeName
     * @param writer
     * @param mc
     * @param parameterValue 
     */
    public static void convertAnother(String nodeName, HierarchicalStreamWriter writer, MarshallingContext mc, ParameterValue parameterValue) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue))  {
            writer.startNode(nodeName);
            mc.convertAnother(parameterValue);            
            writer.endNode();
        }
    }
}
