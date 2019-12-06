package com.example.base.base.util;

import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * @User Xiahangli
 * @Date 2019-12-06  10:57
 * @Email henryatxia@gmail.com
 * @Descrip 容器的常用工具集
 */
public class CollectionUtils {
    /**
     * 判断容器size是否>0
     * @param collection 容器
     * @return 是否size>0
     */
   public static boolean sizeValid(@Nullable  Collection collection){
        if (collection!=null&&collection.size()>0){
            return true;
        }
        return false;
    }
}
