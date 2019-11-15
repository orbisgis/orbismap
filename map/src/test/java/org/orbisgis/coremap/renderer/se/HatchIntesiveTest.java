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

import org.locationtech.jts.geom.Envelope;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.orbisgis.coremap.layerModel.Layer;
import org.orbisgis.coremap.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.coremap.renderer.se.fill.HatchedFill;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealLiteral;
import org.junit.Test;

import static org.junit.Assert.*;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.renderer.MapRenderer;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 *
 * @author Maxence Laurent
 */
public class HatchIntesiveTest {

    private static H2GIS h2GIS;

    @BeforeClass
    public static void tearUpClass() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/"+HatchIntesiveTest.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }

   

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    /**
     * We don't want negative distances
     */
    @Test
    public void testDistanceContext() throws ParameterException {
            HatchedFill hf = new HatchedFill();
            hf.setDistance(new RealLiteral(-1));
            assertTrue(hf.getDistance().getValue(null, 1) == 0);
    }

    public void template(String shapefile, String title, String stylePath, String source,
            String savePath, Envelope ext)
            throws IOException, InvalidStyle, SQLException, LayerException {
            ISpatialTable table = (ISpatialTable) h2GIS.load(new File(shapefile), true);        

            Layer layer = new Layer("swiss", table);

            Style style = new Style(layer, stylePath);
            layer.setStyle(style);

            MapRenderer mapRenderer = new MapRenderer();
            mapRenderer.addLayer(layer);

            mapRenderer.draw();
            

            if (savePath != null) {
                mapRenderer.save(savePath);
            }
    }

    public void drawMaps()
            throws ParameterException, IOException, InvalidStyle, SQLException, LayerException {

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 0°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_0.se", null, "/tmp/hatches_000.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 45°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_45.se", null, "/tmp/hatches_045.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 90°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_90.se", null, "/tmp/hatches_090.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 135°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_135.se", null, "/tmp/hatches_135.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 180°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_180.se", null, "/tmp/hatches_180.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 215°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_215.se", null, "/tmp/hatches_215.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 270°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_270.se", null, "/tmp/hatches_270.png", null);

        this.template("src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_dataset.shp", "Hatches 315°",
               "src/test/resources/org/orbisgis/core/renderer/se/HatchedFill/hatches_315.se", null, "/tmp/hatches_315.png", null);
    }
}
