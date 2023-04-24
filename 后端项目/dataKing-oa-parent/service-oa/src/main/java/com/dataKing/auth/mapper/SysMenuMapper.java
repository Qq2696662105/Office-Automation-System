package com.dataKing.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dataKing.model.system.SysMenu;
import org.springframework.stereotype.Repository;


import javax.websocket.server.PathParam;
import java.util.List;

/**
 * ClassName: SysMenuMapper
 * Package: com.dataKing.auth.mapper
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/9 0009 12:30
 * @Version 1.0
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findMenuListByUserId(@PathParam("userId") Long userId);
}
