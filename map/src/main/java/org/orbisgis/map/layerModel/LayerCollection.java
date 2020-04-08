/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.layerModel;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.api.ILayer;
import org.orbisgis.map.api.ILayerAction;
import org.orbisgis.map.api.IProgressMonitor;
import org.orbisgis.map.api.LayerException;

/**
 *
 * A layer that can contain layer(s)
 * 
 * @author Erwan Bocher, CNRS (2010-2020)
 */
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


    @Override
    public boolean isVisible() {
        for (ILayer layer : getChildren()) {
            if (layer.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        for (ILayer layer : getChildren()) {
            layer.setVisible(isVisible);
        }
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mt, IProgressMonitor pm) throws LayerException {
        List<ILayer> layers = getLayerCollection();
        for (ILayer layer : layers) {
            layer.draw(g2, mt, pm);
        }
    }

    @Override
    public void add(ILayer layer) {
        insert(layer, layerCollection.size());
    }

    @Override
    public void remove(ILayer layer) {
        if (layerCollection.contains(layer)) {
            ILayer[] toRemove = new ILayer[]{layer};
            if (fireLayerRemovingEvent(toRemove)) {
                if (layerCollection.remove(layer)) {
                    fireLayerRemovedEvent(toRemove);
                }
            }
        }
    }

    @Override
    public MapEnvelope getEnvelope() {
        final GetEnvelopeLayerAction tmp = new GetEnvelopeLayerAction();
        processLayersLeaves(this, tmp);
        return tmp.getGlobalEnvelope();
    }

    private class GetEnvelopeLayerAction implements ILayerAction {

        private Envelope globalEnvelope;

        @Override
        public void action(ILayer layer) {
            if (null == globalEnvelope) {
                globalEnvelope =  (Envelope) layer.getEnvelope();
            } else {
                globalEnvelope.expandToInclude((Coordinate) layer.getEnvelope());
            }
        }

        public MapEnvelope getGlobalEnvelope() {
            return new MapEnvelope(globalEnvelope);
        }
    }

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

    @Override
    public void insert(ILayer layer, int index) {
        if (null != layer) {
            int i = Math.min(index, this.layerCollection.size());
            setNamesRecursively(layer, getRoot().getAllLayersNames());
            layerCollection.add(i, layer);
            layer.setParent(this);
            fireLayerAddedEvent(new ILayer[]{layer});
        }
    }

    @Override
    public boolean acceptsChilds() {
        return true;
    }

    @Override
    public ILayer[] getChildren() {
        if (null != layerCollection) {
            ILayer[] result = new ILayer[layerCollection.size()];
            return layerCollection.toArray(result);
        } else {
            return null;
        }
    }

    /*
    * This method will guarantee that layer, and all its potential inner
    * layers, will have names that are not already owned by another, declared, layer.
     */
    private void setNamesRecursively(final ILayer layer,
            final Set<String> allLayersNames) {
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

}
