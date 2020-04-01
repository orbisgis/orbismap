/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.io;

import org.orbisgis.feature2dstyle.io.converter.UomConverter;
import org.orbisgis.feature2dstyle.io.converter.SolidFillConverter;
import org.orbisgis.feature2dstyle.io.converter.PenStrokeConverter;
import org.orbisgis.feature2dstyle.io.converter.GeometryParameterConverter;
import org.orbisgis.feature2dstyle.io.converter.AreaSymbolizerConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.orbisgis.feature2dstyle.io.converter.DescriptionConverter;
import org.orbisgis.feature2dstyle.io.converter.Feature2DRuleConverter;
import org.orbisgis.feature2dstyle.io.converter.LineSymbolizerConverter;
import org.orbisgis.feature2dstyle.io.converter.PointSymbolizerConverter;
import org.orbisgis.feature2dstyle.io.converter.TextSymbolizerConverter;
import org.orbisgis.feature2dstyle.io.converter.Feature2DStyleConverter;
import org.orbisgis.feature2dstyle.io.converter.HaloConverter;
import org.orbisgis.feature2dstyle.io.converter.MarkGraphicConverter;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 *
 * @author ebocher
 */
public class Feature2DStyleWriter {

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
            xstream.registerConverter(new Feature2DStyleConverter());
            xstream.registerConverter(new Feature2DRuleConverter());
            xstream.registerConverter(new AreaSymbolizerConverter());
            xstream.registerConverter(new LineSymbolizerConverter());
            xstream.registerConverter(new PointSymbolizerConverter());
            xstream.registerConverter(new TextSymbolizerConverter());
            xstream.registerConverter(new PenStrokeConverter());
            xstream.registerConverter(new SolidFillConverter());
            xstream.registerConverter(new MarkGraphicConverter());
            xstream.registerConverter(new HaloConverter());
            xstream.registerConverter(new DescriptionConverter());
            xstream.registerConverter(new UomConverter());
            xstream.registerConverter(new GeometryParameterConverter());
            xstream.alias("Feature2DStyle", Feature2DStyle.class);
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
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.registerConverter(new Feature2DStyleConverter());
            xstream.registerConverter(new Feature2DRuleConverter());
            xstream.registerConverter(new AreaSymbolizerConverter());
            xstream.registerConverter(new LineSymbolizerConverter());
            xstream.registerConverter(new PointSymbolizerConverter());
            xstream.registerConverter(new TextSymbolizerConverter());
            xstream.registerConverter(new PenStrokeConverter());
            xstream.registerConverter(new SolidFillConverter());
            xstream.registerConverter(new MarkGraphicConverter());
            xstream.registerConverter(new HaloConverter());
            xstream.registerConverter(new DescriptionConverter());
            xstream.registerConverter(new UomConverter());
            xstream.registerConverter(new GeometryParameterConverter());
            xstream.alias("Feature2DStyle", Feature2DStyle.class);
            xstream.toXML(styleNode, new FileOutputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a json extension file name.");
        }
    }

    /**
     * Method to marshall a parameter value
     *
     * @param fielName
     * @param parameterValue
     * @param writer
     */
    public static void marshalParameterValue(String fielName, ParameterValue parameterValue, HierarchicalStreamWriter writer) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                writer.startNode(fielName);
                writer.setValue(String.valueOf(parameterValue.getValue()));
                writer.endNode();
            } else if (parameterValue instanceof Expression) {
                writer.startNode(fielName);
                writer.startNode("Expression");
                writer.setValue(String.valueOf(parameterValue.getValue()));
                writer.endNode();
                writer.endNode();
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
            writer.startNode("Name");
            writer.setValue(name);
            writer.endNode();
        }
        mc.convertAnother(symbolizer.getUom());
        mc.convertAnother(symbolizer.getDescription());
        mc.convertAnother(symbolizer.getGeometryParameter());
        Feature2DStyleWriter.marshalParameterValue("PerpendicularOffset", symbolizer.getPerpendicularOffset(), writer);

        writer.startNode("Level");
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

}
