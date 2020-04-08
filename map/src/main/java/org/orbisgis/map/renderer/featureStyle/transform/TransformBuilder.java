/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.map.renderer.featureStyle.transform;

import java.awt.geom.AffineTransform;
import java.util.Map;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.orbisgis.map.renderer.featureStyle.ITranformBuilder;
import org.orbisgis.style.Uom;
import org.orbisgis.style.parameter.TransformParameter;

/**
 *
 * @author ebocher
 */
public class TransformBuilder implements ITranformBuilder{

    
    
    @Override
    public AffineTransform getAffineTransform(TransformParameter transformParameter, Map<String, Object> properties) {
        Uom uom = transformParameter.getUom();
        if(transformParameter!=null && !transformParameter.getExpression().isEmpty()){
            return parseTransformExpression(transformParameter.getExpression());
        }
        return null;
    }
    
    
    /**
     * Visit the expression to create the AffineTransform
     * TODO : implement it and cache it
     * @param expression
     * @return 
     */
    private static AffineTransform parseTransformExpression(String expression){
        ExpressionDeParser deparser = new ExpressionDeParser() {
            @Override
            public void visit(Function function) {
                
            }
        };
        return null;
    }
    
}
