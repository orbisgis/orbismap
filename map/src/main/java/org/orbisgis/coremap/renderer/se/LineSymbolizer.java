/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.coremap.renderer.se;

import org.locationtech.jts.geom.Geometry;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.common.Uom;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameterContext;
import org.orbisgis.coremap.renderer.se.stroke.PenStroke;
import org.orbisgis.coremap.renderer.se.stroke.Stroke;

/**
 * A {@code LineSymbolizer} is used to style a {@code Stroke} along a linear 
 * geometry type (a LineString, for instance). It is dependant upon the same
 * parameters as {@link VectorSymbolizer}, and upon two others :
 * <ul><li>PerpendicularOffset : Used to draw lines in parallel to the original
 * geometry</li>
 * <li>Stroke : defines the way to render the line, as described in {@link Stroke}
 * and its children</li>
 * </ul>
 *
 * @todo add perpendicular offset
 *
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class LineSymbolizer extends VectorSymbolizer implements StrokeNode {

        private RealParameter perpendicularOffset;
        private Stroke stroke;
      
        /**
         * Instantiate a new default {@code LineSymbolizer}. It's named {@code
         * Line Symbolizer"}, is defined in {@link Uom#MM}, and is drawn using a
         * default {@link PenStroke}
         */
        public LineSymbolizer() {
                super();
                this.name = "Line Symbolizer";
                setStroke(new PenStroke());
        }        

        @Override
        public Stroke getStroke() {
                return stroke;
        }

        @Override
        public void setStroke(Stroke stroke) {
                this.stroke = stroke;
                stroke.setParent(this);
        }

        /**
         * Get the current perpendicular offset. If null, considered to be set to 0.
         * @return 
         */
        public RealParameter getPerpendicularOffset() {
                return perpendicularOffset;
        }

        /**
         * Set the perpendicular offset. If a {@code null} value is given, the offset
         * will be considered as equal to 0.
         * @param perpendicularOffset 
         */
        public void setPerpendicularOffset(RealParameter perpendicularOffset) {
                this.perpendicularOffset = perpendicularOffset;
                if (this.perpendicularOffset != null) {
                        this.perpendicularOffset.setContext(RealParameterContext.REAL_CONTEXT);
                        this.perpendicularOffset.setParent(this);
                }
        }

        /**
         *
         * @param g2
         * @param rs
         * @param fid
         * @throws ParameterException
         * @throws IOException
         * @todo make sure the geom is a line or an area; implement p_offset
         */
        @Override
        public void draw(Graphics2D g2, ResultSet rs, long fid,
                boolean selected, MapTransform mt, Geometry the_geom)
                throws ParameterException, IOException, SQLException {
                if (stroke != null) {
                        Map<String,Object> map = getFeaturesMap(rs, fid);
                        Shape shp = mt.getShape(the_geom, true);
                        double offset = 0.0;
                        if (perpendicularOffset != null) {
                                offset = Uom.toPixel(perpendicularOffset.getValue(rs, fid),
                                        getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
                        }


                        if (shp != null) {
                                stroke.draw(g2, map, shp, selected, mt, offset);

                        }
                }
        }       

        @Override
        public List<SymbolizerNode> getChildren() {
                List<SymbolizerNode> ls = new ArrayList<SymbolizerNode>();
                if(this.getGeometryAttribute()!=null){
                    ls.add(this.getGeometryAttribute());
                }
                if (perpendicularOffset != null) {
                        ls.add(perpendicularOffset);
                }
                if (stroke != null) {
                        ls.add(stroke);
                }
                return ls;
        }
}
