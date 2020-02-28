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
package org.orbisgis.style.stroke;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import java.util.Map;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;

/**
 * Style description for linear features (Area or Line)
 *
 * @author Maxence Laurent, Alexis Guéganno.
 */
public abstract class Stroke extends StyleNode implements IUom {

    private Uom uom;
    private boolean linearRapport;
    private boolean offsetRapport;

    /**
     * Instanciate a new default {@code Stroke}, with linear and offset rapports
     * set to false.
     */
    protected Stroke(){
        linearRapport = false;
        offsetRapport = false;
    }
   

    /**
     * When delineating closed shapes (i.e. a ring), indicate, whether or not,
     * the length of stroke elements shall be scaled in order to make the pattern
     * appear a integral number of time. This will make the junction more aesthetical
     *
     * @return <cdoe>true</code> if the stroke elements' length shall be scaled,
     * <code>false</code> otherwise.
     */
    public boolean isLengthRapport() {
        return linearRapport;
    }

    /**
     * Determines if we want to use an length rapport or not.
     * @param lengthRapport
     */
    public void setLengthRapport(boolean lengthRapport) {
        this.linearRapport = lengthRapport;
    }

    /**
     * When delineating a line with a perpendicular offset, indicate whether or not
     * stroke element shall following the initial line (rapport=true) or should only
     * be based on the offseted line (rapport=false);
     *
     * @return true if offseted element shall follow initial line
     */
    public boolean isOffsetRapport() {
        return offsetRapport;
    }

    /**
     * Determines if we want to use an offset rapport or not.
     * @param offsetRapport
     */
    public void setOffsetRapport(boolean offsetRapport) {
        this.offsetRapport = offsetRapport;
    }

    /**
     * Apply the present Stroke to the geometry stored in sds, at index fid, in
     * graphics g2.
     * @param g2 draw within this graphics2d
     * @param map
     * @param shp stroke this shape (note this is note a JTS Geometry, because
     *        stroke can be used to delineate graphics (such as MarkGraphic,
     *        PieChart or AxisChart)
     * @param mt the well known IMapTransform 
     * @param  offset perpendicular offset to apply
     * @throws ParameterException
     * @throws IOException
     */
    public abstract void draw(Graphics2D g2, Map<String,Object> map, Shape shp,
             IMapTransform mt, double offset) throws ParameterException, IOException;

    
    /**
     * Returns the stroke pattern natural length, in pixel unit
     * @param map
     * @param shp
     * @param mt
     * @return
     * @throws ParameterException
     * @throws IOException
     */
    public abstract Double getNaturalLength(Map<String,Object> map,
            Shape shp, IMapTransform mt) throws ParameterException, IOException;

    /**
     * same as getNaturalLength, but in some case (i.e. PenStroke) the natural length
     * to use in not the same as returned by the latter : Especially for PenStroke : 
     * To compute a tile for hatching, we need to know the length of the pen stroke
     * (i.e. only when the stroke is dashed...), but to embed such a stroke in a compound, 
     * the natural length shall be +Inf
     * 
     * @param map
     * @param shp
     * @param mt
     * @return
     * @throws ParameterException
     * @throws IOException 
     */
    public Double getNaturalLengthForCompound(Map<String,Object> map,
            Shape shp, IMapTransform mt) throws ParameterException, IOException {
        return getNaturalLength(map, shp, mt);
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if(getParent() instanceof IUom){
            return ((IUom)getParent()).getUom();
        } else {
                return Uom.PX;
        }
    }

    @Override
    public void setUom(Uom u) {
        uom = u;
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }
    
}
