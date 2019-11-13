/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.coremap.layerModel.Layer;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.ImageRenderer;
import org.orbisgis.coremap.renderer.Renderer;
import org.orbisgis.coremap.renderer.se.SeExceptions;
import org.orbisgis.coremap.renderer.se.Style;
import org.orbisgis.coremap.renderer.se.StyleFactory;
import org.orbisgis.coremap.utils.progress.NullProgressMonitor;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 *
 * @author ebocher
 */
public class OrbisMap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LayerException, IOException, SQLException, SeExceptions.InvalidStyle {
        
        //Data
        String inputFile = "/home/ebocher/Autres/data/jgb/landcover2000.shp";
        inputFile ="/home/ebocher/Autres/data/IGN/data_cadastre/parc_dgi/Parc_dgi.shp";
        //inputFile="/home/ebocher/Autres/data/admin/communes.shp";
        //inputFile="/home/ebocher/Autres/data/DONNEES RENNES/Reseau_Rennes.shp";    
        inputFile = "/home/ebocher/Autres/data/admin/cantons.shp";
        
        String stylePath ="/home/ebocher/Autres/codes/orbismap/map/src/test/resources/org/orbisgis/coremap/renderer/se/symbol_prop_canton_interpol_sqrt.se";
        
        //stylePath ="/tmp/routes.se";
        
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/" + OrbisMap.class.getName());
        H2GIS h2GIS = H2GIS.open(map);
        ISpatialTable spatialTable = (ISpatialTable) h2GIS.load(new File(inputFile), "LANDCOVER", true);

        ILayer layer = new Layer(spatialTable);
        layer.addStyle(StyleFactory.createLineSymbolizerStyle(layer));
        layer.addStyle(new Style(layer,stylePath));

        MapRenderer mapRenderer = new MapRenderer();
        mapRenderer.addLayer(layer);

        long draw = System.currentTimeMillis();

        mapRenderer.draw();

        System.out.println("Drawing : " + (System.currentTimeMillis() - draw));

        //renderer.draw(img, effectiveExtent , layer, new NullProgressMonitor());
        long end = System.currentTimeMillis();
        mapRenderer.save("/tmp/orbisgis_carte.png");

        System.out.println("Save file : " + (System.currentTimeMillis() - end));

        
    }
    
    public static void testMap(){
        /*Map map = new Map();   //Default size     
        map.addLayer(new Layer(spatialTable, style));
        map.removeLayer();
        map.getLayers()
        map.draw();*/
        
    }
    
}
