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

import java.awt.Graphics2D;
import org.locationtech.jts.geom.Envelope;
import java.beans.PropertyChangeListener;
import java.util.Set;

import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.LayerListener;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.common.Description;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;

/**
 *
 * @author Erwan Bocher
 */
public interface ILayer {

    //Properties index
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_VISIBLE = "visible";

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

    void addLayerListener(LayerListener listener);

    void removeLayerListener(LayerListener listener);

    void addLayerListenerRecursively(LayerListener listener);

    void removeLayerListenerRecursively(LayerListener listener);

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
     * Get the internal identifier of this layer This is not the displayable
     * layer label. Use the localised description title
     *
     * @return
     */
    String getName();

    /**
     * Set the internal name of the layer
     *
     * @param name
     * @throws LayerException
     */
    void setName(final String name) throws LayerException;

    /**
     *
     * @param parent to set
     * @throws org.orbisgis.coremap.layerModel.LayerException
     */
    void setParent(final ILayer parent) throws LayerException;

    /**
     *
     * @return all layer names
     */
    public Set<String> getAllLayersNames();

    /**
     * True if the layer is visible
     *
     * @return
     */
    boolean isVisible();

    /**
     * Set if the layer is visible or not
     *
     * @param isVisible
     * @throws LayerException
     */
    void setVisible(final boolean isVisible) throws LayerException;

    /**
     *
     * @return the main layer
     * @see org.orbisgis.coremap.layerModel.ILayer#getParent()
     */
    ILayer getParent();

    /**
     * Returns true if and only if we can serialize this layer in a map context.
     *
     * @return
     */
    boolean isSerializable();

    /**
     * Removes the specified child layer.
     *
     * @param layer
     * @param isMoving
     * @return true if the layer was  removed.
     * @throws LayerException
     */
    boolean removeLayer(ILayer layer, boolean isMoving) throws LayerException;

    /**
     * Removes the specified child layer.
     *
     * @param layer
     * @return the true  if the layer was removed. 
     * @throws LayerException
     */
    boolean removeLayer(ILayer layer) throws LayerException;

    /**
     * Removes the specified child layer.
     *
     * @param layerName the name of the layer
     * @return the true if the layer was removed. 
     * @throws LayerException
     */
    boolean removeLayer(String layerName) throws LayerException;

    /**
     * Adds a child layer to this {@code ILayer}.
     *
     * @param layer
     * @return 
     * @throws LayerException If this can't accept a child.
     */
    boolean addLayer(ILayer layer) throws LayerException;

    /**
     * Adds a child to this {@code ILayer}.This method may behave differently
 if {@code layer} is a layer being moved or not.
     *
     * @param layer
     * @param isMoving
     * @return 
     * @throws LayerException
     */
    boolean addLayer(ILayer layer, boolean isMoving) throws LayerException;
    
    
     boolean addLayer(ILayer layer, int index) throws LayerException;
     
    /**
     * 
     * @param layer
     * @param index
     * @param isMoving
     * @return
     * @throws LayerException 
     */
    public boolean addLayer(ILayer layer, int index, boolean isMoving) throws LayerException;
    

    /**
     * Gets the layer with the specified name. It searches in all the subtree
     * that has as root this layer. If there is no layer with that name returns
     * null
     *
     * @param layerName
     * @return
     */
    ILayer getLayerByName(String layerName);

    /**
     * Gets the envelope of the layer
     *
     * @return
     */
    public Envelope getEnvelope();

    /**
     * Clear cached attributes in this layer
     */
    public void clearCache();

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
     * Gets the position index of the layer
     *
     * @param targetLayer
     * @return
     */
    int getIndex(ILayer targetLayer);

    /**
     * Return all the layers from tree layer model.
     *
     * @return
     */
    ILayer[] getLayersRecursively();

    ILayer[] getLayerPath();

    /**
     * Moves this in {@code layer} at index {@code index}.
     *
     * @param layer
     * @param index
     * @throws LayerException
     */
    void moveTo(ILayer layer, int index) throws LayerException;

    void moveTo(ILayer layer) throws LayerException;

    
    /**
     * Gets the number of layers under this layer
     *
     * @return
     */
    int getLayerCount();

    /**
     * Gets the specified child layer
     *
     * @param index
     * @return
     */
    public ILayer getLayer(final int index);    

        
    void draw(Graphics2D g2, MapTransform mt,IProgressMonitor pm) throws LayerException ;

}
