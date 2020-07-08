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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInfo
import org.orbisgis.orbisdata.datamanager.jdbc.postgis.POSTGIS
import org.orbisgis.orbismap.map.layerModel.StyledLayer
import org.orbisgis.orbismap.map.renderer.MapView
import org.junit.jupiter.api.Test
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS
import org.orbisgis.orbismap.style.Feature2DStyle
import org.orbisgis.orbismap.feature2dstyle.io.Feature2DStyleIO;

import java.awt.Color

import static org.junit.jupiter.api.Assertions.*



class MapViewInActionsTests {

    @Disabled
    @Test
    void createEmptyMapView() throws Exception {
        MapView mapView = new MapView()
        assertNull(mapView.envelope)
        //TODO : assertEquals(0, mapView.layers.size)
    }

    @Disabled
    @Test
    void createMapView(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        ISpatialTable spatialTable = h2GIS.link(new File(inputFile), "LANDCOVER", true)
        MapView mapView = new MapView()
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0);
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.show();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
    }

    @Disabled
    @Test
    void createMapViewLinkedTable(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        h2GIS.execute("drop table if exists remoteTable; CREATE LINKED TABLE remoteTable ('org.h2gis.postgis_jts.Driver', 'jdbc:postgresql_h2://localhost:5432/orbisgis_db', 'orbisgis', 'orbisgis', 'public', '(SELECT st_buffer(the_geom, 20) FROM landcover)');");
        ISpatialTable spatialTable = h2GIS.getSpatialTable("remoteTable")
        MapView mapView = new MapView()
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0,Color.BLACK,1 );
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        mapView.show();
    }


    @Test
    void createMapViewPostGIS(TestInfo testInfo) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/orbisgis_db";
        Properties props = new Properties();
        props.setProperty("user", "orbisgis");
        props.setProperty("password", "orbisgis");
        props.setProperty("url", url);
        POSTGIS postgis = POSTGIS.open(props);
        if(postgis!=null){
            postgis.load(new File(this.getClass().getResource("landcover2000.shp").toURI()), "landcover", true)
            postgis.execute("ANALYZE landcover")
            ISpatialTable spatialTable = postgis.getSpatialTable("landcover")
            MapView mapView = new MapView()
            Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.ORANGE, 1, 0, Color.BLACK,2 )
            StyledLayer styledLayer = new StyledLayer(spatialTable, style)
            mapView << styledLayer
            mapView.draw();
            mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        }
    }


    @Test
    void mapViewReadStyle(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        String inputStyle = new File(this.getClass().getResource("styles/single_symbol/single_symbol_map.json").toURI()).getAbsolutePath();
        h2GIS.link(new File(inputFile), "LANDCOVER", true)
        ISpatialTable spatialTable =h2GIS.getSpatialTable("LANDCOVER")
        MapView mapView = new MapView()
        Feature2DStyle style = Feature2DStyleIO.fromJSON(new File(inputStyle));
        StyledLayer styledLayer =new StyledLayer(spatialTable,style )
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        //mapView.show();
    }
    }
