package com.dataKing.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.wechat.Menu;
import com.dataKing.vo.wechat.MenuVo;

import java.util.List;

/**
 * ClassName: MenuService
 * Package: com.dataKing.wechat.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/21 0021 16:30
 * @Version 1.0
 */
public interface MenuService extends IService<Menu> {
    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
