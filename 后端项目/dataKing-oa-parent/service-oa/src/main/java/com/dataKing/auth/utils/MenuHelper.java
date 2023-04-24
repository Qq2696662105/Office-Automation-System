package com.dataKing.auth.utils;

import com.dataKing.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: MenuHelper
 * Package: com.dataKing.auth.utils
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/9 0009 16:30
 * @Version 1.0
 */
public class MenuHelper {

    public static List<SysMenu> buildTrees(List<SysMenu> sysMenus){

        List<SysMenu> sysMenuList = new ArrayList<>();
        for (SysMenu sysMenu:sysMenus){

            if(sysMenu.getParentId().longValue() == 0){
                sysMenuList.add(getChildren(sysMenu,sysMenus));
            }

        }

        return sysMenuList;
    }

    public static SysMenu getChildren(SysMenu sysMenu,List<SysMenu> sysMenus){

        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : sysMenus){
            if(it.getParentId().longValue() == sysMenu.getId().longValue()){
                if (sysMenu.getChildren()==null) sysMenu.setChildren(new ArrayList<SysMenu>());
                sysMenu.getChildren().add(getChildren(it,sysMenus));
            }
        }

        return sysMenu;
    }

}
