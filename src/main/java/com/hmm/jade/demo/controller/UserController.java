package com.hmm.jade.demo.controller;


import com.hmm.jade.demo.entity.User;
import com.hmm.jade.demo.entity.UserAddress;
import com.hmm.jade.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "getUserbyId")
    public User getUserbyId(@RequestBody String id) {

        User user = new User();
        user.setName("jade");
        user.setAddress("suhzou");
        user.setAge(35);

        return user;
    }

    @RequestMapping(value = "getUserbyId2")
    public Map getUserbyId2(@RequestBody String id) {

        System.out.println("id2 = " + id );
        Map userMap = new HashMap();
        userMap.put("name", "jade");
        userMap.put("age", "123");
        userMap.put("address", "suzhou xinqu by jade aaaa good ");
        return userMap;
    }

    @RequestMapping(value = "byId")
    public  User getById(@RequestParam Map  obj){
       String id = (String) obj.get("id");
        System.out.println("id = " + id);
      return  userService.selectUser(100);
    }

    @RequestMapping(value = "getAllUsers")
    public List getAllUsers(){
        return  userService.getAllUsers();
    }

    @RequestMapping(value = "addUser")
    public User insert(@RequestBody User user){
        UserAddress userAdress = user.getUserAddress();
        return user;

    }
}
