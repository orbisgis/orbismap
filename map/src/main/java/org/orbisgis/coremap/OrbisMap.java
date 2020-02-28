/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap;

import java.io.File;
import org.orbisgis.coremap.renderer.MapRenderer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.coremap.layerModel.StyledLayer;
import org.orbisgis.style.SeExceptions;
import org.orbisgis.style.StyleFactory;
import org.orbisgis.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS;
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
        
        runISpatialRenderer();      
    }
    
   

    private static void runISpatialRenderer() throws LayerException, SQLException {
   //Data
        String inputFile = "/home/ebocher/Autres/data/jgb/landcover2000.shp";
        inputFile ="/home/ebocher/Autres/data/IGN/data_cadastre/parc_dgi/Parc_dgi.shp";
        //inputFile = "/home/ebocher/Autres/data/admin/communes.shp";
        //inputFile="/home/ebocher/Autres/data/DONNEES RENNES/Reseau_Rennes.shp";    
        //inputFile = "/home/ebocher/Autres/data/admin/cantons.shp";
        
        String stylePath ="/home/ebocher/Autres/codes/orbismap/map/src/test/resources/org/orbisgis/coremap/renderer/se/symbol_prop_canton_interpol_sqrt.se";
        
        //stylePath ="/tmp/routes.se";
        
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/" + OrbisMap.class.getName());
        H2GIS h2GIS = H2GIS.open(map);        
        long draw = System.currentTimeMillis();
        ISpatialTable spatialTable = (ISpatialTable) h2GIS.link(new File(inputFile), "LANDCOVER", true);
        
        
        //h2GIS.execute("create spatial index on LANDCOVER(THE_GEOM)");
        //ISpatialTable spatialTable = h2GIS.getSpatialTable("LANDCOVER");
        StyledLayer layer = new StyledLayer(spatialTable);
        layer.setStyle(StyleFactory.createAreaSymbolizerStyle());
        //layer.setStyle(StyleFactory.createLineSymbolizerStyle());
        //layer.setStyle(StyleFactory.createAreaSymbolizerStyle(layer));
        
        //layer.setStyle(new Style(layer,stylePath));
        
        //ISpatialTable spatialTable2 = (ISpatialTable) h2GIS.link(new File(inputFile2), "LANDCOVER2", true);

        //Layer layer2 = new Layer(spatialTable2);
        //layer2.setStyle(StyleFactory.createLineSymbolizerStyle(layer));

        MapRenderer mapRenderer = new MapRenderer();
        mapRenderer.addLayer(layer);
        //mapRenderer.addLayer(layer2);
        //mapRenderer.setEnvelope(layer.getEnvelope());


        mapRenderer.draw();        
       

        System.out.println("Drawing : " + (System.currentTimeMillis() - draw));
        
        mapRenderer.show();

        //renderer.draw(img, effectiveExtent , layer, new NullProgressMonitor());
        long end = System.currentTimeMillis();
        /*mapRenderer.save("/tmp/orbisgis_carte.png");

        System.out.println("Save file : " + (System.currentTimeMillis() - end));*/
    }
    
}
