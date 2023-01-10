package com.security.web.sys.comm.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.web.sys.comm.service.CommService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author choihaegun
 * @apiNote common
 */

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private static final Logger logger = LogManager.getLogger(ApiController.class);
    
    @Autowired
    private CommService commService;
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // /**
    //  * @apiNote 로그인 화면 체크
    //  * @return UserDTO
    //  */
    // @PostMapping("/login")
    // public UserDTO callLogin(@RequestBody UserReqDTO req,HttpServletRequest request,HttpSession session) throws Exception {
    //     // 회원조회
    //     UserDTO userDto = commService.getUsersMail(req);
    //     if(userDto != null) {
    //         // if(!req.getSys_code().equals(userDto.getSys_id())){
    //         //     throw new CommDuplicateException("서비스 구분 확인 하세요.",ErrorCode.PASSWORDS_DO_NOT_MATCH);
    //         // }
    //         if(!StringUtils.equals("Y", userDto.getStatus()) || !StringUtils.equals("Y", userDto.getCst_status())){
    //             throw new CommDuplicateException("거래중단 사용자입니다.",ErrorCode.INTER_SERVER_ERROR);
    //         }

    //         if(!passwordEncoder.matches(req.getPwd(), userDto.getPwd())){
    //             throw new CommDuplicateException("비밀번호 맞지 않습니다.",ErrorCode.PASSWORDS_DO_NOT_MATCH);
    //         }
    //         // 공통에 경우 셋팅 한 값으로 지정 sysCode
    //         if(userDto.getSys_id().equals("CO")){
    //             session.setAttribute("sysCode" , req.getSys_code());    // 시스템 코드
    //         } else {
    //             session.setAttribute("sysCode" , userDto.getSys_id());   
    //         }

    //         session.setAttribute("userId"    , userDto.getUser_id());         // 사용자 ID
    //         session.setAttribute("userNm"    , userDto.getUser_nm());         // 사용자 명
    //         session.setAttribute("adminYn"   , userDto.getAdmin_yn());         // 관리자 여부 
    //         session.setAttribute("cstcd"     , userDto.getCst_id());          // 고객 ID
    //         session.setAttribute("dept"      , userDto.getUser_dept_nm());    // 사용자 부서명
    //         session.setAttribute("deptCode"  , userDto.getCst_tp_cd());       // 부서 코드
    //         session.setAttribute("cstNm"     , userDto.getCst_nm());          // 고객명
    //         session.setAttribute("email"     , userDto.getEmail());           // 사용자 이메일
    //         // 운용자이며 관리자 권한 있는사람은 03로
    //         if (userDto.getUser_tp_cd().equals("02") && userDto.getAdmin_yn().equals("Y")) {
    //             session.setAttribute("userTpCd" , "03");    
    //         } else {
    //             session.setAttribute("userTpCd"  , userDto.getUser_tp_cd());      // 사용자 구분코드
    //         }
    //         session.setAttribute("notice1Yn" , userDto.getNotice1_yn());      // 신규의뢰시 알림
    //         session.setAttribute("notice2Yn" , userDto.getNotice2_yn());      // 멘션 알림
    //         session.setAttribute("notice3Yn" , userDto.getNotice3_yn());      // 참여컨설팅 알림
    //         session.setAttribute("notice4Yn" , userDto.getNotice4_yn());      // 견젹서 및 입금확인 알림
    //         session.setAttribute("deptDiv"   , userDto.getDept_div());        // 지역 본부 구분 02 본부, 01 부서 

    //         // 신규 로그인 확인
    //         try {
    //             // 이력조회
    //             List<UserDTO> userDtoLog = commService.getUserHis(req);
    //             // 신규 아닌 경우
    //             if (userDtoLog.size() > 0) { // pwd_init_yn = 'N'
    //                 commService.saveLoginHis(userDto);
    //             }

    //         } catch (IndexOutOfBoundsException  e) {
    //             throw new CommDuplicateException("",ErrorCode.LOGINLOG_DUPLICATION);
    //         }
    //     }else{
    //         session.invalidate();
    //         throw new CommDuplicateException("이메일 맞지 않습니다.",ErrorCode.LOGINLOG_DUPLICATION);
    //     }
    //     return userDto;
    // }



}
