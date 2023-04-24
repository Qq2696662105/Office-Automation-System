package com.dataKing.security.filter;

import com.alibaba.fastjson2.JSON;
import com.dataKing.common.jwt.JwtHelper;
import com.dataKing.common.result.ResponseUtil;
import com.dataKing.common.result.Result;
import com.dataKing.common.result.ResultCodeEnum;
import com.dataKing.security.custom.CustomUser;
import com.dataKing.vo.system.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: TokenLoginFilter
 * Package: com.dataKing.security.filter
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/17 0017 15:35
 * @Version 1.0
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    //构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager,
                            RedisTemplate redisTemplate){
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        this.redisTemplate = redisTemplate;

    }

    //登录认证
    //获取输入的用户名和密码，调用认证方法
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException {

        try {
            //从请求信息中获得loginVo对象
            LoginVo loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVo.class);
            //将用户名和密码封装成一个Authentication对象
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //调用认证方法
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //认证成功调用方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth)
            throws IOException, ServletException {

        //获取CustomUser对象
        CustomUser custom = (CustomUser) auth.getPrincipal();
        //通过custom对象中的用户id和用户名生成token
        String token = JwtHelper.createToken(custom.getSysUser().getId(), custom.getSysUser().getUsername());
        //将生成的token放入map集合中返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);

        //将custom对象中的数据放如redis中
        redisTemplate.opsForValue().set(custom.getUsername(),
                JSON.toJSONString(custom.getAuthorities()));

        ResponseUtil.out(response, Result.ok(map));
    }


    //认证失败调用方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        ResponseUtil.out(response,Result.build(null, ResultCodeEnum.LOGIN_ERROR));

    }

}
