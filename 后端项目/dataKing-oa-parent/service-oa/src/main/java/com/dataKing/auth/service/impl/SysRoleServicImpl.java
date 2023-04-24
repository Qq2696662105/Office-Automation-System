package com.dataKing.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.mapper.SysRoleMapper;
import com.dataKing.auth.service.SysRoleService;
import com.dataKing.auth.service.SysUserRoleService;
import com.dataKing.model.system.SysRole;
import com.dataKing.model.system.SysUser;
import com.dataKing.model.system.SysUserRole;
import com.dataKing.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SysRoleServicImpl
 * Package: com.dataKing.auth.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 9:34
 * @Version 1.0
 */
@Service
public class SysRoleServicImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    //通过userId返回该用户拥有的角色信息，以及查询所有角色的信息
    @Override
    public Map<String, Object> getRoleByUserId(Long userId) {


        //获取所有角色信息
        List<SysRole> sysAllRoleList = this.list();

        //通过userId查询用户角色表，返回对应的用户角色集合
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(wrapper);

        //从sysUserRoleList中取出角色Id
        List<Long> sysUserRoleIdList = new ArrayList<>();
        for (SysUserRole sysUserRole:sysUserRoleList){
            Long roleId = sysUserRole.getRoleId();
            sysUserRoleIdList.add(roleId);
        }

        //通过该用户的角色id列表找出对应的角色信息
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : sysAllRoleList){
            boolean contains = sysUserRoleIdList.contains(sysRole.getId());
            if (contains){
                assginRoleList.add(sysRole);
            }
        }

        //创建一个返回Map集合
        //将结果放入map中用于返回
        Map<String, Object> map = new HashMap<>();
        map.put("allRolesList",sysAllRoleList);
        map.put("assginRoleList",assginRoleList);
        return map;

    }
    //给用户分配角色
    @Transactional
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {

        //封装查询方法，删除该用户原有的所有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //将赋予的角色添加进sysUserRole表中
        for (Long roleId : assginRoleVo.getRoleIdList()){
            if(StringUtils.isEmpty(roleId)) continue;
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }

    }
}
