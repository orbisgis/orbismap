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
public class Literal extends ParameterValue {

    public Literal(String value, String domainExpression) {
        super(value, new ParameterDomain(String.class, domainExpression));   

    }

    public Literal(String value) {
        super(value, new ParameterDomain(String.class));
    }

    public Literal(Boolean value) {
        super(value, new ParameterDomain(Boolean.class));
    }

    public Literal(Double value, String domainExpression) {
        super(value, new ParameterDomain(Double.class, domainExpression));
    }

    public Literal(Double value) {
        super(value, new ParameterDomain(Double.class));
    }    
    public Literal(Float value, String domainExpression) {
        super(value, new ParameterDomain(Float.class, domainExpression));
    }

    public Literal(Float value) {
        super(value, new ParameterDomain(Float.class));
    } 
    public Literal(Integer value, String domainExpression) {
        super(value, new ParameterDomain(Integer.class, domainExpression));
    }

    public Literal(Integer value) {
        super(value, new ParameterDomain(Integer.class));
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
        if (!getValue().equals(other.getValue())) {
            return false;
        }

        if (!getParameterDomain().equals(other.getParameterDomain())) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public void format(Class dataType, String expressionDomain) {
        if(dataType.isAssignableFrom(this.parameterDomain.getDataType())){            
            this.setDomain(dataType, expressionDomain);
        }else{
            throw new RuntimeException("Invalid data type for the value : "+ this.getValue() +". Must be " + dataType.getSimpleName() + 
                    " instead of "+ this.parameterDomain.getDataType().getSimpleName()+ " from style node : "+ getParent().getClass().getSimpleName());
        }   
        checkValue(this.getValue());
    }  

}
