package com.hmm.jade.demo.mapper;

import com.hmm.jade.demo.entity.Messgae;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessgaeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Messgae record);

    Messgae selectByPrimaryKey(Integer id);

    List<Messgae> selectAll();

    int updateByPrimaryKey(Messgae record);
}