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
public class NullParameterValue  extends ParameterValue{

    public NullParameterValue() {
        super(null, null);
    }
    

    @Override
    public Object getValue() {
            return null;
    }

    @Override
    public void setValue(Object value) {
         //Nothing to do
    }
   

    @Override
    public void setParameterDomain(IParameterDomain parameterDomain) {
         //Nothing to do
    }

    @Override
    public void format(Class dataType, String domainExpression) {
        //Nothing to do
    }
    
}
