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
package org.orbisgis.style.visitor;


import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterException;

/**
 * Search for the names of the features that are used in the visited tree of
 * {@link SymbolizerNode} instances.
 *
 * @author Erwan Bocher, CNRS
 */
public class CompareStyleVisitor {

    /**
     * Recursively visits {@code sn} and all its children, searching for
     * feature-dependant nodes.
     *
     * @param sn
     */
    public void visitSymbolizerNode(IStyleNode expectedStyleNode, IStyleNode actualStyleNode) {
        try {
            visitImpl(expectedStyleNode, actualStyleNode);
        } catch (ParameterException ex) {
            throw new IllegalArgumentException();
        }
    }

    /**
     *      *
     * @param expectedStyleNode
     * @param actualStyleNode
     * @throws org.orbisgis.style.parameter.ParameterException
     */
    protected void visitImpl(IStyleNode expectedStyleNode, IStyleNode actualStyleNode) throws ParameterException {
        List<IStyleNode> expectedChildren = expectedStyleNode.getChildren();
        List<IStyleNode> actualChildren = actualStyleNode.getChildren();
        if (expectedChildren.size() != actualChildren.size()) {
            throw new ParameterException("The styles doesn't have the same number of children; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
        }
        if(!expectedStyleNode.getClass().isAssignableFrom(actualStyleNode.getClass())) {
            throw new ParameterException("The styles doesn't have the same object instances ; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
         }
        if (expectedStyleNode instanceof Expression && actualStyleNode instanceof Expression) {
            try {
                net.sf.jsqlparser.expression.Expression expectedParsed = CCJSqlParserUtil.parseExpression(((Expression)expectedStyleNode).getExpression(), false);
                net.sf.jsqlparser.expression.Expression actualParsed = CCJSqlParserUtil.parseExpression(((Expression)actualStyleNode).getExpression(), false);
                if(!expectedParsed.toString().equals(actualParsed.toString())){
                   throw new ParameterException("The styles doesn't have the same expression ; expected style node = " + expectedStyleNode + " and actual style node = " + actualStyleNode);
                    }
            } catch (JSQLParserException ex) {
                throw new ParameterException(ex);
            }
            
        }
        else if(expectedStyleNode instanceof Literal && actualStyleNode instanceof Literal) {
            Literal expectedLiteral = (Literal) expectedStyleNode;
            Literal actualLiteral = (Literal) actualStyleNode;
            if(!expectedLiteral.equals(actualLiteral)){
                throw new ParameterException("The literal values are not equal; expected = " + expectedLiteral + " and actual = " +actualLiteral);
            }
        }
        
        for (int i = 0; i < expectedChildren.size(); i++) {
                visitImpl(expectedChildren.get(i), actualChildren.get(i));           
        }
    }   
   
}
