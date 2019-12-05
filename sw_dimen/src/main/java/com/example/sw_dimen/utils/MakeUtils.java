package com.example.sw_dimen.utils;

import com.example.sw_dimen.constants.DimenTypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class MakeUtils {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
    private static final String XML_RESOURCE_START = "<resources>\r\n";
    private static final String XML_RESOURCE_END = "</resources>\r\n";
    /**
     * 重点：按照比例生成转换后的值，
     *
     * jdk 格式化规范：%[argument_index$][flags][width][.precision]conversion 规范
     */
    private static final String XML_DIMEN_TEMPLETE = "<dimen name=\"dp_%1$spx_%2$d\">%3$.2fdp</dimen>\r\n";


    /**
     * 格式化的String,用于写入<dimen name="qb_px_0">0.00dp</dimen>这样的字符串到文件中
     */
    private static final String XML_BASE_DPI = "<dimen name=\"base_dpi\">%ddp</dimen>\r\n";
    /**
     * 文件中dp值最多到多少，如720代表支持0~720px的适配
     */
    private  static final int MAX_SIZE = 720;

    /**
     * 生成的文件名，如dimens,dimen等xml文件
     */
    private static final String XML_NAME = "dimens.xml";


    /**
     *屏幕的宽度与横向dp的关系如下：
     * px=dp*(dpi/160)=dp*（160的倍数）  【其中dpi是像素密度 dot per inch，如720*1280分辨率的手机，对角线为5.7inch的手机，则dpi=sqrt(720^2+1280^2)/5.7=257】
     * 如小米5手机的px=1080，dpi=480 => dp=px/（dpi/160）=px/（160的倍数）=1080/3=360dp
     * 故小米5的横向dp为360,那么android系统会找是否存在values-sw360dp的文件夹以及对应的资源文件。
     *
     * 上面160是数据mdpi的
     * 像素转成dip格式，转换规则是px的值➗
     * @param pxValue
     * @param sw
     * @param designWidth ui设计稿宽度如360dp
     * @return 转换后的值，输出到dimens.xml文件中,eg：<dimen name="dp_px_1">0.83dp</dimen>中的0.83
     */
    public static float px2dip(float pxValue, int sw,int designWidth) {
        float dpValue =   (pxValue/(float)designWidth) * sw;//核心，
        BigDecimal bigDecimal = new BigDecimal(dpValue);//大数
        float finDp = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return finDp;
    }
    

    /**
     * 生成所有的尺寸数据
     *
     * @param type
     * @param  designWidth 最小宽度，如360px
     * @return
     */
    private static String makeAllDimens(DimenTypes type, int designWidth) {
        float dpValue;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(XML_HEADER);
            sb.append(XML_RESOURCE_START);
            //备份生成的相关信息
            temp = String.format(XML_BASE_DPI, type.getSwWidthDp());//写入base_dpi信息，如<dimen name="base_dpi">300dp</dimen>
            sb.append(temp);
            for (int i = 0; i <= MAX_SIZE; i++) {
            	
                dpValue = px2dip((float) i,type.getSwWidthDp(),designWidth);//按照将300-390区间的值与基准值比较，做px->dip转换
               //格式化输出：
                temp = String.format(XML_DIMEN_TEMPLETE,"", i, dpValue);//第一个是字符型1$s、第二个是整数型2$d、第三个是精度为小数点后2位数3$.2f
                sb.append(temp);
            }


            sb.append(XML_RESOURCE_END);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



    /**
     * 生成的目标文件夹
     * 只需传宽进来就行
     *
     * @param designWidth 最小的宽度 ，一般是UI设计稿的屏幕宽度，如360px
     * @param type 枚举类型
     * @param buildDir 生成的目标文件夹
     */
    public static void makeAll(int designWidth, DimenTypes type, String buildDir) {
        try {
            //生成规则
            final String folderName;
            if (type.getSwWidthDp() > 0) {//swWidthDp范围在300~490
                //适配Android 3.2+
                folderName = "values-sw" + type.getSwWidthDp() + "dp";//文件夹的名字values-swxxxdp格式
            }else {
            	return;
            }
            

            File file = new File(buildDir + File.separator + folderName); //生成目标目录
            if (!file.exists()) {
                file.mkdirs();
            }

            //生成values文件
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + File.separator + XML_NAME);//生成目标目录
            fos.write(makeAllDimens(type,designWidth).getBytes());//
            fos.flush();//一次性将缓冲区的数据写到文件中
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
