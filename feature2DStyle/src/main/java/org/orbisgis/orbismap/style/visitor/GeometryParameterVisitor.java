/**
 * Feature2DStyle is part of the OrbisGIS platform
 * <p>
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 * <p>
 * The GIS group of the DECIDE team is located at :
 * <p>
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 * <p>
 * Feature2DStyle is distributed under LGPL 3 license.
 * <p>
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 * <p>
 * <p>
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.parameter.geometry.GeometryParameter;

/**
 * Search for the names of the features that are used in the visited tree of
 * {@link org.orbisgis.orbismap.style.StyleNode} instances.
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GeometryParameterVisitor {

    private HashMap<String, String> res = new HashMap<String, String>();
    private final List<IFeatureSymbolizer> feature2DSymbolizers;
    private int count = 0;

    public GeometryParameterVisitor(List<IFeatureSymbolizer> feature2DSymbolizers) {
        this.feature2DSymbolizers = feature2DSymbolizers;
    }

    /**
     * Method to visit the style elements
     */
    public void visit() throws Exception {
        if (!feature2DSymbolizers.isEmpty()) {
            res = new HashMap<>();
            for (IFeatureSymbolizer feature2DSymbolizer : feature2DSymbolizers) {
                GeometryParameter gp = feature2DSymbolizer.getGeometryParameter();
                String formatedExp;
                if (gp != null) {
                    Expression expParsed = CCJSqlParserUtil.parseExpression(gp.getExpression(), false);
                    formatedExp = expParsed.toString();
                } else {
                    throw new RuntimeException("The geometry column reference cannot be null");
                }
                String identifier = "geom_" + count++;
                gp.setExpression(formatedExp);
                if (!res.containsKey(formatedExp)) {
                    res.put(formatedExp, identifier);
                    gp.setIdentifier(identifier);
                } else {
                    gp.setIdentifier(res.get(formatedExp));
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public HashMap<String, String> getResult() {
        return res;
    }

    public String getResultAsString() {
        return res.entrySet().stream().
                map(entrySet -> entrySet.getKey() + " as " + entrySet.getValue()).
                collect(Collectors.joining(","));
    }


    public Set<String> getGeometryColumns() {
        return res.keySet();
    }

}
