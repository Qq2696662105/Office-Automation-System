package com.dataKing.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.auth.service.SysRoleService;
import com.dataKing.common.handler.MyException;
import com.dataKing.common.result.Result;
import com.dataKing.model.system.SysRole;
import com.dataKing.vo.system.AssginRoleVo;
import com.dataKing.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SysRoleController
 * Package: com.dataKing.auth.controller
 * Description:角色控制层
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 9:36
 * @Version 1.0
 */

//Http://localhost:8800/admin/system/sysRole
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    //注入Service
    @Autowired
    private SysRoleService sysRoleService;

    //查询所有角色
//    @GetMapping("/findAll")
//    public List<SysRole> findAll(){
//        List<SysRole> list = sysRoleService.list();
//        return list;
//    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据用户id查询对应的角色")
    @GetMapping("/getAssign/{userId}")
    public Result getAssign(@PathVariable Long userId){
        Map<String,Object> data = sysRoleService.getRoleByUserId(userId);
        return Result.ok(data);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("给用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);

        return Result.ok();
    }


    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/findAll")
    public Result findAllRole(){
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //1 创建Page对象，传递分页相关参数
        //page 当前页  limit 每页显示记录数
        Page<SysRole> sysRolePage = new Page<>(page,limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)) {
            //封装 like模糊查询
            wrapper.like(SysRole::getRoleName,roleName);
        }

        //执行service方法进行分页查询
        Page<SysRole> rolePage = sysRoleService.page(sysRolePage, wrapper);

        return Result.ok(rolePage);

    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("通过id获取")
    @GetMapping("get/{id}")
    public Result getSysRoleById(@PathVariable Long id){

        //通过service调用方法实现通过id查询角色信息
        SysRole sysRole = sysRoleService.getById(id);

        if(sysRole != null){
            return Result.ok(sysRole);
        }else {
            return Result.fail();
        }

    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("新增角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){

        boolean is_sucess = sysRoleService.save(sysRole);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PostMapping("update")
    public Result update(@RequestBody SysRole sysRole){

        boolean is_sucess = sysRoleService.updateById(sysRole);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result deleteById(@PathVariable Long id){

        boolean is_sucess = sysRoleService.removeById(id);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "批量删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){

        boolean is_sucess = sysRoleService.removeByIds(idList);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

}
