package org.orbisgis.orbismap.style;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.LineSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.PointSymbolizer;

public class StyleFactory {


    /**
     * Create a default PointSymbolizer
     * @return
     */
    public static  Feature2DStyle createPointSymbolizer(){
        Feature2DStyle style = new Feature2DStyle();
        PointSymbolizer ps = new PointSymbolizer();
        ps.initDefault();
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(ps);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a default LineSymbolizer
     * @return
     */
    public static  Feature2DStyle createLineSymbolizer(){
        Feature2DStyle style = new Feature2DStyle();
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.initDefault();
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(lineSymbolizer);
        style.addRule(rule);
        return style;
    }

    /**
     * Create a default AreaSymbolizer
     * @return
     */
    public static  Feature2DStyle createAreaSymbolizer(){
        Feature2DStyle style = new Feature2DStyle();
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer();
        areaSymbolizer.initDefault();
        Feature2DRule rule = new Feature2DRule();
        rule.addSymbolizer(areaSymbolizer);
        style.addRule(rule);
        return style;
    }

}
