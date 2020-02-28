/**
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
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2017
 * CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.api;

import java.awt.Graphics2D;
import java.util.Set;

/**
 *
 * @author Erwan Bocher
 * @param <MT>
 * @param <D>
 * @param <M>
 */
public interface ILayer<MT extends IMapTransform, D extends IDescription, M extends IMapEnvelope> {

    /**
     * Moves this in {@code layer} at index {@code index}.
     *
     * @param layer
     * @param index
     */
    void moveTo(ILayer layer, int index);

    void add(ILayer layer);

    void remove(ILayer layer);

    /**
     * Insert a layer at a specific position
     *
     * @param layer
     * @param index
     */
    void insert(ILayer layer, int index);

    /**
     * Get the internal identifier of this {@code ILayerNode}
     *
     * @return
     */
    String getName();

    /**
     * Set the internal name of the {@code ILayerNode}
     *
     * @param name
     */
    void setName(final String name);

    /**
     * True if the {@code ILayerNode} is visible
     *
     * @return
     */
    boolean isVisible();

    /**
     * Set if the {@code ILayerNode} is visible or not
     *
     * @param isVisible
     */
    void setVisible(final boolean isVisible);

    D getDescription();

    /**
     *
     * @return
     */
    M getEnvelope();

    /**
     *
     * @return the main layer
     */
    ILayer getParent();

    /**
     *
     * @param parent to set
     */
    void setParent(final ILayer parent);

    /**
     * Return true if the layer accepts childs. This means it's a group of layer
     *
     * @return
     */
    boolean acceptsChilds();

    /**
     * Gets all layer childs under this layer. If the layer doesn't accept
     * childs return an empty layer
     *
     * @return
     */
    ILayer[] getChildren();
    
    /**
     *
     * @return all layer names
     */
    Set<String> getAllLayersNames();

    /**
     * Implements this method to draw the ILayer
     *
     * @param g2
     * @param mt
     * @param pm
     * @throws LayerException
     */
    void draw(Graphics2D g2, MT mt, IProgressMonitor pm) throws LayerException;

}
