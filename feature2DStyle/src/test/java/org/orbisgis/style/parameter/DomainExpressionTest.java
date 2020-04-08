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

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Erwan Bocher
 */
public class DomainExpressionTest {

    @Test
    public void domainExpresionNumberValue() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser(10);
        assertTrue(domainExpression.evaluate(""));
        assertFalse(domainExpression.evaluate("value > 12"));
        assertTrue(domainExpression.evaluate("value > 9"));
        assertTrue(domainExpression.evaluate("value > 9.5"));
        assertFalse(domainExpression.evaluate("value > 0 and value <1"));
        assertTrue(domainExpression.evaluate("value > 0 and value <=10"));
        assertTrue(domainExpression.evaluate("value >= 10 and value <=10"));
        assertTrue(domainExpression.evaluate("value > 0 and value >1"));
        assertTrue(domainExpression.evaluate("value = 10"));
        assertFalse(domainExpression.evaluate("value = 1"));
        assertTrue(domainExpression.evaluate("value > 0 or value <1"));
        assertTrue(domainExpression.evaluate("value >= 10 or value <=10"));
        assertFalse(domainExpression.evaluate("(value > 12) or (value > 15)"));
        assertTrue(domainExpression.evaluate("(value < 12) or (value > 15)"));
        assertTrue(domainExpression.evaluate("value in (12,13,10)"));
    }

    @Test
    public void domainExpresionStringValueError() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser("10");
        Assertions.assertThrows(RuntimeException.class, () -> {
            domainExpression.evaluate("value > 12");
        });
    }
    
    @Test
    public void domainExpresionStringValue() throws ParameterException {
        DomainExpressionParser domainExpression = new DomainExpressionParser("orbisgis");
        assertTrue(domainExpression.evaluate("value = 'orbisgis'"));
        assertFalse(domainExpression.evaluate("value = 'Orbisgis'"));       
        assertTrue(domainExpression.evaluate("value in ('orbisgis', 12, 'super')"));
        assertTrue(domainExpression.evaluate("value in ('orbisgis', 'vannes', 'super')")); 
    }

}
