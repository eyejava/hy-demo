package com.hmm.jade.demo.mapper;


import com.hmm.jade.demo.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("select * from user where id = #{id}")
   public  User selectUser(int id);

    @Select("select * from user")
    public List<User> getAllUsers();
}
