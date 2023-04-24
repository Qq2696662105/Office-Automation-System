package com.dataKing.common.result;

import lombok.Data;

/**
 * ClassName: Result
 * Package: com.dataKing.common.result
 * Description:封装的请求访问结果类
 *
 * @Author dataKing
 * @Create 2023/3/26 0026 10:06
 * @Version 1.0
 */
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    private Result(){}

    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //外部可调用的成功方法
    public static <T> Result<T> ok(){
        return build(null,ResultCodeEnum.SCUSSESS);
    }

    public static <T> Result<T> ok(T data){
        return build(data,ResultCodeEnum.SCUSSESS);
    }

    //外部可调用的失败方法
    public static <T> Result<T> fail(){
        return build(null,ResultCodeEnum.FAIL);
    }


    public static <T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }

    //自定义返回code和message
    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
