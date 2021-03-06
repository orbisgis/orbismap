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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.orbisgis.orbismap.style.parameter.RuleFilter;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;

/**
 *
 * @author ebocher
 */
public class ExpressionParser {

    /**
     *
     * @param expression
     * @return
     * @throws JSQLParserException
     */
    public static String parse(org.orbisgis.orbismap.style.parameter.Expression expression) throws JSQLParserException {
        if (expression != null) {
            Expression expParsed = CCJSqlParserUtil.parseExpression(expression.getExpression(), false);
            return expParsed.toString();
        }
        return "";
    }

    /**
     * Use this class to format a rule and check if it's a conditional
     * expression
     *
     * @param ruleFilter
     * @return String
     * @throws JSQLParserException
     */
    public static String formatConditionalExpression(RuleFilter ruleFilter) throws JSQLParserException {
        if (ruleFilter != null) {
            Expression expParsed = CCJSqlParserUtil.parseCondExpression(ruleFilter.getExpression(), false);
            return expParsed.toString();
        }
        return null;
    }

    /**
     * Use this class to format a geometryParameter and check if the
     * expression is well formatted
     *
     * @param geometryParameter
     * @return String
     * @throws JSQLParserException
     */
    public static String formatGeometryParameterExpression(GeometryParameter geometryParameter) throws JSQLParserException {
        if (geometryParameter != null) {
            Expression expParsed = CCJSqlParserUtil.parseExpression(geometryParameter.getExpression(), false);
            return expParsed.toString();
        }
        return null;
    }

}
