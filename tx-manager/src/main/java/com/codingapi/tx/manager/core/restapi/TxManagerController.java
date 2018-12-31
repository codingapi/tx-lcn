package com.codingapi.tx.manager.core.restapi;

import com.codingapi.tx.commons.rpc.params.NotifyConnectParams;
import com.codingapi.tx.manager.core.service.ManagerService;
import com.codingapi.tx.spi.rpc.exception.RpcException;
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
