/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.graphic;

import org.junit.jupiter.api.Test;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.graphic.PointTextGraphic;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GraphicCollectionTest {  
    
     

    @Test
    public void testMoveUp() throws Exception {
        GraphicCollection gc = new GraphicCollection();
        PointTextGraphic ptg1 = new PointTextGraphic();
        PointTextGraphic ptg2 = new PointTextGraphic();
        PointTextGraphic ptg3 = new PointTextGraphic();
        PointTextGraphic ptg4 = new PointTextGraphic();
        gc.add(ptg1);
        gc.add(ptg2);
        gc.add(ptg3);
        gc.add(ptg4);
        assertEquals(gc.getNumGraphics(), 4);
        assertTrue(gc.getGraphic(0) == ptg1);
        assertTrue(gc.getGraphic(1) == ptg2);
        assertTrue(gc.getGraphic(2) == ptg3);
        assertTrue(gc.getGraphic(3) == ptg4);
        //We move ptg2 up
        gc.moveGraphicUp(1);
        assertTrue(gc.getGraphic(0) == ptg2);
        assertTrue(gc.getGraphic(1) == ptg1);
        assertTrue(gc.getGraphic(2) == ptg3);
        assertTrue(gc.getGraphic(3) == ptg4);
        //We move ptg2 up. Nothing is supposed to happen, as it is the uppest element.
        gc.moveGraphicUp(0);
        assertTrue(gc.getGraphic(0) == ptg2);
        assertTrue(gc.getGraphic(1) == ptg1);
        assertTrue(gc.getGraphic(2) == ptg3);
        assertTrue(gc.getGraphic(3) == ptg4);
        //We move ptg4 up. 
        gc.moveGraphicUp(3);
        assertTrue(gc.getGraphic(0) == ptg2);
        assertTrue(gc.getGraphic(1) == ptg1);
        assertTrue(gc.getGraphic(2) == ptg4);
        assertTrue(gc.getGraphic(3) == ptg3);
    }

    @Test
    public void testMoveDown() throws Exception {
        GraphicCollection gc = new GraphicCollection();
        PointTextGraphic ptg1 = new PointTextGraphic();
        PointTextGraphic ptg2 = new PointTextGraphic();
        PointTextGraphic ptg3 = new PointTextGraphic();
        PointTextGraphic ptg4 = new PointTextGraphic();
        gc.add(ptg1);
        gc.add(ptg2);
        gc.add(ptg3);
        gc.add(ptg4);
        assertEquals(gc.getNumGraphics(), 4);
        //We move ptg2 down
        gc.moveGraphicDown(1);
        assertTrue(gc.getGraphic(0) == ptg1);
        assertTrue(gc.getGraphic(1) == ptg3);
        assertTrue(gc.getGraphic(2) == ptg2);
        assertTrue(gc.getGraphic(3) == ptg4);
        //We move ptg1 down
        gc.moveGraphicDown(0);
        assertTrue(gc.getGraphic(0) == ptg3);
        assertTrue(gc.getGraphic(1) == ptg1);
        assertTrue(gc.getGraphic(2) == ptg2);
        assertTrue(gc.getGraphic(3) == ptg4);
        //We move ptg1 down. Nothing is supposed to happen, as it is the lowest element.
        gc.moveGraphicDown(3);
        assertTrue(gc.getGraphic(0) == ptg3);
        assertTrue(gc.getGraphic(1) == ptg1);
        assertTrue(gc.getGraphic(2) == ptg2);
        assertTrue(gc.getGraphic(3) == ptg4);
    }
}
