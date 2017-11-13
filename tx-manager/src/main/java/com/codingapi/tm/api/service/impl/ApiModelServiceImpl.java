package com.codingapi.tm.api.service.impl;

import com.codingapi.tm.api.service.ApiModelService;
import com.codingapi.tm.manager.ModelInfoManager;
import com.codingapi.tm.model.ModelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by lorne on 2017/11/13
 */
@Service
public class ApiModelServiceImpl implements ApiModelService {


    @Override
    public List<ModelInfo> onlines() {
        return ModelInfoManager.getInstance().getOnlines();
    }


}
