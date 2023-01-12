package com.demo.app.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.app.mapper.CommMapper;
import com.demo.app.member.User;

import lombok.RequiredArgsConstructor;

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
            user = commMapper.findByEmail(email);
            if(user == null) {
                throw new UsernameNotFoundException(email);
            } else {
                switch (user.getUser_tp_cd()) {
                    case "01":
                    logger.info("USERUSERUSERUSERUSERUSER");
                        user.setUserAuth("ROLE_USER");
                        break;
                    case "02":
                    logger.info("MEMBERMEMBERMEMBERMEMBER");
                        user.setUserAuth("ROLE_MEMBER");
                        break;
                    case "03":
                    logger.info("ADMINADMINADMINADMINADMIN");
                        user.setUserAuth("ROLE_ADMIN");
                        break;
                    default:
                        break;
                }
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