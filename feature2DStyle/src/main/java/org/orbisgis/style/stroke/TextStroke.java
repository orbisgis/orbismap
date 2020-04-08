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
package org.orbisgis.style.stroke;

import java.util.ArrayList;
import java.util.List;

import org.orbisgis.style.label.LineLabel;
import org.orbisgis.style.IStyleNode;

/**
 * {@code TexteStroke} is used to render text labels along a line. It is useful 
 * to add informations to the {@code CompoundStroke} elements.
 * It is dependant on a {@link LineLabel}, to store the text to render, and the styling
 * details used for the rendering.
 * 
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public  class TextStroke extends Stroke {

        private LineLabel lineLabel;

        /**
         * Builds a new {@code TexteStroke} 
         */
        public TextStroke() {
        }

       

        /**
         * Get the {@link LineLabel} associated to this {@code TextStroke}.
         * @return
         * A {@link LineLabel} that contains all the informations needed to
         * render the text.
         */
        public LineLabel getLineLabel() {
                return lineLabel;
        }

        /**
         * Set the {@link LineLabel} associated to this {@code TextStroke}.
         * @param lineLabel
         */
        public void setLineLabel(LineLabel lineLabel) {
                this.lineLabel = lineLabel;

                if (lineLabel != null) {
                        lineLabel.setParent(this);
                }
        }
              
        @Override
        public List<IStyleNode> getChildren() {
                List<IStyleNode> ls = new ArrayList<IStyleNode>();
                if (lineLabel != null) {
                        ls.add(lineLabel);
                }
                return ls;
        }

    @Override
    public void initDefault() {
        LineLabel lineLab = new LineLabel();
        lineLab.initDefault();
        setLineLabel(lineLab);
    }
}
