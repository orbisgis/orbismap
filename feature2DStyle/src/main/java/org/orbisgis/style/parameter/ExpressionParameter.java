package org.orbisgis.style.parameter;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IParameterValue;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.StyleNode;

public class ExpressionParameter  extends StyleNode implements IParameterValue, Comparable{

    String expression ="";
    private String identifier;
    public ExpressionParameter(String expression){
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

}
