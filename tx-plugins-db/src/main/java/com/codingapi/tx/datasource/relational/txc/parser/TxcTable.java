package com.codingapi.tx.datasource.relational.txc.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caisirius
 */
public class TxcTable {

    public String schemaName;
    public String tableName;
    public String alias;

    private List<TxcLine> line = new ArrayList();


    @Override
    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < this.line.size(); i++) {

            for (TxcField field : this.line.get(i).getFields()) {

                switch (field.getType()) {
                    case -15:
                    case -9:
                    case -6:
                    case -5:
                    case 1:
                    case 2:
                    case 4:
                    case 12:
                    case 2003:
                        localStringBuilder.append(field.getValue()).append(',');
                    default:
                }
            }
        }
        return localStringBuilder.toString();
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<TxcLine> getLine() {
        return line;
    }

    public void setLine(List<TxcLine> line) {
        this.line = line;
    }
}
