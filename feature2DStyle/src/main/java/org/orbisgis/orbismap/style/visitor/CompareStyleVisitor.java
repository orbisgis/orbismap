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
package org.orbisgis.orbismap.style.visitor;

import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.parameter.Expression;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.ParameterException;

/**
 * A class to compare two style
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class CompareStyleVisitor {

    /**
     * Recursively visits {@code sn} and all its children
     *
     * @param expectedStyleNode
     * @param actualStyleNode
     */
    public void visitSymbolizerNode(IStyleNode expectedStyleNode, IStyleNode actualStyleNode) {
        try {
            visitImpl(expectedStyleNode, actualStyleNode);
        } catch (ParameterException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     *
     * @param expectedStyleNode the expected style node
     * @param actualStyleNode the current style node
     *
     * @throws ParameterException
     */
    protected void visitImpl(IStyleNode expectedStyleNode, IStyleNode actualStyleNode) throws ParameterException {
        List<IStyleNode> expectedChildren = expectedStyleNode.getChildren();
        List<IStyleNode> actualChildren = actualStyleNode.getChildren();
        if (expectedChildren.size() != actualChildren.size()) {
            throw new ParameterException("The styles doesn't have the same number of children; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
        }
        if (!expectedStyleNode.getClass().isAssignableFrom(actualStyleNode.getClass())) {
            throw new ParameterException("The styles doesn't have the same object instances ; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
        }
        if (expectedStyleNode instanceof Expression && actualStyleNode instanceof Expression) {
            //TODO :  to be fixed issue on https://github.com/JSQLParser/JSqlParser/issues/986
            /*try {
                net.sf.jsqlparser.expression.Expression expectedParsed = CCJSqlParserUtil.parseExpression(((Expression) expectedStyleNode).getExpression(), true);
                net.sf.jsqlparser.expression.Expression actualParsed = CCJSqlParserUtil.parseExpression(((Expression) actualStyleNode).getExpression(), true);
                if (!expectedParsed.toString().equals(actualParsed.toString())) {
                    throw new ParameterException("The styles doesn't have the same expression ; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
                }
            } catch (JSQLParserException ex) {
                throw new ParameterException(ex);
            }*/

        } else if (expectedStyleNode instanceof Literal && actualStyleNode instanceof Literal) {
            Literal expectedLiteral = (Literal) expectedStyleNode;
            Literal actualLiteral = (Literal) actualStyleNode;
            if (!expectedLiteral.equals(actualLiteral)) {
                throw new ParameterException("The literal values are not equal; expected = " + expectedLiteral + " and actual = " + actualLiteral);
            }
        }

        for (int i = 0; i < expectedChildren.size(); i++) {
            visitImpl(expectedChildren.get(i), actualChildren.get(i));
        }
    }

}
