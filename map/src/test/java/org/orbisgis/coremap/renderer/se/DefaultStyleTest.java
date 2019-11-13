/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.coremap.renderer.se;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.layerModel.MapContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.orbisgis.coremap.layerModel.Layer;
import org.orbisgis.coremap.layerModel.model.IMapContext;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * @author Nicolas Fortin
 */
public class DefaultStyleTest {

    private static H2GIS h2GIS;

    @BeforeClass
    public static void tearUpClass() throws Exception {        
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/"+DefaultStyleTest.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }

    //@Test
    public void LineStringDefaultStyle() throws Exception {
            h2GIS.execute("DROP TABLE IF EXISTS GTABLE");
            h2GIS.execute("CREATE TABLE GTABLE(the_geom GEOMETRY)");
            h2GIS.execute("INSERT INTO GTABLE VALUES ('POLYGON ((1 1, 3 3, 4 4, 1 1))')");
            h2GIS.execute("INSERT INTO GTABLE VALUES ('LINESTRING (1 1, 3 3)')");
        
        IMapContext mc = new MapContext();
        ILayer layer = new Layer(h2GIS.getSpatialTable("GTABLE"));
        assertEquals(1, layer.getStyles().size());
        assertTrue(layer.getStyle(0).getRules().get(0).getCompositeSymbolizer().getSymbolizerList().get(0) instanceof LineSymbolizer);
    }

   // @Test
    public void PolygonDefaultStyle() throws Exception {
            h2GIS.execute("DROP TABLE IF EXISTS GTABLE");
            h2GIS.execute("CREATE TABLE GTABLE(the_geom GEOMETRY)");
            h2GIS.execute("INSERT INTO GTABLE VALUES ('POLYGON ((1 1, 3 3, 4 4, 1 1))')");
        
        IMapContext mc = new MapContext();
        ILayer layer = new Layer(h2GIS.getSpatialTable("GTABLE"));
        
        assertEquals(1, layer.getStyles().size());
        assertTrue(layer.getStyle(0).getRules().get(0).getCompositeSymbolizer().getSymbolizerList().get(0) instanceof AreaSymbolizer);
    }

    //@Test
    public void MultiPointDefaultStyle() throws Exception {
            h2GIS.execute("DROP TABLE IF EXISTS GTABLE");
            h2GIS.execute("CREATE TABLE GTABLE(the_geom GEOMETRY)");
            h2GIS.execute("INSERT INTO GTABLE VALUES ('MULTIPOINT ((1 1))')");
            h2GIS.execute("INSERT INTO GTABLE VALUES ('MULTIPOINT ((1 1), (3 3), (4 4), (1 1))')");
        
        IMapContext mc = new MapContext();
        ILayer layer = new Layer(h2GIS.getSpatialTable("GTABLE"));
        
        assertEquals(1, layer.getStyles().size());
        Symbolizer symb = layer.getStyle(0).getRules().get(0).getCompositeSymbolizer().getSymbolizerList().get(0);
        assertTrue(symb instanceof PointSymbolizer);
        // Should draw all points
        assertTrue(((PointSymbolizer) symb).isOnVertex());
    }
}
