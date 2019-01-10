package com.codingapi.tx.client.springcloud.spi.sleuth;

import brave.propagation.ExtraFieldPropagation;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Component
public class TracerHelper {


    /**
     * 事务组标示Id
     */
    public static final String GROUP_ID_FIELD_NAME = "groupId";

    /**
     * TxManager模块标示
     */
    public static final String TX_MANAGER_FIELD_NAME = "txManager";

    /**
     * 模块标示
     */
    public static final String TX_APP_LIST = "appList";


    public void createAppList(String appList) {
        ExtraFieldPropagation.set(TX_APP_LIST, appList);
    }

    public void createGroupId(String groupId) {
        ExtraFieldPropagation.set(GROUP_ID_FIELD_NAME, groupId);
    }

    public void createManagerKey(String managerKey) {
        ExtraFieldPropagation.set(TX_MANAGER_FIELD_NAME, managerKey);
    }

    public void createGroupId(String groupId, String managerKey) {
        ExtraFieldPropagation.set(GROUP_ID_FIELD_NAME, groupId);
        ExtraFieldPropagation.set(TX_MANAGER_FIELD_NAME, managerKey);
    }

    public String getGroupId() {
        return ExtraFieldPropagation.get(GROUP_ID_FIELD_NAME);
    }

    public String getTxManagerKey() {
        return ExtraFieldPropagation.get(TX_MANAGER_FIELD_NAME);
    }

    public String getAppList(){
        return ExtraFieldPropagation.get(TX_APP_LIST);
    }



}
