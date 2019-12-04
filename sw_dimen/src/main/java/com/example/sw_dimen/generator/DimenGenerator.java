package com.example.sw_dimen.generator;


import com.example.sw_dimen.constants.DimenTypes;
import com.example.sw_dimen.utils.MakeUtils;

import java.io.File;


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
        int smallest = DESIGN_WIDTH > DESIGN_HEIGHT ? DESIGN_HEIGHT : DESIGN_WIDTH;  //     求得最小宽度
        DimenTypes[] values = DimenTypes.values();
        for (DimenTypes value : values) {
            File file = new File("./sw_dimen/");//生成的文件放在sw_dimen目录下
            MakeUtils.makeAll(smallest, value, file.getAbsolutePath());
        }
    }

}
