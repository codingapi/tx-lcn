package com.codingapi.txlcn.client.initializer;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.commons.util.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/18
 *
 * @author codingapi
 */
@Component
public class AppEvnInitializer implements TxLcnInitializer {


    @Autowired
    private Environment environment;

    @Override
    public void init() throws Exception {
        String name =  environment.getProperty("spring.application.name");
        String application = StringUtils.hasText(name) ? name : "application";
        String port =  environment.getProperty("server.port");
        Transactions.setApplicationId(String.format("%s:%s", application, port));
    }


}
