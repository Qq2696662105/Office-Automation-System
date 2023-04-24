package com.dataKing.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.mapper.SysUserRoleMapper;
import com.dataKing.auth.service.SysUserRoleService;
import com.dataKing.model.system.SysUserRole;
import org.springframework.stereotype.Service;

/**
 * ClassName: SysUserRoleServiceImpl
 * Package: com.dataKing.auth.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/6 0006 20:40
 * @Version 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
