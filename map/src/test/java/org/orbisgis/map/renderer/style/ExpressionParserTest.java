/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.style;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;

/**
 *
 * @author ebocher
 */
public class ExpressionParserTest {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JSQLParserException {
        
        //Field
        Expression expr = CCJSqlParserUtil.parseExpression("the_geom", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        //Value
        expr = CCJSqlParserUtil.parseExpression("2", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());

        //DashArray
        expr = CCJSqlParserUtil.parseExpression("'2 2'", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        expr = CCJSqlParserUtil.parseExpression("'POINT(0 0)'::GEOMETRY", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        expr = CCJSqlParserUtil.parseExpression("CASE 3 + 4 WHEN 1 then 0 END", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        expr = CCJSqlParserUtil.parseExpression("(ST_area(the_geom) + 1)/2", false);  
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        final List<String> columnList = new ArrayList<String>();
        expr.accept(new ExpressionVisitorAdapter() {
            @Override
            public void visit(Column column) {
                super.visit(column);
                columnList.add(column.getColumnName());
            }
        });
        System.out.println(expr.toString());
        System.out.println(String.join(",", columnList));
        expr = CCJSqlParserUtil.parseExpression(" test ", false);         
        System.out.println(expr.toString());
    }
    
    public static void transformExpression() throws JSQLParserException{
        Expression expr = CCJSqlParserUtil.parseExpression("scale(rotate(), 12,12)", false);        
        System.out.println(expr.toString());  
        
    }
    
}
