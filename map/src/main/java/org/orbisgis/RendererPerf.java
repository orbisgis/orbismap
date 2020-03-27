/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.api.LayerException;
import org.orbisgis.map.layerModel.MapEnvelope;
import org.orbisgis.map.layerModel.StyledLayer;
import org.orbisgis.map.renderer.MapRenderer;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcDataSource;
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS;
import org.orbisgis.orbisdata.datamanager.jdbc.postgis.POSTGIS;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.factory.StyleFactory;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 *
 * @author ebocher
 */
public class RendererPerf {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //Feature2DStyle style = StyleFactory.createLineSymbolizer(Color.yellow, 1, 0);
        Feature2DStyle style =  StyleFactory.createPointSymbolizer("square", Color.RED, 2, Color.BLACK, 1);
        //Feature2DStyle style = StyleFactory.createAreaSymbolizer(Color.yellow, 1, -10000);
        JdbcDataSource h2GIS = createDataSource(true);
        /*JdbcSpatialTable spatialTable = (JdbcSpatialTable) h2GIS.getSpatialTable("TMP_GEOFILE");
        spatialTable.where("limit 1");
        spatialTable.next();
        Envelope envelope = spatialTable.getGeometry().buffer(500).getEnvelopeInternal();*/
        template(h2GIS, "TMP_GEOFILE", "LineSymbolizer", style, true, null);
        //template(createDataSource(false), "BLOCK_INDICATORS", "LineSymbolizer", style, true, null);
    }

    public static void template(JdbcDataSource dataSource,String tableName, String title, Feature2DStyle style, boolean display, Envelope extent) throws LayerException, IOException, InterruptedException {
        ISpatialTable spatialTable = (ISpatialTable) dataSource.getSpatialTable(tableName);
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
        File savePath = new File("/tmp/" + RendererPerf.class.getSimpleName() + "_" + title);
        ImageIO.write(mapRenderer.getImage(), "png", savePath);

    }

    private static JdbcDataSource createDataSource(boolean isH2GIS) {
        if (isH2GIS) {
            Map<String, String> map = new HashMap<>();
            map.put(DataSourceFactory.JDBC_DATABASE_NAME, "/tmp/" + RendererPerf.class.getSimpleName());
            H2GIS h2GIS = H2GIS.open(map);
            String inputFile = "/home/ebocher/Autres/data/IGN/data_cadastre/parc_dgi/Parc_dgi.shp";
            //inputFile="/home/ebocher/Autres/data/DONNEES RENNES/Reseau_Rennes.shp";
            h2GIS.link(new File(inputFile), "TMP_GEOFILE", true);
            return h2GIS;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(DataSourceFactory.JDBC_DATABASE_NAME, "");
            map.put(DataSourceFactory.JDBC_URL, "jdbc:postgresql://ns380291.ip-94-23-250.eu/");
            map.put(DataSourceFactory.JDBC_USER, "erwan");
            map.put(DataSourceFactory.JDBC_PASSWORD, "d@nemark");
            return POSTGIS.open(map);
        }
    }
}
