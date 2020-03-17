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
public class NullParameterValue  extends ParameterValue{


    @Override
    public void setDataType(Class dataType) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Class getDataType() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Object getValue() {
            return null;
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
}
