package com.dataKing.auth;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.mapper.SysRoleMapper;
import com.dataKing.auth.mapper.SysUserMapper;
import com.dataKing.auth.service.SysRoleService;
import com.dataKing.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ClassName: TestMpDemo1
 * Package: com.dataKing.auth
 * Description:
 *
 * @Author dataKing
 * @Create 2023/3/25 0025 22:47
 * @Version 1.0
 */

@SpringBootTest
public class TestMpDemo1 {


    //注入
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleService sysRoleService;

    //测试查询所有
    @Test
    public void getAll(){
        List<SysRole> sysRolesList = sysRoleMapper.selectList(null);
        System.out.println(sysRolesList);
    }


    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员1");
        sysRole.setRoleCode("role1");
        sysRole.setDescription("角色管理员1");
        int rows = sysRoleMapper.insert(sysRole);
        System.out.println(rows);
        System.out.println(sysRole.getId());

    }

    @Test
    public void updata(){
        SysRole sysRole = sysRoleMapper.selectById("10");
        sysRole.setRoleName("角色管理员被修改");
        int update = sysRoleMapper.updateById(sysRole);
        System.out.println(update);
        System.out.println(sysRole.getRoleName());
    }

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        //UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        //所以不填写就是无任何条件
        List<SysRole> users = sysRoleService.list();
        users.forEach(sysRole -> {
            System.out.println(sysRole);
        });
    }

}
