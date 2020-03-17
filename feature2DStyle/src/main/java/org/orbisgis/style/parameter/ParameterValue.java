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

    
    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<>();
    }

    public abstract void setDataType(Class dataType);

    public abstract Class getDataType();
    
    public abstract Object getValue();
    
    public abstract void setValue(Object value);
    
}
