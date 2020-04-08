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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.parameter;

import java.util.List;
import java.util.Stack;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.ArrayExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NextValExpression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.FullTextSearch;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * Expression parser to check if the expression value is valid Used to check
 * paramater value domain
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class DomainExpressionParser extends ExpressionDeParser {

    Object value;
    private Boolean result = null;
    Stack<Object> stack = new Stack<Object>();
    Stack<Boolean> stackCondition = new Stack<Boolean>();

    public DomainExpressionParser(String value) {
        this.value = value;
    }

    public DomainExpressionParser(Object value) {
        this.value = value;
    }

    public DomainExpressionParser(Number value) {
        this.value = value;
    }

    public boolean evaluate(String exp) throws ParameterException {
        try {
            if (exp != null && !exp.isEmpty()) {
                net.sf.jsqlparser.expression.Expression parseExpression = CCJSqlParserUtil.parseCondExpression(exp, false);
                StringBuilder b = new StringBuilder();
                setBuffer(b);
                parseExpression.accept(this);
                stack.clear();
                stackCondition.clear();
                return result;
            }
        } catch (JSQLParserException ex) {
            throw new ParameterException(ex);
        }
        return true;
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        super.visit(equalsTo);
        Object right = stack.pop();
        Object left = stack.pop();
        if (left instanceof String) {
            if (right instanceof String) {
                result = left.equals(right);
            } else {
                throw new RuntimeException("Only text value is supported");
            }
        } else if (left instanceof Number) {
            if (right instanceof Number) {
                result = (((Number) left).doubleValue() - ((Number) right).doubleValue() == 0);
            } else {
                throw new RuntimeException("Only number value is supported");
            }
        } else {
            throw new RuntimeException("Unsupported value type");
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        super.visit(greaterThan);
        Object right = stack.pop();
        if (!(right instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        Object left = stack.pop();
        if (!(left instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        result = ((Number) left).doubleValue() > ((Number) right).doubleValue();
        stackCondition.push(result);
    }

    @Override
    public void visit(MinorThan minorThan) {
        super.visit(minorThan);
        Object right = stack.pop();
        if (!(right instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        Object left = stack.pop();
        if (!(left instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        result = ((Number) left).doubleValue() < ((Number) right).doubleValue();
        stackCondition.push(result);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        super.visit(greaterThanEquals);
        Object right = stack.pop();
        if (!(right instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        Object left = stack.pop();
        if (!(left instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        int compare = compareTo((Number) left, (Number) right);
        if (compare == 0) {
            result = true;
        } else if(compare < 0){
            result = false;
        }else{
            result =true;
        }
        stackCondition.push(result);
    }

    int compareTo(Number left, Number right) {
        return new Double(left.doubleValue()).compareTo(right.doubleValue());
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        super.visit(doubleValue);
        stack.push(doubleValue.getValue());
    }

    @Override
    public void visit(StringValue stringValue) {
        super.visit(stringValue);
        stack.push(stringValue.getValue());
    }

    @Override
    public void visit(LongValue longValue) {
        super.visit(longValue);
        stack.push(longValue.getValue());
    }

    @Override
    public void visit(AndExpression andExpression) {
        super.visit(andExpression);
        boolean right = stackCondition.pop();
        boolean left = stackCondition.pop();
        result = (left && right);
    }

    @Override
    public void visit(OrExpression orExpression) {
        super.visit(orExpression);
        boolean right = stackCondition.pop();
        boolean left = stackCondition.pop();
        result = (left || right);
    }

    @Override
    public void visit(InExpression inExpression) {
        //super.visit(inExpression);
        ItemsList itemList = inExpression.getRightItemsList();
        Expression left = inExpression.getLeftExpression();
        if (left instanceof Column) {
            visit((Column) left);
        }
        Object leftValue = stack.pop();
        if (leftValue instanceof String) {
            findStringInExpressionList((String) leftValue, itemList);
        }
        if (leftValue instanceof Number) {
            findNumberInExpressionList((Number) leftValue, itemList);
        }
    }

    private void findNumberInExpressionList(Number valueTyped, ItemsList itemList) {
        if (itemList != null && itemList instanceof ExpressionList) {
            ExpressionList expressionList = (ExpressionList) itemList;
            List<Expression> expressions = expressionList.getExpressions();
            for (Expression expression : expressions) {
                if (expression instanceof LongValue) {
                    LongValue longValue = (LongValue) expression;
                    if ((valueTyped.doubleValue() - longValue.getValue() == 0)) {
                        result = true;
                        stackCondition.push(result);
                        return;
                    }
                } else if (expression instanceof DoubleValue) {
                    DoubleValue doublevalue = (DoubleValue) expression;
                    if ((valueTyped.doubleValue() - doublevalue.getValue() == 0)) {
                        result = true;
                        stackCondition.push(result);
                        return;
                    }
                }
            }
        } else {
            throw new RuntimeException("Unsupported in syntax");
        }
        result = false;
        stackCondition.push(result);
    }

    private void findStringInExpressionList(String valueTyped, ItemsList itemList) {
        if (itemList != null && itemList instanceof ExpressionList) {
            ExpressionList expressionList = (ExpressionList) itemList;
            List<Expression> expressions = expressionList.getExpressions();
            for (Expression expression : expressions) {
                String valueAsString = expression.toString();
                if (expression instanceof StringValue) {
                    valueAsString = ((StringValue) expression).getNotExcapedValue();
                }
                if (valueTyped.equals(valueAsString)) {
                    result = true;
                    stackCondition.push(result);
                    return;
                }
            }
        } else {
            throw new RuntimeException("Unsupported in syntax");
        }
        result = false;
        stackCondition.push(result);
    }

    @Override
    public void visit(Column column) {
        super.visit(column);
        if (column.getColumnName().equalsIgnoreCase("value")) {
            stack.push(value);
        }
    }

    @Override
    public void visit(Addition addition) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Between between) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(ArrayExpression array) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(SimilarToExpression expr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(CollateExpression col) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(NextValExpression nextVal) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(ValueListExpression valueList) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(KeepExpression aexpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(UserVariable var) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(JsonOperator jsonExpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(IntervalExpression iexpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Modulo modulo) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(CastExpression cast) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Matches matches) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Concat concat) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(WhenClause whenClause) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(TimeValue timeValue) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(DateValue dateValue) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Function function) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(SubSelect subSelect) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Subtraction subtraction) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(NullValue nullValue) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Multiplication multiplication) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        super.visit(minorThanEquals);
        Object right = stack.pop();
        if (!(right instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        Object left = stack.pop();
        if (!(left instanceof Number)) {
            throw new RuntimeException("Only number value is supported");
        }
        int compare = compareTo((Number) left, (Number) right);
        if (compare == 0) {
            result = true;
        } else if(compare < 0){
            result = true;
        }else{
            result =false;
        }
        stackCondition.push(result);
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(FullTextSearch fullTextSearch) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(NotExpression notExpr) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(HexValue hexValue) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(IntegerDivision division) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void visit(Division division) {
        throw new RuntimeException("Not supported");
    }
}
