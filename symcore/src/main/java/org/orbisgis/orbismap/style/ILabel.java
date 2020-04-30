/**
 * SYMCORE is part of the OrbisGIS platform.
 * 
 * It's implement the OGC Symbology Conceptual Model: Core part.
 * 
 * Internal reference number of this OGC ® document: 18-067
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
 * SYMCORE is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * SYMCORE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SYMCORE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * SYMCORE. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style;

/**
 * ILabel class defines the graphical symbolizing properties for drawing a text
 * label. As an abstract class and part of the base of the core graphical
 * concepts, ​ LabelClass is a point of extension to specify concrete ways to
 * draw text label according to placement behaviours (e.g., a PointLabel or
 * LineLabel).
 *
 * @author Ertz Olivier, HEIG-VD (2010-2020)
 * @author Erwan Bocher, CNRS (2010-2020)
 * 
 * @param <T>
 */
public interface ILabel<T extends IParameterValue> extends IStyleNode, IUom {

    IFont getFont();

    void setFont(IFont font);

    T getLabelText();

    void setLabelText(T parameterValue);

}
