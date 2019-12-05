package com.example.base.base.util;

/**
 * @User Xiahangli
 * @Date 2019-12-05  10:27
 * @Email henryatxia@gmail.com
 * @Descrip 对象判断的工具类
 */
public class ObjectUtils {
    /**
     * 判断是否非null，兼容android老系统
     * @param object
     * @param message 异常信息
     * @param <T> 判断的对象
     * @return
     */
    public static<T> T requireNonNull(T object,String message){
        if(object == null){
            throw new NullPointerException(message);
        }
        return object;
    }
}
