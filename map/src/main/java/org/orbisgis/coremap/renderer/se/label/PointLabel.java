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
package org.orbisgis.coremap.renderer.se.label;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.Utils.UomUtils;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealLiteral;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameterContext;
import org.orbisgis.style.IStyleNode;

/**
 * A label located at a single point. In addition to all the {@code Label} characteristics,
 * it has two additional properties : 
 * <ul><li>A rotation angle, in degrees.</li>
 * <li>An exclusion zone, ie a zone around the label where no other text will be displayed.</li>
 * </ul>
 * @author Alexis Guéganno, Maxence Laurent
 */
public final class PointLabel extends Label {

    private RealParameter rotation;
    private ExclusionZone exclusionZone;


    /**
     * Creates a new {@code PointLabel} with default values as detailed in 
     * {@link org.orbisgis.coremap.renderer.se.label.Label#Label() Label} and
     * {@link org.orbisgis.coremap.renderer.se.label.StyledText#StyledText() StyledText}.
     * This {@code PointLabel} will be top and right aligned.
     */
    public PointLabel() {
        super();
        rotation = new RealLiteral(0.0);
        setVerticalAlign(VerticalAlignment.TOP);
        setHorizontalAlign(HorizontalAlignment.CENTER);
    }



    /**
     * Get the exclusion zone defined for this {@code PointLabel}. In this zone, 
     * we won't draw any other text.
     * @return 
     * An {@link ExclusionZone} instance.
     */
    public ExclusionZone getExclusionZone() {
        return exclusionZone;
    }


    /**
     * Set the exclusion zone defined for this {@code PointLabel}.
     * @param exclusionZone The new exclusion zone
     */
    public void setExclusionZone(ExclusionZone exclusionZone) {
        this.exclusionZone = exclusionZone;
        if (exclusionZone != null) {
            exclusionZone.setParent(this);
        }
    }


    /**
     * Get the rotation that must be applied to this {@code PointLabel} before rendering.
     * @return 
     * The rotation, in degrees, as a {@link RealParameter}
     */
    public RealParameter getRotation() {
        return rotation;
    }


    /**
     * Set the rotation that must be applied to this {@code PointLabel} before rendering.
     * @param rotation The new rotation to be used.
     */
    public void setRotation(RealParameter rotation) {
        this.rotation = rotation;
        if (this.rotation != null) {
            this.rotation.setContext(RealParameterContext.REAL_CONTEXT);
            this.rotation.setParent(this);
        }
    }


    @Override
    public void draw(Graphics2D g2, Map<String, Object> map,
            Shape shp, boolean selected, MapTransform mt)
            throws ParameterException, IOException {
        double x;
        double y;

        // TODO RenderPermission !
        double deltaX = 0;
        double deltaY = 0;

        Rectangle2D bounds = getLabel().getBounds(g2, map, mt);
        x = shp.getBounds2D().getCenterX() + getHorizontalDisplacement(bounds);
        y = shp.getBounds2D().getCenterY() + bounds.getHeight() / 2;

        if (this.exclusionZone != null) {
            if (this.exclusionZone instanceof ExclusionRadius) {
                double radius = ((ExclusionRadius) (this.exclusionZone)).getRadius().getValue(map);
                radius = UomUtils.toPixel(radius, getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
                deltaX = radius;
                deltaY = radius;
            } else {
                deltaX = ((ExclusionRectangle) (this.exclusionZone)).getX().getValue(map);
                deltaY = ((ExclusionRectangle) (this.exclusionZone)).getY().getValue(map);

                deltaX = UomUtils.toPixel(deltaX, getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
                deltaY = UomUtils.toPixel(deltaY, getUom(), mt.getDpi(), mt.getScaleDenominator(), null);
            }
        }

        AffineTransform at = AffineTransform.getTranslateInstance(x + deltaX, y + deltaY);

        getLabel().draw(g2, map, selected, mt, at, this.getVerticalAlign());
    }

    /**
     * Gets the horizontal displacement to the given bounds according to the currently configured
     * HorizontalAlignment.
     * @param bounds The bounds of the text to be drawn
     * @return The displacement.
     */
    private double getHorizontalDisplacement(Rectangle2D bounds){
        HorizontalAlignment ha = getHorizontalAlign();
        switch(ha){
            case CENTER: return -bounds.getWidth()/2.0;
            case LEFT: return -bounds.getWidth();
            default: return 0.0;
        }
    }
   
        @Override
        public List<IStyleNode> getChildren() {
                List<IStyleNode> ls = new ArrayList<IStyleNode>();
                if (getLabel() != null) {
                        ls.add(getLabel());
                }
                if (exclusionZone != null) {
                        ls.add(exclusionZone);
                }
                if (rotation != null) {
                        ls.add(rotation);
                }
                return ls;
        }

}
