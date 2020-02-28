/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.style.parameter.real;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.orbisgis.style.SeExceptions.InvalidStyle;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.ValueReference;

/**
 * The {@code ValueReference} implementation of {@code RealParameter}. That means that 
 * this class is used to retrieve real (numeric) values by using a table 
 * {@code DataSet} as specified in {@link ValueReference ValueReference}.</p>
 * <p>Note that the {@code DataSet} is not directly attached to the class,
 * and must be specified each time you call {@code getValue}.
 * @author Alexis Guéganno, Maxence Laurent
 */
public class RealAttribute extends ValueReference implements RealParameter {
    

    private RealParameterContext ctx;

    /**
     * Create a new instance of {@code RealAttribute}, with an empty associated field name.
     */
    public RealAttribute() {
        ctx = RealParameterContext.REAL_CONTEXT;
    }

    /**
     * Create a new instance of {@code RealAttribute}, setting the fieldName of the column where
     * the values will be searched.
     * @param fieldName 
     */
    public RealAttribute(String fieldName) {
        super(fieldName);
        ctx = RealParameterContext.REAL_CONTEXT;
    }

    

    @Override
    public Double getValue(ResultSet rs, long fid) throws ParameterException {
        try {
            if(rs.getRow() != fid) {
                rs.absolute((int)fid);
            }
            Object value = this.getFieldValue(rs, fid);
            if (value instanceof Double) {
                return (Double)value;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ParameterException("Could not fetch feature attribute "+ getColumnName(), e);

        }
    }

    @Override
    public Double getValue(Map<String,Object> map) throws ParameterException {
        try {
            Object value = this.getFieldValue(map);
            if (value instanceof Number) {
                return ((Number)value).doubleValue();
            } else {
                return null;
            }
        } catch (ParameterException e) {
            throw new ParameterException("Could not fetch feature attribute "+ getColumnName(), e);
        }
    }

    @Override
    public String toString() {
        return "<" + getColumnName() + ">";
    }

    @Override
    public void setContext(RealParameterContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public RealParameterContext getContext() {
        return ctx;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
