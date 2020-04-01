/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.io;

import java.awt.Color;
import java.io.File;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.orbisgis.style.Feature2DStyle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.style.factory.StyleFactory;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class Feature2DStyleWriterReaderTest {
    
    @Test
    public void writeReadPointSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createPointSymbolizer("circle", Color.yellow, 1, Color.yellow, 0));
    }

    @Test
    public void writeReadLineSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createLineSymbolizer(Color.BLACK, 1, 0));
    } 
    
    @Test
    public void writeReadAreaSymbolizerGeometryExpression(TestInfo testInfo) throws  Exception  {      
       writeReadTest(testInfo.getDisplayName(), StyleFactory.createAreaSymbolizerGeometryExpression());        
    } 
    
    
     @Test
    public void writeReadAreaSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createAreaSymbolizer(Color.yellow, 1, 0));
    }
    
    public static void writeReadTest(String testName, Feature2DStyle inputStyle) throws Exception{
        File outputStyleFile = new File("./target/"+testName+".se");
        if(outputStyleFile==null){
            throw new IllegalArgumentException("The output file to save the json style cannot be null");
        }
        if(inputStyle==null){
            throw new IllegalArgumentException("The input style cannot be null");
        }
        if (outputStyleFile.exists()) {
            outputStyleFile.delete();
        }
        Feature2DStyleWriter.toXML(inputStyle, outputStyleFile);
        
        /*Feature2DStyleWriter feature2DStyleWriter = new Feature2DStyleWriter(inputStyle);
        feature2DStyleWriter.write(outputStyleFile);
        assertTrue(outputStyleFile.exists());
        Feature2DStyleReader feature2DStyleReader = new Feature2DStyleReader(outputStyleFile);
        Feature2DStyle output_fds = feature2DStyleReader.read();
        CompareStyleVisitor cp = new CompareStyleVisitor();
        cp.visitSymbolizerNode(inputStyle, output_fds);*/
    }

}
