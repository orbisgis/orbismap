/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

import java.util.Objects;

/**
 *
 * @author ebocher
 */
public class Literal extends ParameterValue {

    private Object value;
    private Class dataType = Object.class;

    public Literal(String value) {
        this.value = value;
        this.dataType = String.class;
    }

    public Literal(Boolean value) {
        this.value = value;
        this.dataType = Boolean.class;
    }

    public Literal(Number value) {
        this.value = value;
        this.dataType = Number.class;
    }

    @Override
    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    @Override
    public Class getDataType() {
        if (value != null) {
            return value.getClass();
        }
        return Object.class;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Literal)) {
            return false;
        }
        Literal other = (Literal) o;
        if (!value.equals(other.getValue())) {
            return false;
        }     
        
        if (!dataType.equals(other.getDataType())) {
            return false;
        } 
        return true;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.value);
        //hash = 97 * hash + Objects.hashCode(this.dataType);
        return hash;
    }

}
