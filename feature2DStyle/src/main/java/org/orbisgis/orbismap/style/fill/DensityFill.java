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
package org.orbisgis.orbismap.style.fill;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IGraphicNode;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.IUom;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.graphic.Graphic;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;

/**
 * A {@code Fill} implementation where the content of a shape is painted
 * according to a given density and to a given mark or hatch type.</p>
 * <p>
 * If the hatches are used, ie if {@code isHatched()) is {@code true}, the inner
 * {@code PenStroke} and orientation are used. Otherwise, the shape is filled with
 * repeated mark, registered as a {@code GraphicCollection} instance.</p>
 * <p>
 * @author Alexis Guéganno, CNRS (2012-2013) 
 * @author Maxence Laurent, HEIG-VD (2010-2012) 
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class DensityFill extends StyleNode implements IGraphicNode, IFill, IUom {

    private boolean isHatched;
    private Stroke hatches;
    private ParameterValue hatchesOrientation = new NullParameterValue();
    private GraphicCollection graphics;
    private ParameterValue percentageCovered = new NullParameterValue();
    //Some constants we don't want to be considered as magic numbers.
    public static final float ONE_HUNDRED = 100;
    public static final float FIFTY = 50;
    public static final float ONE_HALF = 0.5f;

    /**
     * The default covered percentage.
     */
    public static final float DEFAULT_PERCENTAGE = 0.2f;
    private Uom uom;

    /**
     * Build a default {@code DensityFill}
     */
    public DensityFill() {
        this.graphics = new GraphicCollection();
    }

    /**
     * Set the {@link Stroke} used to draw the hatches in this {@code
     * DensityFill}.
     *
     * @param hatches
     */
    public void setHatches(Stroke hatches) {
        this.hatches = hatches;
        if (hatches != null) {
            this.isHatched = true;
            this.setGraphics(null);
            hatches.setParent(this);
        }
    }

    /**
     * Get the {@link Stroke} used to draw the hatches in this {@code
     * DensityFill}.
     *
     * @return
     */
    public Stroke getHatches() {
        return hatches;
    }

    /**
     * Set the orientation of the hatches associated to this
     * {@code DensityFill}.
     *
     * @param hatchesOrientation angle in degree
     */
    public void setHatchesOrientation(float hatchesOrientation) {
        setHatchesOrientation(new Literal(hatchesOrientation));
    }

    /**
     * Set the orientation of the hatches associated to this
     * {@code DensityFill}.
     *
     * @param hatchesOrientation angle in degree
     */
    public void setHatchesOrientation(ParameterValue hatchesOrientation) {
        if (hatchesOrientation == null) {
            this.hatchesOrientation = new NullParameterValue();
            this.hatchesOrientation.setParent(this);
        } else {
            this.hatchesOrientation = hatchesOrientation;
            this.hatchesOrientation.setParent(this);
            this.hatchesOrientation.format(Float.class, "value>=0 and value<=180");
        }
    }

    /**
     * Get the orientation of the hatches associated to this
     * {@code DensityFill}.
     *
     * @return
     */
    public ParameterValue getHatchesOrientation() {
        return hatchesOrientation;
    }

    @Override
    public void setGraphics(GraphicCollection mark) {
        this.graphics = mark;
        if (mark != null) {
            this.isHatched = false;
            mark.setParent(this);
            setHatches(null);
        }
    }

    @Override
    public GraphicCollection getGraphics() {
        return graphics;
    }

    /**
     * After using this method, marks will be preferred on hatches to render
     * this {@code DensityFill}
     */
    public void useMarks() {
        isHatched = false;
    }

    /**
     *
     * @return {@code true} if hatches are used to render this {@code
     * DensityFill}, false otherwise.
     */
    public boolean isHatched() {
        return isHatched;
    }

    /**
     *
     * @param percentageCovered percentage covered by the marks/hatches [0;100]
     */
    public void setPercentageCovered(float percentageCovered) {
        setPercentageCovered(new Literal(percentageCovered));
    }

    /**
     *
     * @param percentageCovered percentage covered by the marks/hatches [0;100]
     */
    public void setPercentageCovered(ParameterValue percentageCovered) {
        if (percentageCovered == null) {
            this.percentageCovered = new NullParameterValue();

        } else {
            this.percentageCovered = percentageCovered;
            this.percentageCovered.setParent(this);
            this.percentageCovered.format(Float.class, "value>=0 and value<=100");
        }
    }

    /**
     * Get the percentage covered by the marks/hatches.
     *
     * @return A {@code ParameterValue}.
     */
    public ParameterValue getPercentageCovered() {
        return percentageCovered;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (isHatched) {
            if (hatches != null) {
                ls.add(hatches);
            }
            if (hatchesOrientation != null) {
                ls.add(hatchesOrientation);
            }
        } else {
            if (graphics != null) {
                ls.add(graphics);
            }
        }
        if (percentageCovered != null) {
            ls.add(percentageCovered);
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
    public void addGraphic(Graphic graphic) {
        if (graphics != null) {
            this.graphics.add(graphic);
        }
    }

    @Override
    public void initDefault() {
        PenStroke ps = new PenStroke();
        ps.initDefault();
        this.hatches= new PenStroke();
        this.hatchesOrientation = new Literal(HatchedFill.DEFAULT_ALPHA);
        this.percentageCovered=new Literal(DEFAULT_PERCENTAGE);
    }
}
