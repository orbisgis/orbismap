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
package org.orbisgis.style.fill;



import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IGraphicNode;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.UomNode;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 * A "GraphicFill" defines repeated-graphic filling (stippling) pattern for an area geometry.
 * It is defined with a GraphicCollection (that will be used to draw the filling symbol), a Uom,
 * and a gap vector. The gap vector is represented by its two (X and Y) coordinates,
 * stored as <code>RealParameter</code> instances.
 * @author Alexis Guéganno, Maxence Laurent
 */
public  class GraphicFill extends StyleNode implements IGraphicNode, IFill, UomNode{

    private Graphic graphic;
    /**
     * Distance between two graphics in the fill, in X direction.
     */
    private ParameterValue gapX ;
    /**
     * Distance between two graphics in the fill, in Y direction.
     */
    private ParameterValue gapY ;
    private Uom uom;

    /**
     * Creates a new GraphicFill, with the gap's measures set to null.
     */
    public GraphicFill() {
        setGapX(new NullParameterValue());
        setGapY(new NullParameterValue());
    }

    
    
    /**
     * Set the GraphicCollection embedded in this GraphicFill.This is set as the parent of <code>graphic</code>
     * @param graphic
     * @return 
     */
    @Override
    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
        graphic.setParent(this);
    }

    /**
     * Get the GraphicCollection embedded in this GraphicFill.
     * @return 
     */
    @Override
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * Set the gap, upon X direction, between two symbols.
     * @param gap 
     */
    public void setGapX(ParameterValue gap) {        
        ParameterValueHelper.validateAsFloat(gap);
        gapX = gap;
        if (gap != null) {
            gap.setParent(this);
        }
    }

    /**
     * Set the gap, upon Y direction, between two symbols.
     * @param gap 
     */
    public void setGapY(ParameterValue gap) {
        ParameterValueHelper.validateAsFloat(gap);
        gapY = gap;
        if (gap != null) {
            gap.setParent(this);
        }
    }


    /**
     * Get the gap, upon X direction, between two symbols. 
     * @return  
     */
    public ParameterValue getGapX() {
        return gapX;
    }

    /**
     * Get the gap, upon Y direction, between two symbols. 
     * @return  
     */
    public ParameterValue getGapY() {
        return gapY;
    }
    
    

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphic != null) {
            ls.add(graphic);
        }
        if (gapX != null) {
            ls.add(gapX);
        }
        if (gapY != null) {
            ls.add(gapY);
        }
        return ls;
    }

    @Override
    public Uom getUom() {
        return uom == null ? ((UomNode)getParent()).getUom() : uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom =uom;
     }    

    @Override
    public Uom getOwnUom() {
        return uom;
    }

   
}
