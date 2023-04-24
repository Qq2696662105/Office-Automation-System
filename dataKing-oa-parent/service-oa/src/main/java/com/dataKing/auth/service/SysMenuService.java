package com.dataKing.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.system.SysMenu;
import com.dataKing.vo.system.AssginMenuVo;
import com.dataKing.vo.system.RouterVo;

import java.util.List;

/**
 * ClassName: SysMenuService
 * Package: com.dataKing.auth.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/9 0009 12:30
 * @Version 1.0
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> getAssign(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    List<String> findUserPermsByUserId(Long userId);

    List<RouterVo> findMenuListByUserId(Long userId);
}
