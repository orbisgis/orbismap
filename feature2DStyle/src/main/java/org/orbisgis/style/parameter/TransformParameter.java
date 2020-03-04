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
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;

/**
 * SCALE(), ROTATE()...
 *
 * @author ebocher
 */
public class TransformParameter extends StyleNode implements IParameterValue, Comparable, IUom {

    private Uom uom;
    String expression = null;
    private String identifier;

    public TransformParameter(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setExpression(String expression) {
        this.expression = expression.trim();
    }

    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<IStyleNode>();
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof TransformParameter) {
            TransformParameter exp = (TransformParameter) t;
            if (exp.getExpression().equalsIgnoreCase(expression)) {
                return 0;
            } else {
                return -1;
            }

        }
        return -1;
    }

}
