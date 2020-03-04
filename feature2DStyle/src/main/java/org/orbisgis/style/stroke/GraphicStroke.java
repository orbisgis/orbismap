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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.style.utils.UomUtils;
import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.IGraphicNode;
import org.orbisgis.style.graphic.Graphic;

/**
 * A {@code GraphicStroke} is used essentially to repeat a a graphic along a line. It is dependant 
 * upon :
 * <ul><li>A {@link GraphicCollection} that contains the graphic to render</li>
 * <li>The length (as a {@link RealParameter}) to reserve along the line to plot 
 * a single {@code Graphic} instance. Must be positive, and is defaulted to the 
 * {@code Graphic} natural length.</li>
 * <li>A relative orientation, as defined in {@link RelativeOrientation}.</li></ul>
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class GraphicStroke extends Stroke implements IGraphicNode, IUom {

    public static final double MIN_LENGTH = 1; // In pixel !

    private Graphic graphic;
    private RealParameter length;
    private RelativeOrientation orientation;
    private RealParameter relativePosition;

    
    /**
     * Build a new, default, {@code GraphicStroke}. It is defined with a default
     * {@link MarkGraphic}, as defined in {@link MarkGraphic#MarkGraphic() the default constructor}.
     */
    public GraphicStroke() {
        super();
        MarkGraphic mg = new MarkGraphic();
        mg.setTo3mmCircle();
        this.graphic = mg;
    }


    @Override
    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }


    @Override
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * Set the length used to plot the embedded graphic. If set to null, then this length
     * is defaulted to the natural length of the graphic.
     * @param length 
     */
    public void setLength(RealParameter length) {
        this.length = length;
        if (this.length != null) {
            this.length.setContext(RealParameterContext.NON_NEGATIVE_CONTEXT);
            this.length.setParent(this);
        }
    }

    /**
     * Get the length used to plot the embedded graphic. If {@code null}, then the length
     * of the embedded {@code Graphic} is used.
     * @return 
     */
    public RealParameter getLength() {
        return length;
    }

    /**
     * Set the orientation of the graphic.
     * @param orientation 
     */
    public void setRelativeOrientation(RelativeOrientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the orientation of the graphic.
     * @return 
     */
    public RelativeOrientation getRelativeOrientation() {
        if (orientation != null) {
            return orientation;
        } else {
            return RelativeOrientation.PORTRAYAL;
        }
    }


    public RealParameter getRelativePosition() {
        return relativePosition;
    }


    public void setRelativePosition(RealParameter relativePosition) {
        this.relativePosition = relativePosition;
        if (this.relativePosition != null){
            this.relativePosition.setContext(RealParameterContext.PERCENTAGE_CONTEXT);
            this.relativePosition.setParent(this);
        }
    }


    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphic != null) {
            ls.add(graphic);
        }
        if (length != null) {
            ls.add(length);
        }
        if (relativePosition != null) {
            ls.add(relativePosition);
        }
        return ls;
    }

    

}
