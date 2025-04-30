package com.test.lotterysystem.Comment.Interceptor;

import com.test.lotterysystem.Comment.Utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("user_token");
        Claims claims = JWTUtil.parseJWT(token);

        log.info("请求url为：------"+request.getRequestURI());

        if(claims==null){
            response.setStatus(401);
            log.error("token异常....");
            return false;
        }
        return true;
    }
}
