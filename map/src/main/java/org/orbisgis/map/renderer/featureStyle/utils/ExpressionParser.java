/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

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
    public static String parse(org.orbisgis.style.parameter.Expression expression) throws JSQLParserException  {
        if(expression!=null){
          Expression expParsed = CCJSqlParserUtil.parseExpression(expression.getExpression(), false);     
          return expParsed.toString();
        }
        return "";
     }
    
    /**
     * 
     * @param expression
     * @return
     * @throws JSQLParserException 
     */
    public static org.orbisgis.style.parameter.Expression formatConditionalExpression(org.orbisgis.style.parameter.Expression expression) throws JSQLParserException  {
        if(expression!=null){
          Expression expParsed = CCJSqlParserUtil.parseCondExpression(expression.getExpression(), false);     
          return new org.orbisgis.style.parameter.Expression(expParsed.toString());
        }
        return new org.orbisgis.style.parameter.Expression("");
     }
}
