package org.orbisgis.style.parameter;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IParameterValue;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;

/**
 * Class to manage expression used by style elements
 * By default an expression is a literal value, stored a string
 * @author ebocher
 */
public class ExpressionParameter  extends StyleNode implements IValueIdentifier, IParameterValue, Comparable{

    private String expression ="";
    private String identifier ="";
    private boolean isFunction = false;
    
    public ExpressionParameter(String expression){
        this(expression, false);
    }
    
    public ExpressionParameter(String expression, boolean isFunction){        
        this.expression = expression;
        this.isFunction = isFunction;
    }
    
    public ExpressionParameter(double expression){
        this(String.valueOf(expression), false);
    }
    public ExpressionParameter(double expression, boolean isFunction){
        this(String.valueOf(expression), isFunction);
    }
    
    public ExpressionParameter(int expression) {
        this(String.valueOf(expression), false);
    }
    public ExpressionParameter(int expression, boolean isFunction) {
        this(String.valueOf(expression), isFunction);
    }
    
    public ExpressionParameter(boolean expression) {
        this(String.valueOf(expression), false);
    }
    
     public ExpressionParameter(boolean expression, boolean isFunction) {
        this(String.valueOf(expression), isFunction);
    }

    public String getExpression() {
        return expression;
    }
    
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public void setExpression(String expression, boolean isFunction) {       
        this.expression = expression.trim();
        this.isFunction = isFunction;
    }

    public void setExpression(String expression) {
        this.expression = expression.trim();
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean isFunction) {
        this.isFunction = isFunction;
    }
    

   

    @Override
    public int compareTo(Object t) {
        if (t instanceof ExpressionParameter) {
         ExpressionParameter exp = (ExpressionParameter) t;
         if(exp.getExpression().equalsIgnoreCase(expression)){
             return 0;
         }
         else{
             return -1;
         }
            
        }
        return -1;
    }

    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<>();
    }

       

}
