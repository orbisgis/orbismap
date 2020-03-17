/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.parameter;

/**
 *
 * @author ebocher
 */
public class Literal extends ParameterValue{

    private Object value;
    private Class dataType = Object.class;
    
    public Literal(String value){
        this.value=value;
        this.dataType = String.class;
    }
    
    public Literal(Double value){
        this.value=value;
        this.dataType=Double.class;
    }
    
     public Literal(Float value){
        this.value=value;
        this.dataType=Float.class;
    }

    public Literal(Integer value) {
        this.value=value;
        this.dataType=Integer.class;
    }
    
    @Override
    public void setDataType(Class dataType) {
        this.dataType=dataType;
    }

    @Override
    public Class getDataType() {
        if(value!=null){
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
        this.value=value;
    }

}
