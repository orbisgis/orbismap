/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.feature2dstyle.json;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.orbisgis.style.Feature2DStyle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.style.factory.StyleFactory;
import org.orbisgis.style.visitor.CompareStyleVisitor;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class Feature2DStyleWriterReaderTest {
    
    @Test
    public void writeReadPointSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createPointSymbolizer());
    }

    @Test
    public void writeReadLineSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createLineSymbolizer());
    }
    
    
    
     @Test
    public void writeReadAreaSymbolizer(TestInfo testInfo) throws Exception {      
        writeReadTest(testInfo.getDisplayName(), StyleFactory.createAreaSymbolizer());
    }
    
    public static void writeReadTest(String testName, Feature2DStyle inputStyle) throws Exception{
        File outputStyleFile = new File("./target/"+testName+".json");
        if(outputStyleFile==null){
            throw new IllegalArgumentException("The output file to save the json style cannot be null");
        }
        if(inputStyle==null){
            throw new IllegalArgumentException("The input style cannot be null");
        }
        if (outputStyleFile.exists()) {
            outputStyleFile.delete();
        }
        Feature2DStyleWriter feature2DStyleWriter = new Feature2DStyleWriter(inputStyle);
        feature2DStyleWriter.write(outputStyleFile);
        assertTrue(outputStyleFile.exists());
        Feature2DStyleReader feature2DStyleReader = new Feature2DStyleReader(outputStyleFile);
        Feature2DStyle output_fds = feature2DStyleReader.read();
        CompareStyleVisitor cp = new CompareStyleVisitor();
        cp.visitSymbolizerNode(inputStyle, output_fds);
    }

}
