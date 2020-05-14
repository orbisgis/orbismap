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
import org.orbisgis.orbismap.map.layerModel.StyledLayer
import org.orbisgis.orbismap.map.renderer.MapView
import org.junit.jupiter.api.Test
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS
import org.orbisgis.orbismap.style.Feature2DStyle

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
    void createMapView() throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        ISpatialTable spatialTable = h2GIS.link(new File(inputFile), "LANDCOVER", true)
        MapView mapView = new MapView()
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0);
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.show();
    }

    @Disabled
    @Test
    void mapViewReadStyle() throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        String inputStyle = new File(this.getClass().getResource("landcover2000_style.se").toURI()).getAbsolutePath();
        ISpatialTable spatialTable = h2GIS.link(new File(inputFile), "LANDCOVER", true)
        MapView mapView = new MapView()
        StyledLayer styledLayer = Feature2DStyleIO.fromXML(new File(inputStyle));
        mapView << styledLayer
        mapView.draw();
        mapView.show();
    }

    }
