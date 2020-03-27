/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style;

import org.orbisgis.style.common.Description;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 *
 * @author ebocher
 */
public interface IFeatureSymbolizer extends ISymbolizer, Comparable{
    
    GeometryParameter getGeometryParameter();

    void setGeometryParameter(GeometryParameter geometryExpression);
    
    Description getDescription();
    
    void setDescription(Description description);
    
    ParameterValue getPerpendicularOffset();
    
    void setPerpendicularOffset(ParameterValue parameterValue);
    
}
