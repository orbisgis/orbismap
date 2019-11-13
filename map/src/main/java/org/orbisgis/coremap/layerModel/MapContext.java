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
package org.orbisgis.coremap.layerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import org.cts.crs.CoordinateReferenceSystem;
import org.slf4j.*;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.ImageRenderer;
import org.orbisgis.coremap.renderer.Renderer;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;

/**
 * Class that contains the status of the Map .
 *
 *
 *
 */
public final class MapContext extends BeanMapContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapContext.class);
    private ArrayList<MapContextListener> listeners = new ArrayList<MapContextListener>();
    
    private boolean open = false;
    private long idTime;

    /**
     * Default constructor
     */
    public MapContext() {
        setRootLayer(createLayerCollection("root"));
        idTime = System.currentTimeMillis();
    }


    @Override
    public ILayer createLayerCollection(String layerName) {
        return new LayerCollection(layerName);
    }

    private void setRootLayer(ILayer newRoot) {
        super.setLayerModel(newRoot);
    }

    @Override
    protected void setLayerModel(ILayer newRoot) {
        setRootLayer(newRoot);
    }

    @Override
    public void addMapContextListener(MapContextListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeMapContextListener(MapContextListener listener) {
        listeners.remove(listener);
    }

    @Override
    public ILayer getLayerModel() {
        return super.getLayerModel();
    }

    @Override
    public long getIdTime() {
        return idTime;
    }

    @Override
    public ILayer[] getLayers() {
        return getLayerModel().getLayersRecursively();
    }

    @Override
    public ILayer[] getSelectedLayers() {
        
        return super.getSelectedLayers();
    }

    @Override
    public void setSelectedLayers(ILayer[] selectedLayers) {
        ArrayList<ILayer> filtered = new ArrayList<ILayer>();
        for (ILayer layer : selectedLayers) {
            if (layerModel.getLayerByName(layer.getName()) != null) {
                filtered.add(layer);
            }
        }
        super.setSelectedLayers(filtered.toArray(new ILayer[filtered.size()]));

        //DEPRECATED LISTENERS
        listeners.forEach((listener) -> {
            listener.layerSelectionChanged(this);
        });
    }

    
    @Override
    public boolean isLayerModelSpatial() {
        ILayer[] layers = getLayers();
        for (ILayer l : layers) {
            if (!l.acceptsChilds()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implementation of public draw method
     *
     * @param mt Contain the extent and the image to draw on
     * @param pm Object to report process and check the cancelled condition
     * @param layer Draw recursively this layer
     * @throws IllegalStateException If the map is closed
     */
    private void drawImpl(MapTransform mt, IProgressMonitor pm, ILayer layer) throws IllegalStateException {        
        Renderer renderer = new ImageRenderer();
        renderer.draw(mt, layer, pm);
    }

    @Override
    public void draw(MapTransform mt, IProgressMonitor pm, ILayer layer) {
        //Layer must be from this layer model
        if (!isLayerFromThisLayerModel(layer)) {
            throw new IllegalStateException("Layer provided for drawing is not from the map context layer model.");
        }
        drawImpl(mt, pm, layer);
    }

    @Override
    public void draw(MapTransform mt,
            IProgressMonitor pm) {
        drawImpl(mt, pm, getLayerModel());
    }

    /**
     * Search recursively for the specified layer in the layer model
     *
     * @param layer Searched layer
     * @return True if the layer is in the map context layer model
     */
    private boolean isLayerFromThisLayerModel(ILayer layer) {
        ILayer[] allLayers = getLayerModel().getLayersRecursively();
        return Arrays.asList(allLayers).contains(layer);
    }

    

    @Override
    public ILayer getActiveLayer() {
        return activeLayer;
    }

    @Override
    public void setActiveLayer(ILayer activeLayer) {
        ILayer lastActive = this.activeLayer;
        this.activeLayer = activeLayer;

        propertyChangeSupport.firePropertyChange(PROP_ACTIVELAYER, lastActive, activeLayer);
    }


    @Override
    public void addLayer(ILayer layer) {
        try {
            getLayerModel().addLayer(layer);
        } catch (LayerException ex) {
            java.util.logging.Logger.getLogger(MapContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean removeLayer(ILayer layer) {
         try {
            return getLayerModel().removeLayer(layer);         
         } catch (LayerException ex) {
            java.util.logging.Logger.getLogger(MapContext.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }

    @Override
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
        
}
