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
package org.orbisgis.style.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.parameter.geometry.GeometryParameter;

/**
 * Search for the names of the features that are used in the visited tree of
 * {@link SymbolizerNode} instances.
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class GeometryParameterVisitor {

    private HashMap<String, String> res = new HashMap<String, String>();
    private final List<IFeatureSymbolizer> feature2DSymbolizers;
    final Set<String> geometryColumnsValid = new HashSet<String>();
    private int count=0;
    private final List<String> geometryColumns;
    
    public GeometryParameterVisitor(List<IFeatureSymbolizer> feature2DSymbolizers, List<String> geometryColumns) {
        this.feature2DSymbolizers = feature2DSymbolizers;
        this.geometryColumns=geometryColumns;
    }

    /**
     *
     * @param sn
     */
    public void visit() throws Exception {
        if (!feature2DSymbolizers.isEmpty()&& !geometryColumns.isEmpty()) {
            res = new HashMap<>();
            for (IFeatureSymbolizer feature2DSymbolizer : feature2DSymbolizers) {
                GeometryParameter gp = feature2DSymbolizer.getGeometryParameter();
                String formatedExp;
                if(gp!=null){
                Expression expParsed = CCJSqlParserUtil.parseExpression(gp.getExpression(), false);
                expParsed.accept(new ExpressionVisitorAdapter() {
                    @Override
                    public void visit(Column column) {
                        super.visit(column);
                        if (geometryColumns.contains(column.getColumnName().toUpperCase())) {
                            geometryColumnsValid.add(column.getColumnName().toUpperCase());
                        }
                    }
                });
                    formatedExp = expParsed.toString();
                }
                else{
                    gp =  new GeometryParameter(geometryColumns.get(0));
                    formatedExp = gp.getExpression();
                    geometryColumnsValid.add(geometryColumns.get(0));
                    feature2DSymbolizer.setGeometryParameter(gp);
                }
                String identifier = "geom_" + count++;
                gp.setExpression(formatedExp);
                if (!res.containsKey(formatedExp)) {
                    res.put(formatedExp,identifier);
                    gp.setIdentifier(identifier);
                }
                else{
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
                map(entrySet -> entrySet.getKey()+ " as " + entrySet.getValue()).
                collect(Collectors.joining(","));
    }

    
    public Set<String> getGeometryColumns() {
        return geometryColumnsValid;
    }

}
