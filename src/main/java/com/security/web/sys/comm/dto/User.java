package com.security.web.sys.comm.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import groovy.transform.builder.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
 
/**
 * @author zhangmy
 * @date 2021/11/25 14:15
 * @description 1、스프링 보안 전용 사용자 클래스 정의
  * UserDetails를 구현하고 해당 메서드를 다시 작성해야 함
  * 이 클래스 속성의 획득은 UserDetailsService 인터페이스를 구현하는 클래스의 loadUserByUsername() 메서드를 통해 획득됩니다.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
 
    /**
     * 사용자id
     */
    private Long userId;
 
    /**
     * 사용자名
     */
    private String username;
 
    /**
     * 비밀번호
     */
    private String password;
 
    /**
     * token
     */
    private String token;
 
    /**
     * 함된 역할
     */
    // private List<RoleDto> roles;
    /*
     * 함된 역할 단건
     * 
    */
    private String user_auth;

    /**
     * 소유한 메뉴 권한
     */
    // private List<MenuDto> menus;
    private String id;              // 사용자ID
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

 
    /**
     * 사용자의 역할을 권한으로 사용
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        // List<GrantedAuthority> auth = new ArrayList<>();
        // if (roles.size() > 0) {
        //     for (RoleDto role : roles) {
        //         auth.add(new SimpleGrantedAuthority(role.getRoleId().toString()));
        //     }
        // }
        // return auth;
        return Collections.singletonList(new SimpleGrantedAuthority(this.user_auth));
    }
 
    @Override
    public String getPassword() {
        return this.pwd;
    }
 
    @Override
    public String getUsername() {
        return this.email;
    }
 
    /**
     * 사용자가 만료되지 않았습니까?
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    /**
     * 사용자 잠금 해제
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    /**
     * 사용자 인증서가 만료되지 않았는지 여부
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    /**
     * 사용자 활성화 여부
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Object builder() {
        return null;
    }
}

