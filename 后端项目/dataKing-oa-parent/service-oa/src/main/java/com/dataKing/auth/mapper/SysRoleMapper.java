package com.dataKing.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dataKing.model.base.BaseEntity;
import com.dataKing.model.system.SysRole;
import org.springframework.stereotype.Repository;

/**
 * ClassName: SysRoleMapper
 * Package: com.dataKing.auth.mapper
 * Description:角色mapper层
 *
 * @Author dataKing
 * @Create 2023/3/25 0025 22:43
 * @Version 1.0
 */

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
