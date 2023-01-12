package com.demo.app.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demo.app.member.User;


@Mapper
public interface CommMapper {

    /*
     * 사용자 조회 security
     * 
    */
    User findByEmail(String param) throws Exception;

}
