package com.wanjian.sak.page.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public interface Pager {

    /* ============ 操作接口 ================ */

    void attachContext(Context context);

    void setContentView(int resId);

    void setContentView(View view);

    View getContentView();

    FrameLayout.LayoutParams getLayoutParams();

    void setRequestCode(int requestCode);

    int getRequestCode();

    int getResultCode();

    Bundle getResultData();

    /**
     * @return
     */
    String getToken();

    /**
     * 获取当前栈顶页面
     *
     * @return
     */
    Pager getTop();

    /**
     * 启动另外的页面
     *
     * @param intent
     */
    void openPage(Intent intent) throws ClassNotFoundException;

    /**
     * 启动页面并准备接受返回数据
     *
     * @param intent
     * @param requestCode
     */
    void openPageForResult(Intent intent, int requestCode);

    /**
     * 结束并退出当期页面
     */
    void finish();

    /**
     * 结合openPageForResult方法，实现页面间数据交流
     *
     * @param resultCode
     */
    void setResult(int resultCode);

    /**
     * 结合openPageForResult方法，实现页面间数据交流
     *
     * @param resultCode
     * @param data
     */
    void setResult(int resultCode, Bundle data);

    /* ========== 生命周期回调 ============= */

    /**
     * 页面生命周期回调：创建
     *
     * @param params
     */
    void onCreate(Bundle params);

    /**
     * 页面生命周期回调：展示
     */
    void onShow();

    /**
     * 页面生命周期回调：隐藏
     */
    void onHide();

    /**
     * 页面生命周期回调：销毁
     */
    void onDestroy();

    /**
     * 页面生命周期回调：页面结束并返回数据
     * <p>
     * onPageResult回调顺序在onShow之前, 页面切换回调顺序如下：
     * onShow() --> onHide() --> onPageResult() --> onShow()
     *
     * @param requestCode
     * @param resultCode
     */
    void onPageResult(int requestCode, int resultCode, Bundle resultData);
}
