package com.wanjian.sak.page.impl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wanjian.sak.R;
import com.wanjian.sak.page.core.BasePager;

public class PageB extends BasePager implements View.OnClickListener {
    private static final String TAG = PageB.class.getSimpleName();

    @Override
    public void onCreate(Bundle params) {
        super.onCreate(params);
        setContentView(R.layout.pager_test_pageb);

        findViewById(R.id.open_btn).setOnClickListener(this);
        findViewById(R.id.finish_btn).setOnClickListener(this);

        Log.d(TAG, "======== onCreate ========");
    }

    @Override
    public void onShow() {
        super.onShow();
        Log.d(TAG, "======== onShow ========");
    }

    @Override
    public void onHide() {
        super.onHide();
        Log.d(TAG, "======== onHide ========");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "======== onDestroy ========");
    }


    @Override
    public void onPageResult(int requestCode, int resultCode, Bundle resultData) {
        super.onPageResult(requestCode, resultCode, resultData);
        Log.d(TAG, "======== onPageResult ========  requestCode = " + requestCode + ", resultCode = " + resultCode + ", resultData = " + resultData);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.open_btn) {
            openPage(new Intent(getContext(), PageB.class));
        } else if (id == R.id.finish_btn) {
            finish();
        }
    }
}
