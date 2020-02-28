/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.featureStyle;

import java.awt.Graphics2D;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.style.visitors.ParameterValueVisitor;
import org.orbisgis.map.api.IProgressMonitor;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcTable;
import org.orbisgis.style.AreaSymbolizer;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.LineSymbolizer;
import org.orbisgis.style.PointSymbolizer;
import org.orbisgis.style.TextSymbolizer;
import org.orbisgis.style.visitors.GeometryParameterVisitor;

/**
 *
 * @author Erwan Bocher
 */
public class FeatureStyleRenderer {

    private final Feature2DStyle fs;
    
    public FeatureStyleRenderer(Feature2DStyle fs){
        this.fs =fs;
    }
    
    
    public String prepareRule(Feature2DRule rule) {
                ParameterValueVisitor pvv = new ParameterValueVisitor();
                pvv.visitSymbolizerNode(rule);
                return pvv.getResultAsString();
    }
     
     
    public void draw(ISpatialTable spatialTable,  MapTransform mt, Graphics2D g2,IProgressMonitor pm) throws Exception{  
        List<String> geometryColumns = spatialTable.getGeometricColumns();
        for (Feature2DRule rule : fs.getRules()) {
            if(rule.isDomainAllowed(mt.getScaleDenominator())){
                String allExpressions = prepareRule(rule);
                List<IFeatureSymbolizer> sl = rule.getSymbolizers();
                GeometryParameterVisitor gp = new GeometryParameterVisitor(sl);
                gp.visit(geometryColumns);
                String selectGeometry = gp.getResultAsString();
                String query = "";
                if(!selectGeometry.isEmpty() && !allExpressions.isEmpty()){
                    query = selectGeometry+","+allExpressions;  
                }else if(!selectGeometry.isEmpty()){
                    query = selectGeometry; 
                }
                if(!query.isEmpty()){
                //Manage rule expression
                //To build the where query we must find the name of the column                                        
                StringBuilder geofilter = new StringBuilder();                
                geofilter.append("'").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ");        
                String geomFilter = geofilter.toString();
                
                String wherefilter = gp.getGeometryColumns().stream()
                        .map(entry -> geomFilter + " "+ entry)
                        .collect(Collectors.joining(" and "));
                
                JdbcSpatialTable spatialTableQuery = (JdbcSpatialTable) ((JdbcTable) spatialTable.columns(query))
                        .where(wherefilter).getSpatialTable();
                
                ISymbolizerDraw[] symbolizersToDraw = prepareSymbolizers(sl);
                while (spatialTableQuery.next()) {
                    for (ISymbolizerDraw s : symbolizersToDraw) {
                        s.draw(spatialTableQuery, g2, mt);
                    }
                }
                }
            }
        }
    }   
   

    private ISymbolizerDraw[] prepareSymbolizers(List<IFeatureSymbolizer> sl) {
        ISymbolizerDraw[] symbolizerToDraw  = new ISymbolizerDraw[sl.size()];
        for (int i = 0; i < sl.size(); i++) {
             IFeatureSymbolizer featureSymbolizer = sl.get(i);
             if(featureSymbolizer instanceof AreaSymbolizer){
                 symbolizerToDraw[i] = new AreaSymbolizerRenderer((AreaSymbolizer) featureSymbolizer);
             }else if(featureSymbolizer instanceof LineSymbolizer){
                 
             }else if(featureSymbolizer instanceof PointSymbolizer){
                 
             }else if(featureSymbolizer instanceof TextSymbolizer){
                 
             }
        }
        return symbolizerToDraw;
    }
}
