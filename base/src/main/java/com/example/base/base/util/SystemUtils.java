package com.example.base.base.util;

import android.content.Context;

/**
 * @User Xiahangli
 * @Date 2019-12-04  23:01
 * @Email henryatxia@gmail.com
 * @Descrip android系统相关的工具类
 *
 * A utility class (aka helper class) is a “structure” that has only static methods and encapsulates no state
 *
 * 注：对象是用来存储数据的，工具是直接拿来解决问题的，这里一些不太涉及状态常用的固定解决方法,还有与实例关系不大的功能与逻辑
 * （如数据清理，数据转换），适合用静态方法调用
 *
 * static类是一组static方法的集合，可以实现代码的复用（一般是无状态的即没有成员变量），每个函数相互独立，有点面向过程的思维
 * static类不生成实例
 * static类相对单例的性能更好（不用创建），他的方法不需要实例方法的动态绑定（static方法本身不能被复写），
 * static类中的static方法不能多态，无法放在接口类中，无法mock测试
 * *重点*第一次调用静态方法（或静态变量的时候）会触发类加载机制,参考jvm加载笔记
 * @see <a href =https://www.yegor256.com/2014/05/05/oop-alternative-to-utility-classes.html>oop形式的utils工具类</a>
 * @see <a href = https://www.zhihu.com/question/40361359>知乎关于静态'类'&静态方法的讨论</a>
 */
public final class SystemUtils {


//    static int  x =1;
    /**
     * Don't let anyone instantiate this class.
     */
    private SystemUtils(){}

    /**
     * 注意线程安全问题，因为静态方法有可能访问静态变量，而静态变量多线程共享
     * 注意static方法在程序编译的时候就加载到栈中，具有很好的执行效率，同时static方法无法override
     *方法测试需要保证100%statement测试覆盖
     * 注意方法（无论是静态方法或实例方法)在内存里只有一份，无论该类有多少个实例，都共用一个方法，区别在于对于静态方法，
     * 可以使用（类名.方法名）这种方式调用，也可以使用（对象名.方法名）调用，而对象方法只能（对象名.方法）调用
     *
     * 判断是否屏幕亮着
     * @param context 上下文，注意这里传入context并不会造成成内存泄漏，
     *                工具类本身在方法执行完就释放context栈帧局部变量context没有持有Context对象
     *                同事，static方法在第一调用到的时候会初始化类加载
     * @return
     */
    public static boolean isScreenOn(Context context) {

        return false;
    }
}
