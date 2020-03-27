/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.utils;

import java.awt.Color;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterDomain;
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


    /**
     * Return the enum value according the class enum
     *
     * @param <T>
     * @param enumType
     * @param name
     * @return
     */
    public static <T extends Enum<?>> T lookup(Class<T> enumType,
            String name) {
        for (T enumn : enumType.getEnumConstants()) {
            if (enumn.name().equalsIgnoreCase(name)) {
                return enumn;
            }
        }
        return null;
    }

    public static Literal createFloatLiteral(Float value) {
        Literal literal = new Literal(value);
        literal.setParameterDomain(new ParameterDomain(Float.class));
        return literal;
    }

    public static Literal createDoubleLiteral(Double value) {
        Literal literal = new Literal(value);
        literal.setParameterDomain(new ParameterDomain(Double.class));
        return literal;
    }

}
