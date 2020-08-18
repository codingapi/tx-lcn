package com.codingapi.example.tc.db.mapper;

import com.codingapi.example.tc.db.domain.Demo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface LcnSqlParseTest3Mapper {

  @Delete(" DELETE  FROM   lcn_sql_parse_test3  where id =1 ")
  int delete();

  @Delete("  DELETE  t3,t2 FROM lcn_sql_parse_test3 t3,lcn_sql_parse_test2 t2 where t3.job = t2.dept_name AND t2.dept_name = 'test' ")
  int delete2();
}
