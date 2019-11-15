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
package org.orbisgis.coremap.layerModel.model;

import org.locationtech.jts.geom.Envelope;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import org.cts.crs.CoordinateReferenceSystem;

import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.Style;
import org.orbisgis.coremap.renderer.se.common.Description;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.MapContextListener;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;

/**
 * This interface provides information to the tool system and receives
 * notifications from it. Also registers the tool system as a listener in order
 * to notify it about certain events during edition
 */
public interface IMapContext {
    //Properties index

    public static final String PROP_BOUNDINGBOX = "boundingBox";
    public static final String PROP_SELECTEDLAYERS = "selectedLayers";
    public static final String PROP_SELECTEDSTYLES = "selectedStyles";
    public static final String PROP_ACTIVELAYER = "activeLayer";
    public static final String PROP_LAYERMODEL = "layerModel";
    public static final String PROP_COORDINATEREFERENCESYSTEM = "coordinateReferenceSystem";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_LOCATION = "location";

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public Description getDescription();

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(Description description);

    /**
     * Use the description and return the most appropriate Title for the default
     * Locale.
     *
     * @return
     */
    public String getTitle();

    /**
     * Add a property-change listener for all properties. The listener is called
     * for all properties.
     *
     * @param listener The PropertyChangeListener instance
     * @note Use EventHandler.create to build the PropertyChangeListener
     * instance
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Add a property-change listener for a specific property. The listener is
     * called only when there is a change to the specified property.
     *
     * @param prop The static property name PROP_..
     * @param listener The PropertyChangeListener instance
     * @note Use EventHandler.create to build the PropertyChangeListener
     * instance
     */
    void addPropertyChangeListener(String prop, PropertyChangeListener listener);

    /**
     * Remove the specified listener from the list
     *
     * @param listener The listener instance
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove the specified listener for a specified property from the list
     *
     * @param prop The static property name PROP_..
     * @param listener The listener instance
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener listener);

    /**
     * Return a new layer group
     *
     * @param layerName Internal layer index
     * @return A layer group
     * @throws LayerException The creation of the layer fail
     */
    ILayer createLayerCollection(String layerName) throws LayerException;

    /**
     * Gets the root layer of the layer collection in this mapcontext
     *
     * @return
     * @throws IllegalStateException If the map is closed
     */
    ILayer getLayerModel() throws IllegalStateException;

    /**
     * Gets all the layers in the map context
     *
     * @return
     * @throws IllegalStateException If the map is closed
     */
    public ILayer[] getLayers() throws IllegalStateException;

    /**
     * Gets the selected layers
     *
     * @return
     * @throws IllegalStateException If the map is closed
     */
    public ILayer[] getSelectedLayers() throws IllegalStateException;

    /**
     * Adds a listener for map context events
     *
     * @param listener
     */
    public void addMapContextListener(MapContextListener listener);

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
    public Envelope getBoundingBox();

    /**
     * Set a bounding box for this MapContext
     *
     * @param extent
     */
    void setBoundingBox(Envelope extent);

    /**
     * Removes a listener for map context events
     *
     * @param listener
     */
    public void removeMapContextListener(MapContextListener listener);

    /**
     * Sets the selected layers. If the specified layers are not in the map
     * context they are removed from selection.
     *
     * @param selectedLayers
     * @throws IllegalStateException If the map is closed
     */
    public void setSelectedLayers(ILayer[] selectedLayers)
            throws IllegalStateException;

    

    /**
     * Gets the layer where all the edition actions take place
     *
     * @return
     * @throws IllegalStateException If the map is closed
     */
    ILayer getActiveLayer() throws IllegalStateException;

    /**
     * Sets the layer where all the edition actions take place
     *
     * @param activeLayer
     * @return
     * @throws IllegalStateException If the map is closed
     */
    void setActiveLayer(ILayer activeLayer) throws IllegalStateException;

    /**
     * Returns true if the inner layer model contains actual layers (ie not only
     * layer collections).
     *
     * @return
     */
    boolean isLayerModelSpatial();

    /**
     * get the mapcontext {@link CoordinateReferenceSystem}
     *
     * @return
     */
    // CoordinateReferenceSystem getCoordinateReferenceSystem();
    /**
     * set the {@link CoordinateReferenceSystem} to the mapcontext
     *
     * @param crs
     */
    void setCoordinateReferenceSystem(CoordinateReferenceSystem crs);

    /*
        * Add a new {@link ILayer} 
     */
    void addLayer(ILayer layer);

    /**
     * Removes the given layer, if exists
     *
     * @param layer the {@link ILayer} to be removed
     * @return {@code true} if the layer was removed
     */
    public boolean removeLayer(ILayer layer);

}
