package com.vincent.dialoglibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建日期：2019/3/20 0020on 下午 1:43
 * 描述：
 *
 * @author：Vincent QQ：3332168769
 * 备注：
 */
public class Test1 extends View {
    public Test1(Context context) {
        super(context);
    }

    public Test1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    static class A{
        public static void a(){
            B.b();
        }
    }
    static class B{
        public static void b() {

        }
    }
}
