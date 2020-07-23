/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */

package org.orbisgis.orbismap.map.renderer.featureStyle.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.parameter.RuleFilter;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;
import org.orbisgis.orbismap.style.visitor.ParameterValueVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class to collect geometry parameters and filter for each rules
 * This class is useful to build the envelope before rendering rule.
 * It allows to create a ISpatialTable according the rule filter and the parameters available on Symbolizers.
 *
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */

public class EnvelopeVisitor {


    private final Feature2DStyle feature2DStyle;

    /**
     * The map contains for each rule the filter and its geometry parameters
     * Main map :
     * Key  = rule index
     * Values = Sub Map
     *
     * Sub Map
     * Key  = Rule filter
     * Values = HashSet of geometry expressions
     */
    private HashMap<Integer , HashMap<String, HashSet<String>>> ruleFilterAndGeometryParameters = new HashMap();


    HashMap<String, HashSet<String>> collect = new HashMap<>();
    HashMap<String, HashMap<Integer, LinkedList<IFeatureSymbolizer>>> collectSymbolizers = new HashMap<>();

    public EnvelopeVisitor(Feature2DStyle feature2DStyle){
        this.feature2DStyle=feature2DStyle;
    }

    /**
     * Visit the rule to collect its filter and geometry parameters for each symbol
     * Note : the envelope must be computed according the rule scale denominator and other information available from
     * the layer. e.g. isVisible
     */
    public void visit(){
        int ruleIndex = 0;
        int levelCounter =0;
        for (Feature2DRule rule : feature2DStyle.getRules()) {
            //Check rule scale to draw or not
            RuleFilter ruleFilter = rule.getFilter();
            HashMap<String, HashSet<String>> filterAndGeom =  new HashMap<>();
            HashSet<String> geomColumns = new HashSet<>();
            String expressionRule = formatRuleExpression(ruleFilter, ruleIndex);
             for (IFeatureSymbolizer symbolizer : rule.getSymbolizers()){
                 String geometryExpression =formatGeometryParameter(symbolizer.getGeometryParameter());
                 geomColumns.add(geometryExpression);
                 String filterRuleKey = expressionRule == null ? "" : expressionRule;
                 filterAndGeom.put(filterRuleKey, geomColumns);
                 ruleFilterAndGeometryParameters.put(ruleIndex, filterAndGeom);
                 collect.computeIfAbsent(filterRuleKey, x -> new HashSet<String>()).add(geometryExpression);
                 int level = symbolizer.getLevel();
                 if(level==0){
                     levelCounter++;
                 }
                 collectSymbolizers.computeIfAbsent(filterRuleKey, x -> new HashMap<>()).computeIfAbsent(levelCounter, s -> new LinkedList<>()).add(symbolizer);
            }
            ruleIndex++;
        }
    }

    public HashMap<String, HashSet<String>> getCollect() {
        return collect;
    }

    public HashMap<String, HashMap<Integer, LinkedList<IFeatureSymbolizer>>> getCollectSymbolizers() {
        return collectSymbolizers;
    }

    /**
     * Return unique rule filter parameter instance with unique geometry parameters
     * Must take into account if the layer is visible and the rule scales
     * @return
     */
    public HashMap<String, HashSet<String>> getRuleFilterAndGeometryParameters() {
        HashMap<String, HashSet<String>> collect = new HashMap<>();
        ruleFilterAndGeometryParameters.values().forEach( v -> {
            v.entrySet().stream().forEach(e ->{
                collect.computeIfAbsent(e.getKey(), x -> new HashSet<String>()).addAll(e.getValue());
            });
        });
        return collect;
    }

    public void prepareData(){

    }

    /**
     *
     * @param geometryParameter
     * @return
     */
    private String formatGeometryParameter(GeometryParameter geometryParameter) {
        try {
            if(geometryParameter==null){
                throw  new RuntimeException("A geometry property name must be set to each symbolizer");
            }
            return ExpressionParser.formatGeometryParameterExpression(geometryParameter);
        } catch (JSQLParserException e) {
            throw  new RuntimeException("Invalid geometry parameter expression", e);
        }
    }

    /**
     * Format rule filter expression and check if the rule expression is supported
     *
     * @param ruleFilter
     * @throws JSQLParserException
     */
    private String formatRuleExpression(RuleFilter ruleFilter, Integer ruleIndex)  {
            if (ruleFilter != null) {
                String expression = ruleFilter.getExpression();
                if(!expression.isEmpty()) {
                    try {
                        net.sf.jsqlparser.expression.Expression expParsed = CCJSqlParserUtil.parseCondExpression(expression, false);
                        String formatedExp = expParsed.toString();
                        ruleFilter.setExpression(formatedExp);
                        return "WHERE "+ formatedExp;
                    } catch (JSQLParserException e) {
                        try {
                            Select select = (Select) CCJSqlParserUtil.parse("SELECT * from foo " + expression);
                            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                            if(plainSelect.getWhere()!=null){
                               return plainSelect.getWhere().toString().trim();
                            }else if(plainSelect.getLimit()!=null){
                                return plainSelect.getLimit().toString().trim();
                            }else if(plainSelect.getGroupBy()!=null){
                                return plainSelect.getGroupBy().toString().trim();
                            }else if(plainSelect.getOrderByElements()!=null){
                                return plainSelect.getOrderByElements().toString().trim();
                            }
                            else{
                                throw  new RuntimeException("Rule filter no supported", e);
                            }
                        } catch (JSQLParserException ex) {
                            throw  new RuntimeException("Invalid rule filter", ex);
                        }
                    }

                }
            }
            return "";
    }

    /**
     * Traverse the rule symbolizers to
     * @param rule
     * @return
     */
    public ParameterValueVisitor prepareSymbolizerExpression(Feature2DRule rule) {
        ParameterValueVisitor pvv = new ParameterValueVisitor();
        pvv.visitSymbolizerNode(rule);
        return pvv;
    }

    /**
     * Return a ISpatialTable according to the parameters of a rule
     *
     * @param spatialTable the input spatialTable to be reduced
     * @param ruleIndex integer value used to find the desired parameters of the rule
     * @return a new ISpatialTable instance
     */
    ISpatialTable getSpatialTable(ISpatialTable spatialTable,Feature2DRule rule,  int ruleIndex, MapTransform mt){
        return spatialTable.columns(getSelectItems(ruleIndex)).filter(getFilter(ruleIndex, mt)).getSpatialTable();
    }

    /**
     *
     * @param ruleIndex
     * @return
     */
    private String getFilter(int ruleIndex,MapTransform mt) {
        HashMap<String, HashSet<String>> ruleParameters = ruleFilterAndGeometryParameters.get(ruleIndex);
        if(ruleParameters!=null) {
            StringBuilder filterQuery = new StringBuilder();
            String ruleExpression = ruleParameters.keySet().stream().findFirst().get();
            filterQuery.append(ruleExpression).append(" and '").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ");
            return filterQuery.toString();
        }
        return "";
    }

    /***
     * TODO be updated doesn't work yet
     *
     * @param ruleIndex
     * @param mt
     * @return
     */
    private String getSpatialFilter(int ruleIndex,MapTransform mt) {
        StringBuilder geofilter = new StringBuilder();
        geofilter.append("'").append(MapTransform.getGeometryFactory().toGeometry(mt.getAdjustedExtent()).toText()).append("' :: GEOMETRY && ");
        String geomFilter = geofilter.toString();
        ruleFilterAndGeometryParameters.get(ruleIndex).entrySet().stream().map(entry -> geomFilter + " " + entry)
                .collect(Collectors.joining(" , "));
        return geomFilter.toString();
    }

    /**
     * Build the list of columns to be selected in the spatialtable
     * @param ruleIndex
     * @return
     */
    private String getSelectItems(int ruleIndex) {
        return null;
    }
}
