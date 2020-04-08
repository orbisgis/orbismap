/**
 * MAP-API is part of the OrbisGIS platform
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
 * MAP-API  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * MAP-API  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * MAP-API  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MAP-API. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.api;

import java.util.Locale;

/**
 * This class intends to store a description of a {@code Rule}. It is made of
 * lists of title and abstract, and of sets of keywords. This class is built
 * to manage internationalization. As there can be only one title and one
 * abstract per language. Keywords are stored in a dedicated class.</p>
 * <p>According to 0GC 06-121r9, there shall be at most one title and/or
 * abstract per language. However, there may be many keywords associated to the
 * same language in a {@code Keywords} instance. In a {@code Description}
 * instance, there shall be at most one {@code Keywords} instance associated
 * to an authority.</p>
 * <p>Authorities are defined only considering the URI contained in the {@code
 * codeSpace} attribute of the {@code CodeType} element contained in {@code
 * Keywords}, according to 0GC 06-121r9. As there shall not be more than one
 * {@code Keywords} instance associated to a single authority, we map keywords
 * on this authority, ie only on the {@code URI}. The {@code CodeType} is not
 * considered meaningful in this mapping.
 * @author Alexis Guéganno
 * @author Erwan Bocher
 * @see Keywords
 */
public interface IDescription {
    
    /**
     * Gets the title of this {@code Description} associated to the given {@code
     * Locale}.
     * @param locale
     * @return
     */
    public String getTitle(Locale locale);
    
    
    /**
     * Adds a title to this {@code Description}, associated to the given {@code
     * Locale}.
     * @param locale The locale we are managing
     * @param text The new title. If null or empty, the entry is removed.
     * @return
     * The title that was previously associated to {@code Locale}, if any.
     */
    public String addTitle(Locale locale,String text);
    
    /**
     * Gets the abstract of this {@code Description} associated to the given
     * {@code Locale}.
     * @param locale
     * @return
     */
    public String getAbstract(Locale locale);
    
    /**
     * Adds an abstract to this {@code Description}, associated to the given
     * {@code Locale}.
     * @param locale
     * @param text  The new abstract. If null or empty, the entry is removed.
     * @return
     * The title that was previously associated to {@code Locale}, if any.
     */
    public String addAbstract(Locale locale,String text);
    
    
}
