package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.ApiTxManagerService;
import com.codingapi.tm.model.TxState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lorne on 2017/7/1.
 */
@Controller
public class IndexController {

    @Autowired
    private ApiTxManagerService apiTxManagerService;



    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        TxState state = apiTxManagerService.getState();
        request.setAttribute("info", state);
        return "index";
    }


    @RequestMapping("/json")
    @ResponseBody
    public String json() {
        return apiTxManagerService.loadNotifyJson();
    }

}
