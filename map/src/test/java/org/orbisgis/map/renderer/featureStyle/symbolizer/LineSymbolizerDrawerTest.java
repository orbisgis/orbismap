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
package org.orbisgis.map.renderer.featureStyle.symbolizer;

import java.awt.Color;
import org.orbisgis.map.renderer.featureStyle.DrawerBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.AnchorPosition;
import org.orbisgis.style.graphic.ViewBox;
import org.orbisgis.style.label.RelativeOrientation;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.stroke.GraphicStroke;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.transform.Translate;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class LineSymbolizerDrawerTest extends DrawerBaseTest {

    
    @Test
    public void drawLineSymbolizerGraphicStrokeNormal(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(1);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeLine(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.initDefault();
        lineSymbolizerBase.setLevel(0);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(1);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        graphicStroke.setRelativeOrientation(RelativeOrientation.LINE);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    
    
    @Test
    public void drawLineSymbolizerGraphicStrokeAnchorLowerLeft(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 3));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.LOWER_LEFT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeAnchorLowerRight(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 3));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.LOWER_RIGHT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeAnchorUpperLeft(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 3));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.UPPER_LEFT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeAnchorUpperRight(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 3));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setUom(Uom.PX);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.UPPER_RIGHT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }

    @Test
    public void drawLineSymbolizerGraphicStrokeNormalUpAnchorCenter(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.CENTER);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeNormalUpAnchorLowerLeft(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.LOWER_LEFT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerGraphicStrokeNormalUpAnchorUpperLeft(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.UPPER_LEFT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    @Test
    public void drawLineSymbolizerGraphicStrokeNormalUpAnchorUpperRight(TestInfo testInfo) throws Exception {
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerGraphicStroke = new LineSymbolizer();
        lineSymbolizerGraphicStroke.setLevel(0);
        GraphicStroke graphicStroke = new GraphicStroke();
        graphicStroke.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic coldFrontMark = new MarkGraphic();
        coldFrontMark.setUom(Uom.PX);
        coldFrontMark.setWellKnownName(new Literal("triangle"));
        coldFrontMark.setGraphicSize(new ViewBox(10));
        coldFrontMark.setFill(createSolidFill(Color.GRAY));
        coldFrontMark.setStroke(createPenStroke(Color.ORANGE, 1));
        coldFrontMark.setAnchorPosition(AnchorPosition.UPPER_RIGHT);
        graphicStroke.addGraphic(coldFrontMark);
        lineSymbolizerGraphicStroke.setStroke(graphicStroke);        
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerGraphicStroke);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerPatternCircles(TestInfo testInfo) throws Exception { 
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerCircleLeft = new LineSymbolizer();
        lineSymbolizerCircleLeft.setLevel(0);
        GraphicStroke graphicStrokeLeft = new GraphicStroke();
        graphicStrokeLeft.setDistance(10);
        graphicStrokeLeft.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic circleLeft = createMarkGraphic("circle", 10);
        circleLeft.addTransform(new Translate(0, 10));
        MarkGraphic rightLeft = createMarkGraphic("circle", 10);
        rightLeft.addTransform(new Translate(0, -10));
        graphicStrokeLeft.addGraphic(circleLeft);
        graphicStrokeLeft.addGraphic(rightLeft);
        lineSymbolizerCircleLeft.setStroke(graphicStrokeLeft);   
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerCircleLeft);
        saveImage(testInfo);
    }
    
    @Test
    public void drawLineSymbolizerPartternLineOneSide(TestInfo testInfo) throws Exception { 
        LineSymbolizer lineSymbolizerBase = new LineSymbolizer();
        lineSymbolizerBase.setStroke(createPenStroke(Color.RED, 2));
        lineSymbolizerBase.setLevel(1);        
        LineSymbolizer lineSymbolizerCircleLeft = new LineSymbolizer();
        lineSymbolizerCircleLeft.setLevel(0);
        GraphicStroke graphicStrokeLine = new GraphicStroke();
        graphicStrokeLine.setDistance(5);
        graphicStrokeLine.setOverlap(false);
        graphicStrokeLine.setRelativeOrientation(RelativeOrientation.NORMAL_UP);
        MarkGraphic verticalLine = createMarkGraphic("VERTLINE", 10);
        verticalLine.addTransform(new Translate(0, 10));
        MarkGraphic verticalLineTouch = createMarkGraphic("VERTLINE", 20);
        verticalLineTouch.setAnchorPosition(AnchorPosition.UPPER_RIGHT);
        graphicStrokeLine.addGraphic(verticalLineTouch);
        graphicStrokeLine.addGraphic(verticalLine);
        lineSymbolizerCircleLeft.setStroke(graphicStrokeLine);   
        LineSymbolizerDrawer lineSymbolizerDrawer = new LineSymbolizerDrawer();        
        lineSymbolizerDrawer.setShape(getCrossLine());
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerBase);
        lineSymbolizerDrawer.draw(g2, mapTransform, lineSymbolizerCircleLeft);
        saveImage(testInfo);
    }

}
