package com.codingapi.txlcn.logger.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 事务事件
 * Company: CodingApi
 * Date: 2018/12/19
 *
 * @author codingapi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxLog {

    /**
     * TAG
     */
    private String tag;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 事务单元Id
     */
    private String unitId;

    /**
     * 模块名称
     */
    private String appName;


    /**
     * 创建时间
     */
    private String createTime;
}
