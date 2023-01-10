package com.security.web.sys.comm.controller;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.security.web.sys.comm.service.CommService;


@Controller
@RequiredArgsConstructor
public class CommController {
    // private final CommService commService;
    private static final Logger logger = LogManager.getLogger(CommController.class);

    @Autowired
    private final CommService commService;

    @Value("${eformLoc}")
	private String EFORM_LOC;
	
	@Value("${eform.crf.dir}")
	private String EFORM_DIR;

    /**
     * session 체크
     * @return
     */
    @RequestMapping(value = {"/main", "/"})
    public String mainPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if (userId==null || userId.trim().equals("")) {
            return "redirect:/user/login";
        }else {
        String sysCode = (String)session.getAttribute("sysCode");
            if (sysCode.equals("FS")) {
                return "redirect:/fs/main";
            } else {
                return "redirect:/main";
            }
        }
    }  

    /**
     * 로그인 화면
     * @return
     */
	@GetMapping("/user/login")
	public String getLoginPage(Model model,
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "exception", required = false) String exception) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		return "common/user/login";
	}
    /**
     * 아이디 찾기 화면
     * @return
     */
    @GetMapping("/user/loginId")
    public ModelAndView loginId(ModelAndView model) {
        model.setViewName("common/user/loginId");
        return model;
    }

    /**
     * 아이디 찾기 결과 화면
     * @return
     */
    @GetMapping("/user/loginFindIdResult")
    public ModelAndView loginFindIdResult(ModelAndView model,@PathParam("id") String id) {
        model.setViewName("common/user/loginFindIdResult");
        model.addObject("userInfo", id);
        return model;
    }

    /**
     * 비밀번호 찾기 화면
     * @return
     */
    @GetMapping("/user/loginForgotPw")
    public ModelAndView loginForgotPw(ModelAndView model) {
        model.setViewName("common/user/loginForgotPw");
        return model;
    }

    /**
     * 비밀번호 찾기 입력 화면
     * @return
     * @throws Exception
     */
    @GetMapping("/user/loginForgotPwEnter/{email}")
    public ModelAndView loginForgotPwEnter(ModelAndView model,@PathVariable String email) throws Exception {

        model.addObject("userEmail", email);
        model.setViewName("common/user/loginForgotPwEnter");
        return model;
    }

    /**
     * 비밀번호변경 팝업 화면
     * @return
     */
    @GetMapping("/user/loginChangePwPopup")
    public String loginChangePwPopup() {
        return "common/user/loginChangePwPopup";
    }

    /**
     * 고객사검색_팝업 화면
     * @return
     */
    @GetMapping("/user/customerSearchPopup")
    public String customerSearchPopup() {
        return "common/popup/customerSearchPopup";
    }

    /**
     * 담당자검색_팝업 화면
     * @return
     */
    @GetMapping("/user/personSearchPopup")
    public String personSearchPopup() {
        return "common/popup/personSearchPopup";
    }

    /**
     * 담당자추가_팝업 화면
     * @return
     */
    @GetMapping("/user/addContactPopup")
    public ModelAndView addContactPopup(String sysId, String fsNo, ModelAndView model) {
        model.setViewName("common/popup/addContactPopup");
            if(StringUtils.hasLength(fsNo)) model.addObject("fsNo", fsNo);
            if(StringUtils.hasLength(sysId)) model.addObject("sysId", sysId);
        return model;
    }

    // 로그아웃
    @RequestMapping(value = "/user/logout")
    public ModelAndView logout(ModelAndView mav, HttpServletRequest request, HttpSession session) throws Exception {

        //1) 세션 가져오기
        if (session != null){
            session.invalidate();
        }
        mav.setViewName("redirect:/user/login");
        return mav;
    }


    /**
     * 성공 화면 호출
     * @return
     */
    @GetMapping("/user/success")
    public ModelAndView niceSuccess(ModelAndView model) {
        model.setViewName("common/user/niceSuccess");
        // todo....
        // model.addObject("sysId", "");
        return model;
    }

    /**
     * 실페 화면 호출
     * @return
     */
    @GetMapping("/user/nicefail")
    public String niceFail() {
        return "common/user/niceFail";
    }

    /**
     * pdf view
     * @return
     */
    @GetMapping("/etc/serviceStatementView")
    public String pdfview(String sys_id, String fs_bill_no, String fs_bill_seq, Model model) {
        model.addAttribute("sys_id", sys_id);
        model.addAttribute("fs_bill_no", fs_bill_no);
        model.addAttribute("fs_bill_seq", fs_bill_seq);
        model.addAttribute("eformDir", EFORM_DIR);
    	model.addAttribute("eformLoc", EFORM_LOC);
        return "common/etc/serviceStatementView";
    }
    /**
     * 개인정보처리방침
     * @return
     */
    @GetMapping("/user/etcProtect")
    public String etcProtect() {
        return "common/user/etcProtect";
    }
    /**
     * 서비스 이용양관
     * @return
     */
    @GetMapping("/user/etcClause")
    public String etcClause() {
        return "common/user/etcClause";
    }
}
