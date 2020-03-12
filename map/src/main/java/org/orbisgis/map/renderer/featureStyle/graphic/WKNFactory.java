/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.graphic;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.net.URI;

/**
 *
 * @author ebocher
 */
public class WKNFactory implements MarkFactory {

      /**
     * Default size to be used to render graphics based on well-known names.
     */
    public static final double DEFAULT_SIZE = 10.0;
    
   
    /**
     * Create a shape according a name and bbox parameters
     * 
     * @param name
     * @param height of the shape in px
     * @param width of the shape in px
     * @return 
     */
    public Shape getShape(String name, double height,double width) {
        double x=DEFAULT_SIZE, y=DEFAULT_SIZE; // The size of the shape, [final unit] => [px]
        int x2 = (int)(x / 2.0);
        int y2 = (int)(y / 2.0);
	int minxy6 = (int)Math.min(x/6, y/6);
                
        Shape shape =null;
        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            switch (name) {
                case "vertline":
                    shape = new Line2D.Double(0, -height, 0, height);
                    break;
                case "horline":
                    shape =new Line2D.Double(-width, 0, width, 0);
                    break;
                case "slash":
                    shape =new Line2D.Double(-width, -height, width, height);
                    break;
                case "plus":
                    GeneralPath gp = new GeneralPath();
                    gp.moveTo(-1f, 0);
                    gp.lineTo(1f, 0);
                    gp.moveTo(0, -1f);
                    gp.lineTo(0, 1f);
                    shape =gp;
                    break;
                case "square":
                    shape = new Rectangle2D.Double(0, height, width, height);
                    break;
                case "halfcircle":
                    break;
                case "triangle":
                    break;
                case "start":
                    break;
                case "cross":
                    break;
                case "x":
                    break;
                case "rectangle":
                    break;
                default:
                     //shape =  "CIRCLE";
            }
        }
        return shape;
    }

    @Override
    public URI getURI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
