package com.security.web.sys.comm.controller;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.web.sys.comm.dto.User;
import com.security.web.sys.comm.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    /**
     * localhost:8080 시 login 으로 redirect
     * @return
     */
    @GetMapping
    public String root() {
        return "redirect:/login";
    }

    /**
     * 로그인 폼
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 회원가입 폼
     * @return
     */
    @GetMapping("/signUp")
    public String signUpForm() {
        return "signUp";
    }

    /**
     * 로그인 실패 폼
     * @return
     */
    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }

    /**
     * 회원가입 진행
     * @param userVo
     * @return
     */
    @PostMapping("/signUp")
    public String signUp(User userVo) {
        // userService.joinUser(userVo);
        return "redirect:/login";
    }

    /**
     * 유저 페이지
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/user_access")
    public String userAccess(Model model, Authentication authentication) {
        //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
        // logger.info("authentication:*********************"+ passwordEncoder.encode("123"));
        User userVo = (User) authentication.getPrincipal();  //userDetail 객체를 가져옴
        // model.addAttribute("info", userVo.getUser_id() +"의 "+ userVo.getUserName()+ "님");      //유저 아이디
        return "common/user/user_access";
    }
}