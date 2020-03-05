/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IParameterValue;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;

/**
 *
 * @author ebocher
 */
public class WKNExpression extends StyleNode implements IParameterValue, Comparable{

    
    String expression = "CIRCLE";
    private String identifier;
    
    //Use to parser different forme of input
    //expression or uri or name ttf://<fontname>#<hexcode>
    
    public WKNExpression(String expression){
        this.expression =expression;
    }

    public String getExpression() {
        return expression;
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setExpression(String expression) {
        this.expression = expression.trim();
    }
    
    @Override
    public List<IStyleNode> getChildren() {
       return new ArrayList<IStyleNode>();
    }
    

    @Override
    public int compareTo(Object t) {
    if (t instanceof WKNExpression) {
         WKNExpression exp = (WKNExpression) t;
         if(exp.getExpression().equalsIgnoreCase(expression)){
             return 0;
         }
         else{
             return -1;
         }
            
        }
        return -1;
    }

   
    
}
