package com.aloha.security5.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.aloha.security5.service.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity              // 해당 클래스를 스프링 시큐리티 설정 빈으로 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;        // 비밀번호 암호화 객체
		
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomUserDetailsService customUserDetailsService; 


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 인가 설정
        http.authorizeRequests(requests -> requests
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/login").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated());

        // 폼 로그인 설정
        // - 기본 로그인 경로 : /login
        // loginPage("로그인화면경로") : 설정 시, 시큐리티 기본 로그인 페이지 미사용(커스텀 로그인 페이지)
        http.formLogin(login -> login.loginPage("/login")
                                     .permitAll());

        // 로그아웃 설정
        // - 기본 로그아웃 경로 : /logout
        http.logout(logout -> logout.logoutSuccessUrl("/")
                                    // .logoutUrl("/user/logout")
                                    .permitAll()
        
                    );

        // 자동로그인 설정
        http.rememberMe(me -> me
                .key("aloha")
                .tokenRepository(tokenRepository())      // DataSource 가 등록된 PersistentRepository 토큰정보 객체 
                .tokenValiditySeconds(60 * 60 * 24 * 7)) // 토큰 유효기간 지정 : 7일 (초 단위)
            ;


        // csrf 비활성화
        // http.csrf().disable();

    }





    /**
     * 👮‍♂️🔐사용자 인증 관리 메소드
     * ⭐ 인메모리 인증 방식
     * ⭐ JDBC 인증 방식 
     * ⭐ 사용자 정의 인증 방식
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // AuthenticationManagerBuilder : 인증 관리 객체

        // 💚 인메모리 인증 방식
        // auth.inMemoryAuthentication()               
        //     // .withUser("아이디").password("비밀번호").roles("권한")
        //     // passwordEncoder.encode("비밀번호")     :   비밀번호 암호화
        //     // BCryptPasswordEncoder 사용
        //     .withUser("user").password(passwordEncoder.encode("123456")).roles("USER")
        //     .and()
        //     .withUser("admin").password(passwordEncoder.encode("123456")).roles("ADMIN")
        //     ;
        //     // NoOpPasswordEncoder 사용
        //     // .withUser("user").password("123456").roles("USER")
        //     // .and()
        //     // .withUser("admin").password("123456").roles("ADMIN")
        //     // ;

        // 💚 JDBC 인증 방식
        // // ⭐ 사용자 인증 쿼리
        // String sql1 = " SELECT username as username, password as password, enabled "
        //             + " FROM user "
        //             + " WHERE username = ? ";
        // // ⭐ 사용자 권한 쿼리
        // String sql2 = " SELECT username as username, auth " 
        //             + " FROM user_auth "
        //             + " WHERE username = ? ";
        // auth.jdbcAuthentication()
        //     // 데이터 소스 등록
        //     .dataSource( dataSource )
        //     // 인증 쿼리    (아이디/비밀번호/활성여부)
        //     .usersByUsernameQuery(sql1)
        //     // 인가 쿼리    (아이디/권한)
        //     .authoritiesByUsernameQuery(sql2)
        //     // 비밀번호 암호화 방식 지정 - BCryptPasswordEncoder 또는 NoOpPasswordEncoder
        //     .passwordEncoder( passwordEncoder );

        // 💚 사용자 정의 인증 방식
        auth.userDetailsService( customUserDetailsService )
            .passwordEncoder(passwordEncoder);
    }


    // PersistentRepository 토큰정보 객체 - 빈 등록
    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
        JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl(); 
        // 토큰 저장소를 사용하는 데이터 소스 지정
        repositoryImpl.setDataSource(dataSource);
        // persistent_logins 테이블 자동 생성
        // repositoryImpl.setCreateTableOnStartup(true);
        try {
            repositoryImpl.getJdbcTemplate().execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
        } catch (Exception e) {
            log.error("persistent_logins 테이블이 이미 생성되었습니다.");
        }
        return repositoryImpl;
    }


}