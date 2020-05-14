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
package org.orbisgis.orbismap.map.layerModel;

import java.beans.PropertyChangeSupport;
import org.locationtech.jts.geom.Envelope;
import org.slf4j.*;
import org.orbisgis.orbismap.map.api.ILayer;
import org.orbisgis.orbismap.map.api.IMapContext;
import org.orbisgis.orbismap.map.api.LayerException;

/**
 * Class that contains the status of the Map .
 *
 *
 *
 */
public final class MapContext implements IMapContext<Description, MapEnvelope> {

    public static final String PROP_BOUNDINGBOX = "boundingBox";
    public static final String PROP_SELECTEDLAYERS = "selectedLayers";
    public static final String PROP_SELECTEDSTYLES = "selectedStyles";
    public static final String PROP_ACTIVELAYER = "activeLayer";
    public static final String PROP_LAYERMODEL = "layerModel";
    public static final String PROP_COORDINATEREFERENCESYSTEM = "coordinateReferenceSystem";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_LOCATION = "location";
    //Listener container
    protected transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private static final Logger LOGGER = LoggerFactory.getLogger(MapContext.class);

    private long idTime;

    protected Description description = new Description();
    private ILayer layerModel;
    protected MapEnvelope boundingBox = null;

    protected int epsg_code = 0;

    /**
     * Default constructor
     */
    public MapContext() {
        setRootLayer(new LayerCollection("root"));
        idTime = System.currentTimeMillis();
    }

    private void setRootLayer(ILayer newRoot) {
        ILayer oldLayerModel = this.layerModel;
        this.layerModel = newRoot;
        propertyChangeSupport.firePropertyChange(PROP_LAYERMODEL, oldLayerModel, layerModel);
    }

    @Override
    public ILayer getLayerModel() {
        return layerModel;
    }

    @Override
    public long getIdTime() {
        return idTime;
    }

    @Override
    public void add(ILayer layer)  {
         getLayerModel().add(layer);
    }

    @Override
    public void remove(ILayer layer) {
         getLayerModel().remove(layer);
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public void setDescription(Description description) {
        Description oldDescription = this.description;
        this.description = description;
        propertyChangeSupport.firePropertyChange(PROP_DESCRIPTION, oldDescription, description);
    }

    @Override
    public String getTitle() {
        return description.getDefaultTitle();
    }

    @Override
    public MapEnvelope getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void setBoundingBox(MapEnvelope bBox) {
        if ((bBox == null && boundingBox != null)
                || (bBox != null && !bBox.equals(this.boundingBox))) {
            Envelope oldBoundingBox = this.boundingBox;
            this.boundingBox = bBox;
            propertyChangeSupport.firePropertyChange(PROP_BOUNDINGBOX, oldBoundingBox, bBox);
        }
    }

    /**
     * Get the value of the EPSG code
     *
     * @return the value of the EPSG code
     */
    public int getCoordinateReferenceSystem() {
        return epsg_code;
    }

    /**
     * Set the value of the EPSG code
     *
     * @param epsg new value of the EPSG code
     */
    public void setCoordinateReferenceSystem(int epsg) {
        int oldEPSG_code = this.epsg_code;
        this.epsg_code = oldEPSG_code;
        propertyChangeSupport.firePropertyChange(PROP_COORDINATEREFERENCESYSTEM, oldEPSG_code, epsg);
    }

    

}
