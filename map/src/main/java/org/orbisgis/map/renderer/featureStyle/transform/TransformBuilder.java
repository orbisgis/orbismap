/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer.featureStyle.transform;

import java.awt.geom.AffineTransform;
import java.util.Map;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.orbisgis.map.renderer.featureStyle.ITranformBuilder;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.TransformParameter;

/**
 *
 * @author ebocher
 */
public class TransformBuilder implements ITranformBuilder{

    
    
    @Override
    public AffineTransform getAffineTransform(TransformParameter transformParameter, Map<String, Object> properties) {
        Uom uom = transformParameter.getUom();
        if(transformParameter!=null && !transformParameter.getExpression().isEmpty()){
            return parseTransformExpression(transformParameter.getExpression());
        }
        return null;
    }
    
    
    /**
     * Visit the expression to create the AffineTransform
     * TODO : implement it and cache it
     * @param expression
     * @return 
     */
    private static AffineTransform parseTransformExpression(String expression){
        ExpressionDeParser deparser = new ExpressionDeParser() {
            @Override
            public void visit(Function function) {
                
            }
        };
        return null;
    }
    
}
