package com.security.web.sys.comm.service;
 
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.web.sys.comm.dto.User;
import com.security.web.sys.comm.mapper.CommMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * @author navy
 * @date 2023/01/05
 * @description 2、UserDetailsService 인터페이스를 구현하는 클래스를 정의하고 사용자 이름에 따라 사용자 정보를 가져오는 loadUserByUsername 메서드를 다시 작성합니다.
  * 반환되는 사용자 정보도 스프링 보안 전용 사용자 클래스, 즉 com.imysh.zmy.spring.security.config.User
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    private CommMapper commMapper;
 
    // @Autowired
    // private SysRoleMapper roleMapper;
 
    // @Autowired
    // private SysMenuMapper menuMapper;
 
    /**
     * 사용자 정보 캐시
     */
    static Map<String, User> userCache = new ConcurrentHashMap<>();
 
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = new User(); // 임시
        try {
            user = commMapper.selectUsers(email);
            if(user == null) {
                throw new UsernameNotFoundException(email);
            } else {
                user.setUser_auth("ROLE_" + user.getUser_auth());
                // switch (user.getUser_tp_cd()) {
                //     case "01":
                //     logger.info("USERUSERUSERUSERUSERUSER");
                //         user.setUser_auth("ROLE_" + user.getUser_auth());
                //         break;
                //     case "02":
                //     logger.info("MEMBERMEMBERMEMBERMEMBER");
                //         user.setUser_auth("ROLE_MEMBER");
                //         break;
                //     case "03":
                //     logger.info("ADMINADMINADMINADMINADMIN");
                //         user.setUser_auth("ROLE_ADMIN");
                //         break;
                //     default:
                //         break;
                // }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 캐시에 현재 사용자가 포함되어 있는지 확인
        // return user
        // if (userCache.containsKey(email)) {
        //     user = userCache.get(email);
        // } else {
        //     // 기본 사용자 정보
        //     try {
        //         user = commMapper.selectUsers(email);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        //     if (EmptyUtil.isNotEmpty(user)) {
        //         // 사용자 역할 목록
        //         user.setRoles(roleMapper.getUserRoles(user.getUserId()));
        //         // 사용자 메뉴 목록
        //         user.setMenus(menuMapper.getUserMenus(user.getUserId()));
        //         // token
        //         String token = Base64.getEncoder().encodeToString((user.getUsername() + "_" + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
        //         user.setToken(token);
        //         // 사용자 정보를 캐시에 저장
        //         userCache.put(username, user);
        //     }
        // }
        return user;
    }
}