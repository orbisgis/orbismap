package org.orbisgis.map.renderer.featureStyle.utils;

import org.orbisgis.style.parameter.ExpressionParameter;

import java.util.Map;

public class ValueHelper {

    public static String getAsString(Map<String, Object> properties, ExpressionParameter expressionParameter) {
        if (expressionParameter != null) {
            if (!expressionParameter.isFunction()) {
                return expressionParameter.getExpression();
            }
            return (String) properties.get(expressionParameter.getIdentifier());
        }
        return null;
    }

    public static Double getAsDouble(Map<String, Object> properties, ExpressionParameter expressionParameter) {
        if (expressionParameter != null) {
            if (!expressionParameter.isFunction()) {
                return Double.valueOf(expressionParameter.getExpression());
            }
            return (Double) properties.get(expressionParameter.getIdentifier());
        }
        return null;
    }

    public static Float getAsFloat(Map<String, Object> properties, ExpressionParameter expressionParameter) {
        if (expressionParameter != null) {
            if (!expressionParameter.isFunction()) {
                return Float.valueOf(expressionParameter.getExpression());
            }
            return (Float) properties.get(expressionParameter.getIdentifier());
        }
        return null;
    }
    
    public static Integer getAsInteger(Map<String, Object> properties, ExpressionParameter expressionParameter) {
        if (expressionParameter != null) {
            if (!expressionParameter.isFunction()) {
                return Integer.valueOf(expressionParameter.getExpression());
            }
            return (Integer) properties.get(expressionParameter.getIdentifier());
        }
        return null;
    }
    
    public static Boolean getAsBoolean(Map<String, Object> properties, ExpressionParameter expressionParameter) {
        if (expressionParameter != null) {
            if (!expressionParameter.isFunction()) {
                return Boolean.valueOf(expressionParameter.getExpression());
            }
            return (Boolean) properties.get(expressionParameter.getIdentifier());
        }
        return null;
    }
}
