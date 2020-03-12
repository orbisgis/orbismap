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

import java.awt.*;
import java.util.ArrayList;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.StrokeNode;
import org.orbisgis.style.utils.ExpressionHelper;
import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.ExpressionParameter;

/**
 * This class embed all the informations needed to represent text of any kind on a map.
 * A <code>StyledText</code> is defined with several values :
 * <ul><li>A text value</li>
 * <li>A font</li>
 * <li>A weight (Normal or Bold)</li>
 * <li>A style (Normal, Italic or Oblique)</li>
 * <li>A size</li>
 * <li>A stroke</li></ul>
 * Color and opacity of the text are defined using a <code>Fill</code> instance
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class StyledText extends StyleNode implements IUom, FillNode, StrokeNode {
    private ExpressionParameter text;
    private ExpressionParameter fontFamily;
    private ExpressionParameter fontWeight;
    private ExpressionParameter fontStyle;
    private ExpressionParameter fontSize;
    private Stroke stroke;
    private IFill fill;
    private Halo halo;
    private Uom uom;

    /**
     * Fill a new StyledText with default values. Inner text is <code>Label</code>,
     * it will be displayed in Arial, with a normal weight, a normal style, a 12 font size.
     * It is displayed in black, and completely opaque.
     */
    public StyledText() {
        this("Label");
    }

    
    /**
     * Fill a new <code>StyledText</code> with the given text value and default values. The text 
     * will be displayed in Arial, with a normal weight, a normal style, a 12 font size.
     * It is displayed in black, and completely opaque.
     * @param label 
     */
    public StyledText(String label) {
        setText(new ExpressionParameter(label));
        setFontFamily(new ExpressionParameter("Arial"));
        setFontWeight(new ExpressionParameter("Normal"));
        setFontStyle(new ExpressionParameter("Normal"));
        setFontSize(new ExpressionParameter(12));
        setUom(Uom.PT);

        SolidFill f = new SolidFill();
        f.setOpacity(new ExpressionParameter(1.0));
        f.setColor(ExpressionHelper.toExpression(Color.black));

        this.setFill(f);
    }    

    /**
     * Tries to retrieve the UOM of the font if any. If non can be found, return the UOM
     * of the parent node.
     * @return
     */
    public Uom getFontUom() {
        if (uom != null) {
            return uom;
        } else if(getParent() instanceof IUom){
            return ((IUom)getParent()).getUom();
        } else {
                return Uom.PX;
        }
    }

    @Override
    public Uom getUom() {
        // Note: this.uom only affect font size
        return ((IUom)getParent()).getUom();
    }

    @Override
    public void setUom(Uom u) {
        this.uom = u;
    }

    @Override
    public Uom getOwnUom() {
        return this.uom;
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    @Override
    public void setFill(IFill fill) {
        this.fill = fill;
        if (fill != null) {
            fill.setParent(this);
        }
    }

    /**
     * Return the halo associated to this <code>StyledText</code>.
     * @return 
     * A <code>Halo</code> instance, or null if it has not been set.
     */
    public Halo getHalo() {
        return halo;
    }

    /**
     * Set the halo associated to this <code>StyledText</code>
     * @param halo 
     */
    public void setHalo(Halo halo) {
        this.halo = halo;
        if (halo != null) {
            halo.setParent(this);
        }
    }

    /**
     * Get the text contained in this <code>StyledText</code>
     * @return the text contained in this <code>StyledText</code> as a <code>StringParameter</code> instance.
     */
    public ExpressionParameter getText() {
        return text;
    }

    /**
     * Set the text contained in this <code>StyledText</code>
     * @param text 
     */
    public void setText(ExpressionParameter text) {
        if (text != null) {
            this.text = text;
            this.text.setParent(this);
        }
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (stroke != null) {
            stroke.setParent(this);
        }
    }

    /**
     * Get the font family used to represent this <code>StyledText</code>
     * @return 
     * The fontFamily as a <code>StringParameter</code>
     */
    public ExpressionParameter getFontFamily() {
        return fontFamily;
    }

    /**
     * Set the font family used to represent this <code>StyledText</code>
     * @param fontFamily 
     */
    public void setFontFamily(ExpressionParameter fontFamily) {
        if (fontFamily != null) {
            this.fontFamily = fontFamily;
            this.fontFamily.setParent(this);
        }
    }

    /**
     * Get the font size used to represent this <code>StyledText</code>
     * @return 
     * The font size as a <code>RealParameter</code>
     */
    public ExpressionParameter getFontSize() {
        return fontSize;
    }

    /**
     * Set the font size used to represent this <code>StyledText</code>
     * @param fontSize The new font's size
     */
    public void setFontSize(ExpressionParameter fontSize) {
        this.fontSize = fontSize;
        if (this.fontSize != null) {
            this.fontSize.setParent(this);
        }
    }

    /**
     * Get the font style used to represent this <code>StyledText</code>
     * @return 
     * The font style as a <code>StringParameter</code>
     */
    public ExpressionParameter getFontStyle() {
        return fontStyle;
    }

    /**
     * Set the font style used to represent this <code>StyledText</code>
     * @param fontStyle The new font's style
     */
    public void setFontStyle(ExpressionParameter fontStyle) {
        if (fontStyle != null) {
            this.fontStyle = fontStyle;
            this.fontStyle.setParent(this);
        }
    }

    /**
     * Get the font weight used to represent this <code>StyledText</code>
     * @return 
     * The font weight as a <code>StringParameter</code>
     */
    public ExpressionParameter getFontWeight() {
        return fontWeight;
    }

    /**
     * Set the font weight used to represent this <code>StyledText</code>
     * @param fontWeight The new font's weight
     */
    public void setFontWeight(ExpressionParameter fontWeight) {
        if (fontWeight != null) {
            this.fontWeight = fontWeight;
            this.fontWeight.setParent(this);
        }
    }
    

    @Override
    public java.util.List<IStyleNode> getChildren() {
        java.util.List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (text != null) {
            ls.add(text);
        }
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
        if (stroke != null) {
            ls.add(stroke);
        }
        if (fill != null) {
            ls.add(fill);
        }
        if (halo != null) {
            ls.add(halo);
        }
        return ls;
    }
}
