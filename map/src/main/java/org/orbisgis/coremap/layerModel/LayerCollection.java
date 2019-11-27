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

import java.awt.Graphics2D;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.locationtech.jts.geom.Envelope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.coremap.layerModel.model.AbstractLayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;

public class LayerCollection extends AbstractLayer {

    private List<ILayer> layerCollection;

    public LayerCollection(String layerName) {
        super(layerName);
        layerCollection = new ArrayList<ILayer>();
    }

    /**
     * Retrieve the layer collection as a list of layers.
     *
     * @return
     */
    List<ILayer> getLayerCollection() {
        return layerCollection;
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     *
     * @param layer
     * @return
     */
    @Override
    public int getIndex(ILayer layer) {
        return layerCollection.indexOf(layer);
    }

    /**
     * Get the layer stored at the given index in the collection.
     *
     * @param index
     * @return
     */
    @Override
    public ILayer getLayer(final int index) {
        //TODO : get will throw a IndexOutOfBoundsException which is nor catch neither managed here...
        return layerCollection.get(index);
    }

    @Override
    public boolean isSerializable() {
        return true;
    }

    @Override
    public boolean addLayer(final ILayer layer) throws LayerException {
        return addLayer(layer, false);
    }

    
    /**
     * Removes the layer from the collection
     *
     * @param layerName
     * @return the layer removed or null if the layer does not exists
     * @throws LayerException
     *
     */
    @Override
    public boolean removeLayer(final String layerName) throws LayerException {
        for (int i = 0; i < size(); i++) {
            if (layerName.equals(layerCollection.get(i).getName())) {
                return removeLayer(layerCollection.get(i));
            }
        }
        return false;
    }

    /**
     * Retrieve the children of this node as an array.
     *
     * @return
     */
    @Override
    public ILayer[] getChildren() {
        if (null != layerCollection) {
            ILayer[] result = new ILayer[size()];
            return layerCollection.toArray(result);
        } else {
            return null;
        }
    }

    /**
     * Return the number of children in this collection.
     *
     * @return
     */
    private int size() {
        return layerCollection.size();
    }

    /**
     * Check if this layer is visible or not.It is visible if at least one of
     * its children is visible, false otherwise.
     *
     * @return
     * @see org.orbisgis.coremap.layerModel.ILayer#isVisible()
     */
    @Override
    public boolean isVisible() {
        for (ILayer layer : getChildren()) {
            if (layer.isVisible()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the visible attribute. We don't see this object (which is a
     * collection, not a layer) but its leaves. Consequently, whe using this
     * method, we set the visible attribute to isVisible for all the leaves of
     * this collection.
     *
     * @param isVisible
     * @throws LayerException
     * @see org.orbisgis.coremap.layerModel.ILayer#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean isVisible) throws LayerException {
        super.setVisible(isVisible);
        for (ILayer layer : getChildren()) {
            layer.setVisible(isVisible);
        }
    }

   
    /**
     *
     * @return
     */
    @Override
    public Envelope getEnvelope() {
        final GetEnvelopeLayerAction tmp = new GetEnvelopeLayerAction();
        processLayersLeaves(this, tmp);
        return tmp.getGlobalEnvelope();
    }

    @Override
    public void clearCache() {
    }

    @Override
    public boolean removeLayer(ILayer layer) throws LayerException {
        return removeLayer(layer, false);
    }

    /**
     * Inform if children can be added to this layer. It is a collection, so
     * they are.
     *
     * @return true.
     */
    @Override
    public boolean acceptsChilds() {
        return true;
    }

    /**
     * Add the LayerListener listener to this, and to each child of this.
     *
     * @param listener
     */
    @Override
    public void addLayerListenerRecursively(LayerListener listener) {
        this.addLayerListener(listener);
        layerCollection.forEach((layer) -> {
            layer.addLayerListenerRecursively(listener);
        });
    }

    /**
     * Remove the LayerListener listener of this' listeners, and of its
     * children's listeners
     *
     * @param listener
     */
    @Override
    public void removeLayerListenerRecursively(LayerListener listener) {
        this.removeLayerListener(listener);
        layerCollection.forEach((layer) -> {
            layer.removeLayerListenerRecursively(listener);
        });
    }

    
    @Override
    public boolean addLayer(ILayer layer, boolean isMoving) throws LayerException {
        return addLayer(layer, layerCollection.size(), isMoving);
    }

    @Override
    public boolean removeLayer(ILayer layer, boolean isMoving) throws LayerException {
        if (layerCollection.contains(layer)) {
            if (isMoving) {
                return layerCollection.remove(layer);
            } else {
                ILayer[] toRemove = new ILayer[]{layer};
                if (fireLayerRemovingEvent(toRemove)) {
                    if (layerCollection.remove(layer)) {
                        fireLayerRemovedEvent(toRemove);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean addLayer(ILayer layer, int index, boolean isMoving){
        if (null != layer) {
            try {
                int i = Math.min(index, this.layerCollection.size());
                setNamesRecursively(layer, getRoot().getAllLayersNames());
                layerCollection.add(i, layer);
                layer.setParent(this);
                if (!isMoving) {
                    fireLayerAddedEvent(new ILayer[]{layer});
                }
                return true;
            } catch (LayerException ex) {                
                Logger.getLogger(LayerCollection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;

    }

    @Override
    public int getLayerCount() {
        return layerCollection.size();
    }

    @Override
    public ILayer getLayerByName(String layerName) {
        for (ILayer layer : layerCollection) {
            if (layer.getName().equals(layerName)) {
                return layer;
            } else {
                ILayer ret = layer.getLayerByName(layerName);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getAllLayersNames() {
        final Set<String> result = new HashSet<String>();
        final LayerCollection lc = this;
        if (null != lc.getLayerCollection()) {
            for (ILayer layer : lc.getChildren()) {
                if (layer instanceof LayerCollection) {
                    result.addAll(layer.getAllLayersNames());
                } else {
                    result.add(layer.getName());
                }
            }
        }
        result.addAll(super.getAllLayersNames());
        return result;
    }

    
    ///////////Static methods///////////////////////////////
    /**
     * Aooky action to each leave of this layer tree
     *
     * @param root
     * @param action
     */
    public static void processLayersLeaves(ILayer root, ILayerAction action) {
        if (root instanceof LayerCollection) {
            ILayer lc = root;
            ILayer[] layers = lc.getChildren();
            for (ILayer layer : layers) {
                processLayersLeaves(layer, action);
            }
        } else {
            action.action(root);
        }
    }

    /**
     * Apply action to each node of this tree of layers.
     *
     * @param root
     * @param action
     */
    public static void processLayersNodes(ILayer root, ILayerAction action) {
        if (root instanceof LayerCollection) {
            ILayer lc = root;
            ILayer[] layers = lc.getChildren();
            for (ILayer layer : layers) {
                processLayersNodes(layer, action);
            }
        }
        action.action(root);
    }

    /**
     * Count the number of leaves in this tree of layers.
     *
     * @param root
     * @return
     */
    public static int getNumberOfLeaves(final ILayer root) {
        CountLeavesAction ila = new CountLeavesAction();
        LayerCollection.processLayersLeaves(root, ila);
        return ila.getNumberOfLeaves();
    }
    ///////////Private methods//////////////////////////

    /*
         * This method will guarantee that layer, and all its potential inner
         * layers, will have names that are not already owned by another, declared, layer.
     */
    private void setNamesRecursively(final ILayer layer,
            final Set<String> allLayersNames) throws LayerException {
        layer.setName(provideNewLayerName(layer.getName(), allLayersNames));
        if (layer instanceof LayerCollection) {
            LayerCollection lc = (LayerCollection) layer;
            if (null != lc.getLayerCollection()) {
                for (ILayer layerItem : lc.getChildren()) {
                    setNamesRecursively(layerItem, allLayersNames);
                }
            }
        }
    }   

    
    public ISpatialTable getSpatialTable() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mt, IProgressMonitor pm) throws LayerException {
        final LayerCollection lc = this;
        if (null != lc.getLayerCollection()) {
            for (ILayer layer : lc.getChildren()) {
                layer.draw(g2, mt, pm);
            }
        }            
    }

    //////////Private classes//////////////////////////
    private class GetEnvelopeLayerAction implements ILayerAction {

        private Envelope globalEnvelope;

        @Override
        public void action(ILayer layer) {
            if (null == globalEnvelope) {
                globalEnvelope = new Envelope(layer.getEnvelope());
            } else {
                globalEnvelope.expandToInclude(layer.getEnvelope());
            }
        }

        public Envelope getGlobalEnvelope() {
            return globalEnvelope;
        }
    }

    private static class CountLeavesAction implements ILayerAction {

        private int numberOfLeaves = 0;

        @Override
        public void action(ILayer layer) {
            numberOfLeaves++;
        }

        public int getNumberOfLeaves() {
            return numberOfLeaves;
        }
    }
}
