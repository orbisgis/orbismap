/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.StyleFactory;

/**
 *
 * @author Erwan Bocher
 */
public class Writer {

    private final Feature2DStyle fds;

    public Writer(Feature2DStyle fds) {
        this.fds = fds;
    }

    void write(File file) throws Exception {
        
        if(fds!=null){
        JsonEncoding jsonEncoding = JsonEncoding.UTF8;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(new BufferedOutputStream(fos), jsonEncoding);
            
            String title = fds.getTitle(Locale.getDefault());
            String name = fds.getName();
            String resum = fds.getAbstract(Locale.getDefault());
            
            // header of the file
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("style", "Feature2DStyle");            
            jsonGenerator.writeStringField("title", title);
            jsonGenerator.writeStringField("name", name);
            jsonGenerator.writeStringField("abstract", resum);
            
            List<Feature2DRule> rules = fds.getRules();
            for (Feature2DRule rule : rules) {                
                String ruleName = rule.getName();
                jsonGenerator.writeArrayFieldStart("rule");
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("name", ruleName);
                
                jsonGenerator.writeEndObject();
                jsonGenerator.writeEndArray();
                
            }
                            
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
     */
    public static void main(String[] args) throws Exception {

        Feature2DStyle style = StyleFactory.createAreaSymbolizerStyle();
        Writer writer = new Writer(style);
        writer.write(new File("/tmp/mysld.json"));
       
    }
}
