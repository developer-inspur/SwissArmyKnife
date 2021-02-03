package com.wanjian.sak.page.core;

import android.os.Bundle;

public class PageAction {
    private Class mTargetClass;

    private Bundle mData;

    private int mRequestCode;

    public void setTargetClass(Class targetClass) {
        mTargetClass = targetClass;
    }

    public void setData(Bundle data) {
        mData = data;
    }

    public Class getTargetClass() {
        return mTargetClass;
    }

    public Bundle getData() {
        return mData;
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
