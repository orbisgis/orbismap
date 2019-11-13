/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.coremap.layerModel;

import org.locationtech.jts.geom.Envelope;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingWorker;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.renderer.se.Style;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;

public class Layer extends BeanLayer {
    // When dataURI is not specified, this layer use the tableReference instead of external URI
    private static final String JDBC_REFERENCE_SCHEME = "WORKSPACE";

    private ISpatialTable spatialTable;
    private URI dataURI;
    private Envelope envelope = new Envelope();
    private ExecutorService executorService = null;

    public Layer(String name, ISpatialTable spatialTable) {
        super(name);
        this.spatialTable = spatialTable;
    }
    
    public Layer(ISpatialTable spatialTable) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;        
    }
    
    public Layer(ISpatialTable spatialTable, Style style) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
        addStyle(0, style);
    }
       
    @Override
    public void clearCache() {
        envelope = new Envelope();
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    
    @Override
    public Envelope getEnvelope() {
        Envelope cachedEnvelope = envelope;
        if (cachedEnvelope.isNull()) {
                return spatialTable.getEstimatedExtend().getEnvelopeInternal();                    
        }
        return cachedEnvelope;
    }
    
    @Override
    public boolean isSerializable() {
        return spatialTable != null;
    }

    
    private void fireSelectionChanged() {
        listeners.forEach((listener) -> {
            listener.selectionChanged(new SelectionEvent(this));
        });
    }
    
    
    private void executeJob(SwingWorker worker) {
        if (executorService == null) {
            worker.execute();
        } else {
            executorService.execute(worker);
        }
    }    

    @Override
    public ISpatialTable getSpatialTable() {
        return spatialTable;
    }
}
