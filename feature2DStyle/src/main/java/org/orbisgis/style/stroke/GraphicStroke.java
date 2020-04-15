/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.stroke;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.label.RelativeOrientation;
import org.orbisgis.style.graphic.MarkGraphic;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.IGraphicNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * A {@code GraphicStroke} is used essentially to repeat a a graphic along a
 * line. It is dependant upon :
 * <ul><li>A {@link GraphicCollection} that contains the graphic to render</li>
 * <li>The length (as a {@link RealParameter}) to reserve along the line to plot
 * a single {@code Graphic} instance. Must be positive, and is defaulted to the
 * {@code Graphic} natural length.</li>
 * <li>A relative orientation, as defined in
 * {@link RelativeOrientation}.</li></ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GraphicStroke extends Stroke implements IGraphicNode, IUom {
    
    public static final float MIN_LENGTH = 1; // In pixel !

    private GraphicCollection graphics;
    private ParameterValue distance ;
    private RelativeOrientation orientation;
    private ParameterValue overlapMark;

    /**
     * Build a new, default, {@code GraphicStroke}. It is defined with a default
     * {@link MarkGraphic}, as defined in
     * {@link MarkGraphic#MarkGraphic() the default constructor}.
     */
    public GraphicStroke() {
        super();
        this.graphics = new GraphicCollection();
        this.distance = new Literal(MIN_LENGTH);
        this.overlapMark = new Literal(true);
    }
    
    @Override
    public void setGraphics(GraphicCollection graphic) {
        this.graphics = graphic;
    }
    
    @Override
    public GraphicCollection getGraphics() {
        return graphics;
    }

    /**
     * Set the distance used to plot the embedded graphic.
     *
     * @param distance
     */
    public void setDistance(float distance) {
        setDistance(new Literal(distance));
    }

    /**
     * Set the distance used to plot the embedded graphic.
     *
     * @param distance
     */
    public void setDistance(ParameterValue distance) {
        if (distance == null) {
            this.distance = new NullParameterValue();
            this.distance.setParent(this);
        } else {
            this.distance = distance;
            this.distance.setParent(this);
            this.distance.format(Float.class, "value >=0");
        }
    }
    
    /**
     * Return true is distance must be adjusted between all graphics.
     * Otherwise they overlap 
     * Default is true
     * @return 
     */
    public ParameterValue isOverlap() {
        return overlapMark;
    }
    
    /**
     * Set false if the distance between the graphics must be adjusted to avoid
     * overlapping and distribute all graphics according the distance
     * 
     * @param overlapMark
     */
    public void setOverlap(boolean overlapMark) {
        setOverlap(new Literal(overlapMark));
    }    
    
    /**
     * Set false if the distance between the graphics must be adjusted to avoid
     * overlapping and distribute all graphics according the distance
     * 
     * @param overlapMark
     */
    public void setOverlap(ParameterValue overlapMark) {
        if (overlapMark == null) {
            this.overlapMark = new Literal(true);
            this.overlapMark.setParent(this);
        } else {
            this.overlapMark = overlapMark;
            this.overlapMark.setParent(this);
            this.overlapMark.format(Boolean.class);
        }
    }

    /**
     * Get the distance used to plot the embedded graphic.
     *
     * @return
     */
    public ParameterValue getDistance() {
        return distance;
    }

    /**
     * Set the orientation of the graphic.
     *
     * @param orientation
     */
    public void setRelativeOrientation(RelativeOrientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the orientation of the graphic.
     *
     * @return
     */
    public RelativeOrientation getRelativeOrientation() {
        if (orientation != null) {
            return orientation;
        } else {
            return RelativeOrientation.PORTRAYAL;
        }
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphics != null) {
            ls.add(graphics);
        }
        if (distance != null) {
            ls.add(distance);
        }
        return ls;
    }
    
    @Override
    public void addGraphic(Graphic graphic) {
        if (graphics != null) {
            this.graphics.add(graphic);
        }
    }
    
    @Override
    public void initDefault() {
        MarkGraphic mg = new MarkGraphic();
        mg.initDefault();
        addGraphic(mg);
    }
    
}
