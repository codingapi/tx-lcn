package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.ApiAdminService;
import com.codingapi.tm.compensate.model.TxModel;
import com.codingapi.tm.model.TxState;
import com.lorne.core.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "/modelList", method = RequestMethod.GET)
    public List<String> modelList() {
        return apiAdminService.modelList();
    }

    @RequestMapping(value = "/modelTimes", method = RequestMethod.GET)
    public List<String> modelTimes(@RequestParam("model") String model) {
        return apiAdminService.modelTimes(model);
    }


    @RequestMapping(value = "/modelInfos", method = RequestMethod.GET)
    public List<TxModel> modelInfos(@RequestParam("path") String path) {
        return apiAdminService.modelInfos(path);
    }

    @RequestMapping(value = "/compensate", method = RequestMethod.GET)
    public boolean compensate(@RequestParam("path") String path) throws ServiceException {
        return apiAdminService.compensate(path);
    }
}
