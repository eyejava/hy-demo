package com.hmm.jade.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloSpringBoot {

    @RequestMapping(value = "/hello")
    public String say() {
        return "hello jade test";
    }

    @RequestMapping(value = "/say/{id}")
    public String sayId(@PathVariable("id") Integer id) {
        return "id = " + id;
    }

    @RequestMapping(value = "/param")
    public String sayParam(@RequestParam(value = "id", required = false, defaultValue = "0") Integer myId) {
        return "myId = " + myId;
    }
}
