package com.wanjian.sak.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wanjian.sak.R;
import com.wanjian.sak.config.Config;
import com.wanjian.sak.page.impl.EntrancePager;
import com.wanjian.sak.view.SAKContainerView;

public class OptionPanelUtils {
    private static SAKContainerView tinyView;

    @SuppressLint("ClickableViewAccessibility")
    public static void showEntrance(final Application application, final Config config) {

        final ImageView entranceView = new ImageView(application);
        entranceView.setImageResource(R.drawable.sak_launcher_icon);
        int size = ScreenUtils.dp2px(application, 40);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);

        final SAKContainerView containerView = WMUUtil.addContent(application, entranceView, layoutParams, false, true);
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
                WindowManager.LayoutParams params = WMUUtil.getLayoutParam(containerView);
                params.x = (int) (params.x + (lastX - curX));
                params.y = (int) (params.y + (curY - lastY));
                lastX = curX;
                lastY = curY;
                if (distance(downX, downY, curX, curY) > ScreenUtils.dp2px(application, 8)) {
                    click = false;
                }
                WMUUtil.updateLayout(application, containerView, params);
                if (event.getActionMasked() == MotionEvent.ACTION_UP && click) {
                    entranceView.performClick();
                }
                return false;
            }
        });

        entranceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptPanel(application, config);
            }
        });

        tinyView = containerView;
    }

    private static double distance(float downX, float downY, float curX, float curY) {
        return Math.sqrt(Math.pow((downX - curX), 2) + Math.pow((downY - curY), 2));
    }

    private static void showOptPanel(final Application application, Config config) {
        EntrancePager.open(application, config);
    }

    public static void remoteEntrance(final Application application) {
        EntrancePager.destroy(application);
        WMUUtil.removeContent(application, tinyView);
    }
}
