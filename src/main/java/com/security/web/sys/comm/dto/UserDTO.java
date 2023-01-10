package com.security.web.sys.comm.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO implements Serializable{
        
    /**
     * 사용자 DTO 
     */
    private static final long serialVersionUID = -9209196838019082561L;

    private String user_id;              // 사용자ID
    private String user_seq;             // 인덱스 (his 컬럼)
    private String user_nm;              // 사용자명
    private String pwd;                  // 암호
    private String cst_nm;               // 회사명
    private String cst_id;               // 회사코드
    private String user_tp_cd;           // 사용자구분 01 고객 02 운영자 03 관리자
    private String user_ref_id;          // 사용자연결코드
    private String pwd_chg_dt;           // 암호변경일자
    private String email;                // 이메일
    private String tel_no;               // 전화번호
    private String mphone_no;            // 핸드폰
    private String fax_no;               // 팩스
    private String zip_cd;               // 우편번호
    private String bas_addr;             // 기본주소
    private String dtl_addr;             // 상세주소
    private String cst_tp_cd;            // 고객구분01원청02협력사
    private String user_posit_nm;        // 사용자직위명
    private String user_dept_nm;         // 사용자부서명
    private String status;               // 상태
    private String rgstr_id;             // 등록자ID
    private String mdfr_id;              // 수정자ID
    private String pwd_init_yn;          // 비밀번호 초기화 여부
    private String sys_id;
    private String notice1_yn;           // 신규의뢰시 알림
    private String notice2_yn;           // 멘션 알림
    private String notice3_yn;           // 참여컨설팅 알림
    private String notice4_yn;           // 견젹서 및 입금확인 알림
    private String dept_div;             // 지역 본부 구분 02 본부, 01 부서
    private String cst_status;           // 거래 중단 여부
    private String admin_yn;             // 관리자 여부
    // private String rgstrDt;             // 등록일
    // private String mdfrDt;              // 수정일

}