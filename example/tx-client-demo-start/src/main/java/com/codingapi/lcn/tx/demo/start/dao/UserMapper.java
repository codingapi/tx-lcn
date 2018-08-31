package com.codingapi.lcn.tx.demo.start.dao;

import com.codingapi.lcn.tx.demo.start.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     * @param user
     * @return
     */
    @Insert("insert into t_user(name) values(#{name})")
    int insertUser(User user);
}
