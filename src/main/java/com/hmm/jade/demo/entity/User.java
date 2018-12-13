package com.hmm.jade.demo.entity;


import lombok.Data;

@Data
public class User {

    private String name ;
    private Integer age;
    private String address;
    private String id;

    private  UserAddress userAddress;

}
