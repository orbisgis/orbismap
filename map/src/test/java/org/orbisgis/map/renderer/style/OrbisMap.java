/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.awt.Color;
import java.io.File;
import org.orbisgis.map.renderer.MapRenderer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.StyledLayer;
import org.orbisgis.style.SeExceptions;
import org.orbisgis.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IRule;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.ExpressionParameter;
import org.orbisgis.style.stroke.PenStroke;
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
       // inputFile = "/home/ebocher/Autres/data/IGN/data_cadastre/parc_dgi/Parc_dgi.shp";
        //inputFile = "/home/ebocher/Autres/data/admin/communes.shp";
        //Giant data
        //inputFile="/home/ebocher/Autres/data/DONNEES RENNES/Reseau_Rennes.shp";    
        //inputFile = "/home/ebocher/Autres/data/admin/cantons.shp";

        String stylePath = "/home/ebocher/Autres/codes/orbismap/map/src/test/resources/org/orbisgis/coremap/renderer/se/symbol_prop_canton_interpol_sqrt.se";

        //stylePath ="/tmp/routes.se";
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/" + OrbisMap.class.getName());
        H2GIS h2GIS = H2GIS.open(map);

        ISpatialTable spatialTable = (ISpatialTable) h2GIS.link(new File(inputFile), "LANDCOVER", true);
        long draw = System.currentTimeMillis();
        System.out.println("Start drawing ");
        //h2GIS.execute("create spatial index on LANDCOVER(THE_GEOM)");
        //ISpatialTable spatialTable = h2GIS.getSpatialTable("LANDCOVER");
        StyledLayer layer = new StyledLayer(spatialTable);
        //layer.setStyle(createAreaSymbolizerStyleColorExpression());
        //layer.setStyle(createLineSymbolizerStyle());
        layer.setStyle(createAreaSymbolizerHatchedStyle());
        //layer.setStyle(StyleFactoryTest.createAreaSymbolizerDotFillStyle());
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
    
    
    /**
     * Create a style with one <code>LineSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createLineSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        PenStroke ps = new PenStroke();
        ps.setFill(new SolidFill(Color.BLUE));
        lineSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }
    
     /**
     * Create a style with one <code>PointSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createPointSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        rule.addSymbolizer(new PointSymbolizer());
        return style;
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
        ExpressionParameter colorExpression = new ExpressionParameter(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 50000 THEN '#ff6d6d' ELSE '#6d86ff' END  ");
        ExpressionParameter opacity = new ExpressionParameter("1");
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
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        SolidFill solidFill = new SolidFill(Color.GREEN);
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
    
    /**
     * Create a style with one <code>AreaSymbolizer</code>
     * @return a  <code>Style</code>
     */
    public static Feature2DStyle createAreaSymbolizerHatchedStyle() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        HatchedFill hatchedFill = new HatchedFill();
        areaSymbolizer.setFill(hatchedFill);
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
