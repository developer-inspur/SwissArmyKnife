package com.wanjian.sak;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

import com.wanjian.sak.config.Config;
import com.wanjian.sak.utils.OptionPanelUtils;

final class Scaffold {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void install(final Application application, Config config) {
        OptionPanelUtils.showEntrance(application, config);
    }

    public void uninstall(final Application application) {
        OptionPanelUtils.remoteEntrance(application);
    }

}
