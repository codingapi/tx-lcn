package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:46:54
 */
public class WhereFilter  implements SqlAnalysqFilter {
    @Override
    public boolean doFilter(FilterFacade filterFacade) {
        if( (filterFacade.getDeleteStatement() == null ? filterFacade.getUpdateStatement().getWhere() : filterFacade.getDeleteStatement().getWhere()) == null){
            return false;
        }
        return true;
    }
}
