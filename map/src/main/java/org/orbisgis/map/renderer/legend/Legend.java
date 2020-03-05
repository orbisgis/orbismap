/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.legend;

/**
 *
 * @author ebocher
 */
public class Legend {
    
    
    /*private void drawLegend(BufferedImage img, Rule r) {
    for (Symbolizer sym : r.getCompositeSymbolizer().getSymbolizerList()) {
      SimpleFeature feature = null;
      if (sym instanceof LineSymbolizer) {
        LineString line = drawer.line(new int[] { 1, 1, 10, 20, 20, 20 });
        feature = drawer.feature(line);
      } else if(sym instanceof AreaSymbolizer) {
        Polygon  p = drawer.polygon(new int[] { 1, 1, 1, 18, 18, 18, 18, 1, 1,1 });
        feature = drawer.feature(p);
      } else if(sym instanceof PointSymbolizer || sym instanceof TextSymbolizer) {
        Point p = drawer.point(10, 10);
        feature = drawer.feature(p);
      } 
      if(feature == null)
        continue;
      drawer.drawDirect(img, feature, r);
      Graphics2D gr = img.createGraphics();
      gr.setColor(Color.BLACK);
      if (r.getDescription() != null && r.getDescription().getTitle() != null) {
        gr.drawString(r.getDescription().getTitle().toString(), 20, 18);
      }
    }
  }*/
}