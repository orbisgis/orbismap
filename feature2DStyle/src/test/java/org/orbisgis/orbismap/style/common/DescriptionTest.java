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
package org.orbisgis.orbismap.style.common;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.orbisgis.orbismap.style.common.Description;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class DescriptionTest {

    private Description getDescription() {
        Description description = new Description();
        description.addTitle(new Locale("en"), "Hello");
        description.addTitle(new Locale("fr"), "Bonjour");
        description.addTitle(new Locale("br", "FR"), "Salud");
        description.addAbstract(new Locale("en"),"I've said hello !");
        description.addAbstract(new Locale("fr"),"J'ai dit bonjour !");
        description.addAbstract(new Locale("de"),"Ich habe guten Tag gesagt !");
        return description;
    }

    /**
     * We just prove that we count right, for now...
     *
     * @throws Exception
     */
    @Test
    public void testDeserialization() throws Exception {
        Description descr = getDescription();
        assertNotNull(descr);
        assertTrue(descr.getTitles().size() == 3);
        assertTrue(descr.getTitle(new Locale("en")).equals("Hello"));
        assertTrue(descr.getTitle(new Locale("fr")).equals("Bonjour"));
        assertTrue(descr.getTitle(new Locale("br", "FR")).equals("Salud"));
        assertTrue(descr.getAbstractTexts().size() == 3);
        assertTrue(descr.getAbstract(new Locale("en")).equals("I've said hello !"));
        assertTrue(descr.getAbstract(new Locale("fr")).equals("J'ai dit bonjour !"));
        assertTrue(descr.getAbstract(new Locale("de")).equals("Ich habe guten Tag gesagt !"));
    }

    @Test
    public void testAddTitle() throws Exception {
        Description descr = getDescription();
        assertTrue(descr.getTitle(new Locale("en")).equals("Hello"));
        descr.addTitle(new Locale("en", "en"), "Good morning");
        assertTrue(descr.getTitle(new Locale("en", "en")).equals("Good morning"));
        assertTrue(descr.getTitle(new Locale("en")).equals("Hello"));
    }

    @Test
    public void testAddAbstract() throws Exception {
        Description descr = getDescription();
        assertTrue(descr.getAbstract(new Locale("en")).equals("I've said hello !"));
        descr.addAbstract(new Locale("en", "en"), "Good morning world");
        assertTrue(descr.getAbstract(new Locale("en", "en")).equals("Good morning world"));
        assertTrue(descr.getAbstract(new Locale("en")).equals("I've said hello !"));
    }

    @Test
    public void testOverrideTitle() throws Exception {
        Description descr = getDescription();
        assertTrue(descr.getTitle(new Locale("en")).equals("Hello"));
        descr.addTitle(new Locale("en"), "Good morning");
        assertTrue(descr.getTitle(new Locale("en")).equals("Good morning"));
    }

    @Test
    public void testOverrideAbstract() throws Exception {
        Description descr = getDescription();
        assertTrue(descr.getAbstract(new Locale("en")).equals("I've said hello !"));
        descr.addAbstract(new Locale("en"), "Good morning world");
        assertTrue(descr.getAbstract(new Locale("en")).equals("Good morning world"));
    }

}
