package com.dataKing.auth.service.impl;

import com.dataKing.auth.service.SysMenuService;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.model.system.SysUser;
import com.dataKing.security.custom.CustomUser;
import com.dataKing.security.custom.UserDetailsService;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ClassName: UserDetailsServiceImpl
 * Package: com.dataKing.auth.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/17 0017 15:18
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserService.getByUsername(username);

        if(null == user) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        if(user.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //1.根据userId获取按钮权限数据
        List<String> userPerms = sysMenuService.findUserPermsByUserId(user.getId());
        //2.新建一个SimpleGrantedAuthority集合
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        //3.遍历按钮权限数据加入到SimpleGrantedAuthority集合中
        for(String item:userPerms){
            authList.add(new SimpleGrantedAuthority(item));
        }

        return new CustomUser(user, authList);
    }
}
