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
package org.orbisgis.orbismap.style.parameter;

import java.util.Objects;

/**
 * ParamaterValue to manage Expression
 * 
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class Expression extends ParameterValue {

    private final String expression;
    private Object value;
    private String reference = "";

    public Expression(String expression) {
        this.expression = expression;
        this.setLiteral(false);
    }

    @Override
    public Object getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        this.checkValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return expression.equals(that.expression) &&
                reference.equals(that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression, value, reference);
    }

    @Override
    public void format(Class dataType, String expressionDomain) {
        this.setDomain(dataType, expressionDomain);
    }

    @Override
    public void initDefault() {
    }

    @Override
    public String toString() {
        return "Expression :  "+ getExpression();
    }
}
