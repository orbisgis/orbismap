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
package org.orbisgis.style.graphic;

import java.awt.Color;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StrokeNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.parameter.TransformParameter;
import org.orbisgis.style.stroke.PenStroke;

/**
 * A {@code MarkGraphic} is created by stroking and filling a geometry line or
 * shape. It is built using the following parameters :
 * <ul><li>A definition of the contained graphic, that can be exclusively of one
 * of these types :
 * <ul><li> WellKnownText : as defined in {@link WellKnownName}.</li>
 * <li>A unit of measure</li>
 * <li>A viewbox, as described in {@link ViewBox}</li>
 * <li>A {@link Transform}, that describes an affine transformation that must be
 * applied on the mark.</li>
 * <li>A {@link Halo}</li>
 * <li>A {@link Fill}</li>
 * <li>A {@link Stroke}</li>
 * <li>A perpendicular offset</li>
 * </ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class MarkGraphic extends Graphic implements FillNode, StrokeNode,
        IUom, TransformNode {

    /**
     * The default size used to build {@code MarkGraphic} instances.
     */
    public static float DEFAULT_SIZE = 3;
    //private MarkGraphicSource source;
    private Uom uom;
    private TransformParameter transform;
    private ParameterValue wellKnownName;
    private Halo halo;
    private IFill fill;
    private Stroke stroke;
    private GraphicSize graphicSize;

    /**
     * Build an empty MarkGraphic
     */
    public MarkGraphic() {
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    @Override
    public TransformParameter getTransform() {
        return transform;
    }

    @Override
    public void setTransform(TransformParameter transform) {
        if (transform == null) {

        } else {
            this.transform = transform;
            this.transform.setParent(this);
        }
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    @Override
    public void setFill(IFill fill) {
        this.fill = fill;
        if (this.fill != null) {
            this.fill.setParent(this);
        }
    }

    /**
     * Get the {@link Halo} defined around this {@code MarkGraphic}.
     *
     * @return The Halo drawn around the symbol
     */
    public Halo getHalo() {
        return halo;
    }

    /**
     * Set the {@link Halo} defined around this {@code MarkGraphic}.
     *
     * @param halo The new halo to draw around the symbol.
     */
    public void setHalo(Halo halo) {
        this.halo = halo;
        if (this.halo != null) {
            this.halo.setParent(this);
        }
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (this.stroke != null) {
            this.stroke.setParent(this);
        }
    }

    /**
     * Gets the WellKnownName defining this {@code MarkGraphic}.
     *
     * @return the well-known name currently used, as a ParameterValue.
     */
    public ParameterValue getWellKnownName() {
        return wellKnownName;
    }

    /**
     * Sets the WellKnownName defining this {@code MarkGraphic}.
     *
     * @param wellKnownName The new well-known name to use, as a String value.
     */
    public void setWellKnownName(String wellKnownName) {
        setWellKnownName(new Literal(wellKnownName));
    }

    /**
     * Sets the WellKnownName defining this {@code MarkGraphic}.
     *
     * @param wkn The new well-known name to use, as a ParameterValue.
     */
    public void setWellKnownName(ParameterValue wkn) {
        if (wkn == null) {
            this.wellKnownName = new NullParameterValue();
            this.wellKnownName.setParent(this);
        } else {
            this.wellKnownName = wkn;
            this.wellKnownName.setParent(this);
            this.wellKnownName.format(String.class);
        }
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (wellKnownName != null) {
            ls.add(wellKnownName);
        }
        if (graphicSize != null) {
            ls.add(graphicSize);
        }
        if (halo != null) {
            ls.add(halo);
        }
        if (fill != null) {
            ls.add(fill);
        }
        if (stroke != null) {
            ls.add(stroke);
        }
        if (transform != null) {
            ls.add(transform);
        }
        return ls;
    }

    @Override
    public GraphicSize getGraphicSize() {
        return this.graphicSize;
    }

    @Override
    public void setGraphicSize(GraphicSize graphicSize) {
        this.graphicSize = graphicSize;
        if (this.graphicSize != null) {
            this.graphicSize.setParent(this);
        }
    }

    @Override
    public void initDefault() {
        setUom(Uom.MM);
        setWellKnownName(new Literal("circle"));
        setGraphicSize(new ViewBox(DEFAULT_SIZE, DEFAULT_SIZE));
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(Color.GRAY);
        solidFill.setOpacity(1);
        setFill(solidFill);
        PenStroke penStroke = new PenStroke();
        SolidFill solidStroke = new SolidFill();
        solidStroke.setColor(Color.BLACK);
        solidStroke.setOpacity(1);
        penStroke.setFill(solidStroke);
        penStroke.setWidth(1);
        setStroke(penStroke);
    }

}
