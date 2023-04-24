package com.dataKing.auth.controller;

import com.dataKing.auth.service.SysMenuService;
import com.dataKing.common.result.Result;
import com.dataKing.model.system.SysMenu;
import com.dataKing.vo.system.AssginMenuVo;
import com.dataKing.vo.system.AssginRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * ClassName: SysMenuController
 * Package: com.dataKing.auth.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/9 0009 13:58
 * @Version 1.0
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("获取菜单")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> allNodes = sysMenuService.findNodes();
        return Result.ok(allNodes);
    }

    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    @ApiOperation("根据角色Id获取已分配的菜单权限")
    @GetMapping("/getAssign/{roleId}")
    public Result getAssign(@PathVariable Long roleId){

        List<SysMenu> sysMenus = sysMenuService.getAssign(roleId);
        return Result.ok(sysMenus);
    }

    @ApiOperation("更新角色所拥有的权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo){

        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }

}
