package com.dataKing.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.common.handler.MyException;
import com.dataKing.common.jwt.JwtHelper;
import com.dataKing.common.result.Result;
import com.dataKing.common.utils.MD5;
import com.dataKing.model.system.SysUser;
import com.dataKing.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: IndexController
 * Package: com.dataKing.auth.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 20:53
 * @Version 1.0
 */

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;
    /**
     * 登录
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {

        SysUser user = sysUserService.getByUsername(loginVo.getUsername());

        if (user == null){
            throw new MyException(201,"用户名不存在");
        }

        String password_db = user.getPassword();
        String password_input = MD5.encrypt(loginVo.getPassword());
        if (!password_db.equals(password_input)){
            throw new MyException(201,"用户密码错误");
        }
        if (user.getStatus()==0){
            throw new MyException(201,"用户已被禁用");
        }
        Map<String,Object> map = new HashMap<>();
        String token = JwtHelper.createToken(user.getId(), user.getUsername());
        map.put("token",token);
        return Result.ok(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //获取请求头中的token数据
        String token = request.getHeader("token");
        //利用jwt工具从token中提取userId
        Long userId = JwtHelper.getUserId(token);
        //调用userService里的方法获取用户信息
        Map<String, Object> map = sysUserService.getUserInfoByUserId(userId);
        return Result.ok(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
