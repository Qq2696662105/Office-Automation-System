package com.dataKing.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.common.result.Result;
import com.dataKing.common.utils.MD5;
import com.dataKing.model.system.SysUser;
import com.dataKing.vo.system.SysRoleQueryVo;
import com.dataKing.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: SysUserController
 * Package: com.dataKing.auth.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/3/28 0028 20:57
 * @Version 1.0
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result batchUser(@PathVariable Long page,
                            @PathVariable Long limit,
                            SysUserQueryVo sysUserQueryVo) {
        //创建page对象
        Page userPage = new Page(page, limit);
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值
        //根据姓名查询
        String name = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        //like 模糊查询
        if(!StringUtils.isEmpty(name)) {
            wrapper.like(SysUser::getName,name);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //le 小于等于
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
        //调用mp的方法实现条件分页查询
        Page pageModel = sysUserService.page(userPage, wrapper);

        return Result.ok(pageModel);
    }

    @ApiOperation("通过id获取用户信息")
    @GetMapping("get/{id}")
    public Result getUserById(@PathVariable Long id){

        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null){
            return Result.fail().message("该id不存在");
        }else {
            return Result.ok(sysUser);
        }

    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {

        //先对密码进行加密处理，使用MD5
        String passwordMD5 = MD5.encrypt(user.getPassword());
        user.setPassword(passwordMD5);

        boolean is_sucess = sysUserService.save(user);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        boolean is_sucess = sysUserService.updateById(user);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_sucess = sysUserService.removeById(id);
        if (is_sucess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("更改用户状态码")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){

        sysUserService.updateStatus(id,status);
        return Result.ok();
    }

}


















