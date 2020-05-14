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
package org.orbisgis.orbismap.feature2dstyle.io;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.orbisgis.orbismap.style.Feature2DStyle;

import org.junit.jupiter.api.TestInfo;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.IRule;
import org.orbisgis.orbismap.style.color.HexaColor;
import org.orbisgis.orbismap.style.color.RGBColor;
import org.orbisgis.orbismap.style.color.WellknownNameColor;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.graphic.MarkGraphic;
import org.orbisgis.orbismap.style.graphic.graphicSize.Size;
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.symbolizer.LineSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.PointSymbolizer;

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
    public void writeReadColorElementsSymbolizer(TestInfo testInfo) throws Exception {
        Feature2DStyle style = new Feature2DStyle();
        style.addRule(new Feature2DRule());
        IRule rule = style.getRules().get(0);
        //WellknownNameColor
        LineSymbolizer lineSymbolizer = new LineSymbolizer();
        lineSymbolizer.setName("WellknownNameColor");
        PenStroke penStrokeLine = new PenStroke();
        penStrokeLine.initDefault();
        SolidFill solidFill1 = new SolidFill();
        WellknownNameColor wellknownNameColor = new WellknownNameColor();
        wellknownNameColor.setWellknownName(new Literal("orange"));
        solidFill1.setColor(wellknownNameColor);
        penStrokeLine.setFill(solidFill1);
        lineSymbolizer.setStroke(penStrokeLine);
        rule.addSymbolizer(lineSymbolizer);
        //WellknownNameColor expression
        LineSymbolizer lineSymbolizer2 = new LineSymbolizer();
        lineSymbolizer2.setName("WellknownNameColor with expression");
        PenStroke penStrokeLine2 = new PenStroke();
        penStrokeLine2.initDefault();
        SolidFill solidFill2 = new SolidFill();
        WellknownNameColor wellknownNameColor2 = new WellknownNameColor();
        wellknownNameColor2.setWellknownName(new Expression("orange"));
        solidFill2.setColor(wellknownNameColor2);
        penStrokeLine2.setFill(solidFill2);
        lineSymbolizer2.setStroke(penStrokeLine2);
        rule.addSymbolizer(lineSymbolizer2);
        //RGBColor
        LineSymbolizer lineSymbolizer3 = new LineSymbolizer();
        lineSymbolizer3.setName("RGBColor");
        PenStroke penStrokeLine3 = new PenStroke();
        penStrokeLine3.initDefault();
        SolidFill solidFill3 = new SolidFill();
        RGBColor rgbColor3 = new RGBColor(0,0,0);
        solidFill3.setColor(rgbColor3);
        penStrokeLine3.setFill(solidFill3);
        lineSymbolizer3.setStroke(penStrokeLine3);
        rule.addSymbolizer(lineSymbolizer3);
        //RGBColor expression
        LineSymbolizer lineSymbolizer4 = new LineSymbolizer();
        lineSymbolizer4.setName("RGBColor with expression");
        PenStroke penStrokeLine4 = new PenStroke();
        penStrokeLine4.initDefault();
        SolidFill solidFill4 = new SolidFill();
        RGBColor rgbColor4 = new RGBColor();
        rgbColor4.setRed(new Expression("22"));
        rgbColor4.setGreen(new Expression("15"));
        rgbColor4.setBlue(new Expression("120"));
        solidFill4.setColor(rgbColor4);
        penStrokeLine4.setFill(solidFill4);
        lineSymbolizer4.setStroke(penStrokeLine4);
        rule.addSymbolizer(lineSymbolizer4);
        //HexaColor
        LineSymbolizer lineSymbolizer5 = new LineSymbolizer();
        lineSymbolizer5.setName("HexaColor");
        PenStroke penStrokeLine5 = new PenStroke();
        penStrokeLine5.initDefault();
        SolidFill solidFill5 = new SolidFill();
        HexaColor hexaColor5 = new HexaColor("#000000");
        solidFill5.setColor(hexaColor5);
        penStrokeLine5.setFill(solidFill5);
        lineSymbolizer5.setStroke(penStrokeLine5);
        rule.addSymbolizer(lineSymbolizer5);
        //HexaColor expression
        LineSymbolizer lineSymbolizer6 = new LineSymbolizer();
        lineSymbolizer6.setName("HexaColor with expression");
        PenStroke penStrokeLine6 = new PenStroke();
        penStrokeLine6.initDefault();
        SolidFill solidFill6 = new SolidFill();
        HexaColor hexaColor6 = new HexaColor();
        hexaColor6.setHexaColor(new Expression("#000000"));
        solidFill6.setColor(hexaColor6);
        penStrokeLine6.setFill(solidFill6);
        lineSymbolizer6.setStroke(penStrokeLine6);
        rule.addSymbolizer(lineSymbolizer6);
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
        
        //TODO : to be finished
        Feature2DStyle output_fds = Feature2DStyleIO.fromXML(outputStyleFile);

        //CompareStyleVisitor cp = new CompareStyleVisitor();
        //cp.visitSymbolizerNode(inputStyle, output_fds);
    }

}
