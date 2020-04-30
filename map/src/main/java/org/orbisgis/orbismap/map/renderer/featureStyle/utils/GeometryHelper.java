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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.utils;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.shape.PointsShape;

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
     * @param at
     * @param decimationDistance
     * @return
     */
    public static Shape getPointsAsShape(Envelope envelope, Geometry theGeom, AffineTransform at, double decimationDistance) {  
        if(at!=null){
        PointsShape pointsShape = new PointsShape();        
        Coordinate[] coords = theGeom.getCoordinates();   
        Coordinate prev = coords[0];
        Point2D pt = at.transform(new Point2D.Double(prev.x, prev.y), null);  
        pointsShape.add(new Line2D.Double(pt,pt));
        int n = coords.length - 1;    
            for (int i = 1; i <= n; i++) {
                Coordinate currentCoord = coords[i];
                if (envelope.intersects(currentCoord)) {
                    if (decimationDistance > 0.0) {
                        boolean isDecimated = prev != null
                                && Math.abs(currentCoord.x - prev.x) < decimationDistance
                                && Math.abs(currentCoord.y - prev.y) < decimationDistance;
                        if (i < n && isDecimated) {
                            continue;
                        }
                        prev = currentCoord;
                    }
                pt = at.transform(new Point2D.Double(currentCoord.x, currentCoord.y), null);                
                pointsShape.add(new Line2D.Double(pt,pt));
            }
        }        
        return pointsShape;
        }
        return null;
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
