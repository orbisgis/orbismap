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

import java.awt.Graphics2D;
import org.orbisgis.orbismap.map.renderer.featureStyle.FeatureStyleRenderer;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.map.api.IProgressMonitor;
import org.orbisgis.orbismap.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class StyledLayer extends AbstractLayer {

    private ISpatialTable spatialTable;
    private MapEnvelope envelope;
    private Feature2DStyle style;

    public StyledLayer(String name, ISpatialTable spatialTable) {
        super(name);
        this.spatialTable = spatialTable;
    }

    public StyledLayer(ISpatialTable spatialTable) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
    }

    public StyledLayer(ISpatialTable spatialTable, Feature2DStyle style) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
    }

    public Feature2DStyle getStyle() {
        return style;
    }

    public void setStyle(Feature2DStyle style) {
        this.style = style;
    }

    public ISpatialTable getSpatialTable() {
        return spatialTable;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mt, IProgressMonitor pm) throws LayerException {
        if (isVisible() && spatialTable != null) {
            FeatureStyleRenderer fsr = new FeatureStyleRenderer(style);
            try {
                fsr.draw(spatialTable, mt, g2, pm);
            } catch (Exception ex) {
                throw new LayerException(ex);
            }
        }
    }

    @Override
    public MapEnvelope getEnvelope() {
        MapEnvelope cachedEnvelope = envelope;
        if (cachedEnvelope == null) {
            cachedEnvelope = new MapEnvelope(spatialTable.getEstimatedExtend().getEnvelopeInternal());
            envelope = cachedEnvelope;
        }
        return cachedEnvelope;

    }
}
