/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.utils;

import org.orbisgis.style.parameter.ExpressionParameter;

import java.awt.Color;
import static org.orbisgis.style.parameter.color.ColorHelper.toHex;

/**
 *
 * @author ebocher
 */
public class ExpressionHelper {
    
    
     public static ExpressionParameter toExpressionParameter(Color color){
        return new ExpressionParameter("'"+toHex(color)+"'");
    }
    
}
