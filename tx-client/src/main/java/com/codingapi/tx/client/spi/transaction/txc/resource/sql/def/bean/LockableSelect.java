package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean;

import com.codingapi.tx.client.spi.transaction.txc.resource.sql.util.SqlUtils;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.StringUtils;

/**
 * Description: 业务Select语句信息。附加分析此查询是否要锁定
 * Date: 2018/12/17
 *
 * @author ujued
 */
public class LockableSelect {

    private Select select;

    public LockableSelect(Select select) {
        this.select = select;
    }

    public Select statement() {
        return select;
    }

    public boolean isxLock() {
        return StringUtils.endsWithIgnoreCase(StringUtils.trimAllWhitespace(this.select.toString()),
                StringUtils.trimAllWhitespace(SqlUtils.FOR_UPDATE));
    }

    public boolean issLock() {
        return StringUtils.endsWithIgnoreCase(StringUtils.trimAllWhitespace(this.select.toString()),
                StringUtils.trimAllWhitespace(SqlUtils.LOCK_IN_SHARE_MODE));
    }

    public boolean shouldLock() {
        return isxLock() || issLock();
    }
}
