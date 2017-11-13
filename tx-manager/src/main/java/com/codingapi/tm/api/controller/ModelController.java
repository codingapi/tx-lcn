package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.ApiModelService;
import com.codingapi.tm.model.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lorne on 2017/7/1.
 */
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ApiModelService apiModelService;


    @RequestMapping(value = "/onlines", method = RequestMethod.GET)
    public List<ModelInfo> onlines() {
        return apiModelService.onlines();
    }

}
