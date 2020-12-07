package com.wanjian.sak.view;

import android.view.View;

public interface ICustomPanel {

    View getContentView();

    void setDismissHandler(PanelDismissObserver observer);

    public interface PanelDismissObserver {
        void onMessage();
    }
}
