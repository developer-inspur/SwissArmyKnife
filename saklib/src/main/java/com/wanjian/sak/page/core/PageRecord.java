package com.wanjian.sak.page.core;

import android.os.Bundle;

public class PageRecord {
    /**
     * 页面具体实现类
     */
    Class<? extends Pager> type;

    /**
     * 页面实例对象
     */
    Pager pager;

    /**
     * 页面启动参数
     */
    Bundle data;

    /**
     * 页面实例token
     */
    String token;

    /**
     * 是否需要result数据
     */
    boolean isPendingReuslt;
}
