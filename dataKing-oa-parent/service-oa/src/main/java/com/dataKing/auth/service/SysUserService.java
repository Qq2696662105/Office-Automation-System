package com.dataKing.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.system.SysUser;

import java.util.Map;

/**
 * ClassName: SysUserService
 * Package: com.dataKing.auth.service
 * Description:SysUserService接口
 *
 * @Author dataKing
 * @Create 2023/3/28 0028 20:51
 * @Version 1.0
 */

public interface SysUserService extends IService<SysUser> {
    void updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);

    Map<String, Object> getUserInfoByUserId(Long userId);

    Map<String, Object> getCurrentUser();
}
