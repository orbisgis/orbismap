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
package org.orbisgis.style.parameter.string;

import java.sql.ResultSet;
import java.util.Map;
import org.slf4j.*;


import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.parameter.Recode;

/**
 * <code>Recode</code> implementation that maps input values to {@code String} values.
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class Recode2String extends Recode<StringParameter, StringLiteral> implements StringParameter {

        private String[] restriction = new String[]{};
        private static final Logger LOGGER = LoggerFactory.getLogger(Recode2String.class);
        
        /**
         * Creates a new instance of <code>Recode2String</code>. The default result value
         * will be <code>fallback</code>, and the values that need to be processed
         * will be retrieved using <code>lookupValue</code>
         * @param fallback
         * @param lookupValue 
         */
        public Recode2String(StringLiteral fallback, StringParameter lookupValue) {
                super(fallback, lookupValue);
        }

       

        @Override
        public String getValue(ResultSet rs, long fid) throws ParameterException {
                try {
                        return getParameter(rs, fid).getValue(rs, fid);
                } catch (ParameterException ex) {
                        LOGGER.error("Fallback", ex);
                        return getFallbackValue().getValue(rs, fid);
                }
        }

        @Override
        public String getValue(Map<String,Object> map) {
                try {
                        return getParameter(map).getValue(map);
                } catch (ParameterException ex) {
                        LOGGER.error("Fallback", ex);
                        return getFallbackValue().getValue(map);
                }
        }

        @Override
        public void addMapItem(String key, StringParameter value) {
                value.setRestrictionTo(restriction);
                super.addMapItem(key, value);
        }

        @Override
        public void setRestrictionTo(String[] list) {
                restriction = list.clone();
                for (int i = 0; i < this.getNumMapItem(); i++) {
                        getMapItemValue(i).setRestrictionTo(list);
                }
        }
}