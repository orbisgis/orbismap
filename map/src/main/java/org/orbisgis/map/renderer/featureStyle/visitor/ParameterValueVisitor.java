/**
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
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2017
 * CNRS (Lab-STICC UMR CNRS 6285)
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
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IStyleNodeVisitor;

/**
 * Search for the names of the features that are used in the visited tree of
 * {@link SymbolizerNode} instances.
 *
 * @author Erwan Bocher, CNRS
 */
public class ParameterValueVisitor implements IStyleNodeVisitor {
    
    private HashMap<String, org.orbisgis.style.parameter.Expression> expressionsProperties = new HashMap<String, org.orbisgis.style.parameter.Expression>();


    private HashMap<String, String> expression_parameters = new HashMap<String, String>();

    private int count = 0;

    /**
     * Recursively visits {@code sn} and all its children, searching for
     * feature-dependant nodes.
     *
     * @param sn
     */
    @Override
    public void visitSymbolizerNode(IStyleNode sn) {
        try {
            visitImpl(sn);
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * The method that does the work...It is not callable directly by the
     * clients, as it does not clean the inner HashSet. If you want to use it
     * directly, inherit this class.
     *
     * @param sn
     * @throws java.lang.Exception
     */
    protected void visitImpl(IStyleNode sn) throws Exception {
        List<IStyleNode> children = sn.getChildren();
        if (sn instanceof org.orbisgis.style.parameter.Expression) {
            org.orbisgis.style.parameter.Expression exp =  (org.orbisgis.style.parameter.Expression) sn;
            if (exp!=null) {                          
                Expression expParsed = CCJSqlParserUtil.parseExpression(exp.getExpression(), false);                  
                String formatedExp = expParsed.toString();
                String identifier = "unique_exp_id_" + count++;
                if (!expression_parameters.containsKey(formatedExp)) {
                    expression_parameters.put(formatedExp,identifier);
                    exp.setReference(identifier);
                    expressionsProperties.put(identifier,exp);
                } else {
                    String identifierKey = expression_parameters.get(formatedExp);
                    exp.setReference(identifierKey);
                    expressionsProperties.put(identifierKey, exp);
                }
            }
        }
        children.forEach((c) -> {
            try {
                visitImpl(c);
            } catch (Exception ex) {
                throw new IllegalArgumentException();
            }
        });
    }

    /**
     * Gets the {@code HashSet<String>} instance that contains all the field
     * names needed to use safely the last visited {@code SymbolizerNode}.
     *
     * @return
     */
    /*public HashMap<String, Object> getLiteralParameters() {
        return literal_parameters;
    }*/
    /**
     * Gets the {@code HashSet<String>} instance that contains all the field
     * names needed to use safely the last visited {@code SymbolizerNode}.
     *
     * @return
     */
    public HashMap<String, String> getExpressionParameters() {
        return expression_parameters;
    }

    public String getExpressionParametersAsString() {
        return expression_parameters.entrySet().stream().
                map(entrySet -> entrySet.getKey()+ " as " + entrySet.getValue()).
                collect(Collectors.joining(","));
    }

    public HashMap<String, org.orbisgis.style.parameter.Expression> getExpressionsProperties() {
        return expressionsProperties;
    }
}
