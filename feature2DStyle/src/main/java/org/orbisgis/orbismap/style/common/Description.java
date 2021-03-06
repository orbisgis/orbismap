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
package org.orbisgis.orbismap.style.common;

import java.net.URI;
import java.util.*;
import org.orbisgis.orbismap.style.IDescription;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.StyleNode;

/**
 * This class intends to store a description of a {@code Rule}. It is made of
 * lists of title and abstract, and of sets of keywords. This class is built to
 * manage internationalization. As there can be only one title and one abstract
 * per language, we use a {@code HashMap} to manage them. Keywords are stored in
 * a dedicated class.</p>
 * <p>
 * According to 0GC 06-121r9, there shall be at most one title and/or abstract
 * per language. However, there may be many keywords associated to the same
 * language in a {@code Keywords} instance. In a {@code Description} instance,
 * there shall be at most one {@code Keywords} instance associated to an
 * authority.</p>
 * <p>
 * Authorities are defined only considering the URI contained in the {@code
 * codeSpace} attribute of the {@code CodeType} element contained in {@code
 * Keywords}, according to 0GC 06-121r9. As there shall not be more than one
 * {@code Keywords} instance associated to a single authority, we map keywords
 * on this authority, ie only on the {@code URI}. The {@code CodeType} is not
 * considered meaningful in this mapping.
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class Description extends StyleNode implements  IDescription{

    private HashMap<Locale, String> titles;
    private HashMap<Locale, String> abstractTexts;
    private HashMap<URI, Keywords> keywords;

    /**
     * Builds a new, empty, {@code Description}.
     */
    public Description() {
        titles = new HashMap<Locale, String>();
        abstractTexts = new HashMap<Locale, String>();
        keywords = new HashMap<URI, Keywords>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Description)) {
            return false;
        }
        final Description other = (Description) obj;
        if (this.titles != other.titles && (this.titles == null || !this.titles.equals(other.titles))) {
            return false;
        }
        if (this.abstractTexts != other.abstractTexts && (this.abstractTexts == null || !this.abstractTexts.equals(other.abstractTexts))) {
            return false;
        }
        if (this.keywords != other.keywords && (this.keywords == null || !this.keywords.equals(other.keywords))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.titles != null ? this.titles.hashCode() : 0);
        hash = 67 * hash + (this.abstractTexts != null ? this.abstractTexts.hashCode() : 0);
        hash = 67 * hash + (this.keywords != null ? this.keywords.hashCode() : 0);
        return hash;
    }

    /**
     * Find the most appropriate title close to the default Locale
     *
     * @return Title or Null if there is no title at all
     */
    public String getDefaultTitle() {
        // Find the title with default locale
        String title = getTitle(Locale.getDefault());
        if (title != null) {
            return title;
        }
        // Search with the lang only
        title = getTitle(new Locale(Locale.getDefault().getLanguage()));
        if (title != null) {
            return title;
        }
        // Get the first title
        if (!titles.isEmpty()) {
            return titles.values().iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Find the most appropriate title close to the default Locale
     *
     * @return Title or Null if there is no title at all
     */
    public String getDefaultAbstract() {
        // Find the title with default locale
        String defaultAbstract = getAbstract(Locale.getDefault());
        if (defaultAbstract != null) {
            return defaultAbstract;
        }
        // Search with the lang only
        defaultAbstract = getAbstract(new Locale(Locale.getDefault().getLanguage()));
        if (defaultAbstract != null) {
            return defaultAbstract;
        }
        // Get the first title
        if (!abstractTexts.isEmpty()) {
            return abstractTexts.values().iterator().next();
        } else {
            return null;
        }
    }

    

    /**
     * Gets the list of localized abstracts registered in this {@code
     * Description}.
     *
     * @return
     */
    public HashMap<Locale, String> getAbstractTexts() {
        return abstractTexts;
    }

    /**
     * Gets the list of localized keywords registered in this {@code
     * Description}.
     *
     * @return
     */
    public HashMap<URI, Keywords> getKeywords() {
        return keywords;
    }

    /**
     * Gets the set of keywords associated to this {@code URI}.
     *
     * @param uri
     * @return
     */
    public Keywords getKeywords(URI uri) {
        return keywords.get(uri);
    }

    /**
     * Sets the set of keywords associated to this {@code URI}.
     *
     * @param uri
     * @param keys
     */
    public void putKeywords(URI uri, Keywords keys) {
        keywords.put(uri, keys);
    }

    /**
     * Removes the set of keywords associated to this {@code URI}.
     *
     * @param uri
     * @return The {@code Keywords} instance that has just been removed from the
     * map of Keywords.
     */
    public Keywords removeKeywords(URI uri) {
        return keywords.remove(uri);
    }

    /**
     * Gets the list of localized titles registered in this {@code
     * Description}.
     *
     * @return
     */
    public HashMap<Locale, String> getTitles() {
        return titles;
    }

    /**
     * Sets the list of localized titles registered in this {@code
     * Description}.
     *
     * @param titles The map of titles
     */
    public void setTitles(HashMap<Locale, String> titles) {
        this.titles = titles;
    }

    /**
     * Adds a title to this {@code Description}, associated to the given {@code
     * Locale}.
     *
     * @param locale The locale we are managing
     * @param text The new title. If null or empty, the entry is removed.
     * @return The title that was previously associated to {@code Locale}, if
     * any.
     */
    public String addTitle(Locale locale, String text) {
        String ret;
        if (text == null || text.isEmpty()) {
            ret = titles.remove(locale);
        } else {
            ret = titles.put(locale, text);
        }
        return ret;
    }

    /**
     * Gets the title of this {@code Description} associated to the given {@code
     * Locale}.
     *
     * @param locale
     * @return
     */
    public String getTitle(Locale locale) {
        return titles.get(locale);
    }

    /**
     * Adds an abstract to this {@code Description}, associated to the given
     * {@code Locale}.
     *
     * @param locale
     * @param text The new abstract. If null or empty, the entry is removed.
     * @return The title that was previously associated to {@code Locale}, if
     * any.
     */
    public String addAbstract(Locale locale, String text) {
        String ret;
        if (text == null || text.isEmpty()) {
            ret = abstractTexts.remove(locale);
        } else {
            ret = abstractTexts.put(locale, text);
        }
        return ret;
    }

    /**
     * Gets the abstract of this {@code Description} associated to the given
     * {@code Locale}.
     *
     * @param locale
     * @return
     */
    public String getAbstract(Locale locale) {
        return abstractTexts.get(locale);
    }   

    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<IStyleNode>();
    }

    @Override
    public void initDefault() {
    }

}
