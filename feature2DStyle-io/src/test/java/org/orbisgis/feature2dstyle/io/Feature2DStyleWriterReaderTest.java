/**
 * Feature2DStyle-IO is part of the OrbisGIS platform
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
 * Feature2DStyle-IO is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle-IO is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle-IO is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle-IO . If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.feature2dstyle.io;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.orbisgis.style.Feature2DStyle;

import org.junit.jupiter.api.TestInfo;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.IRule;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.Size;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.parameter.geometry.GeometryParameter;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.visitor.CompareStyleVisitor;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class Feature2DStyleWriterReaderTest {

    @Test
    public void writeReadPointSymbolizerWithSize(TestInfo testInfo) throws Exception {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        ps.setGeometryParameter(new GeometryParameter("the_geom"));
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setWellKnownName("circle");
        markGraphic.setGraphicSize(new Size(10f));
        SolidFill solidFill = new SolidFill();
        solidFill.initDefault();
        markGraphic.setFill(solidFill);
        PenStroke penStroke = new PenStroke();
        penStroke.initDefault();
        markGraphic.setStroke(penStroke);
        ps.addGraphic(markGraphic);
        rule.addSymbolizer(ps);
        writeReadTest(testInfo.getDisplayName(), style);
    }

    @Test
    public void writeReadPointSymbolizer(TestInfo testInfo) throws Exception {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        PointSymbolizer ps = new PointSymbolizer();
        ps.setGeometryParameter(new GeometryParameter("the_geom"));
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setWellKnownName("circle");
        markGraphic.setGraphicSize(new ViewBox(10f));
        SolidFill solidFill = new SolidFill();
        solidFill.initDefault();
        markGraphic.setFill(solidFill);
        PenStroke penStroke = new PenStroke();
        penStroke.initDefault();
        markGraphic.setStroke(penStroke);
        ps.addGraphic(markGraphic);
        rule.addSymbolizer(ps);
        writeReadTest(testInfo.getDisplayName(), style);
    }

    @Test
    public void writeReadComplexStyle(TestInfo testInfo) throws Exception {
        //TODO
        
    }

    public static void writeReadTest(String testName, Feature2DStyle inputStyle) throws Exception {
        File outputStyleFile = new File("./target/" + testName + ".se");
        if (outputStyleFile == null) {
            throw new IllegalArgumentException("The output file to save the json style cannot be null");
        }
        if (inputStyle == null) {
            throw new IllegalArgumentException("The input style cannot be null");
        }
        if (outputStyleFile.exists()) {
            outputStyleFile.delete();
        }
        Feature2DStyleIO.toXML(inputStyle, outputStyleFile);        
        assertTrue(outputStyleFile.exists());

        Feature2DStyle output_fds = Feature2DStyleIO.fromXML(outputStyleFile);

        CompareStyleVisitor cp = new CompareStyleVisitor();
        cp.visitSymbolizerNode(inputStyle, output_fds);
    }

}
