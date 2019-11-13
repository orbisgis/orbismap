/**
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
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2017
 * CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.coremap;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.MapContext;

import static org.junit.Assert.assertTrue;
import org.orbisgis.coremap.layerModel.Layer;
import org.orbisgis.coremap.layerModel.model.IMapContext;
import org.orbisgis.coremap.renderer.se.HatchIntesiveTest;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.osgi.service.jdbc.DataSourceFactory;

public class MapContextTest {

    private static URL BV_SAP = MapContextTest.class.getResource("../../../data/bv_sap.shp");
    private static URL LINESTRING = MapContextTest.class.getResource("../../../data/linestring.shp");
    private static H2GIS h2GIS;

    @BeforeClass
    public static void tearUpClass() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/" + HatchIntesiveTest.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }

    @Test
    public void testRemoveSelectedLayer() throws Exception {
        IMapContext mc = new MapContext();
        ISpatialTable spatialTable = (ISpatialTable) h2GIS.load(BV_SAP.toURI(), "BV_SAP", true);
        ILayer layer = new Layer(spatialTable);
        mc.getLayerModel().addLayer(layer);
        mc.setSelectedLayers(new ILayer[]{layer});
        assertTrue(mc.getSelectedLayers().length == 1);
        assertTrue(mc.getSelectedLayers()[0] == layer);
        mc.getLayerModel().removeLayer(layer);
        assertTrue(mc.getSelectedLayers().length == 0);
    }

    @Test
    public void testSetBadLayerSelection() throws Exception {
        IMapContext mc = new MapContext();
        ISpatialTable bvTable = (ISpatialTable) h2GIS.load(BV_SAP.toURI(), "BV_SAP", true);
        ISpatialTable lineTable = (ISpatialTable) h2GIS.load(LINESTRING.toURI(), "LINESTRING", true);
        ILayer layer = new Layer(bvTable);
        ILayer layer2 = new Layer(lineTable);
        mc.getLayerModel().addLayer(layer);
        mc.setSelectedLayers(new ILayer[]{layer2});
        assertTrue(mc.getSelectedLayers().length == 0);
        mc.setSelectedLayers(new ILayer[]{layer});
        assertTrue(mc.getSelectedLayers().length == 1);
    }

    @Test
    public void testRemoveActiveLayer() throws Exception {
        IMapContext mc = new MapContext();
        ISpatialTable bvTable = (ISpatialTable) h2GIS.load(BV_SAP.toURI(), "BV_SAP", true);
        ILayer layer = new Layer(bvTable);
        mc.getLayerModel().addLayer(layer);
        mc.setActiveLayer(layer);
        mc.getLayerModel().removeLayer(layer);
        assertTrue(mc.getActiveLayer() == null);
    }

    private IMapContext getSampleMapContext() throws LayerException {
        IMapContext mc = new MapContext();
        
        ILayer layer = mc.createLayerCollection("a");
        mc.getLayerModel().addLayer(layer);
        
        return mc;
    }
}
