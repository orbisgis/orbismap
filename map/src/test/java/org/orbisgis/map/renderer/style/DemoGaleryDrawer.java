/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.layerModel.MapEnvelope;
import org.orbisgis.map.layerModel.StyledLayer;
import org.orbisgis.map.renderer.MapRenderer;
import org.orbisgis.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS;
import org.orbisgis.style.Feature2DStyle;
import org.osgi.service.jdbc.DataSourceFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ebocher
 */
public class DemoGaleryDrawer {

    private static H2GIS h2GIS;
    
    @BeforeAll
    public static void tearUpClass() throws Exception {
       Map<String, String> map = new HashMap<>();
       map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/" + DemoGaleryDrawer.class.getSimpleName());
       h2GIS = H2GIS.open(map);
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }    

    public void template(String inputFile, String title, Feature2DStyle style, boolean display, Envelope extent) throws LayerException, IOException, InterruptedException {
            ISpatialTable spatialTable = (ISpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
            
            long draw = System.currentTimeMillis();
            System.out.println("Start drawing : "+ title);
       
            StyledLayer layer = new StyledLayer(spatialTable);
            layer.setStyle(style);
            MapRenderer mapRenderer = new MapRenderer();           
            mapRenderer.addLayer(layer);
            
            if(extent!=null){
                mapRenderer.setEnvelope(new MapEnvelope(extent));
            }

            mapRenderer.draw();

            System.out.println("Drawing : " + title+ " in "+ (System.currentTimeMillis() - draw));

            if(display){
                mapRenderer.show();
            }
            File savePath = new File("./target/" + DemoGaleryDrawer.class.getSimpleName()+"_"+title);
            ImageIO.write(mapRenderer.getImage(), "png", savePath);
            
    }
    
    @Test
    public void testAreaSymbolizer() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerStyle();
        this.template(inputFile, "AreaSymbolizer", style, false, null);        
    }
    
     @Test
    public void testAreaSymbolizerGeometryExpression() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerGeometryExpression();
        this.template(inputFile, "AreaSymbolizerGeometryExpression", style, false, null);        
    }

    @Test
    public void testAreaSymbolizerColorExpression() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerStyleColorExpression();
        this.template(inputFile, "AreaSymbolizerColorExpression", style, true, null);
    }

    @Test
    public void testLineSymbolizer() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createLineSymbolizerStyle();
        this.template(inputFile, "LineSymbolizer", style, true, null);
    }

    @Test
    public void testTextSymbolizer() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createTextSymbolizer();
        this.template(inputFile, "TextSymbolizer", style, true, null);
    }
    
    @Test //TODO move fill to textsymbolizer
    public void testTextSymbolizerColorExpression() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createTextSymbolizerColorExpression();
        this.template(inputFile, "TextSymbolizerColorExpression", style, true, null);
    }

    @Test
    public void testTextSymbolizerFieldName() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createTextSymbolizerWithField();
        this.template(inputFile, "TextSymbolizerFieldName", style, true, null);
    }
    
    @Test
    public void testAreaSymbolizerHatched() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();        
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerHatched();
        this.template(inputFile, "AreaSymbolizerHatched", style, false, null);        
    }
    
    @Test
    public void testAreaSymbolizerHatchedColorExpression() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerHatchedColorExpression();
        this.template(inputFile, "AreaSymbolizerHatchedColorExpression", style, true, null);
    }
    
    @Test
    public void testAreaSymbolizerRuleExpression() throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactoryTest.createAreaSymbolizerRuleExpression();
        this.template(inputFile, "AreaSymbolizerRuleExpression", style, true, null);
    }
          
    
}
