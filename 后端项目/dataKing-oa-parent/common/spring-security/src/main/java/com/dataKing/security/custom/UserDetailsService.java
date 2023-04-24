package com.dataKing.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * ClassName: UserDetailsService
 * Package: com.dataKing.security.custom
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/17 0017 15:16
 * @Version 1.0
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    /**
     * 根据用户名获取用户对象（获取不到直接抛异常）
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}