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
package org.orbisgis.coremap.renderer.se;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.SeExceptions.InvalidStyle;
import org.orbisgis.coremap.renderer.se.common.Description;
import org.orbisgis.coremap.renderer.se.graphic.ExternalGraphic;
import org.orbisgis.coremap.renderer.se.graphic.Graphic;
import org.orbisgis.coremap.renderer.se.graphic.GraphicCollection;
import org.orbisgis.coremap.renderer.se.graphic.MarkGraphic;
import org.orbisgis.coremap.renderer.se.visitors.FeaturesVisitor;

/**
 * Rules are used to group rendering instructions by feature-property conditions and map scales.
 * Rule definitions are placed immediately inside of FeatureType- or coverage-style definitions.</p>
 * <p>According to SE 2.0, a <code>Rule</code> contains only one <code>Symbolizer</code> - but that  
 * <code>Symbolizer</code> can be a composite one. This implementation directly embedded 
 * a <code>CompositeSymbolizer</code> that will contain one -or more- actual <code>Symbolizer</code>
 * representation.
 * @author Maxence Laurent
 * @author Erwan Bocher
 */
public final class Rule extends AbstractSymbolizerNode {

    /**
     * The name set to every rule, if not set externally.
     */
    public static final String DEFAULT_NAME = "Default Rule";
    private String name = "";
    private Description description = new Description();
    private String where;
    private Double minScaleDenom = null;
    private Double maxScaleDenom = null;
    private CompositeSymbolizer symbolizer;

    /**
     * Create a default, empty Rule, with a default inner (and empty) CompositeSymbolizer.
     */
    public Rule() {
        symbolizer = new CompositeSymbolizer();
        symbolizer.setParent(this);
    }

    @Override
    public String toString() {
        if (name != null && !name.equalsIgnoreCase("")) {
            return name;
        } else {
            return "Untitled rule";
        }
    }

    /**
     * Build a Rule using a ILayer. This contains a CompositeSymbolizer, populated 
     * according to the first found geometry in the DataSet  embedded in the ILayer.
     * That means we'll obtain a <code>LineSymbolizer</code> if this first geometry is of 
     * dimension 1, a <code>PolygonSymbolizer</code> if it is of dimension 2,
     * and a <code>PointSymbolizer</code> otherwise.
     * @param layer The layer that will receive a new default symbolizer.
     */
    public Rule(ILayer layer) {
        this();
        this.name = "Default Rule";
        createSymbolizer(layer);

    }

    /**
     * Short circuit to create a default point symbolizer 
     * @param layer The layer that will receive a new default symbolizer.
     */
    public void createSymbolizer(ILayer layer) {
        if (layer != null) {
           symbolizer.addSymbolizer(new PointSymbolizer());                    
        }
    }    

    /**
     * Replace the current inner <code>CompositeSymbolizer</code> with <code>cs</code>
     * @param cs The new inner {@link CompositeSymbolizer}.
     */
    public void setCompositeSymbolizer(CompositeSymbolizer cs) {
        this.symbolizer = cs;
        cs.setParent(this);
    }

    /**
     * Get the inner <code>CompositeSymbolizer</code>
     * @return The inner {@link CompositeSymbolizer}.
     */
    public CompositeSymbolizer getCompositeSymbolizer() {
        return symbolizer;
    }
    
    /**
     * Get the <code>where</code> clause associated to this rule.
     * @return 
     *      The associated <code>where</code> clause.
     */
    public String getWhere() {
        return where;
    }

    /**
     * Replace the current inner <code>where</code> clause.
     * @param where The new where clause.
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * Build a OrderBy clause to be used to optimize SQL queries.
     * @return The "order by" clause
     */
    private String getOrderBy() {
        for (Symbolizer s : getCompositeSymbolizer().getSymbolizerList()) {
            if (s instanceof PointSymbolizer) {
                PointSymbolizer ps = (PointSymbolizer) s;
                GraphicCollection gc = ps.getGraphicCollection();
                int i;
                StringBuilder f = new StringBuilder();
                for (i = 0; i < gc.getNumGraphics(); i++) {
                    Graphic g = gc.getGraphic(i);
                    if (g instanceof MarkGraphic) {
                        MarkGraphic mark = (MarkGraphic) g;
                        if (mark.getViewBox() != null) {
                            FeaturesVisitor fv = new FeaturesVisitor();
                            mark.getViewBox().acceptVisitor(fv);
                            f.append(" ");
                            f.append(fv.getResult());
                        }
                    } else if (g instanceof ExternalGraphic) {
                        ExternalGraphic extG = (ExternalGraphic) g;
                        if (extG.getViewBox() != null) {
                            FeaturesVisitor fv = new FeaturesVisitor();
                            extG.getViewBox().acceptVisitor(fv);
                            f.append(" ");
                            f.append(fv.getResult());
                        }
                    }
                    // TODO add others cases !
                }

                // If view box depends on features => order by 
                String result = f.toString().trim();
                if (!result.isEmpty()) {
                    String[] split = result.split(" ");
                    return " ORDER BY " + split[0] + " DESC";
                } else {
                    return "";
                }
            }
        }
        return "";
    }   

    /**
     * Gets the maximum scale for which this <code>Rule</code> (and the features it is applied on)
     * must be displayed.
     * @return 
     * The maximum scale for rendering this Rule. The returned value is actually 
     * <b>the denominator of the scale</b>. Consequently, a value of 10 000 000
     * means a <b>scale of 1:10-million</b>
     */
    public Double getMaxScaleDenom() {
        return maxScaleDenom;
    }

    /**
     * Set the maximum scale for which this <code>Rule</code> (and the things
     * rendered from it) is displayed.
     * @param maxScaleDenom The  expected value is actually 
     * <b>the denominator of the scale</b>. Consequently, a value of 10 000 000
     * means a <b>scale of 1:10-million</b>
     */
    public void setMaxScaleDenom(Double maxScaleDenom) {
        if (maxScaleDenom != null && maxScaleDenom > 0) {
            this.maxScaleDenom = maxScaleDenom;
        } else {
            this.maxScaleDenom = null;
        }
    }

    /**
     * Gets the minimum scale for which this <code>Rule</code> (and the features it is applied on)
     * must be displayed.
     * @return 
     * The minimum scale for rendering this Rule. The returned value is actually 
     * <b>the denominator of the scale</b>. Consequently, a value of 10 000 000
     * means a <b>scale of 1:10-million</b>
     */
    public Double getMinScaleDenom() {
        return minScaleDenom;
    }

    /**
     * Set the minimum scale for which this <code>Rule</code> (and the things
     * rendered from it) is displayed.
     * @param minScaleDenom The  expected value is actually 
     * <b>the denominator of the scale</b>. Consequently, a value of 10 000 000
     * means a <b>scale of 1:10-million</b>
     */
    public void setMinScaleDenom(Double minScaleDenom) {
        if (minScaleDenom != null && minScaleDenom > 0) {
            this.minScaleDenom = minScaleDenom;
        } else {
            this.minScaleDenom = null;
        }
    }

    /**
     * This method checks that this rule is valid for the given 
     * {@link MapTransform}. That means that, if {@code scale} is the scale
     * denominator associated to the {@code MapTransform mt}, this method 
     * returns true if {@code minScaleDenom <= scale <= maxScalDenom}
     * @param mt The tested MapTransform.
     * @return 
     * <ul><li>{@code true} if {@code minScaleDenom <= scale <= maxScalDenom}
     * . Note that {@code null} values for the inner scale denominator values
     * are considered to be equivalent to 0 and positive infinity.</li>
     * <li>false otherwise.</li></ul>
     */
    public boolean isDomainAllowed(MapTransform mt) {
        double scale = mt.getScaleDenominator();

        return (this.minScaleDenom == null && this.maxScaleDenom == null)
                || (this.minScaleDenom == null && this.maxScaleDenom != null && this.maxScaleDenom > scale)
                || (this.minScaleDenom != null && scale > this.minScaleDenom && this.maxScaleDenom == null)
                || (this.minScaleDenom != null && this.maxScaleDenom != null && scale > this.minScaleDenom && this.maxScaleDenom > scale);
    }

    /**
     * Get the description of this rule.
     * @return The Description of this Rule.
     * @see Description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Set the description associated to this rule.
     * @param description The new description for this.
     * @see Description
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * Get the name of this rule.
     * @return The name of the rule.
     */
    public String getName() {
        return name;
    }

    /**
     * Set a new name to this rule.
     * @param name The new name of this rule.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<SymbolizerNode> getChildren() {
            List<SymbolizerNode> ls = new ArrayList<SymbolizerNode>();
            ls.add(getCompositeSymbolizer());
            return ls;
    }


}
