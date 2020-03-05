/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.layerModel;

import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.map.renderer.featureStyle.FeatureStyleRenderer;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.map.api.IProgressMonitor;
import org.orbisgis.map.api.LayerException;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;

/**
 *
 * @author ebocher
 */
public class StyledLayer extends AbstractLayer{
    
    private ISpatialTable spatialTable;
    private MapEnvelope envelope ;
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
        
        FeatureStyleRenderer fsr = new FeatureStyleRenderer(style);
        try {
            fsr.draw(spatialTable, mt, g2, pm);
            
            /*if(isVisible() && mt.getAdjustedExtent().intersects(envelope)){
            if (spatialTable == null && !(spatialTable instanceof JdbcTable)) {
            throw new LayerException("Cannot find any spatial table to draw");
            }
            
            String geomColumnName = spatialTable.getGeometricColumns().get(0);
            StringBuilder geofilter = new StringBuilder();
            geofilter.append("'").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ").append(geomColumnName);
            try {
            IProgressMonitor rulesProgress = pm.startTask(style.getRules().size());
            for (Feature2DRule r : style.getRules()) {
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
            List<Feature2DSymbolizer> sl = r.getSymbolizers();
            for (Feature2DSymbolizer s : sl) {
            boolean res = drawFeature(s, theGeom, spatialTableQuery, row,
            mt.getAdjustedExtent(), selected, mt, g2);
            }
            }
            rowSetProgress.endTask();
            }
            rulesProgress.endTask();
            }
            //disposeLayer(g2);
            } catch (IOException | SQLException ex) {
            Logger.getLogger(StyledLayer.class.getName()).log(Level.SEVERE, null, ex);
            }   catch (ParameterException ex) {
            Logger.getLogger(StyledLayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            }*/
        } catch (Exception ex) {
            Logger.getLogger(StyledLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     private boolean drawFeature(IFeatureSymbolizer s, Geometry geom, ResultSet rs,
            long rowIdentifier, Envelope extent, boolean selected,
            MapTransform mt, Graphics2D g2) throws ParameterException,
            IOException, SQLException {
        Geometry theGeom = geom;
        boolean somethingReached = false;        
        if (theGeom == null) {
            //We try to retrieve a geometry. If we fail, an
            //exception will be thrown by the call to draw,
            //and a message will be shown to the user...
            theGeom = s.getGeometry(rs, rowIdentifier);
            if (theGeom != null && theGeom.getEnvelopeInternal().intersects(extent)) {
                somethingReached = true;
            }
        }
        if (somethingReached || theGeom != null) {
           s.draw(g2, rs, rowIdentifier, mt, theGeom);
           return true;
        } else {
            return false;
        }
    }*/

    @Override
    public MapEnvelope getEnvelope() {
        MapEnvelope cachedEnvelope = envelope;
        if (cachedEnvelope==null) {            
            cachedEnvelope = new MapEnvelope(spatialTable.getEstimatedExtend().getEnvelopeInternal());
            envelope = cachedEnvelope;
        }
        return cachedEnvelope;
         
    }    
}
