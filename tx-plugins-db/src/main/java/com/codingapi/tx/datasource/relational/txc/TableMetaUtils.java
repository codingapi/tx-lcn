package com.codingapi.tx.datasource.relational.txc;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.lorne.core.framework.utils.config.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [类描述]
 *
 * @author caican
 * @date 17/12/23
 */
public class TableMetaUtils {

    private static final Logger logger = LoggerFactory.getLogger(TableMetaUtils.class);

    private static final ConcurrentHashMap<String, TableMetaInfo> tableMetaInfoCache =
            new ConcurrentHashMap<>();
    //TODO 定期让缓存失效

    private static String dbType;


    public static TableMetaInfo getTableMetaInfo(Connection connection, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new RuntimeException("TableMeta cannot fetched without tableName");
        }
        if (connection == null) {
            throw new RuntimeException("TableMeta cannot fetched without Connection");
        }
        String databaseName = null;
        try {
            databaseName = getDbNameFromUrl(connection.getMetaData().getURL());
        } catch (Exception e) { }
        if (StringUtils.isEmpty(databaseName)) {
            databaseName = "NULL";
        }
        TableMetaInfo ret;
        String fullTableName = databaseName + "." + tableName;

        // 先从本地缓存拿
        ret = tableMetaInfoCache.get(fullTableName);
        if (ret == null) {
            try {
                logger.info("meta is null, fetch schema of " + tableName);
                ret = fetchSchema(connection, tableName);
                ret.setTableName(tableName);
                ret.setSchemaName(databaseName);

                tableMetaInfoCache.putIfAbsent(fullTableName, ret);
            } catch (SQLException e) {
                logger.error("tableMeta error", e);
            }
        }
        if (ret == null) {
            throw new RuntimeException(String.format("[groupId:%s]get tablemeta failed",
                    TxTransactionLocal.current().getGroupId()));
        }

        return ret;
    }

    public static String getDbNameFromUrl (String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        int start = 0;
        boolean isThreeFound = true; // 要找到3个 '/'
        for (int i=0; i<3;i++) {
            int index = url.indexOf('/', start+1);
            if (index == -1) {
                isThreeFound = false;
                break;
            }
            start = index;
        }
        if (! isThreeFound) {
            return null;
        }

        int index = url.indexOf('?', start);
        String ret;
        if (index == -1) {
            ret = url.substring(start +1);
        } else {
            ret = url.substring(start + 1, index);
        }
        return ret;
    }

    private static TableMetaInfo fetchSchema(Connection connection, String tableName)
            throws SQLException {
        // TODO 支持 mysql以外的

        return fetchSchemaMysql(connection, tableName);
    }

    private static TableMetaInfo fetchSchemaMysql(Connection connection, String tableName)
            throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("desc " + tableName);
        TableMetaInfo tableMetaInfo = new TableMetaInfo();
        tableMetaInfo.setColumnInfoMap(new HashMap<String, ColumnInfo>());
        while (resultSet.next()) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setTableName(tableName);
            columnInfo.setColumnName(resultSet.getString("Field"));
            boolean isAllowNull = true;
            if ("NO".equalsIgnoreCase(resultSet.getString("Null"))) {
                isAllowNull = false;
            }
            columnInfo.setAllowNull(isAllowNull);
            String key = resultSet.getString("Key");
            int iKey = -1;
            if ("PRI".equalsIgnoreCase(key)) {
                iKey = 0;
            } else if ("UNI".equalsIgnoreCase(key)) {
                iKey = 1;
            } else if ("MUL".equalsIgnoreCase(key)) {
                iKey = 2;
            }
            columnInfo.setKeyType(iKey);// 这个最重要
            columnInfo.setExtra(resultSet.getString("Extra"));

            //TODO
            //  resultSet.getString("Type");
//            columnInfo.setType();

            tableMetaInfo.getColumnInfoMap().put(columnInfo.getColumnName(), columnInfo);
        }
        return tableMetaInfo;
    }


}
