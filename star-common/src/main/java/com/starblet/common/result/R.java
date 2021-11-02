package com.starblet.common.result;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来统一前后端交互的类型
 * 通过定义统一类型的返回类型，可以在使用时，一直调用方法
 *
 * @author starblet
 * @create 2021-08-18 21:57
 */
@Data
public class R {
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    // 私有化构造器
    private R() {}

    /**
     * 成功无数据
     * @return
     */
    public static R success() {
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 失败无数据
     * @return
     */
    public static R error() {
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    /**
     * 设置特定的结果
     * @param responseEnum 响应码枚举类型
     * @return
     */
    public static R setResult(ResponseEnum responseEnum){
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    /**
     * 设置自定义的返回消息，消息个性化
     * @param message
     * @return
     */
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 设置自定义的返回码
     * @param code
     * @return
     */
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 设置携带的数据
     * @param key
     * @param value
     * @return
     */
    public R data(String key,Object value) {
        this.data.put(key,value);
        return this;
    }

    /**
     * 直接设置集合
     * @param map
     * @return
     */
    public R data(Map<String,Object> map) {
        this.setData(map);
        return this;
    }

}
