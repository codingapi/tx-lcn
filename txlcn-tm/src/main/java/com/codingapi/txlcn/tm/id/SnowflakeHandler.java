package com.codingapi.txlcn.tm.id;

import static com.codingapi.txlcn.tm.node.TmNodeInitiator.getSnowflakeVo;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
public class SnowflakeHandler {

    private static final Snowflake SNOWFLAKE =
            SnowflakeHandler.createSnowflake(getSnowflakeVo().getWorkerId(), getSnowflakeVo().getDataCenterId());

    /**
     * 创建Twitter的Snowflake 算法生成器。
     * <p>
     * 特别注意：此方法调用后会创建独立的{@link Snowflake}对象，每个独立的对象ID不互斥，会导致ID重复，请自行保证单例！
     * </p>
     * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
     *
     * <p>
     * snowflake的结构如下(每部分用-分开):<br>
     *
     * <pre>
     * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
     * </pre>
     * <p>
     * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
     * 然后是5位dataCenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
     * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
     *
     * <p>
     * 参考：http://www.cnblogs.com/relucent/p/4955340.html
     *
     * @param workerId 终端ID
     * @return {@link Snowflake}
     */
    public static Snowflake createSnowflake(long workerId, long dataCenterId) {
        return new Snowflake(workerId, dataCenterId);
    }

    /**
     * 创建GroupId策略
     *
     * @return String
     */
    public static String generateGroupId() {
        return Long.toBinaryString(SNOWFLAKE.nextId());
    }


    /**
     * 创建日志id
     *
     * @return long
     */
    public static long generateLogId() {
        return SNOWFLAKE.nextId();
    }
}
