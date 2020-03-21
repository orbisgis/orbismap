/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.IFill;
import org.orbisgis.style.StyleFactory;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.fill.DotMapFill;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.ExternalGraphic;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.PointTextGraphic;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.stroke.GraphicStroke;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.stroke.TextStroke;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;

/**
 *
 * @author Erwan Bocher
 */
public class Feature2DStyleWriter {

    private final Feature2DStyle fds;

    public Feature2DStyleWriter(Feature2DStyle fds) {
        this.fds = fds;
    }

    void write(File file) throws Exception {
        if (fds != null) {
            JsonEncoding jsonEncoding = JsonEncoding.UTF8;

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                JsonFactory jsonFactory = new JsonFactory();
                JsonGenerator jsonGenerator = jsonFactory.createGenerator(new BufferedOutputStream(fos), jsonEncoding);

                String title = fds.getTitle(Locale.getDefault());
                String name = fds.getName();
                String resum = fds.getAbstract(Locale.getDefault());

                // header of the Symbology JSON file
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("Style", "Feature2DStyle");
                writeFieldNotNull(jsonGenerator, "Name", name);
                writeFieldNotNull(jsonGenerator, "Title", title);
                writeFieldNotNull(jsonGenerator, "Abstract", resum);
                List<Feature2DRule> rules = fds.getRules();
                jsonGenerator.writeFieldName("rules");
                jsonGenerator.writeStartArray();
                for (Feature2DRule rule : rules) {
                    writeRule(jsonGenerator, rule);
                }
                jsonGenerator.writeEndArray();
                jsonGenerator.writeEndObject();
                jsonGenerator.flush();
                jsonGenerator.close();

            } catch (FileNotFoundException ex) {
                throw new Exception(ex);

            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException ex) {
                    throw new Exception(ex);
                }
            }
        }

    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        Feature2DStyle style = StyleFactory.createAreaSymbolizerStyle();
        style = StyleFactory.createLineSymbolizerStyle();
        style = StyleFactory.createPointSymbolizerStyle();
        style=createAreaSymbolizerStyleColorExpression();
        Feature2DStyleWriter writer = new Feature2DStyleWriter(style);
        writer.write(new File("/tmp/mysld.json"));

    }

    /**
     * Write a <code>Feature2DRule</code>
     *
     * @param jsonGenerator
     * @param rule
     * @throws IOException
     */
    private void writeRule(JsonGenerator jsonGenerator, Feature2DRule rule) throws IOException, ParameterException {
        jsonGenerator.writeStartObject();
        writeFieldNotNull(jsonGenerator, "Name", rule.getName());
        writeFieldNotNull(jsonGenerator, "Title", rule.getDescription().getTitle(Locale.getDefault()));
        writeFieldNotNull(jsonGenerator, "Abstract", rule.getDescription().getAbstract(Locale.getDefault()));
        for (IFeatureSymbolizer symbolizer : rule.getSymbolizers()) {
            writeSymbolizer(jsonGenerator, symbolizer);
        }
        jsonGenerator.writeEndObject();
    }

    private void writeSymbolizer(JsonGenerator jsonGenerator, IFeatureSymbolizer symbolizer) throws IOException, ParameterException {
        if (symbolizer != null) {
            if (symbolizer instanceof AreaSymbolizer) {
                writeAreaSymbolizer(jsonGenerator, (AreaSymbolizer) symbolizer);
            } else if (symbolizer instanceof LineSymbolizer) {
                writeLineSymbolizer(jsonGenerator, (LineSymbolizer) symbolizer);
            } else if (symbolizer instanceof PointSymbolizer) {
                writePointSymbolizer(jsonGenerator, (PointSymbolizer) symbolizer);
            } else if (symbolizer instanceof TextSymbolizer) {
                writeTextSymbolizer(jsonGenerator, (TextSymbolizer) symbolizer);
            }
        }
    }

    

    private void writeSymbolizerMetadata(JsonGenerator generator, IFeatureSymbolizer symbolizer) throws IOException, ParameterException {
        writeFieldNotNull(generator, "Name", symbolizer.getName());
        writeFieldNotNull(generator, "Title", symbolizer.getDescription().getTitle(Locale.getDefault()));
        writeFieldNotNull(generator, "Abstract", symbolizer.getDescription().getAbstract(Locale.getDefault()));
        writeFieldNotNull(generator, "Uom", symbolizer.getUom().toString());
        writeFieldNotNull(generator, "Geometry", symbolizer.getGeometryParameter().getExpression());
        writeFieldNotNull(generator, "Level", symbolizer.getLevel());
    }

    private void writeAreaSymbolizer(JsonGenerator generator, AreaSymbolizer symbolizer) throws IOException, ParameterException {
        generator.writeFieldName("AreaSymbolizer");
        generator.writeStartObject(); // Start with left brace i.e. {        
        writeSymbolizerMetadata(generator, symbolizer);
        writeFill(generator, symbolizer.getFill());
        writeStroke(generator, symbolizer.getStroke());
        generator.writeEndObject();

    }

    private void writeFieldNotNull(JsonGenerator generator, String fieldName, Object value, boolean mandatory) throws IOException, ParameterException {
        if (value != null) {
            generator.writeObjectField(fieldName, value);
        } else {
            if (mandatory) {
                throw new ParameterException("The style element " + fieldName + " cannot be null");
            }
        }
    }
    
   private void writeParameterValue(JsonGenerator generator, String fieldName, ParameterValue parameterValue) throws IOException, ParameterException {
                writeParameterValue(generator, fieldName, parameterValue, false);
   }
    
   private void writeParameterValue(JsonGenerator generator, String fieldName, ParameterValue parameterValue, boolean mandatory) throws IOException, ParameterException {
       if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
           if(parameterValue instanceof Expression){
               generator.writeFieldName(fieldName);
               generator.writeStartObject(); // Start with left brace i.e. {  
               generator.writeObjectField("Expression", ((Expression) parameterValue).getExpression());
               generator.writeEndObject();               
           }else{
               generator.writeObjectField(fieldName, parameterValue.getValue());
           }
        } else {
            if (mandatory) {
                throw new ParameterException("The style element " + fieldName + " cannot be null");
            }
        }
   }

    private void writeFieldNotNull(JsonGenerator generator, String fieldName, Object value) throws IOException, ParameterException {
        writeFieldNotNull(generator, fieldName, value, false);
    }

    private void writeFill(JsonGenerator generator, IFill fill) throws IOException, ParameterException {
        if (fill != null) {
            if (fill instanceof SolidFill) {
                writeSolidFill(generator, (SolidFill) fill);
            } else if (fill instanceof DensityFill) {
                writeDensityFill(generator, (DensityFill) fill);
            } else if (fill instanceof DotMapFill) {
                writeDotMapFill(generator, (DotMapFill) fill);
            } else if (fill instanceof HatchedFill) {
                writeHatchedFill(generator, (HatchedFill) fill);
            } else if (fill instanceof GraphicFill) {
                writeGraphicFill(generator, (GraphicFill) fill);
            }
        }
    }

    private void writeStroke(JsonGenerator generator, Stroke stroke) throws IOException, ParameterException {
        if(stroke!=null){
        if (stroke instanceof PenStroke) {
            writePenStroke(generator, (PenStroke) stroke);
        } else if (stroke instanceof GraphicStroke) {
            writeGraphicStroke(generator, (GraphicStroke) stroke);
        } else if (stroke instanceof TextStroke) {
            writeTextStroke(generator, (TextStroke) stroke);
        }
        }
    }

    private void writeSolidFill(JsonGenerator generator, SolidFill fill) throws IOException, ParameterException {
        generator.writeFieldName("SolidFill");
        generator.writeStartObject(); // Start with left brace i.e. {        
        writeParameterValue(generator, "Color", fill.getColor());
        writeParameterValue(generator, "Opacity", fill.getOpacity());
        generator.writeEndObject();
    }

    private void writePenStroke(JsonGenerator generator, PenStroke penStroke) throws IOException, ParameterException {
        generator.writeFieldName("PenStroke");
        generator.writeStartObject(); // Start with left brace i.e. {    
        IFill fill = penStroke.getFill();
        if (fill != null) {
            writeFill(generator, fill);
        }
        writeParameterValue(generator, "Width", penStroke.getWidth());
        writeFieldNotNull(generator, "LineCap", penStroke.getLineCap().toString());
        writeFieldNotNull(generator, "LineJoin", penStroke.getLineJoin().toString());
        writeParameterValue(generator, "DashArray", penStroke.getDashArray());
        writeParameterValue(generator, "DashOffset", penStroke.getDashOffset());
        generator.writeEndObject();
    }

    private void writeGraphicStroke(JsonGenerator generator, GraphicStroke graphicStroke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeTextStroke(JsonGenerator generator, TextStroke textStroke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeLineSymbolizer(JsonGenerator generator, LineSymbolizer symbolizer) throws IOException, ParameterException {
        generator.writeFieldName("LineSymbolizer");
        generator.writeStartObject(); // Start with left brace i.e. {        
        writeSymbolizerMetadata(generator, symbolizer);
        writeStroke(generator, symbolizer.getStroke());
        generator.writeEndObject();
    }

    private void writePointSymbolizer(JsonGenerator generator, PointSymbolizer symbolizer) throws IOException, ParameterException {
        generator.writeFieldName("PointSymbolizer");
        generator.writeStartObject(); // Start with left brace i.e. {        
        writeSymbolizerMetadata(generator, symbolizer);
        writeGraphic(generator, symbolizer.getGraphic());
        generator.writeEndObject();

    }

    private void writeTextSymbolizer(JsonGenerator jsonGenerator, TextSymbolizer textSymbolizer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeDensityFill(JsonGenerator generator, DensityFill densityFill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeDotMapFill(JsonGenerator generator, DotMapFill dotMapFill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeHatchedFill(JsonGenerator generator, HatchedFill hatchedFill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeGraphicFill(JsonGenerator generator, GraphicFill graphicFill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeGraphic(JsonGenerator generator, Graphic graphic) throws IOException, ParameterException {
        if(graphic!=null){
        if (graphic instanceof MarkGraphic) {
            writeMarkGraphic(generator, (MarkGraphic) graphic);
        } else if (graphic instanceof PointTextGraphic) {

        } else if (graphic instanceof ExternalGraphic) {

        }
        }
    }

    private void writeMarkGraphic(JsonGenerator generator, MarkGraphic symbolizer) throws IOException, ParameterException {
        generator.writeFieldName("MarkGraphic");
        generator.writeStartObject(); // Start with left brace i.e. {    
        writeParameterValue(generator, "WellKnownName", symbolizer.getWkn());
        writeStroke(generator, symbolizer.getStroke());
        writeFill(generator, symbolizer.getFill());
        writeViewBox(generator, symbolizer.getViewBox());
        generator.writeEndObject();
    }

    private void writeViewBox(JsonGenerator generator, ViewBox viewBox) throws IOException, ParameterException {
        if (viewBox != null) {
            generator.writeFieldName("ViewBox");
            generator.writeStartObject(); // Start with left brace i.e. {    
            writeParameterValue(generator, "Width", viewBox.getWidth());
            writeParameterValue(generator, "Height", viewBox.getHeight());
            generator.writeEndObject();
        }
    }    
    
    /**
     * Create a style with one <code>AreaSymbolizer</code> and a SolidFill color
     * expression
     *
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerStyleColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        Expression colorExpression = new Expression(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        Literal opacity = new Literal(1f);
        SolidFill solidFill = new SolidFill(colorExpression, opacity);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        SolidFill psFill = new SolidFill(Color.BLUE);
        ps.setFill(psFill);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

}
