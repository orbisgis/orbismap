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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.fill;

import org.orbisgis.orbismap.style.stroke.Stroke;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.IStrokeNode;
import org.orbisgis.orbismap.style.IUom;

/**
 * A {@code HatchedFill} will fill a shape with hatches. It is configured
 * according to an angle (the orientation of the hatches), a distance (between
 * the hatches), an offset (from the default location of the hatches) and a
 * stroke (to determine how to draw the hatches).</p>
 * <p>
 * The <b>offset</b> value is used to shift hatches from the location where they
 * are printed by default. That means that, for a given geometry, it becomes
 * possible to paint multiple hatches that do not overlap by using the same
 * orientation, and using an offset so that each hatch of the second
 * {@code HatchedFill} is drawn between two hatches of the first
 * {@code HatchedFill}.</p>
 * <p>
 * The meaning of distance and offset is of course UOM dependant.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class HatchedFill extends StyleNode implements IStrokeNode, IFill, IUom {

    /**
     * Default offset value for hatches.
     */
    public static final float DEFAULT_OFFSET = 0.0f;

    /**
     * The default perpendicular distance between two hatches.
     */
    public static final float DEFAULT_PDIST = 10.0f;
    /**
     * Default orientation value for hatches.
     */
    public static final float DEFAULT_ALPHA = 45.0f;
    /**
     *
     */
    public static final double DEFAULT_NATURAL_LENGTH = 100;
    private ParameterValue angle = new NullParameterValue();
    private ParameterValue distance = new NullParameterValue();
    private ParameterValue offset = new NullParameterValue();
    private Stroke stroke;
    private Uom uom;

    /**
     * Creates a default {@code HatchedFill} with default values and a default
     * penstroke.
     */
    public HatchedFill() {
    }

    /**
     * Get the orientation of the hatches.
     *
     * @return
     */
    public ParameterValue getAngle() {
        return angle;
    }

    /**
     * Set the orientation of the hatches.
     *
     * @param angle
     */
    public void setAngle(float angle) {
        setAngle(new Literal(angle));
    }

    /**
     * Set the orientation of the hatches.
     *
     * @param angle
     */
    public void setAngle(ParameterValue angle) {
        if (angle == null) {
            this.angle = new NullParameterValue();
            this.angle.setParent(this);
        } else {
            this.angle = angle;
            this.angle.setParent(this);
            this.angle.format(Float.class, "value >=0 and value <= 180");
        }
    }

    /**
     * Get the perpendicular distance between two hatches
     *
     * @return
     */
    public ParameterValue getDistance() {
        return distance;
    }

    /**
     * Set the perpendicular distance between two hatches
     *
     * @param distance
     */
    public void setDistance(float distance) {
        setDistance(new Literal(distance));
    }

    /**
     * Set the perpendicular distance between two hatches
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
     * Get the offset of the hatches.
     *
     * @return
     */
    public ParameterValue getOffset() {
        return offset;
    }

    /**
     * Set the offset of the hatches.
     *
     * @param offset
     */
    public void setOffset(float offset) {
        setOffset(new Literal(offset));
    }

    /**
     * Set the offset of the hatches.
     *
     * @param offset
     */
    public void setOffset(ParameterValue offset) {
        if (offset == null) {
            this.offset = new NullParameterValue();
            this.offset.setParent(this);
        } else {
            this.offset = offset;
            this.offset.setParent(this);
            this.offset.format(Float.class);
        }
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (stroke != null) {
            stroke.setParent(this);
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (angle != null) {
            ls.add(angle);
        }
        if (distance != null) {
            ls.add(distance);
        }
        if (offset != null) {
            ls.add(offset);
        }
        if (stroke != null) {
            ls.add(stroke);
        }
        return ls;
    }

    @Override
    public Uom getUom() {
        return uom == null ? ((IUom) getParent()).getUom() : uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void initDefault() {
        PenStroke ps = new PenStroke();
        ps.initDefault();
        this.stroke = ps;
        this.angle = new Literal(DEFAULT_ALPHA);
        this.distance =new Literal(DEFAULT_PDIST);
        this.offset = new Literal(DEFAULT_OFFSET);
    }

}
