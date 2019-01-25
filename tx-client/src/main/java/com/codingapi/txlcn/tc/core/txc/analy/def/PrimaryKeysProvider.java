package com.codingapi.txlcn.tc.core.txc.analy.def;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-25 下午3:43
 *
 * @author ujued
 */
public interface PrimaryKeysProvider {

    /**
     * 提供TXC模式下所有操作的表的主键，数据库指定主键可以不用在这里体现
     * {@code key} 是数据表名，{@code value} 是数据表对应的主键列表
     *
     * @return primary key list mapped by table name
     */
    Map<String, List<String>> provide();
}
