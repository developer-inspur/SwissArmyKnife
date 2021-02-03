package com.wanjian.sak.config;

import android.graphics.drawable.Drawable;

import com.wanjian.sak.layer.Layer;
import com.wanjian.sak.page.core.Pager;

public class Item {
    public Class<?> clzType;
    public Drawable icon;
    public String name;
    private TYPE type;
    private boolean isEnable;

    public Item(Class<?> clzType, Drawable icon, String name, TYPE type) {
        this.clzType = clzType;
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public TYPE getType() {
        return this.type;
    }

    public Class<? extends Pager> getPagerClass() {
        if (TYPE.PAGER == this.type && Pager.class.isAssignableFrom(this.clzType)) {
            return (Class<? extends Pager>) this.clzType;
        }

        return null;
    }

    public Class<? extends Layer> getLayerClass() {
        if (TYPE.LAYER == this.type && Layer.class.isAssignableFrom(this.clzType)) {
            return (Class<? extends Layer>) this.clzType;
        }

        return null;
    }

    public enum TYPE {
        LAYER,
        PAGER;
    }
}
