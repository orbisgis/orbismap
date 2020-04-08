/**
 * Feature2DStyle is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
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

    @Override
    public void initDefault() {
     }

}
