/**
 * MAP-API is part of the OrbisGIS platform
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
 * MAP-API  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * MAP-API  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * MAP-API  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MAP-API. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.api;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Erwan Bocher
 * @param <M>
 */
public interface IMapTransform<M extends IMapEnvelope> {

    /**
     * @return The currently configured dot-per-inch measure.
     */
    public double getDpi();

    /**
     * Gets the scale denominator. If the scale is 1:1000 this method returns
     * 1000. The scale is not absolutely precise and errors of 2% have been
     * measured.
     *
     *
     * @return the scale as double value
     */
    public double getScaleDenominator();

    /**
     * Gets the current {@code RenderingHints}
     *
     * @return the current {@link RenderingHints}
     */
    public RenderingHints getRenderingHints();

    /**
     * Gets the current {@code AffineTransform}
     *
     * @return the current {@link AffineTransform}
     */
    public AffineTransform getAffineTransform();

    /**
     * Sets the extent of the transformation. This extent is not used directly
     * to calculate the transformation but is adjusted to obtain an extent with
     * the same ratio than the image
     *
     * @param newExtent The new base extent.
     */
    public void setExtent(M newExtent);

    /**
     * Gets the extent
     *
     * @return
     */
    public M getExtent();
    

}
