package com.demo.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.demo.app.handler.AuthFailureHandler;
import com.demo.app.handler.AuthSucessHandler;
import com.demo.app.service.UserService;

import lombok.AllArgsConstructor;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
@EnableWebSecurity // 시큐리티 필터 등록
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 권한 및 인증을 미리 체크하겠다는 설정을 활성화한다.
// @Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {

	private final UserService memberService;
	private final AuthSucessHandler authSucessHandler;
	private final AuthFailureHandler authFailureHandler;
	private final UserDetailsService userDetailsService;


	// DigestAuthenticationEntryPoint entryPoint() {
	// 	DigestAuthenticationEntryPoint result = new DigestAuthenticationEntryPoint();
	// 	result.setRealmName("My App Realm");
	// 	result.setKey("3028472b-da34-4501-bfd8-a355c42bdf92");
	// }
	
	// DigestAuthenticationFilter digestAuthenticationFilter() {
	// 	DigestAuthenticationFilter result = new DigestAuthenticationFilter();
	// 	result.setUserDetailsService(userDetailsService);
	// 	result.setAuthenticationEntryPoint(entryPoint());
	// }

	// BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 객체 (BCrypt라는 해시 함수를 이용하여 패스워드를 암호화 한다.)
	// 회원 비밀번호 등록시 해당 메서드를 이용하여 암호화해야 로그인 처리시 동일한 해시로 비교한다.
	@Bean
	public static BCryptPasswordEncoder encryptPassword() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 로그인 과정에서 password를 가로챌때 해당 해쉬로 암호화해서 비교한다.
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(encryptPassword());
	}
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web
		.ignoring()
		.antMatchers("/image/**","/css/**", "/js/**", "/lib/**", "/favicon.ico");
	}
	// @Bean
	// RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
	// 	RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
	// 	TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(myKey, userDetailsService, encodingAlgorithm);
	// 	rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
	// 	return rememberMe;
	// }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
		/*
		 csrf 토큰 활성화시 사용
		 쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스크립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 쿠키의 보안을 강화할 수 있다.
		*/
		//http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		http
			.authorizeHttpRequests(authorize -> authorize
				.antMatchers("/","/login/**").permitAll() // 해당 경로들은 접근을 허용
				.antMatchers("/board/**").hasAnyRole("USER,ADMIN")
				.anyRequest() // 다른 모든 요청은
				.authenticated() // 인증된 유저만 접근을 허용
			).formLogin(form -> form
				.loginPage("/login") // 해당 주소로 로그인 페이지를 호출한다.
				.loginProcessingUrl("/login/action") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 로그인처리를 한다. -> loadUserByName
				.successHandler(authSucessHandler) // 성공시 요청을 처리할 핸들러
				.failureHandler(authFailureHandler) // 실패시 요청을 처리할 핸들러
			).logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL
				.logoutSuccessUrl("/login") // 성공시 리턴 URL
				.invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
				.clearAuthentication(true) // 로그아웃 시 권한 제거
				.deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
				.permitAll()
			)
			.rememberMe(remember -> remember
				.rememberMeServices(rememberMeServices)
			)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
				.maxSessionsPreventsLogin(true) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
				// .sessionRegistry(sessionRegistry())
				.expiredUrl("/login?error=true&exception=Have been attempted to login from a new place. or session expired")
        	)
			.httpBasic(withDefaults())
			// .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()))
			// .addFilterBefore(digestFilter())
			// .securityContext((securityContext) -> securityContext
			// 	.securityContextRepository(new DelegatingSecurityContextRepository(
			// 		new RequestAttributeSecurityContextRepository(),
			// 		new HttpSessionSecurityContextRepository()
			// 	))
			// )
			.csrf().disable();
		return http.build();







        // http.csrf().disable()	// csrf 토큰을 비활성화
        // 	.authorizeRequests() // 요청 URL에 따라 접근 권한을 설정
		// 	.antMatchers("/","/login/**").permitAll() // 해당 경로들은 접근을 허용
		// 	.antMatchers("/board/**").hasAnyRole("USER,ADMIN")
		// 	.anyRequest() // 다른 모든 요청은
		// 	.authenticated() // 인증된 유저만 접근을 허용
		// .and()
		// 	.formLogin() // 로그인 폼은
		// 	.loginPage("/login") // 해당 주소로 로그인 페이지를 호출한다.
		// 	.loginProcessingUrl("/login/action") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 로그인처리를 한다. -> loadUserByName
		// 	.successHandler(authSucessHandler) // 성공시 요청을 처리할 핸들러
		// 	.failureHandler(authFailureHandler) // 실패시 요청을 처리할 핸들러
		// .and()
		// 	.logout()
		// 	.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL
		//     .logoutSuccessUrl("/login") // 성공시 리턴 URL
		//     .invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
		// 	.clearAuthentication(true) // 로그아웃 시 권한 제거
		//     .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
		// 	.permitAll()
		// .and()
        // 	.sessionManagement()
		// 	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //     .maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
        //     .maxSessionsPreventsLogin(true) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
		// 	.sessionRegistry(sessionRegistry())
        //     .expiredUrl("/login?error=true&exception=Have been attempted to login from a new place. or session expired");  // 세션이 만료된 경우 이동 할 페이지를 지정
    
		// return http.build();
		}

	private Customizer<HttpBasicConfigurer<HttpSecurity>> withDefaults() {
		return null;
	}
	// Work around https://jira.spring.io/browse/SEC-2855
	// @Bean
	// public SessionRegistry sessionRegistry() {
	// 	SessionRegistry sessionRegistry = new SessionRegistryImpl();
	// 	return sessionRegistry;
	// }

	// // Register HttpSessionEventPublisher
	// @Bean
	// public static ServletListenerRegistrationBean httpSessionEventPublisher() {
	// 	return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
	// }

	// @Bean
	// public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
	// 	InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	// 	manager.createUser(User.withUsername("user")
	// 	  .password(bCryptPasswordEncoder.encode("userPass"))
	// 	  .roles("USER")
	// 	  .build());
	// 	manager.createUser(User.withUsername("admin")
	// 	  .password(bCryptPasswordEncoder.encode("adminPass"))
	// 	  .roles("USER", "ADMIN")
	// 	  .build());
	// 	return manager;
	// }



}
