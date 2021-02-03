package com.wanjian.sak.view;

import android.app.Application;
import android.view.InputEvent;
import android.view.View;
import android.view.ViewRootImpl;

import com.wanjian.sak.config.Config;
import com.wanjian.sak.config.Item;
import com.wanjian.sak.converter.ISizeConverter;
import com.wanjian.sak.layer.IClip;
import com.wanjian.sak.layer.IRange;
import com.wanjian.sak.layer.ISize;
import com.wanjian.sak.layer.Layer;
import com.wanjian.sak.layer.LayerRoot;
import com.wanjian.sak.system.input.InputEventListener;
import com.wanjian.sak.system.input.InputEventReceiverCompact;
import com.wanjian.sak.system.traversals.ViewTraversalsCompact;
import com.wanjian.sak.system.traversals.ViewTraversalsListener;
import com.wanjian.sak.system.window.compact.IWindowChangeListener;
import com.wanjian.sak.system.window.compact.WindowRootViewCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class OptPanelViewHolder implements IWindowChangeListener{

    private List<WeakReference<LayerRoot>> weakReferences = new ArrayList<>();
    private Config mConfig;
    private Application mApplication;

    public OptPanelViewHolder(Application application, Config config) {
        mApplication = application;
        mConfig = config;
        init();
    }

    public void addLayerRoot(LayerRoot layerRoot) {
        weakReferences.add(new WeakReference<LayerRoot>(layerRoot));
    }

    public Config getConfig() {
        return mConfig;
    }

    public void changeSizeConvert(ISizeConverter converter) {
        for (WeakReference<LayerRoot> weakReference : weakReferences) {
            LayerRoot layerRoot = weakReference.get();
            if (layerRoot == null) {
                continue;
            }
            for (Layer layer : layerRoot.getLayers()) {
                if (layer instanceof ISize) {
                    ((ISize) layer).onSizeConvertChange(converter);
                }
            }
        }
    }

    public void changeClip(boolean clip) {
        for (WeakReference<LayerRoot> weakReference : weakReferences) {
            LayerRoot layerRoot = weakReference.get();
            if (layerRoot == null) {
                continue;
            }
            for (Layer layer : layerRoot.getLayers()) {
                if (layer instanceof IClip) {
                    ((IClip) layer).onClipChange(clip);
                }
            }
        }
    }

    public void changeEndRange(int newVal) {
        for (WeakReference<LayerRoot> weakReference : weakReferences) {
            LayerRoot layerRoot = weakReference.get();
            if (layerRoot == null) {
                continue;
            }
            for (Layer layer : layerRoot.getLayers()) {
                if (layer instanceof IRange) {
                    ((IRange) layer).onEndRangeChange(newVal);
                }
            }
        }
    }

    public void changeStartRange(int newVal) {
        for (WeakReference<LayerRoot> weakReference : weakReferences) {
            LayerRoot layerRoot = weakReference.get();
            if (layerRoot == null) {
                continue;
            }
            for (Layer layer : layerRoot.getLayers()) {
                if (layer instanceof IRange) {
                    ((IRange) layer).onStartRangeChange(newVal);
                }
            }
        }
    }

    public void update() {
        for (WeakReference<LayerRoot> reference : weakReferences) {
            LayerRoot root = reference.get();
            if (root == null) {
                continue;
            }
            List<Layer> layers = root.getLayers();
            for (Item item : mConfig.getFunctionList()) {
                Layer layer = getLayerByType(layers, (Class<? extends Layer>) item.clzType);
                if (layer == null) {
                    continue;
                }
                if (layer.isEnable() != item.isEnable()) {
                    layer.enable(item.isEnable());
                    if (layer.isEnable()) {
                        layers.remove(layer);
                        layers.add(layer);
                    } else {
                        layers.remove(layer);
                        layers.add(0, layer);
                    }
                    break;
                }
            }
        }
    }

    private Layer getLayerByType(List<Layer> layers, Class<? extends Layer> layerType) {
        for (Layer layer : layers) {
            if (layer.getClass() == layerType) {
                return layer;
            }
        }
        return null;
    }

    public void enableIfNeeded(LayerRoot layerRoot) {
        List<Layer> layers = layerRoot.getLayers();
        for (Item item : mConfig.getFunctionList()) {
            if (Item.TYPE.LAYER != item.getType()) {
                continue;
            }

            Layer layer = getLayerByType(layers, item.getLayerClass());
            if (layer == null) {
                continue;
            }
            if (layer.isEnable() != item.isEnable()) {
                layer.enable(item.isEnable());
            }
        }
    }

    private static void observerInputEvent(Config config, final LayerRoot layerRoot, final ViewRootImpl viewRootImpl, final View rootView) {
        InputEventReceiverCompact.get(viewRootImpl, new InputEventListener() {
            @Override
            public boolean onBeforeInputEvent(InputEvent inputEvent) {
                return layerRoot.beforeInputEvent(rootView, inputEvent);
            }

            @Override
            public void onAfterInputEvent(InputEvent inputEvent) {
                layerRoot.afterInputEvent(rootView, inputEvent);
            }
        });
    }

    private static void observerUIChange(Config config, final LayerRoot layerRoot, ViewRootImpl viewRootImpl, View view) {
        new ViewTraversalsCompact().register(viewRootImpl, view, new ViewTraversalsListener() {
            @Override
            public void onBeforeTraversal(View rootView) {
                layerRoot.beforeTraversal(rootView);
            }

            @Override
            public void onAfterTraversal(View rootView) {
                layerRoot.afterTraversal(rootView);
            }
        });
    }

    @Override
    public void onAddWindow(final ViewRootImpl viewRootImpl, final View view) {
        if (view instanceof SAKContainerView) {
            return;
        }
        view.post(new Runnable() {
            @Override
            public void run() {
                LayerRoot layerRoot = LayerRoot.create(mConfig, viewRootImpl, view, mApplication);
                enableIfNeeded(layerRoot);
                observerUIChange(mConfig, layerRoot, viewRootImpl, view);
                observerInputEvent(mConfig, layerRoot, viewRootImpl, view);
                addLayerRoot(layerRoot);
            }
        });
    }

    @Override
    public void onRemoveWindow(ViewRootImpl viewRootImpl, View view) {
        //        CanvasHolder.release(viewRootImpl);
    }

    private void init() {
        WindowRootViewCompat.get(mApplication).addWindowChangeListener(this);
    }

    public void destroy() {
        WindowRootViewCompat.get(mApplication).removeWindowChangeListener(this);
    }
}
