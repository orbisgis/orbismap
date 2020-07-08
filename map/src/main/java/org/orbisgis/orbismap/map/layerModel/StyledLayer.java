/**
 * Map is part of the OrbisGIS platform
 * <p>
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 * <p>
 * The GIS group of the DECIDE team is located at :
 * <p>
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 * <p>
 * Map is distributed under LGPL 3 license.
 * <p>
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 * <p>
 * <p>
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.layerModel;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.orbisgis.orbismap.map.renderer.featureStyle.FeatureStyleRenderer;
import org.orbisgis.orbismap.map.renderer.featureStyle.utils.EnvelopeVisitor;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.map.api.IProgressMonitor;
import org.orbisgis.orbismap.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbismap.style.StyleFactory;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class StyledLayer extends AbstractLayer {

    private ISpatialTable spatialTable;
    private MapEnvelope envelope;
    private Feature2DStyle style;
    private EnvelopeVisitor envelopeVisitor;

    public StyledLayer(String name, ISpatialTable spatialTable) {
        super(name);
        this.spatialTable = spatialTable;
        initStyle();
    }

    public StyledLayer(ISpatialTable spatialTable) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
        initStyle();
    }

    /**
     * Create a default style
     * TODO : create the style according the geometry type
     */
    private void initStyle() {
        this.style = StyleFactory.createPointSymbolizer();
    }

    public StyledLayer(ISpatialTable spatialTable, Feature2DStyle style) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
        this.style = style;
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

    /**
     * This method is used to parse the style and collect informations
     * before computing envelope or rendering style
     */
    private void visitStyle() {
        if (style != null) {
            envelopeVisitor = new EnvelopeVisitor(style);
            envelopeVisitor.visit();
        }
    }

    @Override
    public MapEnvelope getEnvelope() {
        MapEnvelope cachedEnvelope = envelope;
        if (cachedEnvelope == null) {
            Geometry geomEnv = computeEnvelopeFromStyle();
            if (geomEnv == null) {
                throw new RuntimeException("Cannot compute the envelope of the layer");
            }
            cachedEnvelope = new MapEnvelope(geomEnv.getEnvelopeInternal(), geomEnv.getSRID());
            envelope = cachedEnvelope;
        }
        return cachedEnvelope;
    }

    /**
     * Method to compute the envelope according the style parameters
     * @return
     */
    private Geometry computeEnvelopeFromStyle() {
        if (envelopeVisitor == null) {
            visitStyle();
        }
        Envelope aggregatedEnvelope = new Envelope();
        final int[] srid = {0};
        envelopeVisitor.getQueryForEnvelopes().forEach((k, v) -> {
            String filter = "";
            if (k != null && !k.isEmpty()) {
                filter = " where "+ k;
            }
            Geometry geom = spatialTable.getExtent((String[]) v.toArray(new String[0]), filter);
            int currentSRID = geom.getSRID();
            if (srid[0] == 0) {
                srid[0] = currentSRID;
            } else if (srid[0] != currentSRID) {
                throw new RuntimeException("Cannot compute the envelope from mixed SRID geometries");
            }
            aggregatedEnvelope.expandToInclude(geom.getEnvelopeInternal());
        });
        if (aggregatedEnvelope.isNull()) {
            return null;
        } else {
            Geometry geom = new GeometryFactory().toGeometry(aggregatedEnvelope);
            geom.setSRID(srid[0]);
            return geom;
        }
    }
}
