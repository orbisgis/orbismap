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
package org.orbisgis.orbismap.style.parameter;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebocher
 */
public class ExpressionParserTest {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JSQLParserException, ParseException {
        //Field lowercase
        Expression expr = CCJSqlParserUtil.parseExpression("the_geom", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        //Field uppercase
        expr = CCJSqlParserUtil.parseExpression("THE_GEOM", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        //Value
        expr = CCJSqlParserUtil.parseExpression("2", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());

        //DashArray
        expr = CCJSqlParserUtil.parseExpression("'2 2'", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        expr = CCJSqlParserUtil.parseExpression("'POINT(0 0)'::GEOMETRY", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        
        expr = CCJSqlParserUtil.parseExpression("CASE 3 + 4 WHEN 1 then 0 END", false);        
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        expr = CCJSqlParserUtil.parseExpression("(ST_area(the_geom) + 1)/2", false);  
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
        final List<String> columnList = new ArrayList<String>();
        expr.accept(new ExpressionVisitorAdapter() {
            @Override
            public void visit(Column column) {
                super.visit(column);
                columnList.add(column.getColumnName());
            }
        });
        System.out.println(expr.toString());
        System.out.println(String.join(",", columnList));
        expr = CCJSqlParserUtil.parseExpression(" test ", false);
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());

        expr = CCJSqlParserUtil.parseExpression("CASE WHEN ST_AREA(the_geom) > 1000 then 12 else 0 end", false);
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());

        expr = CCJSqlParserUtil.parseCondExpression("id=12", false);
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());

        expr = CCJSqlParserUtil.parseCondExpression("id>12 and id<20", false);
        System.out.println(expr.getClass()+ " evaluate to : "+ expr.toString());
    }
    
}
