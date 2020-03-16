/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.orbisgis.style.IStyleNode;

/**
 *
 * @author ebocher
 * @param <T>
 */
public interface ISymbolizerDraw<T extends IStyleNode> extends IStyleDrawer<T> {

    void setBufferedImage(BufferedImage bufferedImage);

    BufferedImage getBufferedImage();

    void setGraphics2D(Graphics2D sG2);

    Graphics2D getGraphics2D();
    
    /**
     * Is called once the drawer has been rendered
     *
     * @param g2 the graphics the layer has to be drawn on
     */
    void dispose(Graphics2D g2);
}
