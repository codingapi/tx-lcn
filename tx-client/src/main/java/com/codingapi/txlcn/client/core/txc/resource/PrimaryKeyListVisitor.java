/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.client.core.txc.resource;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.aspectj.util.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 2018/12/14
 *
 * @author ujued
 */
public class PrimaryKeyListVisitor implements ItemsListVisitor {

    private List<Column> columns;
    private List<String> primaryKeys;
    private Table table;

    private List<Map<String, Object>> primaryKeyValuesList;

    public PrimaryKeyListVisitor(Table table, List<Column> columns, List<String> primaryKeys) {
        this.columns = columns;
        this.primaryKeys = primaryKeys;
        this.table = table;
    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(ExpressionList expressionList) {
        primaryKeyValuesList = new ArrayList<>();
        primaryKeyValuesList.add(newKeyValues(expressionList.getExpressions()));
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        primaryKeyValuesList = new ArrayList<>(multiExprList.getExprList().size());
        multiExprList.getExprList().forEach(expressionList ->
                primaryKeyValuesList.add(newKeyValues(expressionList.getExpressions()))
        );
    }

    public List<Map<String, Object>> getPrimaryKeyValuesList() {
        return primaryKeyValuesList;
    }

    private Map<String, Object> newKeyValues(List<Expression> expressions) {
        Map<String, Object> keyValues = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setTable(table);
            if (primaryKeys.contains(columns.get(i).getFullyQualifiedName())) {
                Object expression = expressions.get(i).getASTNode().jjtGetValue();
                keyValues.put(columns.get(i).getFullyQualifiedName(),
                        Reflection.invokeN(expression.getClass(), "getValue", expression, new Object[0]));
            }
        }
        return keyValues;
    }
}
