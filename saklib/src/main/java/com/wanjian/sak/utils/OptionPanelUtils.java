package com.wanjian.sak.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wanjian.sak.R;
import com.wanjian.sak.config.Config;
import com.wanjian.sak.layer.LayerRoot;
import com.wanjian.sak.view.ICustomPanel;
import com.wanjian.sak.view.OptPanelView;
import com.wanjian.sak.view.SAKContainerView;

public class OptionPanelUtils {


    @SuppressLint("StaticFieldLeak")
    private static OptPanelView optPanelView;

    public static void showEntrance(final Application application, final Config config) {

//    SAKEntranceView entranceView = new SAKEntranceView(application);
        final ImageView entranceView = new ImageView(application);
        entranceView.setImageResource(R.drawable.sak_launcher_icon);
        int size = ScreenUtils.dp2px(application, 40);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);

        final SAKContainerView containerView = new SAKContainerView(application);
        containerView.addView(entranceView, layoutParams);

        final WindowManager windowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = buildEntranceLayoutParam();
        windowManager.addView(containerView, params);
        entranceView.setOnTouchListener(new View.OnTouchListener() {
            float lastX;
            float lastY;
            float downX;
            float downY;
            boolean click;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    downX = lastX = event.getRawX();
                    downY = lastY = event.getRawY();
                    click = true;
                    return true;
                }
                float curX = event.getRawX();
                float curY = event.getRawY();
                params.x = (int) (params.x + (lastX - curX));
                params.y = (int) (params.y + (curY - lastY));
                lastX = curX;
                lastY = curY;
                if (distance(downX, downY, curX, curY) > ScreenUtils.dp2px(application, 8)) {
                    click = false;
                }
                windowManager.updateViewLayout(containerView, params);
                if (event.getActionMasked() == MotionEvent.ACTION_UP && click) {
                    entranceView.performClick();
                }
                return false;
            }
        });

        optPanelView = new OptPanelView(application);
        optPanelView.attachConfig(config);
        entranceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptPanel(application, optPanelView);
            }
        });


    }

    private static double distance(float downX, float downY, float curX, float curY) {
        return Math.sqrt(Math.pow((downX - curX), 2) + Math.pow((downY - curY), 2));
    }

    private static WindowManager.LayoutParams buildEntranceLayoutParam() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        return params;
    }

    private static WindowManager.LayoutParams buildPanelLayoutParam() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        return params;
    }

    public static void showOptPanel(Application application, ICustomPanel customPanel) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        final SAKContainerView containerView = new SAKContainerView(application);
        containerView.addView(customPanel.getContentView(), layoutParams);

        final WindowManager windowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(containerView, buildPanelLayoutParam());

        customPanel.setDismissHandler(new ICustomPanel.PanelDismissObserver() {
            @Override
            public void onMessage() {
                containerView.removeAllViews();
                windowManager.removeView(containerView);
            }
        });
    }

    public static void addLayerRoot(LayerRoot layerRoot) {
        if (null == optPanelView) {
            return;
        }

//    optPanelView.attachConfig();
        optPanelView.add(layerRoot);
    }

    public static void enableIfNeeded(LayerRoot layerRoot) {
        if (null == optPanelView) {
            return;
        }

        optPanelView.enableIfNeeded(layerRoot);
    }
}
