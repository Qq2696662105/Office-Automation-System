package com.dataKing.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.system.SysRole;
import com.dataKing.vo.system.AssginRoleVo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName: SysRoleService
 * Package: com.dataKing.auth.service
 * Description:角色服务层
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 9:33
 * @Version 1.0
 */

public interface SysRoleService extends IService<SysRole> {
    Map<String,Object> getRoleByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
