package com.wanjian.sak.page.impl;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.wanjian.sak.config.Config;
import com.wanjian.sak.page.core.BasePager;
import com.wanjian.sak.page.core.PageAction;
import com.wanjian.sak.page.core.PageFrameWork;
import com.wanjian.sak.page.core.Pager;
import com.wanjian.sak.utils.WMUUtil;
import com.wanjian.sak.view.OptPanelView;
import com.wanjian.sak.view.SAKContainerView;

public class EntrancePager extends BasePager implements OptPanelView.OnPagerActionListener {

    private static boolean isInited = false;

    @SuppressLint("StaticFieldLeak")
    private static SAKContainerView sRootContainer;

    @SuppressLint("StaticFieldLeak")
    private static OptPanelView sOptPanelView;


    public static void open(final Application application, Config config) {
        initIfNeeded(application, config);

        WMUUtil.addView(application, sRootContainer, true, false);

        PageAction pageAction = new PageAction();
        pageAction.setTargetClass(EntrancePager.class);
        PageFrameWork.getInstance().startPageForResult(pageAction);
    }

    private static void initIfNeeded(final Application application, Config config) {
        if (isInited) {
            return;
        }

        sRootContainer = initPageFramework(application);
        isInited = true;
        sOptPanelView = new OptPanelView(application);
        sOptPanelView.attach(application, config);


        sOptPanelView.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WMUUtil.removeContent(application, sRootContainer);
            }
        });
    }

    public static void destroy(final Application application) {
        if (null != sOptPanelView) {
            sOptPanelView.destroy();
        }

        PageFrameWork.getInstance().detachRootView();

        sOptPanelView = null;
        sRootContainer = null;
        isInited = false;
    }

    private static SAKContainerView initPageFramework(Context context) {
        final SAKContainerView containerView = new SAKContainerView(context);
        containerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                    optPanelView.performClick();
                    return true;
                }
                return false;
            }
        });

        PageFrameWork.getInstance().attachRootView(context, containerView);
        isInited = true;
        return containerView;
    }

    @Override
    public void onCreate(Bundle params) {
        super.onCreate(params);
        setContentView(sOptPanelView);
        sOptPanelView.setPagerActionListener(this);
    }

    @Override
    public void onPagerAction(Class<? extends Pager> type) {
        openPage(new Intent(getContext(), type));
    }

    @Override
    public void onPageFinish() {
        finish();
    }
}
