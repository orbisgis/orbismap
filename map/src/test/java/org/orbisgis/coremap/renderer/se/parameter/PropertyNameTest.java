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
package org.orbisgis.coremap.renderer.se.parameter;

import groovy.lang.Closure;
import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orbisgis.coremap.renderer.se.parameter.real.RealAttribute;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.string.StringAttribute;
import org.orbisgis.coremap.renderer.se.parameter.string.StringParameter;


import static org.junit.Assert.*;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 *
 * @author Maxence Laurent
 */
public class PropertyNameTest {

    private static H2GIS h2GIS;

    @BeforeClass
    public static void tearUpClass() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/"+PropertyNameTest.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }

    // Data to test
    File src = new File(PropertyNameTest.class.getResource("../../../../../../data/landcover2000.shp").getFile());

    /**
     * Test of getValue method, of class StringAttribute.
     * @throws Exception
     */
    @Test
    public void testRealAttribute() throws Exception {
        String tableName = "LANDCOVER";
        h2GIS.load(src.toURI(), tableName, true);
        try (ResultSet rs = h2GIS.getConnection().createStatement().executeQuery("select * from " + tableName)) {
            RealParameter real = new RealAttribute("runoff_win");
            assertTrue(real.getValue(rs, 1) == 0.05);
            assertTrue(real.getValue(rs, 51) == 0.4);
            assertTrue(real.getValue(rs, 1222) == 0.4);
        } finally {
            h2GIS.execute("DROP TABLE if exists "+ tableName);
        }
    }

    /**
     * Test of getValue method, of class StringAttribute.
     * @throws Exception
     */
    @Test
    public void testStringAttribute() throws Exception {
        String tableName = "LANDCOVER";
        h2GIS.load(src.toURI(), tableName, true);
        try (ResultSet rs = h2GIS.getConnection().createStatement().executeQuery("select * from " + tableName)) {
            StringParameter string = new StringAttribute("type");
            assertTrue(string.getValue(rs, 41).equals("grassland"));
            assertTrue(string.getValue(rs, 48).equals("corn"));
            assertTrue(string.getValue(rs, 57).equals("vegetables"));
        } finally {
            h2GIS.execute("DROP TABLE IF EXISTS "+ tableName);
        }
    }
    

}
