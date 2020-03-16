/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Erwan Bocher
 */
public class PointsShape implements Shape {
    
    private ArrayList<Shape> shapes = new ArrayList();

    public PointsShape() {
    }
    
   
    
    public void add(Shape shape) {
        shapes.add(shape);
    }
    

    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle2D getBounds2D() {
        Rectangle2D rectangle = null;
        for (Shape shape : shapes) {
            if (rectangle == null) {
                rectangle = shape.getBounds2D();
            } else {
                rectangle.add(shape.getBounds2D());
            }
        }
        return rectangle;
    
    }

    @Override
    public boolean contains(double d, double d1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean contains(Point2D pd) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean intersects(double d, double d1, double d2, double d3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersects(Rectangle2D rd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(double d, double d1, double d2, double d3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(Rectangle2D rd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int size() {
        return shapes.size();
    }

    public Shape get(int i) {
        return shapes.get(i);
    }
    
}
