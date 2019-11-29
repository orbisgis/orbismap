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

import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.coremap.layerModel.model.AbstractLayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.FeatureRule;
import org.orbisgis.coremap.renderer.se.FeatureStyle;
import org.orbisgis.coremap.renderer.se.FeatureSymbolizer;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.visitors.FeaturesVisitor;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;
import org.orbisgis.datamanager.JdbcSpatialTable;
import org.orbisgis.datamanager.JdbcTable;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.orbisgis.style.IRule;
import org.orbisgis.style.ISymbolizer;

public class Layer extends AbstractLayer {

    private ISpatialTable spatialTable;
    private Envelope envelope = new Envelope();
    private FeatureStyle style;

    public Layer(String name, ISpatialTable spatialTable) {
        super(name);
        this.spatialTable = spatialTable;
    }
    
    public Layer(ISpatialTable spatialTable) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;        
    }
    
    public Layer(ISpatialTable spatialTable, FeatureStyle style) {
        super(spatialTable.getName());
        this.spatialTable = spatialTable;
    }

    public FeatureStyle getStyle() {
        return style;
    }

    public void setStyle(FeatureStyle style) {
        this.style = style;
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
               envelope = spatialTable.getEstimatedExtend().getEnvelopeInternal();      
               return envelope;
        }
        return cachedEnvelope;
    }
    
    @Override
    public boolean isSerializable() {
        return spatialTable != null;
    }
    
    public ISpatialTable getSpatialTable() {
        return spatialTable;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mt, IProgressMonitor pm) throws LayerException {   
        if(isVisible() && mt.getAdjustedExtent().intersects(envelope)){
        if (spatialTable == null && !(spatialTable instanceof JdbcTable)) {
            throw new LayerException("There is neither a ResultSetProviderFactory instance nor available DataSource in the vectorial layer");
        }
       
        String geomColumnName = spatialTable.getGeometricColumns().get(0);
        StringBuilder geofilter = new StringBuilder();
        geofilter.append("'").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ").append(geomColumnName);
        try {
            // i.e. TextSymbolizer are always drawn above all other layer !! Should now be handle with symbolizer level
            // Standard rules (with filter or no filter but not with elsefilter)
            IProgressMonitor rulesProgress = pm.startTask(style.getRules().size());
            for (IRule r : style.getRules()) {
                FeaturesVisitor fv = new FeaturesVisitor();
                fv.visitSymbolizerNode(r);
                Set<String> fields = fv.getResult();
                fields.add(geomColumnName);
                
                JdbcSpatialTable spatialTableQuery = (JdbcSpatialTable) ((JdbcTable) spatialTable.columns(fields.toArray(new String[0])))
                        .where(geofilter.toString()).getSpatialTable();

                IProgressMonitor rowSetProgress = rulesProgress.startTask("Drawing " + getName() + " (Rule " + r.getName() + ")", 1);

                //ResultSet spatialTableQuery = ps.executeQuery();
                long row = -1;

                while (spatialTableQuery.next()) {
                    if (rulesProgress.isCancelled()) {
                        break;
                    }
                    Geometry theGeom = spatialTableQuery.getGeometry();

                    // Do not display the geometry when the envelope
                    //doesn't intersect the current mapcontext area.
                    if (theGeom != null) {
                        //Workaround because H2 linked table doesn't contains PK or _ROWID_
                        row++;
                        //End workaround
                        boolean selected = false;
                        List<ISymbolizer> sl = r.getSymbolizers();
                        for (ISymbolizer s : sl) {
                            boolean res = drawFeature(s, theGeom, spatialTableQuery, row,
                                     mt.getAdjustedExtent(), selected, mt, g2);
                        }
                    }
                    rowSetProgress.endTask();
                }
                rulesProgress.endTask();
            }
            //disposeLayer(g2);
        } catch (ParameterException | IOException | SQLException ex) {
             Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
     private boolean drawFeature(ISymbolizer s, Geometry geom, ResultSet rs,
            long rowIdentifier, Envelope extent, boolean selected,
            MapTransform mt, Graphics2D g2) throws ParameterException,
            IOException, SQLException {
        Geometry theGeom = geom;
        boolean somethingReached = false;
        if (theGeom == null) {
            //We try to retrieve a geometry. If we fail, an
            //exception will be thrown by the call to draw,
            //and a message will be shown to the user...
            FeatureSymbolizer vs = (FeatureSymbolizer) s;
            theGeom = vs.getGeometry(rs, rowIdentifier);
            if (theGeom != null && theGeom.getEnvelopeInternal().intersects(extent)) {
                somethingReached = true;
            }
        }
        if (somethingReached || theGeom != null) {
           FeatureSymbolizer featureSymbolizer = (FeatureSymbolizer) s;
           featureSymbolizer.draw(g2, rs, rowIdentifier, selected, mt, theGeom);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
    
}
