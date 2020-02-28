/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.layerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.map.api.ILayer;

/**
 *
 * @author ebocher
 */
public abstract class AbstractLayer implements ILayer<MapTransform, Description, MapEnvelope> {

    boolean visible = true;
    String name = "Layer";
    private Description description;
    private ILayer parent;
    protected ArrayList<LayerListener> listeners = new ArrayList<LayerListener>();

    public AbstractLayer(String name) {
        this.name = name;
        description = new Description();
        description.addTitle(Locale.getDefault(), name);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ILayer getParent() {
        return parent;
    }

    @Override
    public void setParent(final ILayer parent) {
        this.parent = parent;
    }

    /**
     * Get the root layer
     *
     * @return
     */
    public ILayer getRoot() {
        ILayer root = this;
        while (null != root.getParent()) {
            root = root.getParent();
        }
        return root;
    }

    @Override
    public Set<String> getAllLayersNames() {
        final Set<String> result = new HashSet<String>();
        result.add(getName());
        return result;
    }

    @Override
    public void moveTo(ILayer layer, int index) {
        ILayer oldParent = getParent();
        oldParent.remove(this);
        layer.insert(this, index);
        fireLayerMovedEvent(oldParent, this);
    }

    @Override
    public void add(ILayer layer) {
        throw new IllegalArgumentException("This layer cannot contains another layer");
    }

    @Override
    public void remove(ILayer layer) {
        throw new IllegalArgumentException("This layer cannot contains another layer");
    }
    
    @Override
    public void insert(ILayer layer, int index) {
        throw new IllegalArgumentException("This layer cannot contains another layer");
    }

    @Override
    public Description getDescription() {
        return description;
    }
    
    @Override
    public ILayer[] getChildren() {
        return new ILayer[0];
    }
    
    @Override
    public boolean acceptsChilds() {
        return false;
    }

    /**
     * Event if the layer is moved
     *
     * @param parent
     * @param layer
     */
    private void fireLayerMovedEvent(ILayer parent, ILayer layer) {
        LayerCollectionEvent evt = new LayerCollectionEvent(parent,
                new ILayer[]{layer});
        LayerCollectionEvent ev2 = new LayerCollectionEvent(layer.getParent(),
                new ILayer[]{layer});
        for (LayerListener listener : listeners) {
            listener.layerMoved(evt);
            listener.layerMoved(ev2);
        }

    }

    /**
     * Event if a new layer is added
     *
     * @param added
     */
    protected void fireLayerAddedEvent(ILayer[] added) {
        ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners
                .clone();
        for (LayerListener listener : l) {
            listener.layerAdded(new LayerCollectionEvent(this, added));
        }
    }

    /**
     * Event when the layer is removed
     *
     * @param toRemove
     * @return
     */
    protected boolean fireLayerRemovingEvent(ILayer[] toRemove) {
        ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners
                .clone();
        for (LayerListener listener : l) {
            if (!listener
                    .layerRemoving(new LayerCollectionEvent(this, toRemove))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Event if a layer(s) is (are) removed
     *
     * @param removed
     */
    protected void fireLayerRemovedEvent(ILayer[] removed) {
        ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners
                .clone();
        for (LayerListener listener : l) {
            listener.layerRemoved(new LayerCollectionEvent(this, removed));
        }
    }

    /*
         * Check that name is not already contained in allLayersNames.
         * If it is in, a new String is created and returned, with the form name_i
         * where i is as small as possible.
     */
    protected String provideNewLayerName(final String name,
            final Set<String> allLayersNames) {
        String tmpName = name;
        if (allLayersNames.contains(tmpName)) {
            int i = 1;
            while (allLayersNames.contains(tmpName + "_" + i)) { //$NON-NLS-1$
                i++;
            }
            tmpName += "_" + i; //$NON-NLS-1$
        }
        allLayersNames.add(tmpName);
        return tmpName;
    }

}
