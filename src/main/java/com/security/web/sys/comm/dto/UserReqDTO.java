package com.security.web.sys.comm.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserReqDTO implements Serializable{
        
    /**
     * 사용자 DTO 
     */
    private static final long serialVersionUID = -9209196838019082561L;

    private String user_id;              // 사용자ID
    private String user_nm;              // 사용자명
    private String email;                // email
    private String mphone_no;            // 사용자 폰 번호
    private String sys_code;             // sys code
    private String pwd;                  // 암호
    private String cst_status;           // 거래 중단 여부
    private String admin_yn;
    private String user_tp_cd;           // 사용자구분 01 고객 02 운영자 03 관리자
}