package org.orbisgis.map.renderer.featureStyle.utils;

import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.parameter.ExpressionParameter;

import java.sql.SQLException;

public class ValueHelper {


    /**
     *
     * @param sp
     * @param expressionParameter
     * @return
     * @throws SQLException
     */
    public static String getString(JdbcSpatialTable sp, ExpressionParameter expressionParameter) throws SQLException {
        if(sp!=null && expressionParameter!=null){
            return sp.getString(expressionParameter.getIdentifier());
        }
        return null;
    }

    /**
     *
     * @param sp
     * @param expressionParameter
     * @return
     * @throws SQLException
     */
    public static double getDouble(JdbcSpatialTable sp, ExpressionParameter expressionParameter) throws SQLException {
        if(sp!=null && expressionParameter!=null){
            return sp.getDouble(expressionParameter.getIdentifier());
        }
        return 0;
    }
    
     /**
     *
     * @param sp
     * @param expressionParameter
     * @return
     * @throws SQLException
     */
    public static float getFloat(JdbcSpatialTable sp, ExpressionParameter expressionParameter) throws SQLException {
        if(sp!=null && expressionParameter!=null){
            return sp.getFloat(expressionParameter.getIdentifier());
        }
        return 0;
    }
}
