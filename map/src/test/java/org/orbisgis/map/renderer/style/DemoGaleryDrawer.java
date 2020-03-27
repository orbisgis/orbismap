/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.factory.StyleFactory;

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

    public static void template(String inputFile, String title, Feature2DStyle style, boolean display, Envelope extent) throws LayerException, IOException, InterruptedException {
        ISpatialTable spatialTable = (ISpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);

        long draw = System.currentTimeMillis();
        System.out.println("Start drawing : " + title);

        StyledLayer layer = new StyledLayer(spatialTable);
        layer.setStyle(style);
        MapRenderer mapRenderer = new MapRenderer();
        mapRenderer.addLayer(layer);

        if (extent != null) {
            mapRenderer.setEnvelope(new MapEnvelope(extent));
        }

        mapRenderer.draw();

        System.out.println("Drawing : " + title + " in " + (System.currentTimeMillis() - draw));

        if (display) {
            mapRenderer.show();
        }
        File savePath = new File("./target/" + DemoGaleryDrawer.class.getSimpleName() + "_" + title);
        ImageIO.write(mapRenderer.getImage(), "png", savePath);

    }

    @Test
    public void testAreaSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizer(Color.yellow, 1, 0);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }
    
      @Test
    public void testAreaSymbolizerOffset(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizer(Color.yellow, 1, 5);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerGeometryExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerGeometryExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerStyleColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createLineSymbolizer(Color.BLACK, 1,0);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }
    @Test
    public void testLineSymbolizerOffset(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createLineSymbolizer(Color.green, 1, 5);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineSymbolizerSizeExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createLineSymbolizerSizeExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTextSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createTextSymbolizer();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }
    
    

    @Test //TODO move fill to textsymbolizer
    public void testTextSymbolizerColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createTextSymbolizerColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTextSymbolizerFieldName(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createTextSymbolizerWithField();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatched(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createHatchedAreaSymbolizer(Color.YELLOW, 2, 45, 10, Color.BLACK, 1);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatchedColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerHatchedColorExpression("CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END",2, 45, 10);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerRuleExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerRuleExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerDotFill(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerDotFillStyle();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatchDensityFillColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerHatchDensityFillColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerGraphicFillColor(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerGraphicFillColor();
        template(inputFile,testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerMarkDensityFillColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerMarkDensityFillColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createPointSymbolizer("circle", Color.yellow, 2, Color.BLACK, 1);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizerMarkGraphicSizeExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createPointSymbolizerMarkGraphicSizeExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizerVertex(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerAndPointSymbolizerVertex(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerAndPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerAndPointSymbolizerVertexEnvelope(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StyleFactory.createAreaSymbolizerAndPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

    @Test
    public void testSymbolsWithLevelEnvelope(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StyleFactory.createSymbolsWithLevel();
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }
    
    @Test
    public void testDashedLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StyleFactory.createDashedLineSymbolizer(Color.yellow, 2, 0, "5 2");
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }
    
    @Test
    public void testDashedAreaSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().getEnvelopeInternal();
        Feature2DStyle style = StyleFactory.createDashedAreaymbolizer(Color.yellow, 2, 0, "10");
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

}
