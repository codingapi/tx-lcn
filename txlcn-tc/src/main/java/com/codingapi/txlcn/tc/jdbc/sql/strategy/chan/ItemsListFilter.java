package com.codingapi.txlcn.tc.jdbc.sql.strategy.chan;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-22 00:04:35
 */
public class ItemsListFilter implements SqlAnalysqFilter {


    @Override
    public boolean doFilter(FilterFacaer filterFacaer) {
        if(null == filterFacaer.getItemsList()){
            return false;
        }
        return true;
    }
}
