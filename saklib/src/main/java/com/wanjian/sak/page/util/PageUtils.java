package com.wanjian.sak.page.util;

import com.wanjian.sak.page.core.Pager;

public class PageUtils {
    public static String generatorToken(Pager pagerInstante) {
        return String.valueOf(pagerInstante.hashCode()) + System.currentTimeMillis();
    }
}
