/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

import java.util.Objects;

/**
 *
 * @author Erwan Bocher
 */
public class ParameterDomain implements IParameterDomain{

    private Class dataType = Object.class;
    private String expression = "";
    
    public ParameterDomain(Class dataType) {
        this.dataType =dataType;
    }

    public  ParameterDomain(Class dataType, String expression) {
        this.dataType =dataType;
        this.expression = expression;
    }
     
    @Override
    public Class getDataType() {
        return dataType;
    }
    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.dataType);
        hash = 97 * hash + Objects.hashCode(this.expression);
        return hash;
    }
    
    
    

    
    
}
