package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.utils.ListUtil;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-21 23:35:45
 */
public class CheckTableContainsPkFilter implements SqlAnalysqFilter {
    @Override
    public boolean doFilter(FilterFacade filterFacade) {
        TableInfo tableInfo = filterFacade.getTableList().getTable(filterFacade.getTable().getName());
        if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
            return  true;
        }
        return false;
    }
}
