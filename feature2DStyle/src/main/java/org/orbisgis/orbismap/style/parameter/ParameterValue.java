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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.orbisgis.orbismap.style.IParameterValue;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.StyleNode;

/**
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public abstract class ParameterValue extends StyleNode implements IParameterValue {

    
    private Object value;

    IParameterDomain parameterDomain;
    private boolean isLiteral =true;
    
    public ParameterValue(){
        this.parameterDomain = null;        
        this.value = null;
    }
        
    public ParameterValue(Object value, ParameterDomain parameterDomain){
        this.parameterDomain = parameterDomain;        
        this.value = value;
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        return new ArrayList<>();        
    }
    
    public Object getValue() { 
        return value;
    }
    
    public void setValue(Object value) {   
        this.value = value;
    }
    
    public IParameterDomain getParameterDomain() {
        return parameterDomain;
    }

    public void setParameterDomain(IParameterDomain parameterDomain) {
        this.parameterDomain = parameterDomain;
    }
    
      /**
     * Check if the value is valid
     *
     * @param value
     */
    public void checkValue(Object value) {
        if (this.parameterDomain != null && value != null) {
            String domainExpression = this.parameterDomain.getExpression();
            if (domainExpression != null && !domainExpression.isEmpty()) {
                DomainExpressionParser domainParser = new DomainExpressionParser(value);
                try {
                    if(!domainParser.evaluate(domainExpression)){
                        throw new RuntimeException("The value is not in the range of the domain. Expected : "+ domainExpression
                                + " from style node : " + getParent().getClass().getSimpleName());
                    }
                } catch (ParameterException ex) {
                    throw new RuntimeException("Unsupported value "+  value +" for the style node : "+ getParent().getClass().getSimpleName());
                }
            }
        }
    }

    /**
     * Check if type of the value matches the domain type
     * @param value 
     */
    public void checkValueType(Object value) {
        if (this.parameterDomain != null && value != null) {
            if (!this.getParameterDomain().getDataType().isAssignableFrom(value.getClass())) {
                throw new RuntimeException("The value doesn't not match the data type.\n Expected :  "+ this.getParameterDomain().getDataType());
            }
        }
    }
    
    public void format(Class dataType) {
        format(dataType, "");
    }
    
    public abstract void format(Class dataType, String expressionDomain);
    
    /**
     * 
     * @param dataType
     * @param expressionDomain 
     */
    public void setDomain(Class dataType, String expressionDomain) {
        if (expressionDomain != null && !expressionDomain.isEmpty()) {
            this.parameterDomain = new ParameterDomain(dataType, expressionDomain);
        } else {
            this.parameterDomain = new ParameterDomain(dataType);
        }
    }
    
    /**
     * Return true if the ParameterValue is a <code>Literal</code>
     * @return 
     */
    public boolean isLiteral() {
        return isLiteral;
    }
    
    /**
     * 
     * @param isLiteral 
     */
    public void setLiteral(boolean isLiteral) {
        this.isLiteral=isLiteral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof ParameterValue)) {
            return false;
        }
        ParameterValue other = (ParameterValue) o;
        if(getValue()==null){
            return false;
        }
        if (!getValue().equals(other.getValue())) {
            return false;
        }
        if (!getParameterDomain().equals(other.getParameterDomain())) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.value);
        hash = 83 * hash + Objects.hashCode(this.parameterDomain);
        hash = 83 * hash + (this.isLiteral ? 1 : 0);
        return hash;
    }
    
    
}
