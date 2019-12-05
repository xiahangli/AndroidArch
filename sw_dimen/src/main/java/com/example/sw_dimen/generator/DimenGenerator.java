package com.example.sw_dimen.generator;


import com.example.sw_dimen.constants.DimenTypes;
import com.example.sw_dimen.utils.MakeUtils;

import java.io.File;

/**
 * 主程序类中的的静态变量先于静态代码块初始化，
 * 其后进入主函数类(程序入口处main)，其后根据静态函数的调用情况，才能选择性的初始化。
 * 生成dimens.xml的工具类
 */
public class DimenGenerator {

    /**
     * 设计稿尺寸(将自己设计师的设计稿的宽度填入)
     */
    private static final int DESIGN_WIDTH = 360;

    /**
     * 设计稿的高度  （将自己设计师的设计稿的高度填入）
     */
    private static final int DESIGN_HEIGHT = 640;

    public static void main(String[] args) {
        int smallest = DESIGN_WIDTH > DESIGN_HEIGHT ? DESIGN_HEIGHT : DESIGN_WIDTH;  // 求得最小宽度,即宽和高的小值作为最小宽度
        DimenTypes[] values = DimenTypes.values();//生成300-490共20个最小宽度的适配方案
        for (DimenTypes value : values) {
            File file = new File("./sw_dimen/");//生成的文件放在sw_dimen目录下
            MakeUtils.makeAll(smallest, value, file.getAbsolutePath());
        }
    }

}
