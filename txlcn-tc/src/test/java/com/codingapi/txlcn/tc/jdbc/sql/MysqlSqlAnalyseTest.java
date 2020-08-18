package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.MysqlUpdateAnalyseStrategry;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.SqlAnalyseInfo;
import com.codingapi.txlcn.tc.utils.ListUtil;
import com.google.common.collect.Maps;
import com.sun.deploy.util.StringUtils;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Union;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.schema.CreateSchema;
import net.sf.jsqlparser.statement.create.sequence.CreateSequence;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.dbutils.BaseResultSetHandler;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
@SpringBootTest(classes = DataSourceConfiguration.class)
@Slf4j
public class MysqlSqlAnalyseTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void analyse() throws SQLException, JSQLParserException {
        String sql = "insert into lcn_demo(name,module) values('123','tc-c')";
        Connection connection =  dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));

        QueryRunner queryRunner = new QueryRunner();
        int res =  queryRunner.execute(connection,sql);

        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        String tableName = insert.getTable().getName();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        TableInfo tableInfo =  tableList.getTable(tableName);
        log.info("tableInfo:{}",tableInfo);

        assertTrue(res>0,"数据插入异常.");
        DbUtils.close(connection);
    }

    @Test
    public void updateAnalyse() throws SQLException, JSQLParserException, UnsupportedEncodingException {
       String sql = "update lcn_sql_parse_test1 t1,lcn_sql_parse_test2 t2  set t1.home_address = 'shanghai',t1.age = 30 where t1.job = t2.dept_name AND t2.dept_name = 'test'";
      //  sql="update lcn_sql_parse_test set dept_name = 3 where dept_name =2";
        sql = "update lcn_sql_parse_test3 t1,lcn_sql_parse_test2 t2  set t1.home_address = 'shanghai',t1.age = 30 where t1.job = t2.dept_name AND t2.dept_name = 'test'";
        sql = "update lcn_sql_parse_test3 t1,lcn_sql_parse_test2 t2  set t1.home_address = 'shanghai',t2.dept_name = 'a' where t1.job = t2.dept_name AND t2.dept_name = 'test'";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Update statement = (Update) CCJSqlParserUtil.parse(sql);
        if(! checkTableContainsPk(statement.getTable(),tableList)){
            //TODO
            return;
        }
        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();

//        if(statement.getTables().size() == 1){
//            sqlAnalyseInfo = updateSqlAnalyseSingleTable(tableList, statement,sql);
//        }else {
//            sqlAnalyseInfo = updateSqlAnalyseMultiTable(tableList, statement,sql);
//        }
        String querySql = sqlAnalyseInfo.getQuerySql();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, querySql, new MapListHandler());
        if(ListUtil.isEmpty(query)){
            //TODO
            return;
        }
        sql = getNewSql(sql, sqlAnalyseInfo, query);

        System.out.println(sql);
    }

    private String getNewSql(String sql, SqlAnalyseInfo sqlAnalyseInfo, List<Map<String, Object>> query) {
        String select = sqlAnalyseInfo.getSelect();
        String primaryKey = sqlAnalyseInfo.getPrimaryKey();
        JDBCType primaryKeyType = sqlAnalyseInfo.getPrimaryKeyType();
        sql = sql.concat(" and ").concat(select).concat(" in ( ");
        int size = query.size();
        for (int i = 0; i < query.size(); i++) {
            if(JDBCType.VARCHAR.getName().equals(primaryKeyType.getName())
                    ||JDBCType.NVARCHAR.getName().equals(primaryKeyType.getName())
                    ||JDBCType.CHAR.getName().equals(primaryKeyType.getName())

            ){
                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
            }
          else   if(JDBCType.INTEGER.getName().equals(primaryKeyType.getName())
                    ||JDBCType.SMALLINT.getName().equals(primaryKeyType.getName())
                    ||JDBCType.TINYINT.getName().equals(primaryKeyType.getName())
            ){
                sql  = sql.concat(Integer.toString((Integer) query.get(i).get(primaryKey)));
            }
          else   if(JDBCType.BIGINT.getName().equals(primaryKeyType.getName())){
                sql  = sql.concat(Long.toString((Long) query.get(i).get(primaryKey)));
            }else{
                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
            }

            if(i + 1 < size){
                sql = sql.concat(" , ");
            }

        }
        sql = sql.concat(")");
        return sql;
    }

    private SqlAnalyseInfo updateSqlAnalyseMultiTable(TableList tableList, Update statement, String sql) {
        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();

        StringBuilder querySql = new StringBuilder("select ");
        String select = null;
        Set<String> s2 = new TreeSet<>();
//        for (Table table : statement.getTables()) {
//            TableInfo tableInfo = tableList.getTable(table.getName());
//            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
//                //TODO sql中没有别名的情况
//                if(StringUtils.isBlank(select)){
//                    select = table.getAlias().getName().concat(".").concat(tableInfo.getPrimaryKeys().get(0));
//                    String pk = tableInfo.getPrimaryKeys().get(0);
//                    JDBCType type = tableInfo.getColumnInfos().stream().filter(m -> m.getName().equals(pk)).findFirst().get().getType();
//                    sqlAnalyseInfo.setPrimaryKey(pk);
//                    sqlAnalyseInfo.setPrimaryKeyType(type);
//                }
//                String tableName = table.getName().concat(" ").concat(table.getAlias().getName());
//                s2.add(tableName);
//            }
//        }
//        String from = s2.stream().collect(Collectors.joining(","));
//        querySql.append(select).append(" from ").append(from).append(" where ").append(statement.getWhere().toString());
//        log.info("update Sql Analyse =[{}]",querySql.toString());
//
//        sqlAnalyseInfo.setSelect(select);
//        sqlAnalyseInfo.setQuerySql(querySql.toString());

        return sqlAnalyseInfo;
    }


    private SqlAnalyseInfo updateSqlAnalyseSingleTable(TableList tableList, Update statement, String sql) {
        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();
        //单表操作
//        Table table = statement.getTables().get(0);
//        String querySql = "select ";
//        TableInfo tableInfo = tableList.getTable(table.getName());
//        String primaryKey = tableInfo.getPrimaryKeys().get(0);
//        String select = "";
//        if(null != table.getAlias()){
//            select = table.getAlias().getName().concat(".").concat(primaryKey);
//        }
//        querySql = querySql.concat(select).concat(statement.getWhere().toString());
//        log.info("update Sql Analyse =[{}]",querySql);
//        JDBCType type = tableInfo.getColumnInfos().stream().filter(m -> m.getName().equals(primaryKey)).findFirst().get().getType();
//
//        sqlAnalyseInfo.setSelect(select);
//        sqlAnalyseInfo.setQuerySql(querySql);
//        sqlAnalyseInfo.setPrimaryKey(primaryKey);
//        sqlAnalyseInfo.setPrimaryKeyType(type);
        return sqlAnalyseInfo;
    }

    public static  boolean checkTableContainsPk(Table table, TableList tableList ) {
        TableInfo tableInfo = tableList.getTable(table.getName());
        if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
            return  true;
        }
        return false;
    }


    /**
     * 删除语句分析测试样例
     *
     * @throws SQLException
     * @throws JSQLParserException
     */
    @Test
    public void deleteAnalyse() throws SQLException, JSQLParserException {
        String sql = "DELETE  t2,t3 FROM lcn_sql_parse_test2 t2 ,lcn_sql_parse_test3 t3 where t3.job = t2.dept_name AND t2.dept_name = 'test' and t3.name = 'a' ";
          //   sql = "DELETE  FROM   lcn_sql_parse_test3  where id in (1)";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));

        TableList tableList =  DataBaseContext.getInstance().get(catalog);

        if(sql.toUpperCase().startsWith("DELETE")){
            Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
            String name = delete.getTable().getName();
            TableInfo table = tableList.getTable(name);
            List<String> primaryKeys = table.getPrimaryKeys();
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> result = tablesNamesFinder.getTableList(delete);
            Map<String,String> tableMap = new HashMap<>();
            Table table2 = delete.getTable();
            if(table2.getAlias() != null){
                tableMap.put(table2.getAlias().getName(),table2.getName());
            }
            for (Join join : delete.getJoins()) {
                Table table1 = (Table)join.getRightItem();
                if(table1.getAlias()!=null){
                    tableMap.put(table1.getAlias().getName(),table1.getName());
                }
            }


            AllComparisonExpression where = (AllComparisonExpression) delete.getWhere();
            String primaryKey = primaryKeys.get(0);
            String querySql = sql.replace(sql.substring(0,6), "select ".concat(primaryKey));
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> query = queryRunner.query(connection, querySql, new MapListHandler());
            StringBuilder sb = new StringBuilder("delete from").append(" ").append(name).append(" ").append("where").append(" ").append(primaryKey).append(" ").append("in").append(" ").append("(").append(" ");
            query.forEach(map->{
                Object o = map.get(primaryKey);
                sb.append(o).append(" ").append(",");
            });
            String newSql = sb.replace(sb.lastIndexOf(","),sb.length(),")").toString();
            System.out.println(newSql);
        }


//        TableInfo tableInfo =  tableList.getTable(tableName);
//        log.info("tableInfo:{}",tableInfo);
//
//        assertTrue(res>0,"数据插入异常.");
//        DbUtils.close(connection);
    }



    //实现从SQL中提取表名
    public static final List<String> getTables(String sql) {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Statement stmt;
        try {
            //解析SQL语句
            stmt = parserManager.parse(new StringReader(sql));
        } catch (JSQLParserException e) {
            return null;
        }
        final List<String> tableNames = new ArrayList<String>();
        //使用visitor模式访问SQL的各个组成部分
        stmt.accept(new MyStatementVisitor(tableNames));
        return tableNames;
    }

    static class MySelectVisitor implements SelectVisitor {

        List<String> tableNames;

        public MySelectVisitor(List<String> tableNames) {
            this.tableNames = tableNames;
        }

        @Override
        public void visit(PlainSelect ps) {
            FromItemVisitor fromItemVisitor = new FromItemVisitor() {

                @Override
                public void visit(Table table) {
                    tableNames.add(table.getName());
                }

                @Override
                public void visit(SubSelect ss) {
                    ss.getSelectBody().accept(new MySelectVisitor(tableNames));
                }

                @Override
                public void visit(SubJoin sj) {
                    sj.getLeft().accept(this);
                    sj.getJoinList().forEach(join -> join.getRightItem().accept(this));
                }

                @Override
                public void visit(LateralSubSelect lateralSubSelect) {

                }

                @Override
                public void visit(ValuesList valuesList) {

                }

                @Override
                public void visit(TableFunction tableFunction) {

                }

                @Override
                public void visit(ParenthesisFromItem parenthesisFromItem) {

                }

            };
            //访问select中的from部分，这里获取到第一个表名
            ps.getFromItem().accept(fromItemVisitor);
            //访问select中的join部分，这里获取到第二个表名
            List<Join> joins = ps.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    join.getRightItem().accept(fromItemVisitor);
                }
            }
        }

        @Override
        public void visit(SetOperationList setOperationList) {

        }

        @Override
        public void visit(WithItem withItem) {

        }

        @Override
        public void visit(ValuesStatement valuesStatement) {

        }


    }

    static class MyStatementVisitor implements StatementVisitor {
        List<String> tableNames;

        public MyStatementVisitor(List<String> tableNames) {
            this.tableNames = tableNames;
        }

        //访问select语句
        public void visit(Select select) {
            //访问select的各个组成部分
            select.getSelectBody().accept(new MySelectVisitor(tableNames));
        }

        @Override
        public void visit(Upsert upsert) {

        }

        @Override
        public void visit(UseStatement useStatement) {

        }

        @Override
        public void visit(Block block) {

        }

        @Override
        public void visit(ValuesStatement valuesStatement) {

        }

        @Override
        public void visit(DescribeStatement describeStatement) {

        }

        @Override
        public void visit(ExplainStatement explainStatement) {

        }

        @Override
        public void visit(ShowStatement showStatement) {

        }

        @Override
        public void visit(DeclareStatement declareStatement) {

        }

        @Override
        public void visit(Grant grant) {

        }

        @Override
        public void visit(CreateSequence createSequence) {

        }

        @Override
        public void visit(AlterSequence alterSequence) {

        }

        @Override
        public void visit(CreateFunctionalStatement createFunctionalStatement) {

        }

        @Override
        public void visit(Comment comment) {

        }

        @Override
        public void visit(Commit commit) {

        }

        //访问delete语句
        public void visit(Delete delete) {
            tableNames.add(delete.getTable().getName());
        }

        //访问update语句
        public void visit(Update update) {
            tableNames.add(update.getTable().getName());
        }

        //访问insert语句
        public void visit(Insert insert) {
            tableNames.add(insert.getTable().getName());
        }

        //访问replace，忽略
        public void visit(Replace replace) {
        }

        //访问drop，忽略
        public void visit(Drop drop) {
        }

        //访问truncate，忽略
        public void visit(Truncate truncate) {
        }

        @Override
        public void visit(CreateIndex createIndex) {

        }

        @Override
        public void visit(CreateSchema createSchema) {

        }

        //访问create，忽略
        public void visit(CreateTable arg0) {
        }

        @Override
        public void visit(CreateView createView) {

        }

        @Override
        public void visit(AlterView alterView) {

        }

        @Override
        public void visit(Alter alter) {

        }

        @Override
        public void visit(Statements statements) {

        }

        @Override
        public void visit(Execute execute) {

        }

        @Override
        public void visit(SetStatement setStatement) {

        }

        @Override
        public void visit(ShowColumnsStatement showColumnsStatement) {

        }

        @Override
        public void visit(Merge merge) {

        }
    }
    public String parser(Expression expression, Alias alias){
        try {
            List<String> items = new ArrayList<>();

            String columnName = "";
            if (expression instanceof CaseExpression) {
                // case表达式
                columnName = alias.getName();
            } else if (expression instanceof LongValue || expression instanceof StringValue || expression instanceof DateValue || expression instanceof DoubleValue) {
                // 值表达式
                columnName = Objects.nonNull(alias.getName()) ? alias.getName() : expression.getASTNode().jjtGetValue().toString();
            } else if (expression instanceof TimeKeyExpression) {
                // 日期
                columnName = alias.getName();
            } else {
                if (alias != null) {
                    columnName = alias.getName();
                } else {
                    SimpleNode node = expression.getASTNode();
                    Object value = node.jjtGetValue();
                    if (value instanceof Column) {
                        columnName = ((Column) value).getColumnName();
                    } else if (value instanceof Function) {
                        columnName = value.toString();
                    }else {
                        // 增加对select 'aaa' from table; 的支持
                        columnName = String.valueOf(value);
                        columnName = columnName.replace("'", "");
                        columnName = columnName.replace("\"", "");
                        columnName = columnName.replace("`", "");
                    }
                }
            }

            columnName = columnName.replace("'", "");
            columnName = columnName.replace("\"", "");
            columnName = columnName.replace("`", "");
            items.add(columnName);
            return StringUtils.join(items, ",");
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
