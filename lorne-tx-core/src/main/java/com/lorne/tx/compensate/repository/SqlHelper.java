package com.lorne.tx.compensate.repository;

/**
 * create by lorne on 2017/8/4
 */
public class SqlHelper {


    public static String getCreateTableSql(String dbType, String tableName) {
        String sql = "";
        switch (dbType) {
            case "mysql": {
                sql = "CREATE TABLE `" + tableName + "` (\n" +
                    "  `id` varchar(10) NOT NULL,\n" +
                    "  `retried_count` int(3) NOT NULL,\n" +
                    "  `create_time` datetime NOT NULL,\n" +
                    "  `last_time` datetime NOT NULL,\n" +
                    "  `state` int(2) NOT NULL,\n" +
                    "  `group_id` varchar(10) NOT NULL,\n" +
                    "  `l_unique` varchar(32) NOT NULL,\n" +
                    "  `task_id` varchar(10) NOT NULL,\n" +
                    "  `invocation` longblob NOT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ")";
                break;
            }
            case "oracle": {
                sql = "CREATE TABLE " + tableName + " (\n" +
                    "  id varchar(10) NOT NULL,\n" +
                    "  retried_count number(3,0) NOT NULL,\n" +
                    "  create_time date  NOT NULL,\n" +
                    "  last_time date  NOT NULL,\n" +
                    "  state number(2,0) NOT NULL,\n" +
                    "  l_unique varchar2(32) NOT NULL,\n" +
                    "  group_id varchar2(10) NOT NULL,\n" +
                    "  task_id varchar2(10) NOT NULL,\n" +
                    "  invocation BLOB NOT NULL,\n" +
                    "  PRIMARY KEY (id)\n" +
                    ")";
                break;
            }
            case "sqlserver": {
                sql = "CREATE TABLE " + tableName + " (\n" +
                    "  id varchar(10) NOT NULL,\n" +
                    "  retried_count int NOT NULL,\n" +
                    "  create_time datetime NOT NULL,\n" +
                    "  last_time datetime NOT NULL,\n" +
                    "  state int NOT NULL,\n" +
                    "  l_unique nchar(32) NOT NULL,\n" +
                    "  group_id nchar(10) NOT NULL,\n" +
                    "  task_id nchar(10) NOT NULL,\n" +
                    "  invocation varbinary NOT NULL,\n" +
                    "  PRIMARY KEY (id)\n" +
                    ")";
                break;
            }
            default: {
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql oracle sqlserver.");
            }
        }
        return sql;
    }


    public static String getFindAllByUniqueSql(String dbType, String tableName) {
        return "select * from " + tableName + " where state = ? and l_unique = ? ";
    }

    public static String getCompensateByTaskIdSql(String dbType, String tableName) {
        return "select * from " + tableName + " where state = 0 and task_id = ? ";
    }

    public static String getCompensateByGroupIdSql(String dbType, String tableName) {
        return "select * from " + tableName + " where state = 0 and group_id = ? ";
    }

    public static String loadCompensateList(String dbType, String tableName,int time) {
        switch (dbType){
            case "mysql":{
                return "select * from " + tableName + " where state = 0 and (now() - last_time) > (100 * "+time+")";
            }
            case "sqlserver":{
                return "select * from " + tableName + " where state = 0 and (getdate() - last_time) > (60 * "+time+")";
            }
            case "oracle":{
                return "select * from " + tableName + " where state = 0 and (sysdate - last_time) > (60 * "+time+")";
            }
            default: {
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql oracle sqlserver.");
            }
        }
    }

    public static String getUpdateSql(String dbType,String tableName) {
        switch (dbType){
            case "mysql":{
                return "update " + tableName + " set last_time = now(),state = ?,retried_count = ? where id = ? ";
            }
            case "sqlserver":{
                return "update " + tableName + " set last_time = getdate(),state = ?,retried_count = ? where id = ? ";
            }
            case "oracle":{
                return "update " + tableName + " set last_time = sysdate ,state = ?,retried_count = ? where id = ? ";
            }
            default: {
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql oracle sqlserver.");
            }
        }
    }

    public static String getDeleteSql(String dbType,String tableName) {
        return  "delete from " + tableName + " where state = 0 and id = ?  ";
    }

    public static String getInsertSql(String dbType,String tableName) {
        switch (dbType){
            case "mysql":{
                return  "insert into " + tableName + "(id,l_unique,retried_count,create_time,last_time,group_id,task_id,invocation,state)" +
                    " values(?,?,?,now(),now(),?,?,?,?)";
            }
            case "sqlserver":{
                return  "insert into " + tableName + "(id,l_unique,retried_count,create_time,last_time,group_id,task_id,invocation,state)" +
                    " values(?,?,?,getdate(),getdate(),?,?,?,?)";
            }
            case "oracle":{
                return  "insert into " + tableName + "(id,l_unique,retried_count,create_time,last_time,group_id,task_id,invocation,state)" +
                    " values(?,?,?,sysdate,sysdate,?,?,?,?)";
            }
            default: {
                throw new RuntimeException("dbType类型不支持,目前仅支持mysql oracle sqlserver.");
            }
        }

    }

    public static String countCompensateByTaskId(String dbType, String tableName) {
        return "select count(*) from " + tableName + " where  task_id = ? ";
    }
}
