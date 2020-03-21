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
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jsqlparser.JSQLParserException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.TopologyException;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.symbolizer.PointSymbolizerDrawer;
import org.orbisgis.map.renderer.featureStyle.symbolizer.TextSymbolizerDrawer;
import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.Feature2DRule;
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
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.visitor.GeometryParameterVisitor;
import org.orbisgis.style.visitor.ParameterValueVisitor;

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
        rule.setExpression(ExpressionParser.formatConditionalExpression(rule.getFilterExpression()));
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
                    
                    String ruleFilter = rule.getFilterExpression().getExpression();
                    
                    if(!ruleFilter.isEmpty()){                        
                        ruleFilter += " and " ;
                    }
                    ruleFilter += spatialWherefilter;


                    JdbcSpatialTable spatialTableQuery = (JdbcSpatialTable) ((JdbcTable) spatialTable.columns(query))
                            .where(ruleFilter).getSpatialTable();

                    //This map is populated from the data
                    Map<String, Object> properties = new HashMap<>();
                    Map<IFeatureSymbolizer, ISymbolizerDraw> symbolizersToDraw = prepareSymbolizers(sl, mt);
                    while (spatialTableQuery.next()) {
                        Map<String, Shape> shapes = new HashMap<String, Shape>();
                        Shape currentShape = null;
                        //Populate expressions here
                        populateExpressions(spatialTableQuery, mt, expressionParameters.getExpressionsProperties());
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
                                    Geometry   geomReduced = geom;
                                    try {
                                        boolean overlaps = geom.overlaps(mt.getAdjustedExtentGeometry());
                                        if (overlaps) {
                                            geomReduced = geom.intersection(mt.getAdjustedExtentGeometry());
                                        }
                                    } catch (TopologyException e) {
                                        //ST_MakeValid.validGeom(geom, true).intersection(adjustedExtentGeometry);
                                    }                                
                          
                                    if(featureSymbolizer instanceof PointSymbolizer){
                                        PointSymbolizer ps = (PointSymbolizer) featureSymbolizer;
                                        if (ps.isOnVertex()) {
                                            currentShape = mt.getShapeAsPoints(geomReduced, false, false);
                                        } else {
                                            currentShape = mt.getShapeAsPoints(geomReduced, false, true);
                                        } 
                                    }
                                    else{
                                        currentShape = mt.getShape(geomReduced, true);
                                    }
                                }
                                ISymbolizerDraw symbolizerDraw = symbolizers.getValue();
                                symbolizerDraw.setShape(currentShape);
                                symbolizerDraw.draw(symbolizerDraw.getGraphics2D(), mt, featureSymbolizer, properties);
                                currentShape=null;
                            } catch (ParameterException | SQLException ex) {
                                Logger.getLogger(FeatureStyleRenderer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } 

                    //Draw the bufferedimages in the good order
                    Comparator<Entry<IFeatureSymbolizer, ISymbolizerDraw>> symbolizerLevelComp = (o1, o2) -> {
                        if (o1.getKey().getLevel() > o2.getKey().getLevel()) {
                            return -1;
                        } else if (o1.getKey().getLevel() < o2.getKey().getLevel()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    };

                    Map<IFeatureSymbolizer, ISymbolizerDraw> sortedSymbolizers
                            = symbolizersToDraw.entrySet().stream().
                                    sorted(symbolizerLevelComp.reversed()).
                                    collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                                            (e1, e2) -> e1, LinkedHashMap::new));

                    //Draw all buffered images
                    sortedSymbolizers.values().forEach((symbolizerDraw) -> {
                        symbolizerDraw.dispose(g2);
                    });

                    
                }
            }
        }
    }

    /**
     * TODO : sort the IFeatureSymbolizer in this pass and set the good bufferedimage
     * @param sl
     * @param mt
     * @return 
     */
    private Map<IFeatureSymbolizer, ISymbolizerDraw> prepareSymbolizers(List<IFeatureSymbolizer> sl, MapTransform mt) {
        Map<IFeatureSymbolizer, ISymbolizerDraw> symbolizerDrawer = new LinkedHashMap<>();
        HashMap<Integer, GraphicElements> bufferedImages = new HashMap<>();
        for (int i = 0; i < sl.size(); i++) {
            IFeatureSymbolizer featureSymbolizer = sl.get(i);
            int level = featureSymbolizer.getLevel();
            if (featureSymbolizer instanceof AreaSymbolizer) {
                AreaSymbolizerDrawer drawer = new AreaSymbolizerDrawer();
                initDrawer(level, drawer, mt, bufferedImages);
                symbolizerDrawer.put(featureSymbolizer, drawer);
            } else if (featureSymbolizer instanceof LineSymbolizer) {
                LineSymbolizerDrawer drawer = new LineSymbolizerDrawer();
                initDrawer(level, drawer, mt,bufferedImages);
                symbolizerDrawer.put(featureSymbolizer,drawer);
            } else if (featureSymbolizer instanceof PointSymbolizer) {
                PointSymbolizerDrawer drawer = new PointSymbolizerDrawer();
                initDrawer(level, drawer, mt,bufferedImages);
                symbolizerDrawer.put(featureSymbolizer,drawer);
            } else if (featureSymbolizer instanceof TextSymbolizer) {
                TextSymbolizerDrawer drawer = new TextSymbolizerDrawer();
                initDrawer(level, drawer, mt,bufferedImages);
                symbolizerDrawer.put(featureSymbolizer,drawer);
            }
        }
        return symbolizerDrawer;
    }
    
    public class GraphicElements {

        public final Graphics2D g2;
        public final BufferedImage image;

        public GraphicElements(Graphics2D g2, BufferedImage image) {
            this.g2 = g2;
            this.image = image;
        }

        public Graphics2D getG2() {
            return g2;
        }

        public BufferedImage getImage() {
            return image;
        }
        
    }
    
    /**
     * The number of bufferedimage depend on the symbol level
     * @param drawer
     * @param mt 
     */
    private void initDrawer(int level, ISymbolizerDraw drawer, MapTransform mt, HashMap<Integer, GraphicElements> bufferedImages) {
        GraphicElements graphics = bufferedImages.get(level);
        if (graphics != null) {
            drawer.setBufferedImage(graphics.getImage());
            drawer.setGraphics2D(graphics.getG2());
        } else {
            BufferedImage bufferedImage = new BufferedImage(mt.getWidth(), mt.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D sG2 = bufferedImage.createGraphics();
            sG2.addRenderingHints(mt.getRenderingHints());
            bufferedImages.put(level, new GraphicElements(sG2, bufferedImage));
            drawer.setBufferedImage(bufferedImage);
            drawer.setGraphics2D(sG2);
        }
    }

    /**
     * 
     * @param sp
     * @param mt
     * @param parameterValueIdentifiers
     * @throws SQLException 
     */
    private void populateExpressions(JdbcSpatialTable sp, MapTransform mt, Map<String, org.orbisgis.style.parameter.Expression>  parameterValueIdentifiers) throws SQLException {
        if(!parameterValueIdentifiers.isEmpty()){            
            for (Map.Entry<String, org.orbisgis.style.parameter.Expression> entry : parameterValueIdentifiers.entrySet()) {
                Expression exp = entry.getValue();
                exp.setValue(sp.getObject(exp.getReference(),exp.getDataType()));        
            }
        }
    }
    
}
