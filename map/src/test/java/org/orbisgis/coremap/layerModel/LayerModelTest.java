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
package org.orbisgis.coremap.layerModel;

import org.orbisgis.coremap.layerModel.model.IMapContext;
import org.orbisgis.coremap.layerModel.model.ILayer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orbisgis.coremap.renderer.se.Style;
import org.orbisgis.coremap.renderer.se.common.Description;

import org.orbisgis.datamanager.h2gis.H2GIS;
import org.osgi.service.jdbc.DataSourceFactory;


public class LayerModelTest {

    private static H2GIS h2GIS;
    private String colorRecodeFile = LayerModelTest.class.getResource("../renderer/se/colorRecode.se").getFile();

    @BeforeClass
    public static void tearUpClass() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/"+LayerModelTest.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
         h2GIS.close();
    }


    private IMapContext mc = new MapContext();

	private static final String dummy = "vector1";
	private static final String dummy2 = "vector2";
	private static final String dummy3 = "vector3";

    @Before
    public void setUp() throws Exception {
            h2GIS.execute("create table "+dummy+"(the_geom GEOMETRY)");
            h2GIS.execute("create table "+dummy2+"(the_geom GEOMETRY)");
            h2GIS.execute("create table "+dummy3+"(the_geom GEOMETRY)");
    }

    @After
    public void tearDown() throws SQLException {
        h2GIS.execute("drop table if exists "+dummy+","+dummy2+","+dummy3);
    }

    
        @Test
	public void testLayerEvents() throws Exception {
		TestLayerListener listener = new TestLayerListener();
		Layer vl = new Layer(h2GIS.getSpatialTable(dummy));
		ILayer lc = mc.createLayerCollection("root");
		vl.addLayerListener(listener);
		lc.addLayerListener(listener);
		Layer vl1 = new Layer(h2GIS.getSpatialTable(dummy));
		lc.addLayer(vl1);
		assertTrue(listener.la == 1);
		lc.setName("new name");
		assertTrue(listener.nc == 1);
		lc.setVisible(false);
		assertTrue(listener.vc == 1);
		vl.setStyle(new Style(vl, colorRecodeFile));
		lc.removeLayer(vl1.getName());
		assertTrue(listener.lr == 1);
		assertTrue(listener.lring == 1);
		assertTrue(lc.getLayerCount() == 0);
	}

        @Test
	public void testLayerRemovalCancellation() throws Exception {
		TestLayerListener listener = new TestLayerListener() {
			@Override
			public boolean layerRemoving(LayerCollectionEvent arg0) {
				return false;
			}
		};
		ILayer vl = new Layer(h2GIS.getSpatialTable(dummy));
		ILayer lc = mc.createLayerCollection("root");
		lc.addLayer(vl);
		lc.addLayerListener(listener);
		assertTrue(lc.removeLayer(vl));
		assertTrue(lc.removeLayer(vl.getName()));
		assertTrue(lc.removeLayer(vl, false));
		assertTrue(lc.removeLayer(vl, true));
	}

        @Test
	public void testRepeatedName() throws Exception {
		ILayer lc1 = mc.createLayerCollection("firstLevel");
		ILayer lc2 = mc.createLayerCollection("secondLevel");
		ILayer lc3 = mc.createLayerCollection("thirdLevel");
		ILayer vl1 = new Layer(h2GIS.getSpatialTable(dummy));
		ILayer vl2 = new Layer(h2GIS.getSpatialTable(dummy2));
		ILayer vl3 = new Layer(h2GIS.getSpatialTable(dummy3));
		lc1.addLayer(vl1);
		lc2.addLayer(vl2);
		lc1.addLayer(lc2);
		lc3.addLayer(vl3);
		lc2.addLayer(lc3);
		vl3.setName("vector2");
		assertTrue(!vl3.getName().equals("vector2"));
		vl3.setName("firstLevel");
		assertTrue(!vl3.getName().equals("firstLevel"));
		lc1.setName("vector2");
		assertTrue(!lc1.getName().equals("vector2"));
	}

        @Test
	public void testAddWithSameName() throws Exception {
		ILayer lc = mc.createLayerCollection("firstLevel");
		ILayer vl1 = new Layer(h2GIS.getSpatialTable(dummy));
		ILayer vl2 = new Layer(h2GIS.getSpatialTable(dummy));
		lc.addLayer(vl1);
		lc.addLayer(vl2);
		assertTrue(!vl1.getName().equals(vl2.getName()));

	}

        @Test
	public void testAddToChild() throws Exception {
		ILayer lc1 = mc.createLayerCollection("firstLevel");
		ILayer lc2 = mc.createLayerCollection("secondLevel");
		ILayer lc3 = mc.createLayerCollection("thirdLevel");
		ILayer lc4 = mc.createLayerCollection("fourthLevel");
		lc1.addLayer(lc2);
		lc2.addLayer(lc3);
		lc3.addLayer(lc4);
		try {
			lc2.moveTo(lc4);
			assertTrue(false);
		} catch (LayerException e) {
		}

		TestLayerListener listener = new TestLayerListener();
		lc1.addLayerListenerRecursively(listener);
		lc3.moveTo(lc1);
		assertTrue(lc3.getParent() == lc1);
		assertTrue(lc2.getChildren().length == 0);
		assertTrue(listener.la == 0);
		assertTrue(listener.lr == 0);
		assertTrue(listener.lring == 0);
		assertTrue(listener.lm == 2);
	}

        
        @Test
	public void testGetLayerByName() throws Exception {
		ILayer lc = mc.createLayerCollection("root");
		ILayer l2 = mc.createLayerCollection("secondlevel");
		ILayer l3 = mc.createLayerCollection("secondlevelbis");
		ILayer vl1 = new Layer(h2GIS.getSpatialTable(dummy));
		l2.addLayer(vl1);
		lc.addLayer(l2);
		lc.addLayer(l3);

		assertTrue(lc.getLayerByName("secondlevel") == l2);
		assertTrue(lc.getLayerByName("secondlevelbis") == l3);
		assertTrue(lc.getLayerByName(dummy) == vl1);
	}

        @Test
        public void testInternationalizedTitle() throws Exception {
                Layer bl = new Layer("youhou", h2GIS.getSpatialTable(dummy));
                Description desc = new Description();
                desc.addTitle(Locale.FRENCH, "youhou title");
                bl.setDescription(desc);
                Locale l = Locale.getDefault();
                Locale.setDefault(new Locale("en","EN"));
                assertNotNull(bl.getDescription());
                Locale.setDefault(l);
        }

	private class TestLayerListener implements LayerListener {

		private int nc = 0;

		private int vc = 0;

		private int la = 0;

		private int lm = 0;

		private int lr = 0;

		private int lring = 0;

		private int sc = 0;

		public void nameChanged(LayerListenerEvent e) {
			nc++;
		}

		public void visibilityChanged(LayerListenerEvent e) {
			vc++;
		}

		public void layerAdded(LayerCollectionEvent listener) {
			la++;
		}

		public void layerMoved(LayerCollectionEvent listener) {
			lm++;
		}

		public void layerRemoved(LayerCollectionEvent listener) {
			lr++;
		}

		public void styleChanged(LayerListenerEvent e) {
			sc++;
		}

		public void selectionChanged(SelectionEvent e) {
		}

		@Override
		public boolean layerRemoving(LayerCollectionEvent layerCollectionEvent) {
			lring++;
			return true;
		}
	}
}
