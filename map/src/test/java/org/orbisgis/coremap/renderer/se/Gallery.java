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
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.orbisgis.coremap.layerModel.Layer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.junit.Test;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.renderer.MapRenderer;
import org.orbisgis.coremap.utils.progress.NullProgressMonitor;
import org.orbisgis.datamanager.h2gis.H2GIS;
import org.orbisgis.datamanagerapi.dataset.ITable;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 *
 * @author Maxence Laurent
 */
public class Gallery {

    private static H2GIS h2GIS;

    @BeforeClass
    public static void tearUpClass() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(DataSourceFactory.JDBC_DATABASE_NAME, "./target/"+Gallery.class.getName());
        h2GIS = H2GIS.open(map);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        h2GIS.close();
    }


    public void template(String shapefile, String title, String stylePath, String source,
            String savePath, Envelope extent)
            throws IOException, InvalidStyle, SQLException, LayerException {
        
            ITable table = h2GIS.load(new File(shapefile),true);
            String tableReference = table.getName();          


            Layer layer = new Layer("swiss", h2GIS.getSpatialTable(tableReference));

            Style style = new Style(layer, stylePath);
            layer.setStyle(style);

            MapRenderer mapRenderer = new MapRenderer();
            mapRenderer.addLayer(layer);

            mapRenderer.draw();
            

            if (savePath != null) {
                mapRenderer.save(savePath);
            }
    }

    @Test
    public void testMaps()
            throws ParameterException, IOException, InvalidStyle, SQLException, LayerException {
        
        this.template("/home/ebocher/Autres/codes/orbisgis/bundles/core-map/src/test/resources/data/landcover2000.shp", "Lancover",
               "/home/ebocher/Autres/codes/orbisgis/bundles/core-map/src/test/resources/org/orbisgis/coremap/renderer/se/fills.se", null, "/tmp/extLancover.png", null);

        /*this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp", "SVG",
               "src/test/resources/org/orbisgis/core/renderer/se/svg.se", null, "/tmp/extG.png", null);


        this.template("../../datas2tests/shp/bigshape2D/cantons.shp", "Population canton (linéaire)",
                "src/test/resources/org/orbisgis/core/renderer/se/symbol_prop_canton_interpol_lin.se", null,
                "/tmp/pop_canton_lin.png", new Envelope(47680, 277971, 2265056, 2452630));


        this.template("../../datas2tests/shp/bigshape2D/cantons.shp", "Population canton (sqrt)",
                "src/test/resources/org/orbisgis/core/renderer/se/symbol_prop_canton_interpol_sqrt.se", null,
                "/tmp/pop_canton_sqrt.png", new Envelope(47680, 277971, 2265056, 2452630));


        this.template("../../datas2tests/shp/bigshape2D/cantons.shp", "Population canton (log)",
                "src/test/resources/org/orbisgis/core/renderer/se/symbol_prop_canton_interpol_log.se", null,
                "/tmp/pop_canton_log.png", new Envelope(47680, 277971, 2265056, 2452630));

        this.template("../../datas2tests/shp/bigshape2D/communes.shp", "DotMap Population communes",
                "src/test/resources/org/orbisgis/core/renderer/se/dotmap_communes.se", null, "/tmp/dot_map_communes.png", null);

        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Pie à la con", "src/test/resources/org/orbisgis/core/renderer/se/Districts/pie.se", null, "/tmp/pies.png", null);


        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Silouette", "src/test/resources/org/orbisgis/core/renderer/se/Districts/radar.se", null, "/tmp/radar.png", null);

        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Oui EEE 1992 (%)", "src/test/resources/org/orbisgis/core/renderer/se/Districts/choro.se", null, "/tmp/choro_ouiEEE.png", null);
        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Oui EEE 1992 (%)", "src/test/resources/org/orbisgis/core/renderer/se/Districts/density_hatch.se", null, "/tmp/denstiy_hatch_raw_ouiEEE.png", null);
        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Oui EEE 1992 (%)", "src/test/resources/org/orbisgis/core/renderer/se/Districts/density_hatch_classif.se", null, "/tmp/denstiy_hatch_classif_ouiEEE.png", null);
        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Oui EEE 1992 (%)", "src/test/resources/org/orbisgis/core/renderer/se/Districts/density_mark.se", null, "/tmp/denstiy_mark_ouiEEE.png", null);
        this.template("../../datas2tests/shp/Swiss/g4districts98_region.shp",
                "Oui EEE 1992 (%)", "src/test/resources/org/orbisgis/core/renderer/se/Districts/density_mark_classif.se", null, "/tmp/denstiy_mark_classif_ouiEEE.png", null);

*/
    }
}
