/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.orbismap.map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS;
import org.orbisgis.orbismap.map.api.LayerException;
import org.orbisgis.orbismap.map.layerModel.StyledLayer;
import org.orbisgis.orbismap.map.renderer.MapView;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_CAP;
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_JOIN;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;

/**
 *
 * @author ebocher
 */
public class Demo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, LayerException, IOException {
        System.out.println("Go");
        H2GIS h2GIS = H2GIS.open("./target/mapview");
        h2GIS.link(new File("/tmp/parc_dgi/Parc_dgi.shp"), "LANDCOVER", true);
        ISpatialTable spatialTable =h2GIS.getSpatialTable("LANDCOVER");
        MapView mapView = new MapView();
        Feature2DStyle style = createAreaSymbolizer(Color.yellow, 1, 0,Color.BLACK,1);
        StyledLayer styledLayer = new StyledLayer(spatialTable, style);
        mapView.addLayer(styledLayer);
        mapView.draw();
        //mapView.show();
        mapView.save("./target"+File.separator+ Demo.class.getSimpleName()+".png");
        System.out.println("End");
    }
    
     public static Feature2DStyle createAreaSymbolizer(Color fillColor, float opacity, double offset, Color strokeColor, float strokeWidth) {
        Feature2DStyle style = new Feature2DStyle();
        style.setName("Single symbol map");
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.setGeometryParameter("the_geom");
        areaSymbolizer.setFill(createSolidFill(fillColor, opacity));
        if (offset != 0) {
            areaSymbolizer.setPerpendicularOffset(new Literal(offset));
        }
        areaSymbolizer.setStroke(createPenStroke(strokeColor, strokeWidth));
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
     }

     /**
     * Create a <code>SolidFill</code>
     *
     * @param color
     * @param opacity
     * @return a  <code>SolidFill</code>
     */
    public static SolidFill createSolidFill(Color color, float opacity) {
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(color);
        solidFill.setOpacity(opacity);
        return solidFill;
    }
    
     /**
     * Create a standard PenStroke.
     *
     * @param color
     * @param width
     * @param uom
     * @return
     */
    public static PenStroke createPenStroke(Color color, float width, Uom uom) {
        PenStroke penStroke = new PenStroke();
        penStroke.setFill(createSolidFill(color,1.0f));
        penStroke.setWidth(new Literal(width));
        penStroke.setUom(uom);
        penStroke.setDashOffset(new NullParameterValue());
        penStroke.setDashArray(new NullParameterValue());
        penStroke.setLineCap(DEFAULT_CAP);
        penStroke.setLineJoin(DEFAULT_JOIN);
        return penStroke;
    }

    /**
     * Create a standard PenStroke in PX.
     *
     * @param color
     * @param width
     * @return
     */
    public static PenStroke createPenStroke(Color color, float width) {
        return createPenStroke(color, width, Uom.PX);
    }
    
}
