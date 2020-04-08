/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle;

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
import org.orbisgis.style.Uom;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
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
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerOffset(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 5);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerGeometryExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerGeometryExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerStyleColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createLineSymbolizer(Color.BLACK, 1, 0, Uom.PX);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTwoLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("hedgerow2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createTwoLineSymbolizers();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineSymbolizerOffset(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createLineSymbolizer(Color.green, 1, 5, Uom.PX);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineSymbolizerSizeExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createStyleWithLineSymbolizerSizeExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTextSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createTextSymbolizer();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTextSymbolizerColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createTextSymbolizerColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testTextSymbolizerFieldName(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createTextSymbolizerWithField();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatched(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createHatchedAreaSymbolizer(Color.YELLOW, 2, 45, 10, Color.BLACK, 1);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatchedColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerHatchedColorExpression("CASE WHEN TYPE='cereals' THEN '#ff6d6d' ELSE '#6d86ff' END", 2, 45, 10);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerRuleExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerRuleExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerDotFill(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerDotFillStyle();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerHatchDensityFillColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerHatchDensityFillColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerGraphicFillColor(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerGraphicFillColor();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerMarkDensityFillColorExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerMarkDensityFillColorExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createPointSymbolizer("circle", Color.yellow, 10, Color.BLACK, 1);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizerMarkGraphicSizeExpression(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createPointSymbolizerMarkGraphicSizeExpression();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testPointSymbolizerVertex(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerAndPointSymbolizerVertex(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerAndPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testAreaSymbolizerAndPointSymbolizerVertexEnvelope(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StylesForTest.createAreaSymbolizerAndPointSymbolizerVertex();
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

    @Test
    public void testSymbolsWithLevelEnvelope(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StylesForTest.createSymbolsWithLevel();
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

    @Test
    public void testDashedLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(100).getEnvelopeInternal();
        Feature2DStyle style = StylesForTest.createDashedLineSymbolizer(Color.yellow, 2, 0, "5 2");
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

    @Test
    public void testDashedAreaSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().getEnvelopeInternal();
        Feature2DStyle style = StylesForTest.createDashedAreaymbolizer(Color.yellow, 2, 0, "10");
        template(inputFile, testInfo.getDisplayName(), style, true, envelope);
    }

    @Test
    public void testGraphicStrokeLineSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("hedgerow2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createGraphicStrokeLineSymbolizer(Color.yellow, 2, 0, 10);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }
    
    @Test
    public void testGraphicStrokeLineSymbolizerNoOverlaps(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("hedgerow2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createGraphicStrokeLineSymbolizerNoOverlaps(Color.BLACK, 2, 0, 10);
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineLabelTextSymbolizer(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("hedgerow2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createLineLabelTextSymbolizer();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

    @Test
    public void testLineLabelTextSymbolizerArea(TestInfo testInfo) throws LayerException, IOException, URISyntaxException, InterruptedException, SQLException {
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        Feature2DStyle style = StylesForTest.createPointLabelTextSymbolizer();
        template(inputFile, testInfo.getDisplayName(), style, true, null);
    }

}
