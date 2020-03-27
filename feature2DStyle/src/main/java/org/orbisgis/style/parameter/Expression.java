/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

/**
 *
 * @author Erwan Bocher
 */
public class Expression extends ParameterValue {

    private final String expression;
    private Object value;
    private String reference = "";

    public Expression(String expression) {
        this.expression = expression;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        this.checkValue(value);
    }   

    @Override
    public void format(Class dataType, String expressionDomain) {
        this.setDomain(dataType, expressionDomain);
    }
}
