package com.hmm.jade.demo.service;

import com.hmm.jade.demo.entity.User;
import com.hmm.jade.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User selectUser(int id) {
        return userMapper.selectUser(id);
    }
    public List getAllUsers(){
        return  userMapper.getAllUsers();
    }


}
