package com.dataKing.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.mapper.SysMenuMapper;
import com.dataKing.auth.service.SysMenuService;
import com.dataKing.auth.service.SysRoleMenuService;
import com.dataKing.auth.utils.MenuHelper;
import com.dataKing.common.handler.MyException;
import com.dataKing.model.system.SysMenu;
import com.dataKing.model.system.SysRoleMenu;
import com.dataKing.vo.system.AssginMenuVo;
import com.dataKing.vo.system.MetaVo;
import com.dataKing.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SysMenuServiceImpl
 * Package: com.dataKing.auth.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/9 0009 12:32
 * @Version 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //该方法是为了遍历出menu表中的层级关系，弄成与前端路由形式：parentId==0为父级，里面包含子类
    //SysMenu实体类中包含字段children用来存储该菜单的子菜单
    @Override
    public List<SysMenu> findNodes() {
        //获取sys_menu数据库的数据
        List<SysMenu> menuList = this.list();
        //遍历该数据，构建树形结构
        List<SysMenu> returnList = MenuHelper.buildTrees(menuList);

        return returnList;
    }
    //删除菜单
    @Override
    public void removeMenuById(Long id) {

        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);

        Integer count = this.count(wrapper);
        if (count>0){
            throw new MyException(201,"无法删除！");
        }
        this.removeById(id);

    }
    //根据角色Id获取已分配的菜单权限
    @Override
    public List<SysMenu> getAssign(Long roleId) {
        //先取出sys_menu表中的全部数据，用于之后的比较
        List<SysMenu> allmenus = this.list();
        //根据roleId查询sys_role_menu表找到对应的menuId
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(wrapper);
        //取出查询到的信息里的menuId信息
        List<Long> menuIds = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : roleMenus){
            menuIds.add(sysRoleMenu.getMenuId());
        }
        //选中已分配的菜单权限
        for (SysMenu sysMenu : allmenus){
            if (menuIds.contains(sysMenu.getId())){
                sysMenu.setSelect(true);
            }else {
                sysMenu.setSelect(false);
            }
        }

        allmenus = MenuHelper.buildTrees(allmenus);

        return allmenus;
    }

    //更新角色所拥有的权限
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //先删除原有的权限
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId()));
        //遍历列表插入权限数据
        for (Long menuId : assginMenuVo.getMenuIdList()){
            if(StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    //获取用户可操作的菜单列表
    @Override
    public List<RouterVo> findMenuListByUserId(Long userId) {

        List<SysMenu> sysMenuList = null;
        //1 判断当前用户是否是管理员 userId=1是管理员
        //1.1 如果是管理员，查询所有菜单列表
        if (userId.longValue() == 1){
            //查询所有
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, 1)
                    .orderByAsc(SysMenu::getSortValue));
        }else {
            //1.2如果不是管理员，根据UserId查询可以操作的菜单列表
            //多表关联查询：用户角色表、角色菜单表和菜单表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //2 把查询出来的数据列表构建成框架要求的路由数据结构
        //使用菜单操作工具类构建树形结构
        List<SysMenu> trees = MenuHelper.buildTrees(sysMenuList);
        //构建成框架要求的路由结构
        List<RouterVo> routerMenus = this.buildRouter(trees);

        return routerMenus;
    }

    private List<RouterVo> buildRouter(List<SysMenu> trees) {
        //创建List集合，存储最终数据
        List<RouterVo> routers = new ArrayList<>();
        //trees遍历
        for (SysMenu sysMenu : trees){
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(sysMenu));
            router.setComponent(sysMenu.getComponent());
            router.setMeta(new MetaVo(sysMenu.getName(),sysMenu.getIcon()));
            List<SysMenu> children = sysMenu.getChildren();
            if (sysMenu.getType().intValue() == 1){
                //加载出来下面隐藏路由
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList){
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));

                    routers.add(hiddenRouter);
                }
            }else {
                if(!CollectionUtils.isEmpty(children)){
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    //递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }

        return routers;
    }
    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    //获取用户可访问的按钮权限
    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenus = null;
        //1 判断是否是管理员，如果是管理员，查询所有按钮列表
        if(userId.longValue() == 1){
            sysMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus,1));
        }else {
            //2 如果不是管理员，根据userId查询可以操作的按钮列表
            sysMenus = baseMapper.findMenuListByUserId(userId);

        }
        //3 从查询出来的数据里面，获取可以操作的按钮值的list集合，返回
        List<String> permsList = sysMenus.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());

        return permsList;
    }



}
