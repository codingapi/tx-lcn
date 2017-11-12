package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.ApiAdminService;
import com.codingapi.tm.model.TxState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lorne on 2017/7/1.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ApiAdminService apiAdminService;


    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public TxState setting() {
        return apiAdminService.getState();
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public String json() {
        return apiAdminService.loadNotifyJson();
    }

}
