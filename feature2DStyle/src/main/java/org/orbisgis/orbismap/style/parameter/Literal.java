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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.parameter;

import java.util.Objects;

/**
 * A Literal class to manage value and its respective domain
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class Literal extends ParameterValue {

    public Literal(String value, String domainExpression) {
        super(value, new ParameterDomain(value.getClass(), domainExpression));
    }

    public Literal(String value) {
        super(value, new ParameterDomain(value.getClass()));
    }

    public Literal(Boolean value) {
        super(value, new ParameterDomain(value.getClass()));
    }

    public Literal(Double value, String domainExpression) {
        super(value, new ParameterDomain(value.getClass(), domainExpression));
    }

    public Literal(Double value) {
        super(value, new ParameterDomain(value.getClass()));
    }

    public Literal(Float value, String domainExpression) {
        super(value, new ParameterDomain(value.getClass(), domainExpression));
    }

    public Literal(Float value) {
        super(value, new ParameterDomain(value.getClass()));
    }

    public Literal(Integer value, String domainExpression) {
        super(value, new ParameterDomain(value.getClass(), domainExpression));
    }

    public Literal(Integer value) {
        super(value, new ParameterDomain(value.getClass()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Literal)) {
            return false;
        }
        Literal other = (Literal) o;
        if (!getValue().equals(other.getValue())) {
            return false;
        }

        if (!getParameterDomain().equals(other.getParameterDomain())) {
            return false;
        }
        return true;

    }

    @Override
    public void format(Class expectedDataType, String expressionDomain) {
        if (expectedDataType.isAssignableFrom(this.parameterDomain.getDataType())) {
            this.setDomain(expectedDataType, expressionDomain);
        } else if (expectedDataType.isAssignableFrom(Float.class)) {
            if (getValue() instanceof String) {
                this.setValue(Float.parseFloat((String) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else if (getValue() instanceof Double) {
                this.setValue((Float) getValue());
                this.setDomain(expectedDataType, expressionDomain);
            } else if (getValue() instanceof Integer) {
                this.setValue(((Integer) getValue()).floatValue());
                this.setDomain(expectedDataType, expressionDomain);
            } else {
                throw new RuntimeException("Cannot format the literal value to float");
            }
        } else if (expectedDataType.isAssignableFrom(Double.class)) {
            if (getValue() instanceof String) {
                this.setValue(Double.parseDouble((String) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else if (getValue() instanceof Float) {
                this.setValue(new Double((Float) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else if (getValue() instanceof Integer) {
                this.setValue(new Double((Integer) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else {
                throw new RuntimeException("Cannot format the literal value to double");
            }
        } else if (expectedDataType.isAssignableFrom(Integer.class)) {
            if (getValue() instanceof String) {
                this.setValue(Integer.parseInt((String) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else {
                throw new RuntimeException("Cannot format the literal value to int");
            }
        } else if (expectedDataType.isAssignableFrom(Boolean.class)) {
            if (getValue() instanceof String) {
                this.setValue(Boolean.parseBoolean((String) getValue()));
                this.setDomain(expectedDataType, expressionDomain);
            } else if (getValue() instanceof Integer) {
                this.setValue((Integer) getValue());
                this.setDomain(expectedDataType, expressionDomain);
            } else {
                throw new RuntimeException("Cannot format the literal value to boolean");
            }

        } else {
            throw new RuntimeException("Invalid data type for the value : " + this.getValue() + ". Must be " + expectedDataType.getSimpleName()
                    + " instead of " + this.parameterDomain.getDataType().getSimpleName() + " from style node : " + getParent().getClass().getSimpleName());
        }

        checkValue(this.getValue());
    }

    @Override
    public void initDefault() {
        setValue("Feature2DStyle");
        setParameterDomain(new ParameterDomain(String.class));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.getValue());
        hash = 67 * hash + (this.getParameterDomain() != null ? this.getParameterDomain().hashCode() : 0);
        return hash;
    }

}
