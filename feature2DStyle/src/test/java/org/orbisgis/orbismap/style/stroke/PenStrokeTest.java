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
package org.orbisgis.orbismap.style.stroke;

import java.awt.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.orbisgis.orbismap.style.fill.GraphicFill;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.graphic.PointTextGraphic;

/**
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class PenStrokeTest {

    @Test
    public void testSetWidth() throws Exception {
        PenStroke ps = new PenStroke();
        ps.setWidth(12);
        assertEquals(12.0f, ps.getWidth().getValue());
        ps.setWidth(null);
        assertNull(ps.getWidth().getValue());
    }

    @Test
    public void testSetArray() throws Exception {
        PenStroke ps = new PenStroke();
        assertNull(ps.getDashArray().getValue());
        ps.setDashArray("1 1 1");
        assertEquals("1 1 1", ps.getDashArray().getValue());
    }

    @Test
    public void testSetLineCap() throws Exception {
        PenStroke ps = new PenStroke();
        assertEquals(ps.getLineCap(), LineCap.BUTT);
        ps.setLineCap(null);
        assertEquals(ps.getLineCap(), LineCap.BUTT);
        ps.setLineCap(LineCap.SQUARE);
        assertEquals(ps.getLineCap(), LineCap.SQUARE);
    }

    @Test
    public void testSetLineJoin() throws Exception {
        PenStroke ps = new PenStroke();
        assertEquals(ps.getLineJoin(), LineJoin.MITRE);
        ps.setLineJoin(null);
        assertEquals(ps.getLineJoin(), LineJoin.MITRE);
        ps.setLineJoin(LineJoin.BEVEL);
        assertEquals(ps.getLineJoin(), LineJoin.BEVEL);
    }

    @Test
    public void testSetOffset() throws Exception {
        PenStroke ps = new PenStroke();
        assertNull(ps.getDashOffset().getValue());
        ps.setDashOffset(2.0f);
        assertEquals(2f, ps.getDashOffset().getValue());
    }

    @Test
    public void testSetFill() throws Exception {
        PenStroke ps = new PenStroke();
        assertNull(ps.getFill());
        SolidFill sf = new SolidFill();
        sf.setColor(Color.BLACK);
        ps.setFill(sf);
        assertEquals(Color.BLACK, sf.getAWTColor() );
        assertTrue(ps.getFill() instanceof SolidFill);
        GraphicCollection gc = new GraphicCollection();
        PointTextGraphic ptg1 = new PointTextGraphic();
        gc.add(ptg1);
        GraphicFill graphicFill = new GraphicFill();
        graphicFill.setGraphics(gc);
        ps.setFill(graphicFill);
        assertTrue(ps.getFill() instanceof  GraphicFill);
    }

}
