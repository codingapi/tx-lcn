package com.codingapi.txlcn.manager.support.restapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
@Controller
public class RedirectController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/admin/index.html";
    }
}
