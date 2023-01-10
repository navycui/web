package com.security.web.sys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.security.web.sys.comm.service.UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 권한 및 인증을 미리 체크하겠다는 설정을 활성화한다.
public class SecurityConfiguration {
	/* 로그인 실패 핸들러 의존성 주입 */
	private final AuthFailureHandler authFailureHandler;
	private final AuthSucessHandler authSucessHandler;
	// private final AuthFailureHandler authFailureHandler;

    // Password encoding 방법 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserService userService;

    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userDetailService) 
    //   throws Exception {
    //     return http.getSharedObject(AuthenticationManagerBuilder.class)
    //       .userDetailsService(userService)
    //       .passwordEncoder(passwordEncoder())
    //       .and().build();
    // }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
            "/js/**",
            "/css/**",
            "/images/**",
            "/jqwidgets/**",
            "/error",
            "/etc/serviceStatementView",
            "/upload/images/**" 
        );
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http
        // .antMatcher("/user/**")
        // .authorizeRequests()
        // // .antMatchers("/api/v1/**").hasAuthority(USER.name())
        // .antMatchers("/api/v1/**").hasRole("USER")
        //         .and()
        // .httpBasic().disable()
        // .formLogin().disable()
        // .cors().disable()
        // .csrf().disable()
        // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // .and()
        // .authorizeRequests()
        // .anyRequest().permitAll()
        // .and()
        // // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        // .exceptionHandling()
        //     .authenticationEntryPoint(((request, response, authException) -> {
        //         response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //         userService.writeValue(
        //                 response.getOutputStream(),
        //                 ExceptionResponse.of(ExceptionCode.FAIL_AUTHENTICATION)
        //         );
        // }))
        //     .accessDeniedHandler(((request, response, accessDeniedException) -> {
        //         response.setStatus(HttpStatus.FORBIDDEN.value());
        //         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //         userService.writeValue(
        //                 response.getOutputStream(),
        //                 ExceptionResponse.of(ExceptionCode.FAIL_AUTHORIZATION)
        //         );
        //     })).and().build();


		/*
		 csrf 토큰 활성화시 사용
		 쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스크립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 쿠키의 보안을 강화할 수 있다.
		*/
		// http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
        http.csrf().disable()	// csrf 토큰을 비활성화
        // .authorizeHttpRequests((authz) -> authz
        //     .antMatchers("/","/user/**").permitAll()// 해당 경로들은 접근을 허용
        //     .antMatchers("/user_success").hasRole("ADMIN")
        //     .anyRequest().authenticated()
        // )
        // .httpBasic()
            .authorizeRequests() // 요청 URL에 따라 접근 권한을 설정
            .antMatchers("/","/user/**").permitAll()// 해당 경로들은 접근을 허용
			.antMatchers("/user_success").hasRole("ADMIN")
            .anyRequest() // 다른 모든 요청은
			.authenticated() // 인증된 유저만 접근을 허용
            // .authenticationManager(new CustomAuthenticationManager())
		.and()
			.formLogin() // 로그인 폼은
			.loginPage("/user/login") // 해당 주소로 로그인 페이지를 호출한다.
            // .usernameParameter("email")
            .passwordParameter("pwd")
			.loginProcessingUrl("/api/login") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 로그인처리를 한다. -> loadUserByName
            // .defaultSuccessUrl("/user_access")
			.successHandler(authSucessHandler) // 성공시 요청을 처리할 핸들러
			.failureHandler(authFailureHandler) // 실패시 요청을 처리할 핸들러
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL
		    .logoutSuccessUrl("/user/login") // 성공시 리턴 URL
		    .invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
		    .deleteCookies("JSESSIONID","remember-me") // JSESSIONID 쿠키 삭제
			.permitAll()
		.and()
        	.sessionManagement()
            .maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
            .maxSessionsPreventsLogin(true) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
            .expiredUrl("/user/login?error=true&exception=Have been attempted to login from a new place. or session expired")  // 세션이 만료된 경우 이동 할 페이지를 지정
            // .sessionRegistry(sessionRegistry());
            // .SessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안한 셋팅
        .and()
	        .and()
            .rememberMe() // 로그인 유지
	        .alwaysRemember(false) // 항상 기억할 것인지 여부
	        .tokenValiditySeconds(43200) // in seconds, 12시간 유지
	        .rememberMeParameter("remember-me");
        http.headers().frameOptions().disable();		//로그인 창
        return http.build();
    }
    // @Override
    // public void configure(WebSecurity web) throws Exception {
    //     // 정적인 파일 요청에 대해 무시
    //     web.ignoring().antMatchers(AUTH_WHITELIST);
    // }
}
