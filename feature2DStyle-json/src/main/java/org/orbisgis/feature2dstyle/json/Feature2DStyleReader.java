/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.common.Description;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 *
 * @author ebocher
 */
public class Feature2DStyleReader {

    private final File file;

    public Feature2DStyleReader(File file) {
        this.file = file;

    }

    Feature2DStyle read() throws IOException, Exception {
        if (file != null) {
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON file to map
            Map<?, ?> map = mapper.readValue(file, Map.class);
            return  iterateStyle((Map<String, Object>) map);
        }
        return null;

    }

    Feature2DStyle iterateStyle(Map<String, Object> map) throws Exception {
        boolean asStyle = false;
        Feature2DStyle feature2DStyle = null;
        ArrayList<Map<String, Object>> rules = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("style")) {
                if (((String) entry.getValue()).equalsIgnoreCase("feature2dstyle")) {
                    feature2DStyle = new Feature2DStyle();
                    asStyle = true;
                } else {
                    throw new ParameterException("Unknown style type");
                }
            } else if (keyValue.equalsIgnoreCase("name")) {
                feature2DStyle.setName((String) entry.getValue());
            } else if (keyValue.equalsIgnoreCase("rules")) {
                rules = (ArrayList) entry.getValue();
            }
        }

        if (asStyle) {
            for (Map rule : rules) {
                Feature2DRule feature2DRule = new Feature2DRule();
                iterateRule(feature2DRule, rule);
                feature2DStyle.addRule(feature2DRule);
            }
        }
        return feature2DStyle;

    }

    /**
     *
     * @param rule
     * @param map
     * @throws ParameterException
     */
    void iterateRule(Feature2DRule rule, Map<String, Object> map) throws ParameterException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("name")) {
                rule.setName((String) entry.getValue());
            }
            if (keyValue.equalsIgnoreCase("areasymbolizer")) {
                AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
                initSymbolizerMetadata(areaSymbolizer);
                iterateAreaSymbolizer((Map<String, Object>) entry.getValue(), rule, areaSymbolizer);
                rule.addSymbolizer(areaSymbolizer);
            } else if (keyValue.equalsIgnoreCase("pointsymbolizer")) {
                PointSymbolizer pointSymbolizer = new PointSymbolizer();
                initSymbolizerMetadata(pointSymbolizer);
                iteratePointSymbolizer((Map<String, Object>) entry.getValue(), rule, pointSymbolizer);
                rule.addSymbolizer(pointSymbolizer);
            } else if (keyValue.equalsIgnoreCase("linesymbolizer")) {
                LineSymbolizer lineSymbolizer = new LineSymbolizer();
                initSymbolizerMetadata(lineSymbolizer);
                iterateLineSymbolizer((Map<String, Object>) entry.getValue(), rule, lineSymbolizer);
                rule.addSymbolizer(lineSymbolizer);
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {
        Feature2DStyleReader feature2DStyleReader = new Feature2DStyleReader(new File("/tmp/mysld.json"));
        feature2DStyleReader.read();

    }

    private void iteratePointSymbolizer(Map<String, Object> map, Feature2DRule rule, PointSymbolizer pointSymbolizer) throws ParameterException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            iterateSymbolizerMetadata(entry, pointSymbolizer);
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("onvertex")) {
                pointSymbolizer.setName((String) entry.getValue());
            } else if (keyValue.equalsIgnoreCase("markgraphic")) {

            }

        }

    }

    private void iterateSymbolizerMetadata(Map.Entry<String, Object> entry, IFeatureSymbolizer symbolizer) throws ParameterException {
        String keyValue = entry.getKey();
        if (keyValue.equalsIgnoreCase("name")) {
            symbolizer.setName((String) entry.getValue());
        } else if (keyValue.equalsIgnoreCase("geometry")) {
            String geometry = (String) entry.getValue();
            if (geometry == null) {
                throw new ParameterException("The geometry field element cannot be null");
            }
            symbolizer.setGeometryParameter(new GeometryParameter(geometry));
        } else if (keyValue.equalsIgnoreCase("description")) {
            symbolizer.setDescription(iterateDescription((Map<String, Object>) entry.getValue()));
        } else if (keyValue.equalsIgnoreCase("level")) {
            symbolizer.setLevel((int) entry.getValue());
        } else if (keyValue.equalsIgnoreCase("uom")) {
            Uom uom = Uom.isSupported((String) entry.getValue());
            if (uom != null) {
                symbolizer.setUom(uom);
            } else {
                throw new ParameterException("Unsupported uom value");
            }
        }
    }

    private void iterateAreaSymbolizer(Map<String, Object> map, Feature2DRule rule, AreaSymbolizer areaSymbolizer) throws ParameterException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            iterateSymbolizerMetadata(entry, areaSymbolizer);
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("solidfill")) {
                areaSymbolizer.setFill(buildSolidFill((Map<String, Object>) entry.getValue()));
            } else if (keyValue.equalsIgnoreCase("penstroke")) {
                areaSymbolizer.setStroke(buildPenStroke((Map<String, Object>) entry.getValue()));
            }
        }
    }

    private void iterateLineSymbolizer(Map<String, Object> map, Feature2DRule rule, LineSymbolizer lineSymbolizer) throws ParameterException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            iterateSymbolizerMetadata(entry, lineSymbolizer);
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("penstroke")) {
                lineSymbolizer.setStroke(buildPenStroke((Map<String, Object>) entry.getValue()));
            }
        }
    }

    private Description iterateDescription(Map<String, Object> map) {

        return null;
    }

    private void initSymbolizerMetadata(IFeatureSymbolizer symbolizer) {
        symbolizer.setName("Default name");
        symbolizer.setUom(Uom.PX);
        symbolizer.setLevel(0);
    }

    private ParameterValue buildParameterValue(Map.Entry<String, Object> entry) throws ParameterException {
        if (entry.getValue() instanceof Map) {
            return buildExpression((Map<String, Object>) entry.getValue());
        } else if (entry.getValue() instanceof Double) {
            return new Literal((Double) entry.getValue());
        } else if (entry.getValue() instanceof Float) {
            return new Literal((Float) entry.getValue());
        } else if (entry.getValue() instanceof String) {
            return new Literal((String) entry.getValue());
        }else {
            throw new ParameterException("Unknow type of value");
        }
    }

    private SolidFill buildSolidFill(Map<String, Object> map) throws ParameterException {
        SolidFill solidFill = new SolidFill();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("color")) {
                solidFill.setColor(buildParameterValue(entry));
            } else if (keyValue.equalsIgnoreCase("opacity")) {
                solidFill.setOpacity(buildParameterValue(entry));
            }
        }
        return solidFill;
    }

    private Expression buildExpression(Map<String, Object> map) throws ParameterException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("expression")) {
                return new Expression((String) entry.getValue());
            } else {
                throw new ParameterException("Unknow expression type");
            }
        }
        throw new ParameterException("Unknow expression type");
    }

    private PenStroke buildPenStroke(Map<String, Object> map) throws ParameterException {
        PenStroke penStroke = new PenStroke();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyValue = entry.getKey();
            if (keyValue.equalsIgnoreCase("solidfill")) {
                penStroke.setFill(buildSolidFill((Map<String, Object>) entry.getValue()));
            } else if (keyValue.equalsIgnoreCase("width")) {
                penStroke.setWidth(buildParameterValue(entry));
            } else if (keyValue.equalsIgnoreCase("linecap")) {
                penStroke.setLineCap(ParameterValueHelper.lookup(PenStroke.LineCap.class, (String) entry.getValue()));
            } else if (keyValue.equalsIgnoreCase("linejoin")) {
                penStroke.setLineJoin(ParameterValueHelper.lookup(PenStroke.LineJoin.class, (String) entry.getValue()));
            } else if (keyValue.equalsIgnoreCase("dasharray")) {
                penStroke.setDashArray(buildParameterValue(entry));
            } else if (keyValue.equalsIgnoreCase("dashoffset")) {
                penStroke.setDashOffset(buildParameterValue(entry));
            }
        }
        return penStroke;
    }

}
