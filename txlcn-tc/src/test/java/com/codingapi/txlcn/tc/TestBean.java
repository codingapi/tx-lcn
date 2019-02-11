package com.codingapi.txlcn.tc;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class TestBean implements Serializable {

    private Date time;

    private Timestamp timestamp;

    public TestBean() {
        this.time = new Date();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
