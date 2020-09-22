package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

import net.sf.jsqlparser.statement.update.Update;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:46:54
 */
public class WhereFilter  implements SqlAnalysqFilter {
    @Override
    public boolean doFilter(FilterFacaer filterFacaer) {
        if( (filterFacaer.getDeleteStatement() == null ? filterFacaer.getUpdateStatement().getWhere() : filterFacaer.getDeleteStatement().getWhere()) == null){
            return false;
        }
        return true;
    }
}
