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
package org.orbisgis.orbismap.map.renderer.featureStyle.graphic;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.orbismap.map.renderer.featureStyle.DrawerBaseTest;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
//Disabled until call to GraphicsEnvironment is solved (java.awt.HeadlessException).
@Disabled
public class GraphicCollectionDrawerTest extends DrawerBaseTest {

    @Test
    public void drawGraphicCollectionTest(TestInfo testInfo) throws Exception {
        GraphicCollection graphicCollection = new GraphicCollection();
        graphicCollection.setUom(Uom.PX);
        MarkGraphic circle = createMarkGraphic("CIRCLE", 50);
        MarkGraphic cross = createMarkGraphic("CROSS", 40);
        graphicCollection.add(circle);
        graphicCollection.add(cross);
        GraphicCollectionDrawer graphicCollectionDrawer = new GraphicCollectionDrawer();
        Shape shape = getPoint();
        Rectangle2D bounds = shape.getBounds2D();
        graphicCollectionDrawer.setAffineTransform(AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY()));
        graphicCollectionDrawer.draw(g2, mapTransform, graphicCollection);

        MarkGraphicDrawer markDrawer = (MarkGraphicDrawer) graphicCollectionDrawer.getDrawer(circle);
        Shape makShape = markDrawer.getShape(mapTransform,circle);
        assertTrue(makShape instanceof Ellipse2D);
        assertEquals(50, makShape.getBounds2D().getHeight());
        assertEquals(50, makShape.getBounds2D().getWidth());
        markDrawer = (MarkGraphicDrawer) graphicCollectionDrawer.getDrawer(cross);
        makShape = markDrawer.getShape(mapTransform,cross );
        assertTrue(makShape instanceof Polygon);
        assertEquals(40, makShape.getBounds2D().getHeight());
        assertEquals(40, makShape.getBounds2D().getWidth());
        saveImage(testInfo);
    }

}
