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
package org.orbisgis.orbismap.style.transform;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import org.orbisgis.orbismap.style.*;
import org.orbisgis.orbismap.style.StyleNode;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.ITransform;

/**
 *
 * This class contains a collection of {@code ITransformation}s.
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class Transform extends StyleNode implements IUom {

    private Uom uom;
    private AffineTransform consolidated;
    private ArrayList<ITransform> transformations;

    /**
     * Gets a {@code String} representation of this {@code Transform}, by
     * printing the {@code String} representation of each inner
     * {@code Transformation}.
     *
     * @return A {@code String} representation of this {@code Transform}
     */
    @Override
    public String toString() {
        String r = "";
        for (ITransform t : transformations) {
            r += t.toString();
        }
        return r;
    }

    /**
     * Build a new {@code Transform}, that does not contain any {@code
     * Transformation}
     */
    public Transform() {
        transformations = new ArrayList<ITransform>();
        consolidated = null;
    }

    /**
     * Move the ith {@code Transformation} up in this {@code Transform}.
     *
     * @param i
     * @return {@code true} if the ith exists and has been moved successfully
     * (ie if i represents an existing element that is not the last one.)
     */
    public boolean moveDown(int i) {
        if (i >= 0 && i < transformations.size() - 1) {
            ITransform remove = transformations.remove(i);
            transformations.add(i + 1, remove);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Move the ith {@code Transformation} down in this {@code Transform}.
     *
     * @param i
     * @return {@code true} if the ith exists and has been moved successfully
     * (ie if i represents an existing element that is not the first one.)
     */
    public boolean moveUp(int i) {
        if (i > 0 && i < transformations.size()) {
            ITransform remove = transformations.remove(i);
            transformations.add(i - 1, remove);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a {@code Transformation} to this {@code Transform}.
     *
     * @param t
     */
    public void addTransformation(ITransform t) {
        transformations.add(t);
    }

    /**
     * Remove the ith {@code Transformation} of this {@code Transform}
     *
     * @param i
     * @return {@code true} if the removal has been successful.
     */
    public boolean removeTransformation(int i) {
        try {
            transformations.remove(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the number of {@code Transformation}s registered in this
     * {@code Transform}.
     *
     * @return
     */
    public int getNumTransformation() {
        return transformations.size();
    }

    /**
     * Get the ith {@code Transformation} registered in this {@code
     * Transform}.
     *
     * @param i
     * @return
     */
    public ITransform getTransformation(int i) {
        return transformations.get(i);
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<>();
        ls.addAll(transformations);
        return ls;
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

    /**
     * Return an affine transformation for java Shape object.The purpose is to
     * transfom se.graphics
     *
     * @param dpi
     * @param scaleDenominator
     * @param width
     * @param height
     * @return
     * @throws ParameterException
     */
    public AffineTransform getAffineTransform(
            double dpi,
            double scaleDenominator, Float width, Float height)
            throws ParameterException {
        // Result is Identity
        consolidated = new AffineTransform();
        for (ITransform t : transformations) {
            AffineTransform at = t.getAffineTransform(this.getUom(), scaleDenominator, dpi, width, height);
            consolidated.preConcatenate(at);
        }
        return consolidated;
    }

}
