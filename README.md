# OrbisMap

OrbisMap is POC about the SymCore specification implementation.
OrbisMap intends to propose a Feature 2D Style model with two encodings in JSON and XML.
The basic rendering engine called MapView is implemented to draw the symbology elements describe in a Feature2DStyle model.

Note that OrbisMap is under active development so the API is moving.


If you want to test the feature style model and its capabilities, run the folowing code in a Groovy Console.


```
@GrabResolver(name='orbisgis', root='https://nexus.orbisgis.org/repository/orbisgis/')
@Grab(group='org.orbisgis.orbismap', module='map', version='0.0.1-SNAPSHOT')


import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.map.renderer.MapView
import org.orbisgis.orbismap.map.layerModel.StyledLayer
import java.awt.Color;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.color.HexaColor;

        //H2GIS in memory database to manage data
        def dbPath = "mem:"
        H2GIS h2gis = H2GIS.open(dbPath);
        def spatialTable = h2gis.link("polygons.shp","mydata", true)
        //Renderer
        def mapView = new MapView()
        //Create a feature style
        Feature2DStyle style = createAreaSymbolizerStyleColorExpression();
        //Link the style with the data in a StyledLayer object
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        //Add the style to the renderer
        mapView << styledLayer
        //Draw and show it in a JFrame
        mapView.draw();
        mapView.show();
        
        /**
        * A basic style AreaSymbolizer style
        **/
        def createStyle(){
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.initDefault();
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
        }
        
        /**
        * An AreaSymbolizer style with a
        * color expression
        **/
       def createAreaSymbolizerStyleColorExpression() {
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        Expression colorExpression = new Expression(""
                + "CASE WHEN ST_AREA(THE_GEOM)> 10000 THEN '#ff6d6d' ELSE '#6d86ff' END");
        SolidFill solidFill = new SolidFill();
        HexaColor hexaColor = new HexaColor();
        hexaColor.setHexaColor(colorExpression);
        solidFill.setColor(hexaColor);
        solidFill.setOpacity(1f);
        areaSymbolizer.setFill(solidFill);
        PenStroke ps = new PenStroke();
        ps.setWidth(1.0f);
        SolidFill solidFill_ps = new SolidFill();
        solidFill_ps.setColor(Color.BLUE);                
        ps.setFill(solidFill_ps);
        areaSymbolizer.setStroke(ps);
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style
    }
```
