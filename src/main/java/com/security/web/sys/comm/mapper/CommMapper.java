package com.security.web.sys.comm.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.security.web.sys.comm.dto.User;

@Mapper
public interface CommMapper {

    // 로그인
    User selectUsers(String email);
    // 회원가입
    void saveUser(User userVo);


}
