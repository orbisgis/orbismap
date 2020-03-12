/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.orbisgis.style.parameter.ExpressionParameter;

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
    public static String parse(ExpressionParameter expression) throws JSQLParserException  {
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
    public static ExpressionParameter formatConditionalExpression(ExpressionParameter expression) throws JSQLParserException  {
        if(expression!=null){
          Expression expParsed = CCJSqlParserUtil.parseCondExpression(expression.getExpression(), false);     
          return new ExpressionParameter(expParsed.toString());
        }
        return new ExpressionParameter("");
     }
}
