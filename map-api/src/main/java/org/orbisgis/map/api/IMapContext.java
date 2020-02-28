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

/**
 * This interface provides information to the tool system and receives
 * notifications from it.Also registers the tool system as a listener in order
 * to notify it about certain events during edition
 *
 * @param <D>
 * @param <M>
 */
public interface IMapContext<D extends IDescription, M extends IMapEnvelope> {
    

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public D getDescription();

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(D description);

    /**
     * Use the description and return the most appropriate Title for the default
     * Locale.
     *
     * @return
     */
    public String getTitle();


    /**
     * Gets the root layer of the layer collection in this mapcontext
     *
     * @return
     * @throws org.orbisgis.map.api.LayerException
     * @throws IllegalStateException If the map is closed
     */
    ILayer getLayerModel() throws LayerException;

         

    /**
     * This method is uses instead of get id to have a unique id for a
     * mapcontext that cannot change.Based on time creation
     *
     * @return a unique identifier for the mapContext
     */
    long getIdTime();

    /**
     * Get the bounding box of all the visible layers in this MapContext. If the
     * bounding box cannot be determined return null
     *
     * @return
     */
    public M getBoundingBox();

    /**
     * Set a bounding box for this MapContext
     *
     * @param boundingBox
     */
    void setBoundingBox(M boundingBox);

        
    
    /*
        * Add a new {@link ILayer} 
     */
    void add(ILayer layer) throws  LayerException;

    /**
     * Removes the given layer, if exists
     *
     * @param layer the {@link ILayer} to be removed
     * @return {@code true} if the layer was removed
     */
     void remove(ILayer layer);

}
