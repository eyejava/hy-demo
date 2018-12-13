package com.hmm.jade.demo.service;

import com.hmm.jade.demo.mapper.MessgaeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessgaeService {

    @Autowired
    MessgaeMapper  messgaeMapper;

    public List getAllMessages(){
        return  messgaeMapper.selectAll();
    }
}
