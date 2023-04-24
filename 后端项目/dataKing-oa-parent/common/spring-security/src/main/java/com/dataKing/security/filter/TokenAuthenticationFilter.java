package com.dataKing.security.filter;

import com.alibaba.fastjson.JSON;
import com.dataKing.common.jwt.JwtHelper;
import com.dataKing.common.result.ResponseUtil;
import com.dataKing.common.result.Result;
import com.dataKing.common.result.ResultCodeEnum;
import com.dataKing.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TokenAuthenticationFilter
 * Package: com.dataKing.security.filter
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/17 0017 16:12
 * @Version 1.0
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        logger.info("url:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    //判断请求头之中有没有token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)){
            //通过ThreadLocal记录当前登录人信息
            LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
            String username = JwtHelper.getUsername(token);
            LoginUserInfoHelper.setUsername(username);
            if (!StringUtils.isEmpty(username)) {
                //通过username从redis中获取权限数据
                String authString = (String) redisTemplate.opsForValue().get(username);
                //将权限数据封装成List<SimpleGrantedAuthority>形式
                if (!StringUtils.isEmpty(authString)){
                    List<SimpleGrantedAuthority> authList = JSON.parseArray(authString, SimpleGrantedAuthority.class);
//                    List<Map> mapList = JSON.parseArray(authString, Map.class);
//                    System.out.println(mapList);
//                    List<SimpleGrantedAuthority> authList = new ArrayList<>();
//                    for (Map map:mapList){
//                        String authority = (String) map.get("authority");
//                        authList.add(new SimpleGrantedAuthority(authority));
//
//                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authList);

                }else {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }
            }
        }
        return null;
    }
}
