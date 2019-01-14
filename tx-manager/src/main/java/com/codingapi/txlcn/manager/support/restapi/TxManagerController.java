package com.codingapi.txlcn.manager.support.restapi;

import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.manager.support.service.ManagerService;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@RestController
@RequestMapping("/manager")
public class TxManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/refresh")
    public boolean refresh(@RequestBody NotifyConnectParams notifyConnectParams) throws RpcException {
        return managerService.refresh(notifyConnectParams);
    }


}
