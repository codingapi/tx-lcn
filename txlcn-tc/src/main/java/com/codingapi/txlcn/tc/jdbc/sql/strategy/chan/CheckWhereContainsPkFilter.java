package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

import com.codingapi.txlcn.tc.jdbc.database.TableInfo;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:45:27
 */
public class CheckWhereContainsPkFilter implements SqlAnalysqFilter {
    @Override
    public boolean doFilter(FilterFacade filterFacade) {
        TableInfo tableInfo = filterFacade.getTableList().getTable(filterFacade.getTable().getName());
        return  (filterFacade.getDeleteStatement() == null ? filterFacade.getUpdateStatement().getWhere() : filterFacade.getDeleteStatement().getWhere()).toString().toUpperCase().contains(tableInfo.getPrimaryKeys().get(0).toUpperCase());
    }
}
