package com.wanjian.sak.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wanjian.sak.layer.Layer;
import com.wanjian.sak.utils.OptionPanelUtils;
import com.wanjian.sak.view.ICustomPanel;

public class TestLayer extends Layer implements ICustomPanel {

    private View mContent;
    private PanelDismissObserver mDissOb;

    public TestLayer() {
        super();
    }

    @Override
    protected void onAttach(View rootView) {
        super.onAttach(rootView);
        initUI();
    }

    private void initUI() {
        Context context = getContext();
        mContent = LayoutInflater.from(context).inflate(R.layout.user_window, null);

//        // 重新测量一遍View的宽高
//        mContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        // 确定View的位置
//        mContent.layout(0, 0, mContent.getMeasuredWidth(), mContent.getMeasuredHeight());

        mContent.findViewById(R.id.ivtest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDissOb.onMessage();
            }
        });
    }

    @Override
    protected void invalidate() {
        View view = getRootView();
        if (view == null) {
            return;
        }
//        super.invalidate();
        OptionPanelUtils.showOptPanel(getContext(), this);
    }

    @Override
    public View getContentView() {
        return mContent;
    }

    @Override
    public void setDismissHandler(PanelDismissObserver observer) {
        mDissOb = observer;
    }
}
