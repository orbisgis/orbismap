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
package org.orbisgis.map.renderer.featureStyle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.layerModel.MapEnvelope;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.layerModel.Rectangle2DDouble;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.graphic.Size;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.stroke.PenStroke;
import static org.orbisgis.style.stroke.PenStroke.DEFAULT_CAP;
import static org.orbisgis.style.stroke.PenStroke.DEFAULT_JOIN;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class DrawerBaseTest {

    public static MapTransform mapTransform;
    public static int width = 100;
    public static int height = 100;
    public static Graphics2D g2;
    private static BufferedImage image;

    @BeforeEach
    public void tearUpBaseTest() throws Exception {
        mapTransform = new MapTransform();
        Envelope envelope = new Envelope(0, 100, 0, 100);
        mapTransform.setExtent(new MapEnvelope(envelope));
        mapTransform.resizeImage(width, height);
        image = mapTransform.getImage();
        g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.addRenderingHints(mapTransform.getRenderingHints());
    }

    @AfterEach
    public void tearDownBaseTest() throws Exception {
        g2.dispose();
        mapTransform = null;
        image = null;
    }

    public static void saveImage(TestInfo testInfo) throws IOException {
        File savePath = new File("./target/" + testInfo.getDisplayName() + ".png");
        ImageIO.write(image, "png", savePath);
    }

    public static Shape getPoint() {
        return new Line2D.Double(50, 50, 50, 50);
    }

    public static Shape getSimpleaAxisLine() {
        return new Line2D.Double(0, 50, 100, 50);
    }

    public static Shape getCrossLine() {
        return new Line2D.Double(0, 0, 100, 100);
    }

    public static Shape getRectangle() {
        return new Rectangle2DDouble(0, 10, 10, 10);
    }

    /**
     * Create a MarkGraphic
     *
     * @param wellKnownName
     * @param size
     * @return
     */
    public static MarkGraphic createMarkGraphic(String wellKnownName, float size) {
        MarkGraphic markGraphic = new MarkGraphic();
        markGraphic.setUom(Uom.PX);
        markGraphic.setWellKnownName(wellKnownName);
        markGraphic.setGraphicSize(new Size(size));
        PenStroke penStroke = new PenStroke();
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(Color.BLACK);
        penStroke.setFill(solidFill);
        penStroke.setWidth(1f);
        markGraphic.setStroke(penStroke);
        return markGraphic;
    }

    /**
     * Create a <code>SolidFill</code>
     *
     * @param color
     * @return a  <code>SolidFill</code>
     */
    public static SolidFill createSolidFill(Color color) {
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(color);
        solidFill.setOpacity(1.0f);
        return solidFill;
    }

    /**
     * Create basic PenStroke
     *
     * @param color
     * @param width
     * @return
     */
    public static PenStroke createPenStroke(Color color, float width) {
        PenStroke penStroke = new PenStroke();
        penStroke.setFill(createSolidFill(color));
        penStroke.setWidth(new Literal(width));
        penStroke.setUom(Uom.PX);
        penStroke.setDashOffset(new NullParameterValue());
        penStroke.setDashArray(new NullParameterValue());
        penStroke.setLineCap(DEFAULT_CAP);
        penStroke.setLineJoin(DEFAULT_JOIN);
        return penStroke;
    }

}
