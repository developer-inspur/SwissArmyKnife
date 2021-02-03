package com.wanjian.sak.page.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wanjian.sak.page.util.PageUtils;

public class BasePager implements Pager {
    private String mToken;
    private Context mContext;
    private View mContent;
    private FrameLayout.LayoutParams mLayoutParams;
    private int mRequestCode;
    private int mResultCode;
    private Bundle mResultData;

    public BasePager() {
        super();
        mToken = PageUtils.generatorToken(this);
    }

    @Override
    public final void attachContext(Context context) {
        mContext = context;
    }

    public final Context getContext() {
        return mContext;
    }

    @Override
    public final void setContentView(int resId) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContent = LayoutInflater.from(mContext).inflate(resId, null);
        mLayoutParams = layoutParams;
    }

    @Override
    public final void setContentView(View contentView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContent = contentView;
        mLayoutParams = layoutParams;
    }

    public final View getContentView() {
        return mContent;
    }

    public View findViewById(int id) {
        return mContent.findViewById(id);
    }

    public final FrameLayout.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    @Override
    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    @Override
    public int getRequestCode() {
        return mRequestCode;
    }

    @Override
    public int getResultCode() {
        return mResultCode;
    }

    @Override
    public Bundle getResultData() {
        return mResultData;
    }

    @Override
    public String getToken() {
        return mToken;
    }

    @Override
    public final Pager getTop() {
        PageRecord top = PageFrameWork.getInstance().getTop();
        return null != top ? top.pager : null;
    }

    @Override
    public final void openPage(Intent intent) {

        try {
            PageAction pageAction = new PageAction();
            pageAction.setTargetClass(Class.forName(intent.getComponent().getClassName()));
            pageAction.setData(intent.getExtras());
            PageFrameWork.getInstance().startPageForResult(pageAction);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    @Override
    public final void openPageForResult(Intent intent, int requestCode) {
        try {
            PageAction pageAction = new PageAction();
            pageAction.setTargetClass(Class.forName(intent.getComponent().getClassName()));
            pageAction.setData(intent.getExtras());
            pageAction.setRequestCode(requestCode);
            PageFrameWork.getInstance().startPageForResult(pageAction);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public final void finish() {
        PageFrameWork.getInstance().finishPage(mToken);
    }

    @Override
    public final void setResult(int resultCode) {
        mResultCode = resultCode;
        mResultData = null;
    }

    @Override
    public final void setResult(int resultCode, Bundle data) {
        mResultCode = resultCode;
        mResultData = data;
    }

    @Override
    public void onCreate(Bundle params) {
        // override and add your code
    }

    @Override
    public void onShow() {
        // override and add your code
    }

    @Override
    public void onHide() {
        // override and add your code
    }

    @Override
    public void onDestroy() {
        // override and add your code
    }

    @Override
    public void onPageResult(int requestCode, int resultCode, Bundle resultData) {
        // override and add your code
    }
}
