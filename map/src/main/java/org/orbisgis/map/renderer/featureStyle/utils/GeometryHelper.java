/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.orbisgis.map.layerModel.MapTransform;

/**
 *
 * @author ebocher
 */
public class GeometryHelper {
    
     /**
     * Return all vertices of the geometry 
     * TODO : Take distance to remove redundant points
     *
     * @param mt
     * @param theGeom
     * @return
     */
    public static List<Point2D> getPoints(MapTransform mt, Geometry theGeom) {
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        Envelope envelope = mt.getExtent();
        AffineTransform at = mt.getAffineTransform();
        Coordinate[] coordinates = theGeom.getCoordinates();
        for (Coordinate coord : coordinates) {
            if (envelope.intersects(coord)) {
                points.add(at.transform(new Point2D.Double(coord.x, coord.y), null));
            }
        }
        return points;
    }
    
    /**
     * Return all vertices of the geometry 
     * TODO : Take distance to remove redundant points
     *
     * @param envelope
     * @param theGeom
     * @return
     */
    public static Geometry getPoints(Envelope envelope, Geometry theGeom) {        
        ArrayList<Coordinate> finalCoords = new ArrayList<>();
        Coordinate[] coords = theGeom.getCoordinates();    
        for (Coordinate coord : coords) {
            if (envelope.intersects(coord)) {
                finalCoords.add(coord);
            }
        }
        if(finalCoords.size()==1){
            return theGeom.getFactory().createPoint(finalCoords.get(0));
        }
        return theGeom.getFactory().createMultiPointFromCoords(finalCoords.toArray(new Coordinate[0]));
    }
    
     /**
     * Return one point for each geometry
     *
     * @param mt
     * @param the_geom
     * @return
     */
    public static Point2D getInteriorPoint(MapTransform mt, Geometry the_geom) {
        AffineTransform at = mt.getAffineTransform();
        Point  point = the_geom.getInteriorPoint();
        return at.transform(new Point2D.Double(point.getX(), point.getY()), null);
    }
}