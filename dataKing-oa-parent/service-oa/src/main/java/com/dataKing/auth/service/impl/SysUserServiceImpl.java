package com.dataKing.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.mapper.SysUserMapper;
import com.dataKing.auth.service.SysMenuService;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.model.system.SysUser;
import com.dataKing.security.custom.LoginUserInfoHelper;
import com.dataKing.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SysUserServiceImpl
 * Package: com.dataKing.auth.service.impl
 * Description:SysUserService实现类
 *
 * @Author dataKing
 * @Create 2023/3/28 0028 20:53
 * @Version 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;

    //更新用户状态
    @Override
    public void updateStatus(Long id, Integer status) {
        //先获取该用户原有信息
        SysUser user = this.getById(id);
        //更改原有信息的状态码
        if(status.intValue() == 1){
            user.setStatus(status);
        }else {
            user.setStatus(0);
        }
        //更新用户表
        this.updateById(user);

    }

    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public Map<String, Object> getUserInfoByUserId(Long userId) {

        //根据id获取用户信息
        SysUser user = this.getById(userId);
        //根据UserId获取能够访问的菜单列表信息
        List<RouterVo> routerList = sysMenuService.findMenuListByUserId(userId);
        //根据UserId获取能够访问的按钮权限信息
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",user.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers",routerList);
        map.put("buttons",permsList);
        return map;
    }

    @Override
    public Map<String, Object> getCurrentUser() {

        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = this.getById(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        return map;
    }
}
