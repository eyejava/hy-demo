package com.hmm.jade.demo.controller;

import com.hmm.jade.demo.service.MessgaeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessgaeService  messgaeService;


    @RequestMapping(value = "getAllMsg")
    public List getAllMegs(){
        return  messgaeService.getAllMessages();
    }
}
