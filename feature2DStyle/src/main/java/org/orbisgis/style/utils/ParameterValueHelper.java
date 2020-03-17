/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.utils;

import java.awt.Color;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterValue;
import static org.orbisgis.style.utils.ColorHelper.toHex;

/**
 *
 * @author Erwan Bocher
 */
public class ParameterValueHelper {

    public static Literal toExpression(Color color) {
        return new Literal(toHex(color));
    }

    public static Literal randomColor() {
        return new Literal(ColorHelper.toHex(ColorHelper.getRandomColor()));
    }

    public static void validateAsString(ParameterValue parameterValue) {
        if (parameterValue != null) {
            if (parameterValue instanceof Expression) {
                //Force the returned data type
                parameterValue.setDataType(String.class);
            } else if(parameterValue instanceof Literal){
                Object value = parameterValue.getValue();
                if (value != null) {
                    if (!(value instanceof String)) {
                        parameterValue.setValue(String.valueOf(value));
                    }
                }
            }
        }        
    }

    public static void validateAsFloat(ParameterValue parameterValue) {
        if (parameterValue != null) {
            if (parameterValue instanceof Expression) {
                //Force the returned data type
                parameterValue.setDataType(Float.class);
            } else if(parameterValue instanceof Literal) {
                Object value = parameterValue.getValue();
                if (value != null) {
                    if (value instanceof String) {
                        parameterValue.setValue(Float.parseFloat((String) value));
                    } else if (value instanceof Number) {
                        parameterValue.setValue(((Number) value).floatValue());
                    }
                }
            }
        }
    }

    public static void validateAsDouble(ParameterValue parameterValue) {
        if (parameterValue != null) {
            if (parameterValue instanceof Expression) {
                //Force the returned data type
                parameterValue.setDataType(Double.class);
            } else if(parameterValue instanceof Literal) {
                Object value = parameterValue.getValue();
                if (value != null) {
                    if (value instanceof String) {
                        parameterValue.setValue(Double.parseDouble((String) value));
                    } else if (value instanceof Number) {
                        parameterValue.setValue(((Number) value).doubleValue());
                    }
                }
            }
        }        
    }

    public static void validateAsInteger(ParameterValue parameterValue) {
        if (parameterValue != null) {
            if (parameterValue instanceof Expression) {
                //Force the returned data type
                parameterValue.setDataType(Integer.class);
            } else if(parameterValue instanceof Literal) {
                Object value = parameterValue.getValue();
                if (value != null) {
                    if (value instanceof String) {
                        parameterValue.setValue(Integer.parseInt((String) value));
                    } else if (value instanceof Number) {
                        parameterValue.setValue(((Number) value).intValue());
                    }
                }
            }
        } 
    }

}
