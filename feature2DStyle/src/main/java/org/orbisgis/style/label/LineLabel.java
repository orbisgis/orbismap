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
package org.orbisgis.style.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.style.common.RelativeOrientation;
import org.orbisgis.style.common.ShapeHelper;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.map.api.IMapTransform;
import org.orbisgis.style.IStyleNode;

/**
 * A {@code LineLabel} is a text of some kinf associated to a Line (polygon or not).
 * @author Alexis Guéganno, Maxence Laurent
 * @todo implements
 */
public class LineLabel extends Label {

    private RelativeOrientation orientation;

    /**
         * Build a new default {@code LineLabel}, using the defaults in 
         * {@link org.orbisgis.coremap.renderer.se.label.StyledText#StyledText()  StyledText}.
         * The label will be centered (horizontally), and in the middle (vertically)
         * of the graphic.
         */
    public LineLabel() {
        super();
        setVerticalAlign(VerticalAlignment.MIDDLE);
        setHorizontalAlign(HorizontalAlignment.CENTER);
    }

    

    

    /**
     * Gets the orientation of the characters along the line.
     */
    public final RelativeOrientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of the characters along the line.
     * @param orientation
     */
    public final void setOrientation(RelativeOrientation orientation) {
        this.orientation = orientation;
    }

   
        @Override
        public List<IStyleNode> getChildren() {
                List<IStyleNode> ls = new ArrayList<IStyleNode>();
                if (getLabel() != null) {
                        ls.add(getLabel());
                }
                return ls;
        }    
}
