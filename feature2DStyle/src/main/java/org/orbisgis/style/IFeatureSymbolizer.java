/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style;

import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 *
 * @author ebocher
 */
public interface IFeatureSymbolizer extends ISymbolizer, Comparable{
    
    GeometryParameter getGeometryParameter();

    void setGeometryParameter(GeometryParameter geometryExpression);
    
}
