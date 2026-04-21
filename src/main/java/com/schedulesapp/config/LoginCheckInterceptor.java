package com.schedulesapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 현재 요청의 세션을 가져옵니다. (false: 세션이 없으면 새로 만들지 않음)
        HttpSession session = request.getSession(false);

        // 2. 세션이 아예 없거나, "loginUser" 정보가 없다면 튕겨냅니다.
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.");
        }

        // 3. 통과! (true를 리턴하면 다음 단계(컨트롤러)로 요청이 넘어갑니다.)
        return true;
    }
}