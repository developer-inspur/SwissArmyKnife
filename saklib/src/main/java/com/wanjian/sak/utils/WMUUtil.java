package com.wanjian.sak.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import com.wanjian.sak.view.SAKContainerView;

import java.util.HashMap;
import java.util.Map;

public class WMUUtil {
    private static Map<View, WindowManager.LayoutParams> sWPMap = new HashMap<>();

    public static void addView(Context context, View root, boolean fullScreen, boolean noFocus) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        final int whParam = fullScreen ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = whParam;
        params.height = whParam;

        params.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        if (noFocus) {
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        windowManager.addView(root, params);
        sWPMap.put(root, params);
    }

    public static SAKContainerView addContent(Context context, View content, LayoutParams contentParam, boolean fullScreen, boolean noFocus) {
        final SAKContainerView contentContainer = new SAKContainerView(context);
        if (null != content) {
            contentContainer.addView(content, contentParam);
        }

        addView(context, contentContainer, fullScreen, noFocus);
        return contentContainer;
    }

    public static WindowManager.LayoutParams getLayoutParam(SAKContainerView root) {
        return sWPMap.get(root);
    }

    public static void updateLayout(Context context, SAKContainerView root, WindowManager.LayoutParams params) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.updateViewLayout(root, params);
        sWPMap.put(root, params);
    }

    public static void removeContent(Context context, SAKContainerView root) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(root);
        sWPMap.remove(root);
    }
}
