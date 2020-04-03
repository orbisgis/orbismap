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

import java.awt.Color;
import java.util.ArrayList;
import org.orbisgis.style.IFont;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 *
 * @author ebocher
 */
public class StyleFont extends StyleNode implements IFont<ParameterValue> {

    private ParameterValue fontFamily = new NullParameterValue();
    private ParameterValue fontWeight = new NullParameterValue();
    private ParameterValue fontStyle = new NullParameterValue();
    private ParameterValue fontSize = new NullParameterValue();
    private Uom uom;

    public StyleFont(){
        setUom(Uom.PT);
    }
    
    @Override
    public void initDefault(){;
        setFontFamily(new Literal("Arial"));
        setFontWeight(new Literal("Normal"));
        setFontStyle(new Literal("Normal"));
        setFontSize(new Literal(12f));
        setUom(Uom.PT);
    }
    
    @Override
    public java.util.List<IStyleNode> getChildren() {
        java.util.List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (fontFamily != null) {
            ls.add(fontFamily);
        }
        if (fontWeight != null) {
            ls.add(fontWeight);
        }
        if (fontStyle != null) {
            ls.add(fontStyle);
        }
        if (fontSize != null) {
            ls.add(fontSize);
        }
        return ls;
    }

   
    @Override
    public ParameterValue getFontFamily() {
        return fontFamily;
    }

    /**
     * Set the font family used to represent this <code>StyledText</code>
     *
     * @param fontFamily
     */
    public void setFontFamily(String fontFamily) {
        setFontFamily(new Literal(fontFamily));
    }

    
    @Override
    public void setFontFamily(ParameterValue fontFamily) {
        if (fontFamily == null) {
            this.fontFamily = new NullParameterValue();
            this.fontFamily.setParent(this);
        } else {
            this.fontFamily = fontFamily;
            this.fontFamily.setParent(this);
            this.fontFamily.format(String.class);
        }
    }

    
    @Override
    public ParameterValue getFontSize() {
        return fontSize;
    }

    /**
     * Set the font size used to represent this <code>StyledText</code>
     *
     * @param fontSize The new font's size
     */
    public void setFontSize(float fontSize) {
        setFontSize(new Literal(fontSize));
    }

    
    @Override
    public void setFontSize(ParameterValue fontSize) {
        if (fontSize == null) {
            this.fontSize = new NullParameterValue();
            this.fontSize.setParent(this);
        } else {
            this.fontSize = fontSize;
            this.fontSize.setParent(this);
            this.fontSize.format(Float.class, "value >0");
        }
    }

    
    @Override
    public ParameterValue getFontStyle() {
        return fontStyle;
    }

    /**
     * Set the font style used to represent this <code>StyledText</code>
     *
     * @param fontStyle The new font's style
     */
    public void setFontStyle(String fontStyle) {
        setFontStyle(new Literal(fontStyle));
    }

    
    @Override
    public void setFontStyle(ParameterValue fontStyle) {
        if (fontStyle == null) {
            this.fontStyle = new NullParameterValue();
            this.fontStyle.setParent(this);
        } else {
            this.fontStyle = fontStyle;
            this.fontStyle.setParent(this);
            this.fontStyle.format(String.class, "value in ('Normal', 'Italic', 'Oblique')");
        }
    }

    
    @Override
    public ParameterValue getFontWeight() {
        return fontWeight;
    }
    
    /**
     * Set the font weight used to represent this <code>StyledText</code>
     *
     * @param fontWeight The new font's weight
     */
    public void setFontWeight(String fontWeight) {
        setFontWeight(new Literal(fontWeight));
    }

    
    @Override
    public void setFontWeight(ParameterValue fontWeight) {
        if (fontWeight == null) {
            this.fontWeight = new NullParameterValue();
            this.fontWeight.setParent(this);
        } else {
            this.fontWeight = fontWeight;
            this.fontWeight.setParent(this);
            this.fontWeight.format(String.class, "value in ('Normal', 'Bold')");
        }
    }

     /**
     * Tries to retrieve the UOM of the font if any. If non can be found, return
     * the UOM of the parent node.
     *
     * @return
     */
    public Uom getFontUom() {
        if (uom != null) {
            return uom;
        } else if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }
    
    @Override
    public Uom getUom() {
        // Note: this.uom only affect font size
        return ((IUom) getParent()).getUom();
    }
    
    @Override
    public void setUom(Uom u) {
        this.uom = u;
    }
    
    @Override
    public Uom getOwnUom() {
        return this.uom;
    }

}
