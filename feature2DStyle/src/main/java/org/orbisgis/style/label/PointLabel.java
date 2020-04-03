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
package org.orbisgis.style.label;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IFont;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 * A label located at a single point. In addition to all the {@code Label}
 * characteristics, it has two additional properties :
 * <ul><li>A rotation angle, in degrees.</li>
 * <li>An exclusion zone, ie a zone around the label where no other text will be
 * displayed.</li>
 * </ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class PointLabel extends Label {
    
    private ParameterValue rotation = new NullParameterValue();
    private ExclusionZone exclusionZone;

    /**
     * Creates a new {@code PointLabel} with default values as detailed in
     * {@link org.orbisgis.coremap.renderer.se.label.Label#Label() Label} and
     * {@link org.orbisgis.coremap.renderer.se.label.StyledText#StyledText() StyledText}.
     * This {@code PointLabel} will be top and right aligned.
     */
    public PointLabel() {
        super();
    }

    /**
     * Get the exclusion zone defined for this {@code PointLabel}. In this zone,
     * we won't draw any other text.
     *
     * @return An {@link ExclusionZone} instance.
     */
    public ExclusionZone getExclusionZone() {
        return exclusionZone;
    }

    /**
     * Set the exclusion zone defined for this {@code PointLabel}.
     *
     * @param exclusionZone The new exclusion zone
     */
    public void setExclusionZone(ExclusionZone exclusionZone) {
        this.exclusionZone = exclusionZone;
        if (exclusionZone != null) {
            exclusionZone.setParent(this);
        }
    }

    /**
     * Get the rotation that must be applied to this {@code PointLabel} before
     * rendering.
     *
     * @return The rotation, in degrees, as a {@link RealParameter}
     */
    public ParameterValue getRotation() {
        return rotation;
    }

    /**
     * Set the rotation that must be applied to this {@code PointLabel} before
     * rendering.
     *
     * @param rotation The new rotation to be used.
     */
    public void setRotation(float rotation) {
        setRotation(new Literal(rotation));
    }

    /**
     * Set the rotation that must be applied to this {@code PointLabel} before
     * rendering.
     *
     * @param rotation The new rotation to be used.
     */
    public void setRotation(ParameterValue rotation) {
        if (rotation == null) {
            this.rotation = new NullParameterValue();
            this.rotation.setParent(this);
        } else {
            this.rotation = rotation;
            this.rotation.setParent(this);
            this.rotation.format(Float.class, "value>=0 and value <=180");
        }
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();        
        
        if (getLabelText() != null) {
            ls.add(getLabelText());
        }
        IFont font =  getFont();
        if(font!=null){
        if (font.getFontFamily() != null) {
            ls.add(font.getFontFamily());
        }
        if (font.getFontWeight() != null) {
            ls.add(font.getFontWeight());
        }
        if (font.getFontStyle() != null) {
            ls.add(font.getFontStyle());
        }
        if (font.getFontSize() != null) {
            ls.add(font.getFontSize());
        }
        }
        if (getStroke() != null) {
            ls.add(getStroke());
        }
        if (getFill() != null) {
            ls.add(getFill());
        }
        if (getHalo() != null) {
            ls.add(getHalo());
        }
        
        if (exclusionZone != null) {
            ls.add(exclusionZone);
        }
        if (rotation != null) {
            ls.add(rotation);
        }
        return ls;
    }

    @Override
    public void initDefault() {
        StyleFont styleFont =  new StyleFont();
        styleFont.initDefault();
        setFont(styleFont);
        SolidFill solidFill = new SolidFill();
        solidFill.initDefault();
        setFill(solidFill);
    }
    
}
