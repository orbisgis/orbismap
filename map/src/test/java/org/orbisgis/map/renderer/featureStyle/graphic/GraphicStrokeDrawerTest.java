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
package org.orbisgis.map.renderer.featureStyle.graphic;

import org.orbisgis.map.renderer.featureStyle.DrawerBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.map.renderer.featureStyle.stroke.GraphicStrokeDrawer;
import org.orbisgis.style.Uom;
import org.orbisgis.style.stroke.GraphicStroke;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class GraphicStrokeDrawerTest extends DrawerBaseTest {

    @Test
    public void drawGraphicStroke1(TestInfo testInfo) throws Exception {
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setDistance(20);
        MarkGraphic markGraphic = createMarkGraphic("square", 20);
        graphicStroke.addGraphic(markGraphic);
        GraphicStrokeDrawer graphicStrokeDrawer = new GraphicStrokeDrawer();
        graphicStrokeDrawer.setShape(getSimpleaAxisLine());
        graphicStrokeDrawer.draw(g2, mapTransform, graphicStroke);
        saveImage(testInfo);
    }

    @Test
    public void drawGraphicStroke2(TestInfo testInfo) throws Exception {
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setDistance(10);
        MarkGraphic markGraphic = createMarkGraphic("square", 20);
        graphicStroke.addGraphic(markGraphic);
        GraphicStrokeDrawer graphicStrokeDrawer = new GraphicStrokeDrawer();
        graphicStrokeDrawer.setShape(getSimpleaAxisLine());
        graphicStrokeDrawer.draw(g2, mapTransform, graphicStroke);
        saveImage(testInfo);
    }

    @Test
    public void drawGraphicStroke3(TestInfo testInfo) throws Exception {
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setDistance(20);
        MarkGraphic markGraphic = createMarkGraphic("square", 20);
        MarkGraphic markGraphic2 = createMarkGraphic("square", 10);
        graphicStroke.addGraphic(markGraphic);
        graphicStroke.addGraphic(markGraphic2);
        GraphicStrokeDrawer graphicStrokeDrawer = new GraphicStrokeDrawer();
        graphicStrokeDrawer.setShape(getSimpleaAxisLine());
        graphicStrokeDrawer.draw(g2, mapTransform, graphicStroke);
        saveImage(testInfo);
    }

    @Test
    public void drawGraphicStroke4(TestInfo testInfo) throws Exception {
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setDistance(10);
        MarkGraphic markGraphic = createMarkGraphic("square", 20);
        MarkGraphic markGraphic2 = createMarkGraphic("square", 10);
        graphicStroke.addGraphic(markGraphic);
        graphicStroke.addGraphic(markGraphic2);
        GraphicStrokeDrawer graphicStrokeDrawer = new GraphicStrokeDrawer();
        graphicStrokeDrawer.setShape(getSimpleaAxisLine());
        graphicStrokeDrawer.draw(g2, mapTransform, graphicStroke);
        saveImage(testInfo);
    }

    @Test
    public void drawGraphicStroke5(TestInfo testInfo) throws Exception {
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setDistance(20);
        graphicStroke.setOverlap(false);
        MarkGraphic markGraphic = createMarkGraphic("square", 20);
        MarkGraphic markGraphic2 = createMarkGraphic("square", 10);
        MarkGraphic markGraphic3 = createMarkGraphic("square", 5);
        graphicStroke.addGraphic(markGraphic);
        graphicStroke.addGraphic(markGraphic2);
        graphicStroke.addGraphic(markGraphic3);
        GraphicStrokeDrawer graphicStrokeDrawer = new GraphicStrokeDrawer();
        graphicStrokeDrawer.setShape(getSimpleaAxisLine());
        graphicStrokeDrawer.draw(g2, mapTransform, graphicStroke);
        saveImage(testInfo);
    }

}
