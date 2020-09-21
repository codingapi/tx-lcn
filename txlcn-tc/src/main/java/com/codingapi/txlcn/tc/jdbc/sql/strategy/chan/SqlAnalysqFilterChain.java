package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:36:58
 */
public class SqlAnalysqFilterChain implements SqlAnalysqFilter {

    List<SqlAnalysqFilter> filterList = new ArrayList<>();

    public SqlAnalysqFilterChain add(SqlAnalysqFilter filter){
        filterList.add(filter);
        return this;
    }

    @Override
    public boolean doFilter(FilterFacaer filterFacaer) {
        for (SqlAnalysqFilter filter : filterList) {
            if(!filter.doFilter(filterFacaer)){
                return false;
            }
        }
        return true;
    }
}
