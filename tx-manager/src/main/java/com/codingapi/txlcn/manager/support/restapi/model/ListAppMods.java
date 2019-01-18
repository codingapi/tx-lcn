package com.codingapi.txlcn.manager.support.restapi.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Date: 19-1-17 上午11:56
 *
 * @author ujued
 */
@Data
public class ListAppMods {
    private long total;
    private List<AppMod> appMods;

    @Data
    public static class AppMod {
        private String modId;
        private String registerTime;
    }
}
