package com.dataKing.common.result;

import lombok.Getter;

/**
 * ClassName: ResultEnum
 * Package: com.dataKing.common.result
 * Description:返回json格式枚举类
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 10:08
 * @Version 1.0
 */

@Getter
public enum ResultCodeEnum {

    SCUSSESS(200,"成功"),
    FAIL(201,"失败"),
    LOGIN_ERROR(204,"登录认证失败")
    ;

    private Integer code;//状态码

    private String message;//描述信息

    private ResultCodeEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

}
