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
 * @author Erwan Bocher
 */
public abstract class ParameterValue extends StyleNode implements IParameterValue {

    
    private Object value;

    IParameterDomain parameterDomain;
    
    public ParameterValue(){
        this.parameterDomain = null;        
        this.value = null;
    }
        
    public ParameterValue(Object value, ParameterDomain parameterDomain){
        this.parameterDomain = parameterDomain;        
        this.value = value;
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<>();        
    }
    
    public Object getValue() { 
        return value;
    }
    
    public void setValue(Object value) {   
        this.value = value;
    }
    
    public IParameterDomain getParameterDomain() {
        return parameterDomain;
    }

    public void setParameterDomain(IParameterDomain parameterDomain) {
        this.parameterDomain = parameterDomain;
    }
    
      /**
     * Check if the value is valid
     *
     * @param value
     */
    public void checkValue(Object value) {
        if (this.parameterDomain != null && value != null) {
            String domainExpression = this.parameterDomain.getExpression();
            if (domainExpression != null && !domainExpression.isEmpty()) {
                DomainExpressionParser domainParser = new DomainExpressionParser(value);
                try {
                    if(!domainParser.evaluate(domainExpression)){
                        throw new RuntimeException("The value is not in the range of the domain. Expected : "+ domainExpression
                                + " from style node : " + getParent().getClass().getSimpleName());
                    }
                } catch (ParameterException ex) {
                    throw new RuntimeException("Unsupported value "+  value +" for the style node : "+ getParent().getClass().getSimpleName());
                }
            }
        }
    }

    /**
     * Check if type of the value matches the domain type
     * @param value 
     */
    public void checkValueType(Object value) {
        if (this.parameterDomain != null && value != null) {
            if (!this.getParameterDomain().getDataType().isAssignableFrom(value.getClass())) {
                throw new RuntimeException("The value doesn't not match the data type.\n Expected :  "+ this.getParameterDomain().getDataType());
            }
        }
    }
    
    public void format(Class dataType) {
        format(dataType, "");
    }
    
    public abstract void format(Class dataType, String expressionDomain);
    
    /**
     * 
     * @param dataType
     * @param expressionDomain 
     */
    public void setDomain(Class dataType, String expressionDomain) {
        if (expressionDomain != null && !expressionDomain.isEmpty()) {
            this.parameterDomain = new ParameterDomain(dataType, expressionDomain);
        } else {
            this.parameterDomain = new ParameterDomain(dataType);
        }
    }
}
