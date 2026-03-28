package com.libase.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 状态栏工具类
 */
public class StatusBarUtils {
    private static final String TAG = "StatusBarUtils";

    /**
     * 给根布局加上系统栏边距
     * @param view
     */
    public static void AddStatusHeightToRootView(View view) {
        //EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * 给子控件加上系统栏边距
     * @param rootView
     * @param view
     */
    public static void AddStatusHeightToKidView(View rootView, View... view) {
        AtomicBoolean isAdd = new AtomicBoolean(true);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            if (!isAdd.get()) {  //输入框点击等事件会引起这个方法被不断触发
                return insets;
            }
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            for (int i = 0; i < view.length; i++) {
                View view1 = view[i];
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view1.getLayoutParams();
                int topMargin = params.topMargin;
                Log.d(TAG, "AddStatusHeightToKidView: " + i+"号控件"+ params.rightMargin);
                params.setMargins(0, topMargin + systemBars.top, 0, 0);
                Log.d(TAG, "AddStatusHeightToKidView: " + i+"号控件"+ params.rightMargin);
                view1.setLayoutParams(params);
            }
            isAdd.set(false);
            return insets;
        });
    }

    /**
     * 设置沉浸式布局让页面顶上去
     * @param activity
     */
    public static void setImmersiveLayout(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);  //状态栏不再有默认的半透明黑色背景，而是完全透明，让内容可以延伸到状态栏区域
        View decorView = activity.getWindow().getDecorView();
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE//保持布局稳定，避免因系统 UI（如状态栏、导航栏）的显示/隐藏而导致布局跳动。
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//让内容布局延伸到状态栏下方（全屏布局），但状态栏仍然可见（只是透明）。
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//将状态栏的文本和图标变为深色（黑色或深灰色），适合浅色或透明状态栏背景。
        decorView.setSystemUiVisibility(flag);
    }
}
