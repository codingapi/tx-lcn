package com.codingapi.tx.datasource.relational.txc.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caisirius
 */
public class TxcRuntimeContext {
    private static final Logger logger = LoggerFactory.getLogger(TxcRuntimeContext.class);

    /**
     * 事务组Id 对应于txc的 xid
     */
    public String groupId;
    /**
     * 分支事务Id lcn里叫 kid
     */
    public String branchId;
    /**
     * 提交信息
     */
    private List<CommitInfo> info = new ArrayList();
    
    public int status;
    /**
     * 分支所在IP
     */
    public String server;

    public List<CommitInfo> getInfo() {
        return info;
    }

    public void setInfo(List<CommitInfo> info) {
        this.info = info;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}