package com.codingapi.example.tc.db.mapper;

import com.codingapi.example.tc.db.domain.Demo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper {

  @Insert("insert into lcn_demo(name,module) values(#{name},#{module})")
  int save(Demo demo);

}
