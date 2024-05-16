package com.banne.template.interceptor;

import com.banne.template.common.context.BaseContext;
import com.banne.template.common.properties.JwtProperties;
import com.banne.template.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 获取请求头中的令牌  需要进行前端联调  将令牌存入到请求头重
        String jwtToken = request.getHeader("Token");
        log.error("拦截器中的内容 " + jwtToken);

        if(jwtToken != null){
            Claims claims = JwtUtil.parseJWT(jwtProperties.getKey(), jwtToken);
            Long userId = (Long)claims.get("userId");
            // 将 Integer 的 userId 转换为 Long
            log.error("ThreadLocal中存放的内容 " + userId);
            // 将当前登录的用户id存放在ThreadLocal中,方便用户的获取;
            BaseContext.setCurrentId(userId);
            return true;
        }

        // 前端请求 发过来了已经  携带了令牌
        log.info("拦截器已拦截!");

        return true;
    }
}
