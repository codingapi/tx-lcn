package com.codingapi.example.tc.db.mapper;

import com.codingapi.example.tc.db.domain.Demo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface DemoMapper {

  @Insert("insert into lcn_demo(name,module) values(#{name},#{module})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int save(Demo demo);

}
