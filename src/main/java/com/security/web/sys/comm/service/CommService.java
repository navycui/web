package com.security.web.sys.comm.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.web.sys.comm.dto.User;
import com.security.web.sys.comm.dto.UserDTO;
import com.security.web.sys.comm.dto.UserReqDTO;
import com.security.web.sys.comm.mapper.CommMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommService {
    private static final Logger looger = LogManager.getLogger(CommService.class);

    private final CommMapper commMapper;

    // /**
    //  * 사용자 조회
    //  * @return
    //  */
    // public User getUser(UserReqDTO uto) throws Exception{
    //     looger.info("사용자 조회:::",uto);
    //     return commMapper.getUserAccount(uto);
    // }
    // /**
    //  * 사용자 조회 이메일
    //  * @return
    //  */
    // public UserDTO getUsersMail(UserReqDTO uto) throws Exception{
    //     looger.info("사용자 조회:::",uto);
    //     return commMapper.getUsersMail(uto);
    // }
 

}
