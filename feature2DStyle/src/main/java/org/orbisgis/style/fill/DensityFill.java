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
import org.orbisgis.style.parameter.real.RealLiteral;
import org.orbisgis.style.parameter.real.RealParameter;
import org.orbisgis.style.parameter.real.RealParameterContext;
import org.orbisgis.style.stroke.PenStroke;

/**
 * A {@code Fill} implementation where the content of a shape is painted according 
 * to a given density and to a given mark or hatch type.</p>
 * <p>If the hatches are used, ie if {@code isHatched()) is {@code true}, the inner
 * {@code PenStroke} and orientation are used. Otherwise, the shape is filled with 
 * repeated mark, registered as a {@code GraphicCollection} instance.</p>
 * <p>In every cases, the needed coverage percentage must be specified. If not set,
 * It will be defaulted to {@code DEFAULT_PERCENTAGE}.
 * @author Alexis Guéganno
 * @author Maxence Laurent
 * @author Erwan Bocher, CNRS
 */
public final class DensityFill extends StyleNode implements IGraphicNode, IFill, UomNode {

    private boolean isHatched;
    private PenStroke hatches;
    private RealParameter orientation;
    private Graphic mark;
    private RealParameter percentageCovered;
    //Some constants we don't want to be considered as magic numbers.
    public static final double ONE_HUNDRED = 100;
    public static final double FIFTY = 50;
    public static final double ONE_HALF= 0.5;

    /**
     * The default covered percentage.
     */
    public static final double DEFAULT_PERCENTAGE = 0.2;
    private Uom uom;

    /**
     * Build a default {@code DensityFill}
     */
    public DensityFill() {
        this.setHatches(new PenStroke());
        this.setHatchesOrientation(new RealLiteral(HatchedFill.DEFAULT_ALPHA));
        this.setPercentageCovered(new RealLiteral(DEFAULT_PERCENTAGE));
    }    

    /**
     * Set the {@link PenStroke} used to draw the hatches in this {@code 
     * DensityFill}.
     * @param hatches 
     */
    public void setHatches(PenStroke hatches) {
        this.hatches = hatches;
        if (hatches != null) {
            this.isHatched = true;
            this.setGraphic(null);
            hatches.setParent(this);
        }
    }

    /**
     * Get the {@link PenStroke} used to draw the hatches in this {@code 
     * DensityFill}.
     * @return 
     */
    public PenStroke getHatches() {
        return hatches;
    }

    /**
     * Set the orientation of the hatches associated to this {@code DensityFill}.
     * @param orientation angle in degree
     */
    public void setHatchesOrientation(RealParameter orientation) {
        this.orientation = orientation;
        if (this.orientation != null) {
            this.orientation.setContext(RealParameterContext.REAL_CONTEXT);
            this.orientation.setParent(this);
        }
    }

    /**
     * Get the orientation of the hatches associated to this {@code DensityFill}.
     * @return 
     */
    public RealParameter getHatchesOrientation() {
        return orientation;
    }

    @Override
    public void setGraphic(Graphic mark) {
        this.mark = mark;
        if (mark != null) {
            this.isHatched = false;
            mark.setParent(this);
            setHatches(null);
        }
    }

    @Override
    public Graphic getGraphic() {
        return mark;
    }

    /**
     * After using this method, marks will be preferred on hatches to render this
     * {@code DensityFill}
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
     * @param percent percentage covered by the marks/hatches [0;100]
     */
    public void setPercentageCovered(RealParameter percent) {
        this.percentageCovered = percent;
        if (this.percentageCovered != null) {
            this.percentageCovered.setContext(RealParameterContext.PERCENTAGE_CONTEXT);
            this.percentageCovered.setParent(this);
        }
    }

    /**
     * Get the percentage covered by the marks/hatches.
     * @return 
     * A {@code RealParameter} that is in a {@link RealParameterContext#PERCENTAGE_CONTEXT}
     * if not null.
     */
    public RealParameter getPercentageCovered() {
        return percentageCovered;
    }

    

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (isHatched) {
            if (hatches != null) {
                ls.add(hatches);
            }
            if (orientation != null) {
                ls.add(orientation);
            }
        } else {
            if (mark != null) {
                ls.add(mark);
            }
        }
        if (percentageCovered != null) {
            ls.add(percentageCovered);
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
