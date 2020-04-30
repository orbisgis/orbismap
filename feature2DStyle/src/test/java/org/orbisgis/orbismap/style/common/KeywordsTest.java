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

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.SortedSet;
import org.junit.jupiter.api.Test;
import org.orbisgis.orbismap.style.common.Description;
import org.orbisgis.orbismap.style.common.Keywords;
import org.orbisgis.orbismap.style.common.LocalizedText;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class KeywordsTest {

    public Description getDescription() throws Exception {
        Description description = new Description();
        URI uri = new URI("http://www.orbisgis.org");
        Keywords keywords = new Keywords();
        keywords.addLocalizedText(new LocalizedText("salut", new Locale("fr")));
        keywords.addLocalizedText(new LocalizedText("Bonjour", new Locale("fr")));
        keywords.addLocalizedText(new LocalizedText("Hallo", new Locale("de")));
        keywords.setType("politesse");
        description.putKeywords(uri, keywords);

        return description;
    }

    @Test
    public void testUnmarshall() throws Exception {
        HashMap<URI, Keywords> ks = getDescription().getKeywords();
        assertTrue(ks.size() == 1);
        Keywords k1 = ks.get(new URI("http://www.orbisgis.org"));
        SortedSet<LocalizedText> words = k1.getKeywords();
        assertTrue(words.size() == 3);
        assertTrue(k1.getKeywords(new Locale("fr")).size() == 2);
        assertTrue(k1.getType().equals("politesse"));
        Keywords k2 = ks.get(new URI("http://www.orbisgis.org"));
        assertEquals(3,k2.getKeywords().size());
        assertEquals(2,k2.getKeywords(new Locale("fr")).size());
        assertEquals(0,k2.getKeywords(new Locale("en")).size());
        assertEquals(1,k2.getKeywords(new Locale("de")).size());
        assertTrue(k2.getType().equals("politesse"));
    }
    

    @Test
    public void testPut() throws Exception {
        Description descr = getDescription();
        Keywords kds = new Keywords();
        kds.addLocalizedText(new LocalizedText("oh hai", Locale.UK));
        kds.setType("val");
        descr.putKeywords(new URI("http://www.cnrs.fr"), kds);
        assertTrue(descr.getKeywords().size() == 2);
        assertTrue(descr.getKeywords(new URI("http://www.cnrs.fr")) == kds);
    }

    @Test
    public void testPutTwice() throws Exception {
        Description descr = getDescription();
        Keywords kds = new Keywords();
        kds.addLocalizedText(new LocalizedText("oh hai", Locale.UK));
        kds.setType("val");
        descr.putKeywords(new URI("http://www.cnrs.fr"), kds);
        Keywords kdsb = new Keywords();
        kdsb.addLocalizedText(new LocalizedText("oh hai", Locale.UK));
        kdsb.setType("val");
        descr.putKeywords(new URI("http://www.cnrs.fr"), kdsb);
        assertTrue(descr.getKeywords().size() == 2);
        assertTrue(descr.getKeywords(new URI("http://www.cnrs.fr")) == kdsb);
    }

    @Test
    public void testPutTwiceNull() throws Exception {
        Description descr = getDescription();
        Keywords kds = new Keywords();
        kds.addLocalizedText(new LocalizedText("oh hai", Locale.UK));
        kds.setType("val");
        descr.putKeywords(null, kds);
        Keywords kdsb = new Keywords();
        kdsb.addLocalizedText(new LocalizedText("oh hai", Locale.UK));
        kdsb.setType("val");
        descr.putKeywords(null, kdsb);
        assertTrue(descr.getKeywords().size() == 2);
        assertTrue(descr.getKeywords(null) == kdsb);
    }

}
