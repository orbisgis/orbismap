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
package org.orbisgis.orbismap.map.renderer.featureStyle;

import org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer.LineSymbolizerDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer.AreaSymbolizerDrawer;
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
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer.PointSymbolizerDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer.TextSymbolizerDrawer;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.map.api.IProgressMonitor;
import org.orbisgis.orbismap.map.renderer.featureStyle.utils.ExpressionParser;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.LineSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.PointSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.TextSymbolizer;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.utils.UomUtils;
import org.orbisgis.orbismap.style.visitor.GeometryParameterVisitor;
import org.orbisgis.orbismap.style.visitor.ParameterValueVisitor;

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
     *
     * @param rule
     * @throws JSQLParserException
     */
    public void formatRuleExpression(Feature2DRule rule) throws JSQLParserException {
        rule.setFilter(ExpressionParser.formatConditionalExpression(rule.getFilter()));
    }

    //TODO : Add a short circuit method to not iterate the symbolizer when some requiered elements are null
    /**
     *
     * @param spatialTable
     * @param mt
     * @param g2
     * @param pm
     * @throws Exception
     */
    public void draw(ISpatialTable spatialTable, MapTransform mt, Graphics2D g2, IProgressMonitor pm) throws Exception {
        List<String> geometryColumns = spatialTable.getGeometricColumns();
        for (Feature2DRule rule : fs.getRules()) {
            if (rule.isDomainAllowed(mt.getScaleDenominator())) {
                formatRuleExpression(rule);
                ParameterValueVisitor expressionParameters = prepareSymbolizerExpression(rule);
                String allExpressions = expressionParameters.getExpressionParametersAsString();
                List<IFeatureSymbolizer> sl = rule.getSymbolizers();
                GeometryParameterVisitor gp = new GeometryParameterVisitor(sl, geometryColumns);
                gp.visit();
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

                    String ruleFilter = rule.getFilter().getExpression();

                    if (!ruleFilter.isEmpty()) {
                        ruleFilter += " and ";
                    }
                    ruleFilter += spatialWherefilter;

                    ISpatialTable spatialTableQuery =  spatialTable.columns(query).filter(ruleFilter);

                    //This map is populated from the data
                    Map<IFeatureSymbolizer, ISymbolizerDraw> symbolizersToDraw = prepareSymbolizers(sl, mt);
                    while (spatialTableQuery.next()) {
                        Map<String, Shape> shapes = new HashMap<>();
                        Shape currentShape = null;
                        Geometry geomReduced = null;
                        //Populate expressions here
                        populateExpressions(spatialTableQuery, mt, expressionParameters.getExpressionsProperties());
                        for (Map.Entry<IFeatureSymbolizer, ISymbolizerDraw> symbolizers : symbolizersToDraw.entrySet()) {
                            try {
                                //Set the shape to the symbolizer to draw
                                //Geometry identifier
                                IFeatureSymbolizer featureSymbolizer = symbolizers.getKey();
                                String geomIdentifier = featureSymbolizer.getGeometryParameter().getIdentifier();
                                String geomMapKey = geomIdentifier;
                                //Because we transform the shape into a set of points
                                if (featureSymbolizer instanceof PointSymbolizer) {
                                    geomMapKey = geomIdentifier + "_points";
                                }
                                //Perpendicular offset workarround
                                float offsetInPixel = 0.0f;
                                Double poffset = (Double) featureSymbolizer.getPerpendicularOffset().getValue();
                                if (poffset != null) {
                                    offsetInPixel = UomUtils.toPixel(poffset.floatValue(), featureSymbolizer.getUom(), mt.getDpi(), mt.getScaleDenominator());
                                    if (Math.abs(offsetInPixel) > 0) {
                                        geomMapKey = geomIdentifier + "_offset";
                                    }
                                }
                                if (shapes.containsKey(geomMapKey)) {
                                    currentShape = shapes.get(geomMapKey);
                                } else {
                                    //We have already the geom in memory
                                    if (geomReduced == null) {
                                        Geometry geom = spatialTableQuery.getGeometry(geomIdentifier);
                                        geomReduced = geom;
                                        try {
                                            boolean overlaps = geom.overlaps(mt.getAdjustedExtentGeometry());
                                            if (overlaps) {
                                                geomReduced = geom.intersection(mt.getAdjustedExtentGeometry());
                                            }
                                        } catch (TopologyException e) {
                                            //ST_MakeValid.validGeom(geom, true).intersection(adjustedExtentGeometry);
                                        }
                                    }
                                    if (featureSymbolizer instanceof PointSymbolizer) {
                                        PointSymbolizer ps = (PointSymbolizer) featureSymbolizer;
                                        if (ps.isOnVertex()) {
                                            currentShape = mt.getShapeAsPoints(geomReduced, false, false);
                                        } else {
                                            currentShape = mt.getShapeAsPoints(geomReduced, false, true);
                                        }
                                    } else {
                                        if (Math.abs(offsetInPixel) > 0) {
                                            currentShape = mt.getShape(geomReduced, true, offsetInPixel);
                                        } else {
                                            currentShape = mt.getShape(geomReduced, true);
                                        }
                                    }
                                    shapes.put(geomMapKey, currentShape);
                                }
                                if (currentShape != null) {
                                    ISymbolizerDraw symbolizerDraw = symbolizers.getValue();
                                    symbolizerDraw.setShape(currentShape);
                                    symbolizerDraw.draw(symbolizerDraw.getGraphics2D(), mt, featureSymbolizer);
                                }
                                currentShape = null;
                            } catch (ParameterException ex) {
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

                    //Sort the symbolizer to draw the image accoring the symbol level
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
     * TODO : sort the IFeatureSymbolizer in this pass and set the good
     * bufferedimage
     *
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
                initDrawer(level, drawer, mt, bufferedImages);
                symbolizerDrawer.put(featureSymbolizer, drawer);
            } else if (featureSymbolizer instanceof PointSymbolizer) {
                PointSymbolizerDrawer drawer = new PointSymbolizerDrawer();
                initDrawer(level, drawer, mt, bufferedImages);
                symbolizerDrawer.put(featureSymbolizer, drawer);
            } else if (featureSymbolizer instanceof TextSymbolizer) {
                TextSymbolizerDrawer drawer = new TextSymbolizerDrawer();
                initDrawer(level, drawer, mt, bufferedImages);
                symbolizerDrawer.put(featureSymbolizer, drawer);
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
     *
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
    private void populateExpressions(ISpatialTable sp, MapTransform mt, Map<String, Expression> parameterValueIdentifiers) throws  Exception {
        if (!parameterValueIdentifiers.isEmpty()) {
            for (Map.Entry<String, Expression> entry : parameterValueIdentifiers.entrySet()) {
                Expression exp = entry.getValue();
                exp.setValue(sp.getObject(exp.getReference(), exp.getParameterDomain().getDataType()));
            }
        }
    }

}
