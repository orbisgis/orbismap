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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.legend;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
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