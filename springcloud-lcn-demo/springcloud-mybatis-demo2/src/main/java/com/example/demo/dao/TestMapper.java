package com.example.demo.dao;

import com.example.demo.entity.Test;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by lorne on 2017/6/28.
 */
@Mapper
public interface TestMapper {


    @Select("SELECT * FROM tx_lcn_test1")
    List<Test> findAll();

    @Insert("INSERT INTO tx_lcn_test1(exec_desc) VALUES (#{exec_desc})")
    int save(@Param("exec_desc") String execDesc);

}
