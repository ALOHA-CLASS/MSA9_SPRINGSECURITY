package com.aloha.thymeleaf_security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.thymeleaf_security.domain.CustomUser;
import com.aloha.thymeleaf_security.domain.Users;
import com.aloha.thymeleaf_security.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    /**
     * 메인 화면
     * 🔗 [GET] - / 
     * 📄 index.html
     * @return
     * @throws Exception 
    */
    @GetMapping("")
    // public String home(Principal principal, Model model) throws Exception {
    // public String home(Authentication authentication, Model model) throws Exception {
    // public String home(@AuthenticationPrincipal User authUser, Model model) throws Exception {
    public String home(@AuthenticationPrincipal CustomUser authUser, Model model) throws Exception {
        log.info(":::::::::: 메인 화면 ::::::::::");

        // if (principal != null) {
        //     String username = principal.getName();           // 인증된 사용자 아이디
        //     log.info("username : " + username);
        //     Users user = userService.select(username);       // 사용자 정보 조회
        //     log.info("user : " + user);         
        //     model.addAttribute("user", user);                // 사용자 정보를 모델에 등록
        // } else {
        //     log.info("principal is null");
        // }

        // if( authentication != null ) {
        //     User authAuth = (User) authentication.getPrincipal();
        //     String username = authAuth.getUsername();
        //     log.info("username : " + username);
        //     Users user = userService.select(username);          // 사용자 정보 조회
        //     log.info("user : " + user);         
        //     model.addAttribute("user", user);
        // }

       
        // if( authUser != null ) {
        //     String username = authUser.getUsername();           // 인증된 사용자 아이디
        //     log.info("username : " + username);
        //     Users user = userService.select(username);          // 사용자 정보 조회
        //     log.info("user : " + user);         
        //     model.addAttribute("user", user);     // 사용자 정보를 모델에 등록
        //     log.info("authAuth : " + authUser);
        // }

        if( authUser != null ) {
            log.info("authUser : " + authUser);
            Users user = authUser.getUser();
            model.addAttribute("user", user);
        }
        
        return "index";
    }

    /**
     * 회원 가입 화면
     * 🔗 [GET] - /join
     * 📄 join.html
     * @return
     */
    @GetMapping("/join")
    public String join() {
        log.info(":::::::::: 회원 가입 화면 ::::::::::");
        return "join";
    }

    /**
     * 회원 가입 처리
     * 🔗 [POST] - /join
     * ➡   ⭕ /login
     *      ❌ /join?error
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/join")
    public String joinPro(Users user, HttpServletRequest request) throws Exception {
        log.info(":::::::::: 회원 가입 처리 ::::::::::");
        log.info("user : " + user);

        // 암호화 전 비밀번호
        String plainPassword = user.getPassword();
        // 회원 가입 요청
        int result = userService.join(user);
        
        // 회원 가입 성공 시, 바로 로그인
        boolean loginResult = false;
        if( result > 0 ) {
            // 암호화 전 비밀번호 다시 세팅
            // 회원가입 시, 비밀번호 암호화하기 때문에, 
            user.setPassword(plainPassword);
            loginResult = userService.login(user, request);
        }
        if (loginResult) {
            return "redirect:/";        // 메인화면으로 이동
        }
        if( result > 0 ) {
            return "redirect:/login";
        }
        
        return "redirect/join?error";
        
    }


    /**
     * 아이디 중복 검사
     * @param username
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> userCheck(@PathVariable("username") String username) throws Exception {
        log.info("아이디 중복 확인 : " + username);
        Users user = userService.select(username);
        // 아이디 중복
        if( user != null ) {
            log.info("중복된 아이디 입니다 - " + username);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        // 사용 가능한 아이디입니다.
        log.info("사용 가능한 아이디 입니다." + username);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    /**
     * 로그인 화면
     * @return
     */
    @GetMapping("/login")
    public String login(@CookieValue(value="remember-id", required = false) Cookie cookie
                       ,Model model ) {
        // @CookieValue(value="쿠키이름", required = 필수여부)
        // - required=true (default)  : 쿠키를 필수로 가져와서 없으면 에러
        // - required=false           : 쿠키 필수 ❌ ➡ 쿠키가 없으면 null, 에러❌
        log.info(":::::::::: 로그인 페이지 ::::::::::");

        String username = "";
        boolean rememberId = false;
        if( cookie != null ) {
            log.info("CookieName : " + cookie.getName());
            log.info("CookieValue : " + cookie.getValue());
            username = cookie.getValue();
            rememberId = true;
        }
        model.addAttribute("username", username);
        model.addAttribute("rememberId", rememberId);
        return "/login";
    }
    
}
