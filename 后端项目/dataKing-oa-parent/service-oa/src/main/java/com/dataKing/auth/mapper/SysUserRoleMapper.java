package com.dataKing.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dataKing.model.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName: SysUserRole
 * Package: com.dataKing.auth.mapper
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/6 0006 20:35
 * @Version 1.0
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
