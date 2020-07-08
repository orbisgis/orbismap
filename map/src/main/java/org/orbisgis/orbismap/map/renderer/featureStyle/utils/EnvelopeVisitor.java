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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.parameter.RuleFilter;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;

/**
 * A class to collect geometry parameters and filter for each rules
 * It's used to compute the envelope before drawing the rule
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */

public class EnvelopeVisitor {


    private final Feature2DStyle feature2DStyle;

    /**
     * Key  = Rule filter
     * Values = Geometry expressions
     */
    private HashMap<String, HashSet<String>> queryForEnvelopes = new HashMap();


    public EnvelopeVisitor(Feature2DStyle feature2DStyle){
        this.feature2DStyle=feature2DStyle;
    }

    /**
     *
     */
    public void visit(){
        int ruleIndex = 0;
        for (Feature2DRule rule : feature2DStyle.getRules()) {
            RuleFilter ruleFilter = rule.getFilter();
            String expressionRule = formatRuleExpression(ruleFilter);
             for (IFeatureSymbolizer symbolizer : rule.getSymbolizers()){
                String geometryExpression =formatGeometryParameter(symbolizer.getGeometryParameter());
                queryForEnvelopes.computeIfAbsent( expressionRule==null?"":expressionRule, x -> new HashSet<String>()).add(geometryExpression);
            }
            ruleIndex++;
        }
    }

    /**
     *
     * @return
     */
    public HashMap<String, HashSet<String>> getQueryForEnvelopes() {
        return queryForEnvelopes;
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
     * Format rule filter expression
     *
     * @param ruleFilter
     * @throws JSQLParserException
     */
    private String formatRuleExpression(RuleFilter ruleFilter)  {
        try {
            if (ruleFilter != null) {
                net.sf.jsqlparser.expression.Expression expParsed = CCJSqlParserUtil.parseCondExpression(ruleFilter.getExpression(), false);
                expParsed.accept(new ExpressionVisitorAdapter());
                return expParsed.toString();
            }
            return "";
        } catch (JSQLParserException e) {
            throw  new RuntimeException("Invalid filter rule", e);
        }
    }
    /**
     * Recursively visits {@code styleNode} and all its children
     *
     * @param rule to visit
     */
    private void visitRule(Feature2DRule rule) {
        try {
            visitImpl(rule);
        } catch (ParameterException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param styleNode the visited style node
     *
     * @throws ParameterException
     */
    protected void visitImpl(IStyleNode styleNode) throws ParameterException {
        List<IStyleNode> actualChildren = styleNode.getChildren();
        if (styleNode instanceof Expression) {
            try {
                net.sf.jsqlparser.expression.Expression actualParsed = CCJSqlParserUtil.parseExpression(((Expression) styleNode).getExpression(), true);

            } catch (JSQLParserException ex) {
                throw new ParameterException(ex);
            }

        } else if (styleNode instanceof Literal) {
            Literal actualLiteral = (Literal) styleNode;
        }

        actualChildren.forEach((c) -> {
            try {
                visitImpl(c);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot parse the expression for the node : "+ c.getClass().getSimpleName());
            }
        });
    }
}
