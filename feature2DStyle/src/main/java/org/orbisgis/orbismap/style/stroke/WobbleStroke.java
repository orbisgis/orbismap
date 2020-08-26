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
package org.orbisgis.orbismap.style.stroke;

import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IFillNode;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom stroke for linear features which adds random points to the input Shape to draw.
 * WobbleStroke is useful to create effects on a  Stroke.
 * It is designed according to :
 * <ul><li>A {@link IFillNode} value</li>
 * <li>A width</li>
 * <li>A detail</li>
 * <li>An amplitude</li>
 * <li>A way to draw the extremities of the lines</li>
 * <li>A way to draw the joins between the segments of the lines</li>
 * <li>An array of dashes, that is used to draw the lines. The array is stored
 * as a StringParamater, that contains space separated double values. This
 * double values are used to determine the length of each opaque part (even
 * elements of the array) and the length of each transparent part (odd elements
 * of the array). If an odd number of values is given, the pattern is expanded
 * by repeating it twice to give an even number of values.</li>
 * <li>An offset used to know where to draw the line.</li>
 * </ul>
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class WobbleStroke extends PenStroke {

    private ParameterValue amplitude = new NullParameterValue();
    private ParameterValue detail = new NullParameterValue();

    public WobbleStroke(){

    }

    /**
     *
     * @param amplitude
     */
    public void setAmplitude(float amplitude) {
        setAmplitude(new Literal(amplitude));
    }

    /**
     *
     * @param amplitude
     */
    public void setAmplitude(ParameterValue amplitude) {
        if (amplitude == null) {
            this.amplitude = new NullParameterValue();
            this.amplitude.setParent(this);
        } else {
            this.amplitude = amplitude;
            this.amplitude.setParent(this);
            this.amplitude.format(Float.class);
        }
    }

    /**
     *
     * @param detail
     */
    public void setDetail(float detail) {
        setDetail(new Literal(detail));
    }

    /**
     *
     * @param detail
     */
    public void setDetail(ParameterValue detail) {
        if (detail == null) {
            this.detail = new NullParameterValue();
            this.detail.setParent(this);
        } else {
            this.detail = amplitude;
            this.detail.setParent(this);
            this.detail.format(Float.class);
        }
    }

    public ParameterValue getDetail() {
        return detail;
    }

    public ParameterValue getAmplitude() {
        return amplitude;
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = super.getChildren();
        if (amplitude != null) {
            ls.add(amplitude);
        }
        if (detail != null) {
            ls.add(detail);
        }
        return ls;
    }
}
