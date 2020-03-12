/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle;

import org.orbisgis.map.renderer.featureStyle.symbolizer.LineSymbolizerDrawer;
import org.orbisgis.map.renderer.featureStyle.symbolizer.AreaSymbolizerDrawer;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jsqlparser.JSQLParserException;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.symbolizer.PointSymbolizerDrawer;
import org.orbisgis.map.renderer.featureStyle.symbolizer.TextSymbolizerDrawer;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.Feature2DRule;
import org.orbisgis.map.renderer.featureStyle.visitor.ParameterValueVisitor;
import org.orbisgis.map.api.IProgressMonitor;
import org.orbisgis.map.renderer.featureStyle.utils.ExpressionParser;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcTable;
import org.orbisgis.style.symbolizer.AreaSymbolizer;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.symbolizer.LineSymbolizer;
import org.orbisgis.style.symbolizer.PointSymbolizer;
import org.orbisgis.style.symbolizer.TextSymbolizer;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.map.renderer.featureStyle.visitor.GeometryParameterVisitor;

/**
 *
 * @author Erwan Bocher
 */
public class FeatureStyleRenderer {

    private final Feature2DStyle fs;

    public FeatureStyleRenderer(Feature2DStyle fs) {
        this.fs = fs;
    }

    public ParameterValueVisitor prepareSymbolizerExpression(Feature2DRule rule) {
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(rule);
        return pvv;
    }
    
    /**
     * Format rule filter expression
     * @param rule
     * @throws JSQLParserException 
     */
    public void formatRuleExpression(Feature2DRule rule) throws JSQLParserException {
        rule.setExpression(ExpressionParser.formatConditionalExpression(rule.getExpression()));
    }

    public void draw(ISpatialTable spatialTable, MapTransform mt, Graphics2D g2, IProgressMonitor pm) throws Exception {
        List<String> geometryColumns = spatialTable.getGeometricColumns();
        for (Feature2DRule rule : fs.getRules()) {
            if (rule.isDomainAllowed(mt.getScaleDenominator())) {
                formatRuleExpression(rule);
                ParameterValueVisitor expressionParameters = prepareSymbolizerExpression(rule);
                String allExpressions = expressionParameters.getExpressionParametersAsString();
                List<IFeatureSymbolizer> sl = rule.getSymbolizers();
                GeometryParameterVisitor gp = new GeometryParameterVisitor(sl);
                gp.visit(geometryColumns);
                String selectGeometry = gp.getResultAsString();
                String query = "";
                if (!selectGeometry.isEmpty() && !allExpressions.isEmpty()) {
                    query = selectGeometry + "," + allExpressions;
                } else if (!selectGeometry.isEmpty()) {
                    query = selectGeometry;
                }
                if (!query.isEmpty()) {
                    //Manage rule expression
                    //To build the where query we must find the name of the column                                        
                    StringBuilder geofilter = new StringBuilder();
                    geofilter.append("'").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ");
                    String geomFilter = geofilter.toString();

                    String spatialWherefilter = gp.getGeometryColumns().stream()
                            .map(entry -> geomFilter + " " + entry)
                            .collect(Collectors.joining(" and "));
                    
                    String ruleFilter = rule.getExpression().getExpression();
                    
                    if(!ruleFilter.isEmpty()){                        
                        ruleFilter += " and " ;
                    }
                    ruleFilter += spatialWherefilter;


                    JdbcSpatialTable spatialTableQuery = (JdbcSpatialTable) ((JdbcTable) spatialTable.columns(query))
                            .where(ruleFilter).getSpatialTable();

                    //This map is populated from the data
                    Map<String, Object> properties = new HashMap<>();
                    Map<IFeatureSymbolizer, ISymbolizerDraw> symbolizersToDraw = prepareSymbolizers(sl);
                    while (spatialTableQuery.next()) {
                        Map<String, Shape> shapes = new HashMap<String, Shape>();
                        Shape currentShape = null;
                        //Populate properties here
                        populateProperties(spatialTableQuery, mt, properties, expressionParameters.getExpressionParameters().keySet());
                        for (Map.Entry<IFeatureSymbolizer, ISymbolizerDraw> symbolizers : symbolizersToDraw.entrySet()) {
                            try {
                                //Set the shape to the symbolizer to draw
                                //Geometry identifier
                                IFeatureSymbolizer featureSymbolizer = symbolizers.getKey();
                                String geomIdentifier = featureSymbolizer.getGeometryParameter().getIdentifier();
                                if(shapes.containsKey(geomIdentifier)){
                                    currentShape= shapes.get(geomIdentifier);
                                }
                                else{
                                    Geometry geom = spatialTableQuery.getGeometry(geomIdentifier);
                                    if(featureSymbolizer instanceof PointSymbolizer){
                                        PointSymbolizer ps = (PointSymbolizer) featureSymbolizer;
                                        if (ps.isOnVertex()) {
                                            currentShape = mt.getShapeAsPoints(geom, false, false);
                                        } else {
                                            currentShape = mt.getShapeAsPoints(geom, false, true);
                                        } 
                                    }
                                    else{
                                        currentShape = mt.getShape(geom, true);
                                    }
                                }
                                ISymbolizerDraw symbolizerDraw = symbolizers.getValue();
                                symbolizerDraw.setShape(currentShape);
                                symbolizerDraw.draw(g2, mt, featureSymbolizer, properties);
                                currentShape=null;
                            } catch (ParameterException | SQLException ex) {
                                Logger.getLogger(FeatureStyleRenderer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<IFeatureSymbolizer, ISymbolizerDraw> prepareSymbolizers(List<IFeatureSymbolizer> sl) {
        Map<IFeatureSymbolizer, ISymbolizerDraw> symbolizerDrawer = new HashMap<>();
        for (int i = 0; i < sl.size(); i++) {
            IFeatureSymbolizer featureSymbolizer = sl.get(i);
            if (featureSymbolizer instanceof AreaSymbolizer) {
                symbolizerDrawer.put(featureSymbolizer, new AreaSymbolizerDrawer());
            } else if (featureSymbolizer instanceof LineSymbolizer) {
                symbolizerDrawer.put(featureSymbolizer, new LineSymbolizerDrawer());
            } else if (featureSymbolizer instanceof PointSymbolizer) {
                symbolizerDrawer.put(featureSymbolizer, new PointSymbolizerDrawer());
            } else if (featureSymbolizer instanceof TextSymbolizer) {
                symbolizerDrawer.put(featureSymbolizer, new TextSymbolizerDrawer());
            }
        }
        return symbolizerDrawer;
    }

    private void populateProperties(JdbcSpatialTable sp, MapTransform mt, 
            Map<String, Object> properties, Collection<String> parameterValueIdentifiers) throws SQLException {
        if(!parameterValueIdentifiers.isEmpty()){
            for (String parameterValueIdentifier : parameterValueIdentifiers) {
                properties.put(parameterValueIdentifier, sp.getObject(parameterValueIdentifier));
            }
        }
    }
    
    /**
     * Create the shape awt according the symbolizer
     * @param sp
     * @param mt
     * @param properties
     * @param symbolizersToDraw 
     */
    private void buildShapeForSymbolizer(JdbcSpatialTable sp,MapTransform mt,Map<String, Object> properties,
            List<IFeatureSymbolizer>  symbolizersToDraw){
        symbolizersToDraw.forEach((iFeatureSymbolizer) -> {
                Geometry geom = sp.getGeometry(iFeatureSymbolizer.getGeometryParameter().getIdentifier());
                if (iFeatureSymbolizer instanceof PointSymbolizer) {
                    PointSymbolizer ps =  (PointSymbolizer) iFeatureSymbolizer;
                    if(ps.isOnVertex()){
                        properties.put("geom_shape_points",  mt.getShapeAsPoints(geom, true, true));
                    }
                    else{
                         properties.put("geom_shape_points",  mt.getShapeAsPoints(geom, true, true));
                    }                    
                } else {
                    properties.put("geom_shape", mt.getShape(geom, true));
                }
            });
    }

    
}
